package hms.ussd.manager;

import hms.ussd.manager.annotations.Config;
import hms.ussd.manager.annotations.Index;
import hms.ussd.manager.conf.AppConfig;
import hms.ussd.manager.exceptions.DuplicateMenuIdException;
import hms.ussd.manager.exceptions.IndexMenuNotDefinedException;
import hms.ussd.manager.exceptions.MultipleIndexMenuException;
import hms.ussd.manager.exceptions.UssdInitializationException;
import hms.ussd.manager.menu.DefaultFirstMenu;
import hms.ussd.manager.menu.Menu;
import hms.ussd.manager.menu.MenuUtil;
import hms.ussd.manager.repo.GuavaCacheSessionRepoImpl;
import hms.ussd.manager.repo.SessionRepo;
import hms.tap.api.TapException;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.UssdRequestSender;
import hms.tap.api.ussd.messages.MoUssdReq;
import hms.tap.api.ussd.messages.MtUssdReq;
import hms.tap.api.ussd.messages.MtUssdResp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static hms.ussd.manager.menu.MenuUtil.getMenuId;
import static hms.ussd.manager.menu.MenuUtil.getOperationType;
import static hms.ussd.manager.menu.MenuUtil.isIndexMenu;

public class UssdMessageProcessor {

    Logger logger = Logger.getLogger(UssdMessageProcessor.class.getName());

    private Menu firstMenu;
    private SessionRepo sessionRepo;
    private AppConfig appConfig;

    List<Menu> menus;

    /**
     * This Object needs to be initialise with following mandatory parameters.
     * If those parameters are not initialize default values will be assign.
     * In memory database {@link hms.ussd.manager.repo.GuavaSessionRepo}. Default {@link DefaultFirstMenu} and
     * default {@link hms.ussd.manager.conf.AppConfig}
     * @param builder
     */
    private UssdMessageProcessor(Builder builder) throws UssdInitializationException, MultipleIndexMenuException, DuplicateMenuIdException, IndexMenuNotDefinedException {

        if (builder.menus == null || builder.menus.isEmpty()) {
            throw new UssdInitializationException("No ussd menus defined.");
        }
        else {
            validateMenus(builder.menus);
            this.menus = builder.menus;
        }


        if (builder.sessionRepo == null)
            this.sessionRepo = new GuavaCacheSessionRepoImpl();
        else
            this.sessionRepo = builder.sessionRepo;

        if (builder.appConfig == null)
            this.appConfig = new AppConfig.Builder().build();
        else
            this.appConfig = builder.appConfig;
    }

    private void validateMenus(List<Menu> menus) throws DuplicateMenuIdException, MultipleIndexMenuException, IndexMenuNotDefinedException {
        Map<String, Class<? extends Menu>> menuNames = new HashMap<String, Class<? extends Menu>>();
        for (Menu menu : menus) {
            String menuId = getMenuId(menu);
            Class<? extends Menu> aClass = menuNames.get(menuId);
            if(aClass != null) {
                throw new DuplicateMenuIdException(menuId, aClass, menu.getClass());
            } else {
                menuNames.put(menuId, menu.getClass());
            }
            if(isIndexMenu(menu)) {
                if(firstMenu == null) {
                    firstMenu = menu;
                }
                else {
                    throw new MultipleIndexMenuException();
                }
            }
        }
        if(firstMenu == null) {
            throw new IndexMenuNotDefinedException();
        }
    }

    /**
     * This method will process the ussd request with session management.
     *
     * @param moUssdReq {@link hms.tap.api.ussd.messages.MoUssdReq}
     */
    public void processMessage(MoUssdReq moUssdReq) {

        Session session = sessionRepo.findBySessionId(moUssdReq.getSessionId());

        if (session.getCustomerMobileNo() == null)
            session.setCustomerMobileNo(moUssdReq.getSourceAddress());

        //By default set the firstmenu.
        Menu menu = firstMenu;
        //Handle back button request.
        if (!moUssdReq.getUssdOperation().equals(OperationType.MO_INIT.getName())
                && moUssdReq.getMessage().equals(appConfig.getBackButtonCode())) {
            menu = getPreviousMenu(session);
        }
        //Handle normal flow.
        else {
            menu = generateMenu(session, moUssdReq);
        }

        //Get the message of new menu.
        String menuMessage = menu.getMessage(session, moUssdReq);

        OperationType operationType = getOperationType(menu);

        //Generate back feature if session have more the 1 previous menus and if operation type not MT_FIN.
        if (session.getAccessedMenuNames().size() > 1
                && operationType != OperationType.MT_FIN) {
            menuMessage = generateMessageWithBackFeature(menu.getMessage(session, moUssdReq));
        }

        sendUssdResponse(moUssdReq.getSourceAddress(), menuMessage, operationType, moUssdReq.getSessionId());
    }

    private Menu getPreviousMenu(Session session) {

        String previousMenuName = session.getAccessedMenuNames().pop();
        session.setCurrentMenuName(previousMenuName);
        sessionRepo.update(session);

        return getMenuByName(previousMenuName);
    }

    private String generateMessageWithBackFeature(String message) {

        StringBuilder menuBuilder = new StringBuilder(message);
        if (appConfig.isBackButtonEnable()) {
            menuBuilder.append("\n ");
            menuBuilder.append(appConfig.getBackButtonCode());
            menuBuilder.append(".Back");
        }
        return menuBuilder.toString();
    }

    private Menu generateMenu(Session session, MoUssdReq moUssdReq) {

        String currentMenuName = session.getCurrentMenuName();

        //if user doesn't have a current menu send first menu.
        if (currentMenuName == null) {
            updateSessionDetail(session, getMenuId(firstMenu), firstMenu);
            return firstMenu;
        }

        //Get the accessed menu object by its name.
        Menu currentMenu = getMenuByName(currentMenuName);

        // message cannot be null or empty, then send same menu.
        if (moUssdReq.getMessage() == null || moUssdReq.getMessage().isEmpty()) {
            return currentMenu;
        }

      /*  // Validate user input , if validation fail send the same menu.
        if (!currentMenu.validate(session, moUssdReq)) {
            return currentMenu;
        }*/

        // If all success send the next menu. We can find the next menu name from the current menus, nextMenu Method.
        Menu nextMenu = getMenuByName(currentMenu.getNextMenu(session, moUssdReq));
        if (nextMenu != null) {
            updateSessionDetail(session, currentMenuName, nextMenu);
            return nextMenu;
        }

        return currentMenu;

    }

    private void updateSessionDetail(Session session, String currentMenuName, Menu nextMenu) {
        session.setCurrentMenuName(getMenuId(nextMenu));
        session.getAccessedMenuNames().add(currentMenuName);
        sessionRepo.update(session);
    }

    /**
     * Get the menu by its name from the menu registry.
     * @param menuName Menu name to get the Menu.
     * @return the {@link Menu}
     */
    private Menu getMenuByName(String menuName) {
        for (Menu menu : menus) {
            if (getMenuId(menu).equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return firstMenu;
    }

    /**
     * Sending ussd response for every ussd request.
     * @param subscriberId Ussd requester MSISDN
     * @param message Message to the USSD menu.
     * @param operationType {@link hms.tap.api.ussd.OperationType} for the particular menu.
     * @param sessionId Ussd requested session id.
     */
    private void sendUssdResponse(String subscriberId, String message, OperationType operationType, String sessionId) {
        try {
            final MtUssdReq request = new MtUssdReq();
            request.setApplicationId(appConfig.getApplicationId());
            // request.setEncoding(moUssdReq.getEncoding());
            request.setMessage(message);
            request.setPassword(appConfig.getPassword());
            request.setSessionId(sessionId);
            request.setUssdOperation(operationType.getName());
            // request.setVersion(moUssdReq.getVersion());
            request.setDestinationAddress(subscriberId);

            logger.info("Ussd Request " + request);
            UssdRequestSender ussdMtSender = new UssdRequestSender(new URL(appConfig.getUrl()));

            MtUssdResp ussdResp = ussdMtSender.sendUssdRequest(request);
            logger.info("Ussd Response " + ussdResp);
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Error in URL " + e);
        } catch (TapException e) {
            logger.log(Level.SEVERE, "Error sending ussd request " + e);
        }
    }

    public static final class Builder {

        private SessionRepo sessionRepo;
        private AppConfig appConfig;
        private List<Menu> menus;

        public Builder sessionRepo(SessionRepo sessionRepo) {
            this.sessionRepo = sessionRepo;
            return this;
        }

        public Builder appConfig(AppConfig appConfig) {
            this.appConfig = appConfig;
            return this;
        }

        public Builder menus(List<Menu> menus) {
            this.menus = menus;
            return this;
        }

        public UssdMessageProcessor build() throws UssdInitializationException, MultipleIndexMenuException, DuplicateMenuIdException, IndexMenuNotDefinedException {
            return new UssdMessageProcessor(this);
        }
    }
}

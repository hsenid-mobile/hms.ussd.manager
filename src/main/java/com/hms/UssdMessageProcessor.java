package com.hms;

import com.hms.conf.AppConfig;
import com.hms.menu.DefaultFirstMenu;
import com.hms.menu.Menu;
import com.hms.repo.GuavaCacheSessionRepoImpl;
import com.hms.repo.SessionRepo;
import hms.tap.api.TapException;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.UssdRequestSender;
import hms.tap.api.ussd.messages.MoUssdReq;
import hms.tap.api.ussd.messages.MtUssdReq;
import hms.tap.api.ussd.messages.MtUssdResp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UssdMessageProcessor {

    Logger logger = Logger.getLogger(UssdMessageProcessor.class.getName());

    private Menu firstMenu;
    private SessionRepo sessionRepo;
    private AppConfig appConfig;

    List<Menu> menus = new ArrayList<Menu>();

    /**
     * This Object needs to be initialise with following mandatory parameters.
     * If those parameters are not initialize default values will be assign.
     * In memory database {@link com.hms.repo.GuavaSessionRepo}. Default {@link com.hms.menu.DefaultFirstMenu} and
     * default {@link com.hms.conf.AppConfig}
     * @param initialMenu
     * @param sessionRepo
     * @param appConfig
     */
    public UssdMessageProcessor(Menu initialMenu, SessionRepo sessionRepo, AppConfig appConfig) {
        buildUssdConfiguration(initialMenu, sessionRepo, appConfig);
    }

    private void buildUssdConfiguration(Menu initialMenu, SessionRepo sessionRepo, AppConfig appConfig) {

        firstMenu = initialMenu;
        this.sessionRepo = sessionRepo;
        this.appConfig = appConfig;

        if (firstMenu == null) {
            firstMenu = new DefaultFirstMenu();
            menus.add(firstMenu);
        }

        if (sessionRepo == null)
            this.sessionRepo = new GuavaCacheSessionRepoImpl();

        if (appConfig == null)
            this.appConfig = new AppConfig();
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

        //Generate back feature if session have more the 1 previous menus and if operation type not MT_FIN.
        if (session.getAccessedMenuNames().size() > 1
                && !menu.getOperationType().getName().equalsIgnoreCase(OperationType.MT_FIN.getName())) {
            menuMessage = generateMessageWithBackFeature(menu.getMessage(session, moUssdReq));
        }

        sendUssdResponse(moUssdReq.getSourceAddress(), menuMessage, menu.getOperationType(), moUssdReq.getSessionId());
    }

    private Menu getPreviousMenu(Session session) {

        String previousMenuName = session.getAccessedMenuNames().pop();
        session.setCurrentMenuName(previousMenuName);
        sessionRepo.update(session);

        return getMenubyName(previousMenuName);
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
            updateSessionDetail(session, firstMenu.getMenuName(), firstMenu);
            return firstMenu;
        }

        //Get the accessed menu object by its name.
        Menu currentMenu = getMenubyName(currentMenuName);

        // message cannot be null or empty, then send same menu.
        if (moUssdReq.getMessage() == null || moUssdReq.getMessage().isEmpty()) {
            return currentMenu;
        }

        // Validate user input , if validation fail send the same menu.
        if (!currentMenu.validate(session, moUssdReq)) {
            return currentMenu;
        }

        // If all success send the next menu. We can find the next menu name from the current menus, nextMenu Method.
        Menu nextMenu = getMenubyName(currentMenu.getNextMenu());
        if (nextMenu != null) {
            updateSessionDetail(session, currentMenuName, nextMenu);
            return nextMenu;
        }

        return currentMenu;

    }

    private void updateSessionDetail(Session session, String currentMenuName, Menu nextMenu) {
        session.setCurrentMenuName(nextMenu.getMenuName());
        session.getAccessedMenuNames().add(currentMenuName);
        sessionRepo.update(session);
    }

    private Menu getMenubyName(String menuName) {
        for (Menu menu : menus) {
            if (menu.getMenuName().equalsIgnoreCase(menuName)) {
                return menu;
            }
        }
        return firstMenu;
    }

    /**
     * Every menu has to be register using this. Other wise there is no way to processor to get menus.
     * @param menu {@link com.hms.menu.Menu} implementation.
     */
    public void registerMenus(Menu menu) {
        menus.add(menu);
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

    }

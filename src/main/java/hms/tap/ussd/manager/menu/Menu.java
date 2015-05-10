package hms.tap.ussd.manager.menu;

import hms.tap.ussd.manager.Session;
import hms.tap.api.ussd.messages.MoUssdReq;

/**
 * This the interface for the ussd menus. Every menu have to implement this interface to work with ussd.manager framework.
 * {@link Menu#getMenuName()} will help to identify the menu by its unique name.
 * {@link Menu#getMessage(hms.tap.ussd.manager.Session, hms.tap.api.ussd.messages.MoUssdReq)} will help to get the menus message (i.e menu content).
 * {@link Menu#getNextMenu(hms.tap.ussd.manager.Session, hms.tap.api.ussd.messages.MoUssdReq)} this method will be called as soon as user send an input for this menu.
 * {@link com.hms.ussd.manager.menu.Menu#getOperationType()} please see the method doc.
 */
public interface Menu {

    /**
     * This implementation should return the menu message.
     * SEE {@link com.hms.ussd.manager.menu.Menu} Documentation for method calling order.
     *
     * @param session   {@link hms.tap.ussd.manager.Session} object.
     * @param moUssdReq {@link hms.tap.api.ussd.messages.MoUssdReq} depend on the user message they can change the
     *                  own message
     * @return
     */
    String getMessage(Session session, MoUssdReq moUssdReq);

    /**
     * This method have to return next menu name. Basically all the own menu validations should handle by this method.
     * According to the current input validation menu name can be change.
     * SEE {@link com.hms.ussd.manager.menu.Menu} Documentation for method calling order.
     *
     * @param session   {@link hms.tap.ussd.manager.Session} object.
     * @param moUssdReq {@link hms.tap.api.ussd.messages.MoUssdReq} depend on the user message they can change the menu
     *                  next menu name.
     * @return {@link java.lang.String} next menu name. This should match registered {@link com.hms.ussd.manager.menu.Menu}'s
     * getMenuId() value
     */
    String getNextMenu(Session session, MoUssdReq moUssdReq);

}

package com.hms.menu;

import com.hms.Session;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.messages.MoUssdReq;

/**
 * This the interface for the ussd menus. Every menu have to implement this interface to work with ussd.manager framework.
 * {@link Menu#getMenuName()} will help to identify the menu by its unique name.
 * {@link Menu#getMessage(com.hms.Session, hms.tap.api.ussd.messages.MoUssdReq)} will help to get the menus message (i.e menu content).
 * {@link Menu#getNextMenu(com.hms.Session, hms.tap.api.ussd.messages.MoUssdReq)} this method will be called as soon as user send an input for this menu.
 * {@link com.hms.menu.Menu#getOperationType()} please see the method doc.
 */
public interface Menu {

    /**
     * This implementation should return the Menu Name. Each menu must have a unique name.
     * Better way to get the unique name is Class.getName().
     *
     * @return {@link java.lang.String} class name
     */
    String getMenuName();

    /**
     * This implementation should return the menu message.
     * SEE {@link com.hms.menu.Menu} Documentation for method calling order.
     *
     * @param session   {@link com.hms.Session} object.
     * @param moUssdReq {@link hms.tap.api.ussd.messages.MoUssdReq} depend on the user message they can change the
     *                  own message
     * @return
     */
    String getMessage(Session session, MoUssdReq moUssdReq);

    /**
     * This method have to return next menu name. Basically all the own menu validations should handle by this method.
     * According to the current input validation menu name can be change.
     * SEE {@link com.hms.menu.Menu} Documentation for method calling order.
     *
     * @param session   {@link com.hms.Session} object.
     * @param moUssdReq {@link hms.tap.api.ussd.messages.MoUssdReq} depend on the user message they can change the menu
     *                  next menu name.
     * @return {@link java.lang.String} next menu name. This should match registered {@link com.hms.menu.Menu}'s
     * getMenuName() value
     */
    String getNextMenu(Session session, MoUssdReq moUssdReq);

    /**
     * This implementation have to return the own menu's {@link hms.tap.api.ussd.OperationType}.
     * According to this value menu will render with user input or final menu.
     * Every menu have to return either {@link hms.tap.api.ussd.OperationType#MT_CONT} or {@link hms.tap.api.ussd.OperationType#MT_FIN}.
     *
     * if its returns null, {@link com.hms.UssdMessageProcessor} assumes it is {@link hms.tap.api.ussd.OperationType#MT_FIN}
     *
     * @return Implemented menu OperationType.
     */
    OperationType getOperationType();


}

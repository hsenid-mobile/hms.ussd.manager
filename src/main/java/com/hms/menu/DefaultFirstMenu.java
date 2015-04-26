package com.hms.menu;

import com.hms.Session;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.messages.MoUssdReq;

/**
 * Created by rajive on 4/24/15.
 */
public class DefaultFirstMenu implements Menu {

    private static final String MENU_NAME=DefaultFirstMenu.class.getName();

    @Override
    public boolean validate(Session session, MoUssdReq moUssdReq) {
        return false;
    }

    @Override
    public String getMenuName() {
        return MENU_NAME;
    }

    @Override
    public String getMessage(Session session, MoUssdReq moUssdReq) {
        return "Welcome to the Default menu.";
    }

    @Override
    public String getNextMenu() {
        return null;
    }

    @Override
    public OperationType     getOperationType() {
        return OperationType.MT_FIN;
    }
}

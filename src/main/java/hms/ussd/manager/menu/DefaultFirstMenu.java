package hms.ussd.manager.menu;

import hms.ussd.manager.Session;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.messages.MoUssdReq;
import hms.ussd.manager.Session;
import hms.ussd.manager.annotations.Config;
import hms.ussd.manager.annotations.Index;

/**
 * Created by rajive on 4/24/15.
 */
@Index
public class DefaultFirstMenu implements Menu {

    private static final String MENU_NAME=DefaultFirstMenu.class.getName();

    @Override
    public String getMessage(Session session, MoUssdReq moUssdReq) {
        return "Welcome to the Default menu.";
    }

    @Override
    public String getNextMenu(Session session, MoUssdReq moUssdReq) {
        return null;
    }

}

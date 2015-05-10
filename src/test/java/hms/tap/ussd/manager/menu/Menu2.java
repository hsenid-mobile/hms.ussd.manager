package hms.tap.ussd.manager.menu;

import hms.tap.api.ussd.messages.MoUssdReq;
import hms.tap.ussd.manager.Session;
import hms.tap.ussd.manager.annotations.Config;

/**
 * Created by isuru on 5/10/15.
 */
@Config(id = "test", end = true)
public class Menu2 implements Menu {
    @Override
    public String getMessage(Session session, MoUssdReq moUssdReq) {
        return null;
    }

    @Override
    public String getNextMenu(Session session, MoUssdReq moUssdReq) {
        return null;
    }
}

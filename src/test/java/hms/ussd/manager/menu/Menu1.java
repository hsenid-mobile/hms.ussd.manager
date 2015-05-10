package hms.ussd.manager.menu;

import hms.tap.api.ussd.messages.MoUssdReq;
import hms.ussd.manager.Session;
import hms.ussd.manager.annotations.Index;

/**
 * Created by isuru on 5/10/15.
 */
@Index
public class Menu1 implements Menu {
    @Override
    public String getMessage(Session session, MoUssdReq moUssdReq) {
        return null;
    }

    @Override
    public String getNextMenu(Session session, MoUssdReq moUssdReq) {
        return null;
    }
}

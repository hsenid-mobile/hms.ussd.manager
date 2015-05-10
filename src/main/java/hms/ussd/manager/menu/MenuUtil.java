package hms.ussd.manager.menu;

import hms.tap.api.ussd.OperationType;
import hms.ussd.manager.annotations.Config;
import hms.ussd.manager.annotations.Index;

/**
 * Created by isuru on 5/10/15.
 */
public final class MenuUtil {

    public static boolean isIndexMenu(Menu menu) {
        if(menu.getClass().isAnnotationPresent(Index.class)) {
            return true;
        }
        return false;
    }

    public static String getMenuId(Menu menu) {
        if(menu.getClass().isAnnotationPresent(Config.class)) {
            if(menu.getClass().getAnnotation(Config.class).id() != null) {
                return menu.getClass().getAnnotation(Config.class).id();
            }
        }
        return menu.getClass().getName();
    }


    public static OperationType getOperationType(Menu menu) {
        if(menu.getClass().isAnnotationPresent(Config.class)) {
            return menu.getClass().getAnnotation(Config.class).end() ? OperationType.MT_FIN : OperationType.MT_CONT;
        }
        return OperationType.MT_CONT;
    }
}

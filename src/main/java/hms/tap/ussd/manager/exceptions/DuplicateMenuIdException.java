package hms.tap.ussd.manager.exceptions;

import hms.tap.ussd.manager.menu.Menu;

/**
 * Created by isuru on 5/10/15.
 */
public class DuplicateMenuIdException extends Exception {

    private final String message;

    public DuplicateMenuIdException(String menuId, Class<? extends Menu>... classes) {
        StringBuffer stringBuffer = new StringBuffer("Duplicate menu id " + menuId + "\n");
        if(classes != null) {
            for (Class<? extends Menu> aClass : classes) {
                stringBuffer.append(aClass).append("\n");
            }
        };
        message = stringBuffer.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

}

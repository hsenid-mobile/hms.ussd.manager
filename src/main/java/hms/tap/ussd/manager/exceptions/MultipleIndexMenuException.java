package hms.tap.ussd.manager.exceptions;

/**
 * Created by isuru on 5/10/15.
 */
public class MultipleIndexMenuException extends Exception {
    public MultipleIndexMenuException() {
        super("Multiple index menus were defined");
    }
}

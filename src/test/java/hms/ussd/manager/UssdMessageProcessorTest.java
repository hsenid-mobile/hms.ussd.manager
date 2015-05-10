package hms.ussd.manager;

import hms.ussd.manager.conf.AppConfig;
import hms.ussd.manager.exceptions.DuplicateMenuIdException;
import hms.ussd.manager.exceptions.IndexMenuNotDefinedException;
import hms.ussd.manager.exceptions.MultipleIndexMenuException;
import hms.ussd.manager.exceptions.UssdInitializationException;
import hms.ussd.manager.menu.DefaultFirstMenu;
import hms.ussd.manager.menu.Menu;
import hms.ussd.manager.repo.GuavaCacheSessionRepoImpl;
import junit.framework.TestCase;

import java.util.Arrays;

public class UssdMessageProcessorTest {
    public static void main(String[] args) {
        try {
            UssdMessageProcessor ussdMessageProcessor = new UssdMessageProcessor.Builder().
                    appConfig(new AppConfig.Builder().build()).
                    sessionRepo(new GuavaCacheSessionRepoImpl()).menus(Arrays.<Menu>asList(new DefaultFirstMenu())).build();
        } catch (UssdInitializationException e) {
            e.printStackTrace();
        } catch (MultipleIndexMenuException e) {
            e.printStackTrace();
        } catch (DuplicateMenuIdException e) {
            e.printStackTrace();
        } catch (IndexMenuNotDefinedException e) {
            e.printStackTrace();
        }
    }
}
package hms.ussd.manager;

import hms.ussd.manager.conf.AppConfig;
import hms.ussd.manager.exceptions.DuplicateMenuIdException;
import hms.ussd.manager.exceptions.IndexMenuNotDefinedException;
import hms.ussd.manager.exceptions.MultipleIndexMenuException;
import hms.ussd.manager.exceptions.UssdInitializationException;
import hms.ussd.manager.menu.*;
import hms.ussd.manager.repo.GuavaCacheSessionRepoImpl;
import junit.framework.TestCase;

import java.util.Arrays;

public class UssdMessageProcessorTest {
    public static void main(String[] args) {
        try {
            System.out.println(MenuUtil.getOperationType(Menu2.class.newInstance()));;

            UssdMessageProcessor ussdMessageProcessor = new UssdMessageProcessor.Builder().
                    appConfig(new AppConfig.Builder().build()).
                    sessionRepo(new GuavaCacheSessionRepoImpl()).menus(Arrays.<Menu>asList(Menu1.class.newInstance(), Menu2.class.newInstance(), Menu3.class.newInstance())).build();
        } catch (UssdInitializationException e) {
            e.printStackTrace();
        } catch (MultipleIndexMenuException e) {
            e.printStackTrace();
        } catch (DuplicateMenuIdException e) {
            e.printStackTrace();
        } catch (IndexMenuNotDefinedException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
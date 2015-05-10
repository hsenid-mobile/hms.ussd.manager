package hms.tap.ussd.manager.menu;

public class MenuUtilTest {

    public static void main(String[] args) {
        DefaultFirstMenu defaultFirstMenu = new DefaultFirstMenu();
        System.out.println(MenuUtil.isIndexMenu(defaultFirstMenu));
        System.out.println(MenuUtil.getOperationType(defaultFirstMenu));
        System.out.println(MenuUtil.getMenuId(defaultFirstMenu));
    }

}
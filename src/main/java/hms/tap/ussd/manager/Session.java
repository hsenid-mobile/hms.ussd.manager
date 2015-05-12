package hms.tap.ussd.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;


public class Session {

    private String sessionId;
    private String customerMobileNo;
    private Date lastAccessDate = new Date();

    private String currentMenuName;
    private String nextMenu;
    private boolean isAuthenticate = false;
    private String valueName;
    private String value;

    private Stack<String> accessedMenuNames = new Stack<String>();

    public void setValueHolders(List<ValueHolder> valueHolders) {
        this.valueHolders = valueHolders;
    }

    public List<ValueHolder> getValueHolders() {
        return valueHolders;
    }

    private List<ValueHolder> valueHolders = new ArrayList<ValueHolder>();

    public Session() {
        // TODO Auto-generated constructor stub
    }

    public Session(String sessionId, String customerMobileNo) {

        this.sessionId = sessionId;
        this.customerMobileNo = customerMobileNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getCurrentMenuName() {
        return currentMenuName;
    }

    public void setCurrentMenuName(String currentMenuName) {
        this.currentMenuName = currentMenuName;
    }

    public String getNextMenu() {
        return nextMenu;
    }

    public void setNextMenu(String nextMenu) {
        this.nextMenu = nextMenu;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }

    public Stack<String> getAccessedMenuNames() {
        return accessedMenuNames;
    }

    public boolean isAuthenticate() {
        return isAuthenticate;
    }

    public void setAuthenticate(boolean isAuthenticate) {
        this.isAuthenticate = isAuthenticate;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAccessedMenuNames(Stack<String> accessedMenuNames) {
        this.accessedMenuNames = accessedMenuNames;
    }


    public void addValueHolder(ValueHolder valueHolder) {
        valueHolders.add(valueHolder);
    }

    public String getValueByKey(String key) {

        for (ValueHolder valueHolder : valueHolders) {
            if (valueHolder.getKey() != null && valueHolder.getKey().equals(key))
                return valueHolder.getValue();
        }
        return null;
    }


}

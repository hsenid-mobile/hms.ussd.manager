package com.hms.menu;

import com.hms.Session;
import hms.tap.api.ussd.OperationType;
import hms.tap.api.ussd.messages.MoUssdReq;


public interface Menu {
		
	boolean validate(Session session, MoUssdReq moUssdReq);
	
	String getMenuName();
	
	String getMessage(Session session, MoUssdReq moUssdReq);
	
	String getNextMenu();
	
	OperationType getOperationType();
	
	
}

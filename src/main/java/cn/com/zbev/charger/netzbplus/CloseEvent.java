package cn.com.zbev.charger.netzbplus;

import java.util.EventObject;

public class CloseEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1840516617465730860L;

	public static final int USER_REQ =0;
	
	public static final int SERVER_REQ =1;
	
	public static final int CLIENT_REQ =2;

	public static final int INTERNAL = 3 ;
	
	private final String msg;
	private final int initiator;
	
	public CloseEvent(Object source,final int initiator,final String reason) {
		super(source);
		// TODO Auto-generated constructor stub
		this.initiator = initiator;
		this.msg = reason;
	}

	public final String getReason() {
		return msg;
	}
	
	/**
	 * 
	 * @return defined are {@link #USER_REQ},{@link #SERVER_REQ},{@link #INTERNAL}
	 */
	public final int getInitiator() {
		return initiator;
	}
	
}

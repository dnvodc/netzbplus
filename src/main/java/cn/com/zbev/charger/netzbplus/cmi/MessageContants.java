package cn.com.zbev.charger.netzbplus.cmi;

/*
 * 给通讯协议中来自客户的每类请求进行编号
 */
public final class MessageContants {

	public static final int CLIENT_LOGIN_REQ  = 0x01;
	
	public static final int CLIENT_HEARTBEAT_REQ = 0x00;
	
	public static final int CLIENT_DEVICE_INFO_SYNC_REQ = 0x02;
	
	public static final int CLIENT_CHARGER_WORK_DATA_UPLOAD_REQ = 0x03;
	
	public static final int CLIENT_EVENT_UPLOAD_REQ = 0x04;
	
	public static final int CLIENT_TIME_SYNC_REQ = 0x05;
	
	public static final int CLIENT_CHARGER_OPERATE_RES = 0x06;
	
	/**
	 * signal quality 
	 */
	public static final int CLIENT_ICCID_SQ_UPLOAD_REQ = 0x07;
	
	public static final int CLIENT_CHARGER_STATE_UPLOAD_REQ = 0x08;
	
	public static final int CLIENT_GATEWAY_PASSWORD_UPDATE_RES = 0x09;
	
	public static final int CLIENT_GATEWAY_MONITOR_DATA_UPLOAD_RES = 0x0A;
	
	public static final int CLIENT_GATEWAY_RECV_UPDATE_DATA_RES = 0x0B;
	
	public static final int CLIENT_READ_SOFT_VERSION_RES = 0x0C;
	
	public static final int CLIENT_USER_NO_CLOSE_AMOUNT_REQ = 0x0D;
	
	public static final int CLIENT_USER_READ_CARD_OPERATE_REQ = 0x0F;
	
	public static final int CLIENT_USER_CARD_INFO_REQ  = 0x10;
	
	public static final int CLIENT_ELEC_FEE_TABLE_REQ = 0x11;
	
	public static final int CLIENT_USER_CARD_CHARGE_REQ = 0x12;

	public static final int CLIENT_QUERY_USER_CARD_CONSUME_LIST_REQ = 0x13;
	
	public static final int CLIENT_PARAM_CFG_REQ  = 0x14;
	
	public static final int CLIENT_CHARGE_STATE_UPLOAD_REQ =0x15;
	
	public static final int CLIENT_SUM_MSG_REQ = 0x16;
	
	public static final int CLIENT_ERR_MSG_REQ = 0x17;
		
	public static final int CLIENT_FEE_TABLE_UPDATE_RES  = 0x18;
	
	public static final int CLIENT_RESTART_DEV_RES = 0x19; 

	public static final int CLIENT_CHARGE_RECORD_UPLOAD_REQ = 0xA0;
	
	public static final int CLIENT_GATEWAY_UPGRADE_CHARGE_REQ = 0xA1;
	
	public static final int CLIENT_UPDATE_CHARGER_PARAM_RES = 0xA2;
	
	
	
	
	
	
	
	
	
}

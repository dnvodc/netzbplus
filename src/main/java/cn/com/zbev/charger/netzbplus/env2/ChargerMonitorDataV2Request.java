package cn.com.zbev.charger.netzbplus.env2;

import java.io.UnsupportedEncodingException;


public class ChargerMonitorDataV2Request extends ENDataV2{
	
	/**
	 * 充电桩监控数据请求
	 */
	public static final int CHARGER_MONITOR_DATA_REQ = 0x04;
	
	private String gatewaySn;
	
	public ChargerMonitorDataV2Request (int msgId,int param,byte[] data) throws UnsupportedEncodingException {
		this.ctrl1= SERVER_REQ;
		this.ctrl2 = CHARGER_MONITOR_DATA_REQ;
		this.msgId = msgId;
		this.param = param;
		this.data = data;
		if (data != null) {
			this.dataLength = (short) data.length;
			
			this.gatewaySn = new String(data,"US-ASCII");	
		} 
		
	}

	public ChargerMonitorDataV2Request (int msgId,String gatewaySn) throws UnsupportedEncodingException {
		this.ctrl1= SERVER_REQ;
		this.ctrl2 = CHARGER_MONITOR_DATA_REQ;
		
		this.gatewaySn = gatewaySn;
		this.msgId = msgId;
		this.param = 0x01;
		this.data = gatewaySn.getBytes("US-ASCII");
		this.dataLength = this.data.length;
	}

	public String getGatewaySn() {
		return gatewaySn;
	}

	public void setGatewaySn(String gatewaySn) throws UnsupportedEncodingException {
		this.gatewaySn = gatewaySn;
		
		this.data = gatewaySn.getBytes("US-ASCII");
		this.dataLength = this.data.length;
		
	}
}

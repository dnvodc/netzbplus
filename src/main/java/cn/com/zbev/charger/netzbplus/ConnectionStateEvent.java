package cn.com.zbev.charger.netzbplus;

import java.util.EventObject;

public class ConnectionStateEvent extends EventObject{

	private String deviveSerialNo;
	private int deviceType;  //0=网关，1=充电桩
	
	public ConnectionStateEvent(Object source,String deviceSerialNo,int deviceType) {
		super(source);
		this.deviveSerialNo = deviceSerialNo;
		this.deviceType = deviceType;
		// TODO Auto-generated constructor stub
	}

	public String getDeviveSerialNo() {
		return deviveSerialNo;
	}

	public void setDeviveSerialNo(String deviveSerialNo) {
		this.deviveSerialNo = deviveSerialNo;
	}

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

}

package cn.com.zbev.charger.netzbplus.env2;

import java.nio.ByteBuffer;

public class StopChargeV2Request extends ENDataV2 {

	public static final int  CHARGER_ACTION_REQ = 0x04;

	private String chargerSerialNo;
	private String plugNo;		
	private String orderId;
	private long balance;
	
	public StopChargeV2Request(int msgId,int param,String chargerSerialNum, String plugNum, String orderId, long balance) {
		super(msgId,param,CHARGER_ACTION_REQ);
		this.balance = balance;
		this.chargerSerialNo = chargerSerialNum;		
		this.orderId = orderId;		
		this.plugNo = plugNum;
		
	}
	
	public String getChargerSerialNo() {
		return chargerSerialNo;
	}


	public void setChargerSerialNo(String chargerSerialNo) {
		this.chargerSerialNo = chargerSerialNo;
	}


	public String getPlugNo() {
		return plugNo;
	}


	public void setPlugNo(String plugNo) {
		this.plugNo = plugNo;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public long getBalance() {
		return balance;
	}


	public void setBalance(long balance) {
		this.balance = balance;
	}
	
	@Override
	public ByteBuffer getBuffer() {
		//产生 data ,dataLength;		
		
		return super.getBuffer();
		
	}
	
}

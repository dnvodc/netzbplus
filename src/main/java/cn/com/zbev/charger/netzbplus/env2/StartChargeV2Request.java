package cn.com.zbev.charger.netzbplus.env2;

import java.nio.ByteBuffer;

/**
 * 启动充电请求
 * @author wanwy 2018-08-02
 *
 */
public class StartChargeV2Request extends ENDataV2 {

	public static final int CHARGER_ACTION_REQ = 0x01;
	
	private String chargerSerialNo;
	private String plugNo;
	private int model;
	private int chargeTime;
	private int chargeEnergy;
	private String orderId;
	private long balance;
	
	
	/**
	 * 
	 * @param chargerSerialNum 充电桩编号 
	 * @param plugNum   充电枪编号,1,2,3,4...
	 * @param model  充电模式
	 * @param chargeTime 定时长充电
	 * @param chargeEnergy  定量充电 
	 * @param orderId  订单号
	 * @param balance  用户余额
	 */
	public StartChargeV2Request(int msgId,String chargerSerialNum, String plugNum, int model, int chargeTime,
										 int chargeEnergy, String orderId, long balance) {
		
		super(msgId,0x03,CHARGER_ACTION_REQ);
		
		this.balance = balance;
		this.chargerSerialNo = chargerSerialNum;
		this.chargeTime = chargeTime;
		this.orderId = orderId;
		this.chargeEnergy  = chargeEnergy;
		this.plugNo = plugNum;
		this.model = model;
		
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


	public int getModel() {
		return model;
	}


	public void setModel(int model) {
		this.model = model;
	}


	public int getChargeTime() {
		return chargeTime;
	}


	public void setChargeTime(int chargeTime) {
		this.chargeTime = chargeTime;
	}


	public int getChargeEnergy() {
		return chargeEnergy;
	}


	public void setChargeEnergy(int chargeEnergy) {
		this.chargeEnergy = chargeEnergy;
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

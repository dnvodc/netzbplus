package cn.com.zbev.charger.netzbplus.env1;

/**
 * 
 * @author wanwy
 *充电预约
 */
public class ChargeBookV1Request extends ChargeV1BaseRequest {
	
	public static final int PARAM_CODE = 0x04;
	
	public ChargeBookV1Request(int msgId, String chargerSerialNo) {
		super(msgId, PARAM_CODE, chargerSerialNo);
		// TODO Auto-generated constructor stub
		this.chargeEnergy = 0;
		this.chargeTime = 0;
		this.model = 0;
	}

}

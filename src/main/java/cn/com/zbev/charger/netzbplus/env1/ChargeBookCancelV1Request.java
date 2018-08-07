package cn.com.zbev.charger.netzbplus.env1;

/**
 * 
 * @author wanwy
 *取消充电预约
 */
public class ChargeBookCancelV1Request extends ChargeV1BaseRequest {

	public static final int PARAM_CODE = 0x06;
	
	public ChargeBookCancelV1Request(int msgId,  String chargerSerialNo) {
		super(msgId, PARAM_CODE, chargerSerialNo);
		this.chargeEnergy = 0;
		this.chargeTime = 0;
		this.model = 0;
		// TODO Auto-generated constructor stub
	}

	
	
}

package cn.com.zbev.charger.netzbplus.env1;



public class StartChargeV1Request extends ChargeV1BaseRequest {

	public static final int PARAM_CODE = 0x03;	
	
	/**
	 * 
	 * @param chargerSerialNum 充电桩编号 
	 * @param plugNum   充电枪编号,1,2,3,4...
	 * @param model  充电模式 1：充满为止； 2：定时充电；3：定量充电； 4：定金额充电（转换为充电量下发）；5：停车
	 * @param chargeTime 定时长充电
	 * @param chargeEnergy  定量充电 
	 * @param orderId  订单号
	 * @param balance  用户余额
	 */
	public StartChargeV1Request(int msgId,String chargerSerialNum,  int model, int chargeTime,
										 int chargeEnergy) {
		
		super(msgId,PARAM_CODE,chargerSerialNum);		
		
		this.chargeTime = chargeTime;		
		this.chargeEnergy  = chargeEnergy;		
		this.model = model;
		
	}


	public String getChargerSerialNo() {
		return chargerSerialNo;
	}


	public void setChargerSerialNo(String chargerSerialNo) {
		this.chargerSerialNo = chargerSerialNo;
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
	
	

	/*@Override
	public byte[] toByteArray()  {

		byte[] buff = new byte[22];		
			
		try {
			
			byte[] tmp = this.chargerSerialNo.getBytes("US-ASCII");
			for(int i =0;i<=16;i++) {
				buff[i] = tmp[i];
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		buff[16] = (byte)model;
		if (model == 2) {
			//
			buff[17]= (byte) ((chargeTime >>> 24) & 0xFF);
			buff[18]= (byte) ((chargeTime >>> 16) & 0xFF);
			buff[19]= (byte) ((chargeTime >>>  8) & 0xFF);
			buff[20]= (byte) ((chargeTime >>>  0) & 0xFF);
		}else {
			buff[17]= (byte) ((chargeEnergy >>> 24) & 0xFF);
			buff[18]= (byte) ((chargeEnergy >>> 16) & 0xFF);
			buff[19]= (byte) ((chargeEnergy >>>  8) & 0xFF);
			buff[20]= (byte) ((chargeEnergy >>>  0) & 0xFF);
		}
			
		this.data = buff;
		this.dataLength = 22;
		
		return super.toByteArray();
	}*/

	
}

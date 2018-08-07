package cn.com.zbev.charger.netzbplus.env1;

import java.io.UnsupportedEncodingException;

public class StopChargeV1Request extends ChargeV1BaseRequest {
	public static final int PARAM_CODE = 0x04;
		
	public StopChargeV1Request(int msgId,String chargerSerialNo) {
		super(msgId,0x04,chargerSerialNo);
		
		this.chargeEnergy = 0;
		this.chargeTime = 0;
		this.model = 0;
		
	}

	public String getChargerSerialNo() {
		return chargerSerialNo;
	}

	public void setChargerSerialNo(String chargerSerialNo) {
		this.chargerSerialNo = chargerSerialNo;
	}
	
	/*@Override
	public byte[] toByteArray() {
		
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
			
		buff[16] = 0;
		
			buff[17]= 0;
			buff[18]= 0;
			buff[19]= 0;
			buff[20]= 0;
		
			
		this.data = buff;
		this.dataLength = 22;
		
		return super.toByteArray();
		
		
	}*/
	
	
	
}

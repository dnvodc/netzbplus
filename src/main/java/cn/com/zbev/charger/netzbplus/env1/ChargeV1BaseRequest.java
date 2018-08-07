package cn.com.zbev.charger.netzbplus.env1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author wanwy
 *充电桩操作基类
 *操作命令 0x02,0x01,参数如下表
 * 0x01	开启车位锁/进入车位
 * 0x02	关闭车位锁/离开车位
 * 0x03	开始充电
 * 0x04	停止充电
 * 0x05	预约电桩
 * 0x06	取消预约
 */
public class ChargeV1BaseRequest extends ServerRequestV1 {
	
	public static final int ACTION_CODE = 0x01;
	
	protected String chargerSerialNo;
	protected int model;
	protected int chargeTime;
	protected int chargeEnergy;
	
	public ChargeV1BaseRequest(int msgId,int param,String chargerSerialNo) {
		this.ctrl2 = ACTION_CODE;
		this.msgId = msgId;
		this.param = param;
		this.chargerSerialNo = chargerSerialNo;
		this.model = 0;
		this.chargeTime = 0;
		this.chargeEnergy = 0;
		
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
	
	@Override
	public ByteBuffer getBuffer() throws UnsupportedEncodingException {		
		
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
		
		return super.getBuffer();
	}
	
}

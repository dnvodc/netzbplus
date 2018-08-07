package cn.com.zbev.charger.netzbplus.env1;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import cn.com.zbev.charger.netzbplus.utils.Util;

public class DataUpdateV1Request extends ClientRequestV1 {
	
	public static final int PARAM_CHARGING = 0x01;
	public static final int PARAM_PARKING = 0x02;
	public static final int ACTION_CODE = 0x03;
	
	private String chargerSerialNo;  //充电桩编号 16Byte
	private int plugNo;              //充电枪编号 1Byte
	private Date updateTime;       //上传时间 y,m,d,h,m,s 6Byte
	private int chargeTimeLength;    //充电时长 分钟 2Byte
	private int chargeVoltage;     //充电电压 2Byte	 
    private int chargeCurrent;    //充电电流 2Byte
    private int chargeEnergy;     //充电电量  4Byte          
    private int chargePower;      //充电功率  4Byte
    
    
    public DataUpdateV1Request() {
    	this.ctrl1 = CLIENT_REQ;
    	this.ctrl2 = ACTION_CODE;    	
    }
    
    public DataUpdateV1Request(int msgId,int param,byte[] data) {
    	this.ctrl1 = CLIENT_REQ;
    	this.ctrl2 = ACTION_CODE;
    	this.msgId = msgId;
    	this.param = param;
    	this.data = data;
    	this.dataLength = data.length;
    }
    
    private void Parser() {
    	
    	if (data != null) {
    		if (data.length >=16) {
    			try {
					this.chargerSerialNo =  new String (data,0,16,"US-ASCII");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    			
    		}
    		if (data.length >=17) {
    			plugNo = data[16];
    		}
    		
    		if (data.length >=23) {
    			this.updateTime = Util.getCalendar(data, 17).getTime();
    		}
    			
    		
    	}
    	
    }
    
	public String getChargerSerialNo() {
		return chargerSerialNo;
	}
	public void setChargerSerialNo(String chargerSerialNo) {
		this.chargerSerialNo = chargerSerialNo;
	}
	public int getPlugNo() {
		return plugNo;
	}
	public void setPlugNo(int plugNo) {
		this.plugNo = plugNo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getChargeTimeLength() {
		return chargeTimeLength;
	}
	public void setChargeTimeLength(int chargeTimeLength) {
		this.chargeTimeLength = chargeTimeLength;
	}
	public int getChargeVoltage() {
		return chargeVoltage;
	}
	public void setChargeVoltage(int chargeVoltage) {
		this.chargeVoltage = chargeVoltage;
	}
	public int getChargeCurrent() {
		return chargeCurrent;
	}
	public void setChargeCurrent(int chargeCurrent) {
		this.chargeCurrent = chargeCurrent;
	}
	public int getChargeEnergy() {
		return chargeEnergy;
	}
	public void setChargeEnergy(int chargeEnergy) {
		this.chargeEnergy = chargeEnergy;
	}
	public int getChargePower() {
		return chargePower;
	}
	public void setChargePower(int chargePower) {
		this.chargePower = chargePower;
	}

}

package cn.com.zbev.charger.netzbplus.env1;

public class DataUpdateV1Response extends ServerResponseV1 {

	public static final int ACTION_CODE = 0x03;
	
	private String chargerSerialNo;
	
	public DataUpdateV1Response(int msgId,int param,String chargerSerialNo) {
		this.msgId = msgId;
		this.param = param;
		this.ctrl2 = ACTION_CODE ;
		this.chargerSerialNo = chargerSerialNo;
		
	}

	public String getChargerSerialNo() {
		return chargerSerialNo;
	}

	public void setChargerSerialNo(String chargerSerialNo) {
		this.chargerSerialNo = chargerSerialNo;
	}
}

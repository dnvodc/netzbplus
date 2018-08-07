package cn.com.zbev.charger.netzbplus.env1;

public class HeartBeatV1Request extends ServerRequestV1 {

	public static final int ACTION_CODE = 0x00;
	
	public HeartBeatV1Request(int msgId) {
		this.msgId = msgId;		
		this.ctrl2 = ACTION_CODE;
		this.param =0;
		this.dataLength = 0;		
	}
}

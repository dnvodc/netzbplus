package cn.com.zbev.charger.netzbplus.env1;

public class ClientRequestV1 extends ENDataBaseV1{

	public ClientRequestV1() {
		this.ctrl1 = CLIENT_REQ;
	}
	
	public ClientRequestV1(int msgId,int param,byte[] data) {
		
		this.ctrl1 = CLIENT_REQ;
		this.msgId = msgId;
		this.param = param;
		this.data = data;
		this.dataLength = data.length;
	}
	
}

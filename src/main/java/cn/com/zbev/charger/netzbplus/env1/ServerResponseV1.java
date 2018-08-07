package cn.com.zbev.charger.netzbplus.env1;



public class ServerResponseV1 extends ENDataBaseV1 {
	
	public ServerResponseV1() {
		this.ctrl1 = SERVER_RES;
	}
	
	public ServerResponseV1(int msgId,int param,int ctrl2) {
				
		this.msgId = msgId;
		this.param = param;
		this.ctrl1 = SERVER_RES;
		this.ctrl2 = ctrl2;		
		
	}
	
}

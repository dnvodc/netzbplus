package cn.com.zbev.charger.netzbplus.env1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author wanwy
 *设备复位
 */
public class ResetV1Request extends ServerRequestV1 {

	public static final int ACTION_CODE = 0x0A;
	
	private String gatewaySerialNo;
	
	/**
	 * 
	 * @param msgId
	 * @param param 1=网关，2=充电桩
	 * @param gatewaySerialNo 网关序列号
	 */
	public ResetV1Request(int msgId,int param,String gatewaySerialNo) {
		this.ctrl2 = ACTION_CODE; 
		this.msgId = msgId;
		this.param = param;
		this.setGatewaySerialNo(gatewaySerialNo);
	}

	public String getGatewaySerialNo() {
		return gatewaySerialNo;
	}

	public void setGatewaySerialNo(String gatewaySerialNo) {
		this.gatewaySerialNo = gatewaySerialNo;
	}
	
	public ByteBuffer getBuffer() throws UnsupportedEncodingException  {
		
	
		this.data  =  this.gatewaySerialNo.getBytes("US-ASCII");
		this.dataLength = data.length;	
		
			
		return super.getBuffer();
	}
	
}

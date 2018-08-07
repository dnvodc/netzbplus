package cn.com.zbev.charger.netzbplus.env1;



import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;

public class ServerRequestV1 extends ENDataBaseV1 {

	
	public ServerRequestV1() {
		this.ctrl1 = SERVER_REQ;
	}
	
	public ServerRequestV1(int msgId,int param,int ctrl2) {
		this.msgId = msgId;
		this.param = param;
		this.ctrl1 = SERVER_REQ;
		this.ctrl2 = ctrl2;
	}
	
	

	/**
	 * Frame Structure 
	 * +---------+---------+-------+------+-------+---------+-------+
	 * + 起始标记 | 协议版本 | 操作码 | 参数 | MSGID | 数据长度 | 数据    | 
	 * +---------+---------+-------+------+-------+---------+-------+
	 * +  0xA5   |  1B     | 2B    |  1B  |  4B   |   2B    | 2000B |
	 * +---------+---------+-------+------+-------+---------+-------+
	 * 第一代协议没有校验和帧尾
	 */	
	public ByteBuffer getBuffer() throws UnsupportedEncodingException {
		
		//total length = 14 + datalength
		
		/*byte[] byteBuff = new byte[12+dataLength];
		int i = 0;
		byteBuff[i++] = BEG;
		byteBuff[i++] = (byte) getVersion();
		byteBuff[i++] = (byte) ctrl1;
		byteBuff[i++] = (byte) ctrl2;
		byteBuff[i++] = (byte) param;
		
		byteBuff[i++] = (byte) (msgId >> 24);
		byteBuff[i++] = (byte) (msgId >> 16);
		byteBuff[i++] = (byte) (msgId >> 8);
		byteBuff[i++] = (byte) (msgId);
		
		byteBuff[i++] = (byte) (dataLength >> 8);
		byteBuff[i++] = (byte) (dataLength);
		
		if (dataLength >0) {
			if (data != null) {
				for(i=0; i < (dataLength) ;i++) {
					byteBuff[i+12] =data[i];
				}
			}
		}
		//byteBuff[11+dataLength] = END;
		
		return byteBuff;*/

		return super.getBuffer();
	}

	
	
}

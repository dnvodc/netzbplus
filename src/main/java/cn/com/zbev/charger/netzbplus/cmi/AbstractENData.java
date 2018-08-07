package cn.com.zbev.charger.netzbplus.cmi;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;

public abstract class AbstractENData implements CMI {

	public static final int CLIENT_REQ = 0x01;  //客户端请求
	public static final int SERVER_RES = 0xA1;  //服务器应答
	public static final int SERVER_REQ = 0x02;  //服务器请求
	public static final int CLIENT_RES = 0xA2;  //客户端应答
	
	protected final byte BEG = (byte) 0xA5;
	protected final byte END =(byte) 0xED;
	
	protected int ctrl1;                   //操作码1,低字节
	protected int ctrl2;                    //操作码2,高字节
	protected int param;					//参数, 1Byte
	protected int msgId;					//消息ID, 由1开始递增, 4Byte
	
	protected int dataLength;				//数据长度, 2Byte
	protected byte[] data;					//数据区数据
	protected String gatewaySerialNo;
	
	
	private long currentTime;  //数据体创建时间，即数据接收时间,用于判断数据过期时间
	
	public AbstractENData() {
		
		currentTime  = System.currentTimeMillis();
		
	}
	
	public long getTime() {
		return currentTime;
		
	}
	
	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	
	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getParam() {
		return param;
	}

	public void setParam(int value) {
		this.param = value;
	}
	
	public int getCtrl1() {
		return this.ctrl1;
	}
	
	public void setCtrl1(byte value) {
		this.ctrl1 = value;
	}

	public int getCtrl2() {
		return this.ctrl2;
	}
	
	public void setCtrl2(byte value) {
		this.ctrl2 = value;
	}
	
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getProtocolCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getPayload() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMessageCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getStructLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Frame Structure 
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * + 起始标记 | 协议版本 | 操作码 | 参数 | MSGID | 数据长度 | 数据    | 校验码 | 帧尾  +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * +  0xA5   |  1B     | 2B    |  1B  |  4B   |   2B    | 2000B | 2B    | 0xED +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 */
	
	public ByteBuffer getBuffer() throws UnsupportedEncodingException {

				
		// TODO Auto-generated method stub
		return null;
	}
	              
	public String getGatewaySerialNo() {
		return gatewaySerialNo;
	}

	public void setGatewaySerialNo(String gatewaySerialNo) {
		this.gatewaySerialNo = gatewaySerialNo;
	}

}

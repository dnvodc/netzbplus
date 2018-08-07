package cn.com.zbev.charger.netzbplus.env2;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;

public class ENDataV2 extends AbstractENData {

	protected final byte BEG = (byte) 0xA5;
	protected final byte END =(byte) 0xED;
	
	protected int crc16;						//CRC16校验码
	
	public ENDataV2() {
		this.ctrl1 = SERVER_REQ;
	}
	
	public ENDataV2(int msgId,int param,int ctrl2) {
		this.msgId = msgId;
		this.param = param;
		this.ctrl1 = SERVER_REQ;
		this.ctrl2 = ctrl2;
	}
	
	
	public int getCrc16() {
		return crc16;
	}
	
	public void setCrc16(int value) {
		this.crc16 = value;
	}
	
	public int getVersion() {
		// TODO Auto-generated method stub
		return 2;
	}

	public String getProtocolCode() {
		// TODO Auto-generated method stub
		return "EN";
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

	
	public ByteBuffer getBuffer() {
		
		int size =11;
		if (data != null) {
			size = size + data.length;
		}
		
		ByteBuffer  buffer  =ByteBuffer.allocate(size) ;
		
		buffer.put(BEG);
		buffer.put((byte)getVersion());
		buffer.put((byte)ctrl1);
		buffer.put((byte)ctrl2);
		buffer.put((byte)param);
		buffer.putInt(msgId);
		buffer.putShort((short) dataLength);
		if (data != null) {
			
			buffer.put(data);
		}
		
		//buffer.put(b); CRC16;
		
		buffer.put(END);

		return buffer;
		
	}

	

	/**
	 * Frame Structure 
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * + 起始标记 | 协议版本 | 操作码 | 参数 | MSGID | 数据长度 | 数据    | 校验码 | 帧尾  +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * +  0xA5   |  1B     | 2B    |  1B  |  4B   |   2B    | 2000B | 2B    | 0xED +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 */
	
	/*public byte[] toByteArray() {
		// TODO Auto-generated method stub
		
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(BEG);
		os.write(getVersion());
		os.write(ctrl1);
		os.write(ctrl2);
		os.write(param);
		
		byte[] buf  = new byte[] { (byte) (msgId >> 24),(byte) (msgId >> 16),(byte) (msgId >> 8), (byte) dataLength };
		
		os.write(buf,0,buf.length);
		//无符号右移，高8位移到低8位
		buf = new byte[] { (byte) (dataLength >>> 8), (byte) dataLength };
		
		os.write(buf,0,buf.length);		
		
		os.write(data,0,data.length);
		//crc16
		buf = new byte[] { (byte) (crc16 >> 8), (byte) crc16 };
		os.write(buf,0,buf.length);
		
		os.write(END);
		
		return os.toByteArray();
		
	}*/
	
	 

}

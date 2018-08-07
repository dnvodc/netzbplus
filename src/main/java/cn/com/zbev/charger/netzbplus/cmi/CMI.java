package cn.com.zbev.charger.netzbplus.cmi;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author wanwy
 *Charger Message Interface
 */
public interface CMI {

	String getGatewaySerialNo();
	
	void setGatewaySerialNo(String value);
	/**
	 *    取得协议版本
	 * @return
	 */
	int getVersion();

	/**
	 * 取得协议代号，如EN,泰坦，聚电等
	 * @return
	 */
	String getProtocolCode();
	/**
	 * 数据内容
	 */
	byte[] getPayload();
	
	/**
	 * 指令代码
	 * @return
	 */
	int getMessageCode();
	
	/**
	 *  Message Byte length
	 * @return
	 */
	int getStructLength();
	
	/**
	 * Frame Structure 
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * + 起始标记 | 协议版本 | 操作码 | 参数 | MSGID | 数据长度 | 数据    | 校验码 | 帧尾  +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * +  0xA5   |  1B     | 2B    |  1B  |  4B   |   2B    | 2000B | 2B    | 0xED +
	 * +---------+---------+-------+------+-------+---------+-------+-------+------+
	 * @throws UnsupportedEncodingException 
	 */
	ByteBuffer getBuffer() throws UnsupportedEncodingException;
	
}

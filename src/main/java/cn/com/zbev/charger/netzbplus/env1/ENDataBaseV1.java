package cn.com.zbev.charger.netzbplus.env1;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;

public class ENDataBaseV1 extends AbstractENData {

	public ENDataBaseV1() {}
	
	@Override
	public ByteBuffer getBuffer() throws UnsupportedEncodingException {
		
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
		}*/
		
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

		return buffer;
		
		
	}
	
}

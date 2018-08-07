package cn.com.zbev.charger.netzbplus.ProtocolParser;

public interface ProtocolFactoryInterface {

	//通过识别协议的标志位，识别出不同品牌充电桩协议
	String getProtocolCode(byte[] buffer);
	
	IENProtocolParser getParser(byte[] buffer);
}

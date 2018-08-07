package cn.com.zbev.charger.netzbplus.ProtocolParser;

import java.util.List;

import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;
import cn.com.zbev.charger.netzbplus.cmi.CMI;

public interface IENProtocolParser {

	/*
	 * 对Buffer 操作后的新的offset
	 */
	int getCurrentOffset();	
	
	List<AbstractENData> Parser(byte[] buffer) ;
	
	//是否本协议格式
	boolean isSelf(byte[] buffer);
	
	String getProtocolCode();
}

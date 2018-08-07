package cn.com.zbev.charger.netzbplus.ProtocolParser;

import java.util.List;

import cn.com.zbev.charger.netzbplus.cmi.AbstractENData;


public class ENProtocolV1Parser implements IENProtocolParser {

	private static final ENProtocolV1Parser instance = new ENProtocolV1Parser();
	
	public static ENProtocolV1Parser getInstance() {
		
		return instance;
	}
	
	public int getCurrentOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<AbstractENData> Parser(byte[] buffer) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSelf(byte[] buffer) {
		// TODO Auto-generated method stub
		return false;
	}

	public String getProtocolCode() {
		// TODO Auto-generated method stub
		return "EN";
	}

}

package cn.com.zbev.charger.netzbplus.ProtocolParser;

public class ProtocolFactory implements ProtocolFactoryInterface {

	 
	
	public String getProtocolCode(byte[] buffer) {
		// TODO Auto-generated method stub
		
		if (ENProtocolV1Parser.getInstance().isSelf(buffer)) {
			return ENProtocolV1Parser.getInstance().getProtocolCode();
		}else{
			return "";
		}
		
	}

	public IENProtocolParser getParser(byte[] buffer) {
		// TODO Auto-generated method stub
		
		if (ENProtocolV1Parser.getInstance().isSelf(buffer)) {
			return ENProtocolV1Parser.getInstance();
		}	
		else if (ENProtocolV2Parser.getInstance().isSelf(buffer)) {
			return ENProtocolV2Parser.getInstance();
		}else {		
			return null;
		}
	}

}

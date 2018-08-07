package cn.com.zbev.charger.netzbplus.tcp;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import cn.com.zbev.charger.netzbplus.ProtocolParser.IENProtocolParser;

public class ClientConnection {

	private byte[] remainingData ;
	//public ByteBuffer writeBuffer;
	
	private SocketChannel channel;
	//public SelectionKey key;
	
	private boolean isLogin;
	private IENProtocolParser parser;
	private int protocolVersion;
	private long lastHeartbeatTime;
	private long lastIOExceptionTime;

	private String gatewaySerialNo;
	private String gatewayCompanyCode;
	private long gatewayStationId;
	private boolean isNeedResetAuthKey;
	

	public ClientConnection(SocketChannel channel) {
		this.channel = channel;
		
	}
	
	public SocketChannel getChannel() {
		
		return channel;
	}	
	
	public IENProtocolParser  getParser() {
		return this.parser;
	}
	
	
	
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getGatewaySerialNo() {
		return gatewaySerialNo;
	}
	public void setGatewaySerialNo(String gatewaySerialNo) {
		this.gatewaySerialNo = gatewaySerialNo;
	}
	public String getGatewayCompanyCode() {
		return gatewayCompanyCode;
	}
	public void setGatewayCompanyCode(String gatewayCompanyCode) {
		this.gatewayCompanyCode = gatewayCompanyCode;
	}
	public long getGatewayStationId() {
		return gatewayStationId;
	}
	public void setGatewayStationId(long gatewayStationId) {
		this.gatewayStationId = gatewayStationId;
	}

	//存放未解析后完的数据
	public byte[] getRemainingData() {
		return remainingData;
	}

	public void setRemainingData(byte[] remainingData) {
		this.remainingData = remainingData;
	}
	
	
}

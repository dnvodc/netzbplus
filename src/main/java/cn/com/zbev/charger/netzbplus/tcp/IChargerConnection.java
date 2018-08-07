package cn.com.zbev.charger.netzbplus.tcp;

import cn.com.zbev.charger.netzbplus.cmi.CMI;

public interface IChargerConnection {
	
	void addConnectionListener(ChargerListener l);
	
	void removeConnectionListener(ChargerListener l);
	
	void send(CMI frame);
	
	void close();
}

package cn.com.zbev.charger.netzbplus.tcp;



public interface ServerInterface {

	void start();
	
	void stop();
	
	boolean isStarted();
	
	void join() throws InterruptedException;
	
	void setMaxThreadSize(int size);
	
	
	void addChargerListener(ChargerListener l);

	/**
	 * Removes the specified event listener <code>l</code>, so it does no longer
	 * receive events from this link.
	 * <p>
	 * If <code>l</code> was not added in the first place, no action is performed.
	 * 
	 * @param l the listener to remove
	 */
	void removeChargerListener(ChargerListener l);
}

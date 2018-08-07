package cn.com.zbev.charger.netzbplus;

import java.util.EventObject;

import cn.com.zbev.charger.netzbplus.cmi.CMI;

public class FrameEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4699689210297300985L;

	private final byte[] b;
	private final String hexText;
	private final CMI c;
	
	public FrameEvent(Object source,final CMI frame) {
		super(source);
		// TODO Auto-generated constructor stub
		c = frame;
		b = null;
		hexText = null;
	}
	
	public FrameEvent(Object source,final byte[] frame) {
		super(source);
		// TODO Auto-generated constructor stub
		c = null;
		b = frame;
		hexText = null;
	}
	
	public FrameEvent(Object source,final String frame) {
		super(source);
		// TODO Auto-generated constructor stub
		c = null;
		b = null;
		hexText = frame;
	}
	
	public final byte[] getFrameBytes() {
		return b != null?(byte[])b.clone():null;
	}
	
	public final String getFrameHex() {
		return hexText != null ? hexText : null;
	}

}

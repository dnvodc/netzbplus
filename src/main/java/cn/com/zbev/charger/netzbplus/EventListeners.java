package cn.com.zbev.charger.netzbplus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public class EventListeners {

	private final List<EventListener> listeners = new ArrayList<EventListener>();
	private EventListener[] listenersCopy = new EventListener[0];

	public EventListeners() {
		
	}
	
	public void add(final EventListener l) {
		if (l == null) {
			
			return ;
		}
		synchronized (listeners) {
			if (!listeners.contains(l)) {
				listeners.add(l);
				
				listenersCopy = listeners.toArray(new EventListener[listeners.size()]);
				
			}
		}
	}
	
	public void remove(final EventListener l) {
		synchronized(listeners) {
			if (listeners.remove(l)) {
				listenersCopy = listeners.toArray(new EventListener[listeners.size()]);
			}
		}
	}
	
	public void removeAll() {
		synchronized (listeners) {
			listeners.clear();
			listenersCopy = new EventListener[0];
		}
	}

	public EventListener[] listeners() {
		return listenersCopy;
	}
	
	public Iterator<EventListener> iterator() {
		return Arrays.asList(listenersCopy).iterator();
	}
}

package de.macbury.zanbox.events;

import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 08.06.14.
 */
public class EventsManager {
  private Array<Event> events;
  private Array<EventListener> listeners;
  public EventsManager() {
    events    = new Array<Event>();
    listeners = new Array<EventListener>();
  }

  public void subscribe(EventListener listener) {
    if (!listeners.contains(listener, true))
      listeners.add(listener);
  }

  public void clear() {
    listeners.clear();
    events.clear();
  }

  public void unsubscribe(EventListener listener) {
    if (listeners.contains(listener, true))
      listeners.removeIndex(listeners.indexOf(listener, true));
  }
  public void trigger(Event event) {
    synchronized (events) {
      events.add(event);
    }
  }

  public void tick() {
    while(events.size >= 1) {
      Event event = events.removeIndex(0);
      for(EventListener listener : listeners) {
        listener.onEvent(event);
      }
    }
  }
}

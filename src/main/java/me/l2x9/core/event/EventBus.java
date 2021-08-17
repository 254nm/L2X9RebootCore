package me.l2x9.core.event;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private final ConcurrentHashMap<Listener, List<Method>> listeners = new ConcurrentHashMap<>();

    public void subscribe(Listener listener) {
        listeners.put(listener, listener.getMethodsByPrio());
    }

    public void unSubscribe(Listener listener) {
        listeners.remove(listener);
    }

    public synchronized void post(Event event) {
        listeners.forEach((listener, methods) -> methods.forEach(method -> {
            try {
                Class<?> param = method.getParameters()[0].getType();
                if (param.equals(event.getClass())) {
                    method.invoke(listener, event);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }));
    }
}

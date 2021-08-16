package ftp.sh.core.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//For tagging all event listeners
public interface Listener {
    default List<Method> getMethodsByPrio() {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.getClass().getMethods()) {
            if (!method.isAccessible()) method.setAccessible(true);
            if (method.isAnnotationPresent(CustomEventHandler.class)) {
                if (method.getParameterCount() == 1) {
                    Class<?> param = method.getParameters()[0].getType();
                    if (param.getSuperclass().equals(Event.class) || param.getSuperclass().getSuperclass().equals(Event.class)) {
                        methods.add(method);
                    }
                }
            }
        }
        return methods
                .stream()
                .sorted(Comparator.comparing(m -> m.getAnnotation(CustomEventHandler.class).priority()))
                .collect(Collectors.toList()); //Return all methods tagged with @EventHandler sorted by priority
    }
}

package org.tryImpl.framework.transaction;

import java.util.HashMap;
import java.util.Map;

public abstract class TransactionSynchronizationManager {

    private static final ThreadLocal<Map<Object, Object>> resources =
            new ThreadLocal<>();

    public static Object getResource(Object key) {
        Object value = doGetResource(key);
        return value;
    }

    private static Object doGetResource(Object actualKey) {
        Map<Object, Object> map = resources.get();
        if (map == null) {
            return null;
        }
        Object value = map.get(actualKey);
        return value;
    }

    public static void bindResource(Object key, Object value) throws IllegalStateException {
        Map<Object, Object> map = resources.get();
        // set ThreadLocal Map if none found
        if (map == null) {
            map = new HashMap<>();
            resources.set(map);
        }
        Object oldValue = map.put(key, value);
        if (oldValue != null) {
            throw new IllegalStateException("Already value [" + oldValue + "] for key [" +
                    key + "] bound to thread [" + Thread.currentThread().getName() + "]");
        }
    }

    public static Object unbindResource(Object key) throws IllegalStateException {
        Object value = doUnbindResource(key);
        if (value == null) {
            throw new IllegalStateException(
                    "No value for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
        }
        return value;
    }

    private static Object doUnbindResource(Object actualKey) {
        Map<Object, Object> map = resources.get();
        if (map == null) {
            return null;
        }
        Object value = map.remove(actualKey);
        // Remove entire ThreadLocal if empty...
        if (map.isEmpty()) {
            resources.remove();
        }
        return value;
    }
}

package ftp.sh.core;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ViolationManager {
    private final ConcurrentHashMap<UUID, Integer> map;
    private final int addAmount;

    public ViolationManager(int addAmount) {
        this.addAmount = addAmount;
        map = new ConcurrentHashMap<>();
        L2X9RebootCore.getInstance().registerViolationManager(this);
    }

    public void decrementAll() {
        map.forEach((key, val) -> {
            if (val <= 0) {
                map.remove(key);
                return;
            }
            map.replace(key, val - addAmount);
        });
    }

    public void increment(UUID uuid) {
        if (!map.containsKey(uuid)) {
            map.put(uuid, 0);
        } else map.replace(uuid, map.get(uuid) + addAmount);
    }

    public int getVLS(UUID id) {
        return map.getOrDefault(id, -1);
    }

    public void remove(UUID id) {
        map.remove(id);
    }
}

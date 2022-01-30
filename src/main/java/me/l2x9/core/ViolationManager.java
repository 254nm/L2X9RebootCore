package me.l2x9.core;

import me.l2x9.core.L2X9RebootCore;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ViolationManager {
    private final ConcurrentHashMap<Integer, Integer> map;
    private final int addAmount;
    private int removeAmount;

    public ViolationManager(int addAmount) {
        this.addAmount = addAmount;
        this.removeAmount = addAmount;
        map = new ConcurrentHashMap<>();
        L2X9RebootCore.getInstance().registerViolationManager(this);
    }
    public ViolationManager(int addAmount, int removeAmount) {
        this.addAmount = addAmount;
        this.removeAmount = removeAmount;
        map = new ConcurrentHashMap<>();
        L2X9RebootCore.getInstance().registerViolationManager(this);
    }

    public void decrementAll() {
        map.forEach((key, val) -> {
            if (val <= removeAmount) {
                map.remove(key);
                return;
            }
            map.replace(key, val - removeAmount);
        });
    }

    public void increment(int uuid) {
        if (!map.containsKey(uuid)) {
            map.put(uuid, 0);
        } else map.replace(uuid, map.get(uuid) + addAmount);
    }

    public int getVLS(int id) {
        return map.getOrDefault(id, -1);
    }

    public void remove(int id) {
        map.remove(id);
    }
}

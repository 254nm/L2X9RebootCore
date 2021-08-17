package me.l2x9.core.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}

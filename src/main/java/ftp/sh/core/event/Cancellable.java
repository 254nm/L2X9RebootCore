package ftp.sh.core.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}

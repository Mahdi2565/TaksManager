package ir.mahdidev.taksmanager.util;

public class EventBusSearchEvent {
    public final String searchMessage;
    public EventBusSearchEvent(String message) {
        this.searchMessage = message;
    }
}

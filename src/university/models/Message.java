package university.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private final String from;
    private final String to;
    private final String text;
    private final LocalDateTime createdAt;

    public Message(String from, String to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
        this.createdAt = LocalDateTime.now();
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

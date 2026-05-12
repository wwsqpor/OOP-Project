package university.models;

import java.io.Serializable;

public class Complaint implements Serializable {
    private final String author;
    private final String targetId;
    private final String text;

    public Complaint(String author, String targetId, String text) {
        this.author = author;
        this.targetId = targetId;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getText() {
        return text;
    }
}

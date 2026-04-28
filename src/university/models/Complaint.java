package university.models;

import java.io.Serializable;

public class Complaint implements Serializable {
    private final String author;
    private final String text;

    public Complaint(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }
}

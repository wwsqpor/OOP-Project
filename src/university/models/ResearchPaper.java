package university.models;

import java.io.Serializable;
import java.time.LocalDate;

public class ResearchPaper implements Serializable {
    private final String title;
    private final String author;
    private final LocalDate publishedAt;
    private final int citations;
    private final int pages;

    public ResearchPaper(String title, String author, LocalDate publishedAt, int citations, int pages) {
        this.title = title;
        this.author = author;
        this.publishedAt = publishedAt;
        this.citations = citations;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public int getCitations() {
        return citations;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (citations=" + citations + ", pages=" + pages + ")";
    }
}

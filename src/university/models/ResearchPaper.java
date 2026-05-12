package university.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;

public class ResearchPaper implements Serializable {
    public static final Comparator<ResearchPaper> BY_DATE_PUBLISHED = Comparator
            .comparing(ResearchPaper::getPublishedAt);
    public static final Comparator<ResearchPaper> BY_CITATIONS = Comparator.comparingInt(ResearchPaper::getCitations);
    public static final Comparator<ResearchPaper> BY_PAGES = Comparator.comparingInt(ResearchPaper::getPages);

    private final String title;
    private final String author;
    private final String journal;
    private final String doi;
    private final LocalDate publishedAt;
    private final int citations;
    private final int pages;

    public ResearchPaper(String title, String author, String journal, String doi, LocalDate publishedAt, int citations,
            int pages) {
        this.title = title;
        this.author = author;
        this.journal = journal;
        this.doi = doi;
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

    public String getJournal() {
        return journal;
    }

    public String getDoi() {
        return doi;
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
        return String.format("%s by %s in %s (DOI: %s, citations=%d, pages=%d)",
                title, author, journal, doi, citations, pages);
    }
}

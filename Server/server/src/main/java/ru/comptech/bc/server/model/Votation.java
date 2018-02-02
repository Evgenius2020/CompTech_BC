package ru.comptech.bc.server.model;

import java.util.Date;

public class Votation {

    private final long id;

    private final String title;
    private final String description;

    private final String[] options;
    private final int votesCount;

    private final String author;

    private final Date start;
    private final Date end;

    private final boolean active;

    public Votation(long id,
                    String title, String description,
                    String rawOptions, int optionsCount, int votesCount,
                    String author,
                    Date start, Date end,
                    boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.options = rawOptions.split(",");
        this.votesCount = votesCount;
        this.author = author;
        this.start = start;
        this.end = end;
        this.active = active;

        if (id == 0)
            throw new IllegalArgumentException("Bad id");

        if (options.length != optionsCount)
            throw new IllegalArgumentException(
                    String.format("Wrong options count: expected %d got %d",
                            optionsCount, options.length));
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String[] getOptions() {
        return options;
    }

    public int getVotesCount() {
        return votesCount;
    }

    public String getAuthor() {
        return author;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public boolean isActive() {
        return active;
    }
}

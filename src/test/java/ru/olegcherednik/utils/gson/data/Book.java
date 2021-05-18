package ru.olegcherednik.utils.gson.data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author Oleg Cherednik
 * @since 08.01.2021
 */
@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
public final class Book {

    private String title;
    private ZonedDateTime date;
    private int year;
    private List<String> authors;

    public Book() {
    }

    public Book(String title, ZonedDateTime date, int year, List<String> authors) {
        this.title = title;
        this.date = date;
        this.year = year;
        this.authors = authors;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Book))
            return false;

        Book book = (Book)obj;
        return year == book.year && title.equals(book.title) && date.equals(book.date) && authors.equals(book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, year, authors);
    }

}

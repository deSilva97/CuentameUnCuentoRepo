package es.unir.cuentameuncuento.models;

import java.util.Objects;

public class Book {
    private String id;
    private String title;
    private String narrative;
    private boolean favorite;
    private String fk_user;

    public Book() {
    }

    public Book(String id, String title, String narrative, boolean favorite, String fk_user) {
        this.id = id;
        this.title = title;
        this.narrative = narrative;
        this.favorite = favorite;
        this.fk_user = fk_user;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getFk_user() {
        return fk_user;
    }

    public void setFk_user(String fk_user) {
        this.fk_user = fk_user;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", narrative='" + narrative + '\'' +
                ", favorite=" + favorite +
                '}';
    }


}

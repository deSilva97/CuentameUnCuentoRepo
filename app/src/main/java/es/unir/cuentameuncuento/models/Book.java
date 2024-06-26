package es.unir.cuentameuncuento.models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Book implements Serializable {
    private String id;
    private String title;
    private String narrative;
    private boolean favorite;
    private String fk_user;

    private String iconID;

    private Date date;


    public Book(String id, String title, String narrative, boolean favorite, String fk_user, String iconID, Date date) {
        this.id = id;
        this.title = title;
        this.narrative = narrative;
        this.favorite = favorite;
        this.fk_user = fk_user;
        this.iconID = iconID;
        this.date = date;
    }

    public Book() {
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getIconID() {
        return iconID;
    }

    public void setIconID(String iconID) {
        this.iconID = iconID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", narrative='" + narrative + '\'' +
                ", favorite=" + favorite +
                ", fk_user='" + fk_user + '\'' +
                ", iconID='" + iconID + '\'' +
                ", date=" + date +
                '}';
    }
}

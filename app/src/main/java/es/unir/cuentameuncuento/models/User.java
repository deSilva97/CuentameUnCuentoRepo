package es.unir.cuentameuncuento.models;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private List<Book> bookList;

    public User() {
    }
    public User(String id, String name, String email, String password, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.bookList = bookList;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", bookList=" + bookList +
                '}';
    }
}

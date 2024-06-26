package es.unir.cuentameuncuento.adapters;

import android.graphics.Bitmap;

import java.io.Serializable;

import es.unir.cuentameuncuento.controllers.MainController;
import es.unir.cuentameuncuento.managers.SessionManager;
import es.unir.cuentameuncuento.models.Book;

public class BookAdapterElement implements Serializable {

    MainController controller;

    Book book;
    Bitmap icon;

    private String textTitle;

    public BookAdapterElement(){

    }

    public BookAdapterElement(MainController controller, Book book, Bitmap icon, String textTitle) {
        this.controller = controller;
        this.book = book;
        this.icon = icon;
        this.textTitle = textTitle;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Bitmap getIcon(){
        return icon;
    }

    public void setIcon(Bitmap bitmap){
        icon = bitmap;
    }

    public void actionReadBook(){
        SessionManager.currentStory = this;
        controller.readBook(book);
    }

    public void actionDelete(){
        controller.confirmDeleteBook(book);
    }
}

package es.unir.cuentameuncuento.adapters;

import android.graphics.drawable.Icon;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import es.unir.cuentameuncuento.controllers.MainController;
import es.unir.cuentameuncuento.models.Book;

public class BookAdapterElement {

    MainController controller;
    Book book;


    private Icon iconImage;
    private String textTitle;

    public BookAdapterElement(MainController controller, Book book, int iconResourceID, String textTitle) {
        this.controller = controller;
        this.book = book;
        this.textTitle = textTitle;
    }

    public Icon getIconImage() {
        return iconImage;
    }

    public void setIconImage(Icon iconImage) {
        this.iconImage = iconImage;
    }

    public String getTextTitle() {
        return textTitle;
    }

    public void setTextTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public void actionReadBook(){
//        Log.w("BookAdapterElement", "Not implemented method: actionReadBook");
        controller.readBook(book);
    }

    public void actionDelete(){
        controller.confirmDeleteBook(book.getId());
    }

    public void actionFavorite() {
        Log.w("BookAdapterElement", "Not implemented method: actionFavorite");
    }
}

package es.unir.cuentameuncuento.adapters;

import android.graphics.drawable.Icon;
import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListElement {

    private Icon iconImage;
    private String textTitle;

    private Button bAction;
    private Button bFavorite;
    private Button bDelete;

    public ListElement(int iconResourceID, String textTitle) {
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
}

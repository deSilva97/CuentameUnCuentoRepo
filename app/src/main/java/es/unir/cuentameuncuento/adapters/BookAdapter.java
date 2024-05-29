package es.unir.cuentameuncuento.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unir.cuentameuncuento.R;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<BookAdapterElement> mData;
    private final LayoutInflater mInflater;

    public BookAdapter(List<BookAdapterElement> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemList != null ? itemList : new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookAdapter.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<BookAdapterElement> items) {
        mData = items != null ? items : new ArrayList<>();
    }

    public void addItem(BookAdapterElement element) {
        if (mData != null) {
            mData.add(element);
            notifyItemInserted(mData.size() - 1);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImage;
        TextView title;
        ImageButton bDelete;
        ImageButton bFavorite;
        LinearLayout bSelect;

        @SuppressLint("WrongViewCast")
        ViewHolder(View itemView) {
            super(itemView);

            iconImage = itemView.findViewById(R.id.book_icon_container);
            title = itemView.findViewById(R.id.book_title_container);
            bDelete = itemView.findViewById(R.id.btn_delete_book_container);
            bSelect = itemView.findViewById(R.id.cardViewBook);
        }

        void bindData(final BookAdapterElement item) {
            if (item != null) {
                title.setText(item.getTextTitle());
                Bitmap icon = item.getIcon();
                if (icon != null) {
                    iconImage.setImageBitmap(icon);
                } else {
                    iconImage.setImageResource(R.drawable.book_placeholder);
                }

                bSelect.setOnClickListener(null);
                bSelect.setOnClickListener(v -> item.actionReadBook());

                bDelete.setOnClickListener(null);
                bDelete.setOnClickListener(v -> item.actionDelete());
            }
        }
    }
}

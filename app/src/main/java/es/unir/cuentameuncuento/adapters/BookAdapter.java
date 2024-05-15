package es.unir.cuentameuncuento.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unir.cuentameuncuento.R;
import es.unir.cuentameuncuento.impls.BookDAOImpl;
import es.unir.cuentameuncuento.impls.IconStorageDAOImpl;
import es.unir.cuentameuncuento.impls.UserDAOImpl;

//https://www.youtube.com/watch?v=HrZgfoBeams

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<BookAdapterElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public BookAdapter(List<BookAdapterElement> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public  int getItemCount(){
        return mData.size();
    }

    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.book_container, null);
        return new BookAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BookAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<BookAdapterElement> items){mData = items;}

    public void addItem(BookAdapterElement element) {
        mData.add(element);
        notifyItemInserted(mData.size() - 1);
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView title;

        ImageButton bDelete;
        ImageButton bFavorite;

        LinearLayout bSelect;

        View itemView;

        @SuppressLint("WrongViewCast")
        ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;

            iconImage = itemView.findViewById(R.id.book_icon_container);
            title = itemView.findViewById(R.id.book_title_container);

            bDelete = itemView.findViewById(R.id.btn_delete_book_container);
            bSelect = itemView.findViewById(R.id.cardViewBook);

            itemView.setVisibility(View.GONE);

        }

        void bindData(final BookAdapterElement item){
            item.setItemView(itemView);
            item.setIconImage(iconImage);

            title.setText(item.getTextTitle());
            iconImage.setImageBitmap(item.getIcon());
            bSelect.setOnClickListener(null);
            bSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.actionReadBook();
                }
            });

            bDelete.setOnClickListener(null);
            bDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Accion delete", Toast.LENGTH_SHORT).show();
                    item.actionDelete();
                }
            });
        }
    }
}

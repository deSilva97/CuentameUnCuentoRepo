package es.unir.cuentameuncuento.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unir.cuentameuncuento.R;

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

    public class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView title;

        ImageButton bDelete;
        ImageButton bFavorite;

        Button bSelect;

        @SuppressLint("WrongViewCast")
        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.book_icon_container);
            title = itemView.findViewById(R.id.book_title_container);

            bFavorite = itemView.findViewById(R.id.btn_favorite_book_container);
            bDelete = itemView.findViewById(R.id.btn_delete_book_container);
            bSelect = itemView.findViewById(R.id.btn_select_book_container);
        }

        void bindData(final BookAdapterElement item){
            iconImage.setImageResource(R.drawable.book_placeholder);
            title.setText(item.getTextTitle());

            bSelect.setOnClickListener(null);
            bSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.actionReadBook();
                }
            });

            bFavorite.setOnClickListener(null);
            bFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(itemView.getContext(), "Accion favorite", Toast.LENGTH_SHORT).show();
                    item.actionFavorite();
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

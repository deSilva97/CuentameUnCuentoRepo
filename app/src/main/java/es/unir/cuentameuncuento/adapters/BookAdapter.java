package es.unir.cuentameuncuento.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.unir.cuentameuncuento.R;

//https://www.youtube.com/watch?v=HrZgfoBeams

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    public BookAdapter(List<ListElement> itemList, Context context){
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

    public void setItems(List<ListElement> items){mData = items;}

    public class  ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView title;

        Button buttonAction;
        Button buttonDelete;
        Button buttonFavorite;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.book_icon_container);
            title = itemView.findViewById(R.id.book_title_container);
        }

        void bindData(final ListElement item){
            iconImage.setImageResource(R.drawable.book_placeholder);
            title.setText(item.getTextTitle());
        }
    }
}

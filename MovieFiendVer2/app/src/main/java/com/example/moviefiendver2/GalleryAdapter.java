package com.example.moviefiendver2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviefiendver2.helper.ImageLoadTask;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;
    ArrayList<String> items = new ArrayList<>();    //String인 이유는 photos가 서버에 String으로 돼있기 때문이다.
    OnItemClickListener listener;

    //리사이클러뷰 아이템 클릭을 위해 만듦.
    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, View view, int position);
    }

    //어댑터 생성자
    public GalleryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String photo) {
        items.add(photo);
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.gallery_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        String item = items.get(position);
        holder.setItem(item);

        holder.setOnItemClickListener(listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        OnItemClickListener listener;

        //뷰홀더 생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gal_imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        public void setItem(String photo) {
            ImageLoadTask imageLoadTask = new ImageLoadTask(photo, imageView);
            imageLoadTask.execute();    //String을 받아와서 이렇게 하면 될까? TEST
        }
    }
}

package com.example.moviefiendver2;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

    //ArrayList가 비어있는지 확인하는 것을 다른 클래스에서 사용하기 위해 메소드를 만듦.
    public boolean isEmpty(){
        if(items.isEmpty()){
            return true;
        }else{
            return false;
        }
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
        holder.setItem(item, context);

        holder.setOnItemClickListener(listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageView playIcon;
        OnItemClickListener listener;

        //뷰홀더 생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.gal_imageView);
            playIcon = itemView.findViewById(R.id.play_icon);

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

        public void setItem(String photo, Context context) {
//            ImageLoadTask imageLoadTask = new ImageLoadTask(photo, imageView);
//            imageLoadTask.execute();    //String을 받아와서 이렇게 하면 될까? 된다.
            Glide.with(context)
                    .load(photo)
                    .placeholder(R.drawable.loading)
                    .error(R.mipmap.ic_launcher)
                    .thumbnail(0.1f)
                    .into(imageView);    //Glide를 사용하기 위해 context를 가져온다.

            //동영상 썸네일인 경우에만 플레이아이콘이 보이도록 설정
            if(photo.endsWith("jpg")){
                playIcon.setVisibility(View.VISIBLE);
            }else{
                playIcon.setVisibility(View.INVISIBLE);
            }
        }
    }
}

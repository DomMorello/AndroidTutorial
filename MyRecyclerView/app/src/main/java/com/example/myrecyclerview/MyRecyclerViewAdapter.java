package com.example.myrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int images[] = {R.drawable.hk1,R.drawable.hk2,R.drawable.hk3,R.drawable.hk4,R.drawable.hk5};
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int width = parent.getResources().getDisplayMetrics().widthPixels/3;


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        view.setLayoutParams(new LinearLayoutCompat.LayoutParams(width, width));
        //XML디자인 한 부분을 적용
        return new RowCell(view);
    }

    @Override

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //XML디자인 한 부분에 안에 내용 변경
        ((RowCell)holder).imageView.setImageResource(images[position]);

    }

    @Override
    public int getItemCount() {
        //디자인을 측정하는 카운터터

        return images.length;
    }

    private static class RowCell extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public RowCell(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.recyclerview_item_imageview);
        }
    }
}




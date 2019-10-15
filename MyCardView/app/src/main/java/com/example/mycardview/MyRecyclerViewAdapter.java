package com.example.mycardview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CardViewItemDTO> cardViewItemDTOs = new ArrayList<>();

    public MyRecyclerViewAdapter() {

        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.hk1, "첫번째", "풍경1"));
        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.hk2, "두번째", "풍경2"));
        cardViewItemDTOs.add(new CardViewItemDTO(R.drawable.hk3, "세번째", "풍경3"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //XML세팅

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carview_item, parent, false);

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((RowCell) holder).imageView.setImageResource(cardViewItemDTOs.get(position).imageview);
        ((RowCell) holder).title.setText(cardViewItemDTOs.get(position).title);
        ((RowCell) holder).subtitle.setText(cardViewItemDTOs.get(position).subtitle);
        //아이템 세팅
    }

    @Override
    public int getItemCount() {
        //이미지 카운터
        return cardViewItemDTOs.size();
    }

    private class RowCell extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView title;
        public TextView subtitle;

        public RowCell(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.cardview_imageview);
            title = (TextView) view.findViewById(R.id.cardview_title);
            subtitle = (TextView) view.findViewById(R.id.cardview_subtitle);
        }
    }
}

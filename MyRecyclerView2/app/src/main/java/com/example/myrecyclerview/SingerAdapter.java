package com.example.myrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.ViewHolder> {

    Context context;
    ArrayList<SingerItem> items = new ArrayList<>();
    OnItemClickListener listener;

    //리스트뷰와 달리 클릭리스너를 정의해서 써야 한다.
    public static interface OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }

    //어댑터 생성자
    public SingerAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(SingerItem item){
        items.add(item);
    }

    public void addItems(ArrayList<SingerItem> items){
        this.items = items;
    }

    public SingerItem getItem(int position){
        return items.get(position);
    }

    //따로 정의
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.singer_item,parent,false);

        return new ViewHolder(itemView);
    }

    //데이터와 뷰홀더가 서로 결합되는 경우
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingerItem item = items.get(position);
        holder.setItem(item);   //뷰홀더가 데이터를 알 수 있게 됨. 뷰홀더 안에 뷰에다가 데이터를 설정할 수 있게 됨.

        holder.setOnItemClickListener(listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        TextView textView2;

        OnItemClickListener listener;

        //생성자
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        public void setItem(SingerItem item){
            textView.setText(item.getName());
            textView2.setText(item.getMobile());
        }
    }
}

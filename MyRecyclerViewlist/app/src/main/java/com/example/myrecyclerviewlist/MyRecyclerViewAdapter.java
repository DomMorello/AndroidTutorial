package com.example.myrecyclerviewlist;

import android.inputmethodservice.Keyboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<MemberDTO> memberDTOS = new ArrayList<>();
    public MyRecyclerViewAdapter() {
        memberDTOS.add(new MemberDTO(R.drawable.hk1,"기명간","오늘 예비군"));
        memberDTOS.add(new MemberDTO(R.drawable.hk2,"김형관","앙기모링"));
        memberDTOS.add(new MemberDTO(R.drawable.hk3,"이동현","최고의 콤퓨타전문가"));
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //XML을 가져오는 부분
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);

        return new RowCell(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //데이터를 넣어주는 부분

        ((RowCell)holder).circleImageView.setImageResource(memberDTOS.get(position).image);
        ((RowCell)holder).name.setText(memberDTOS.get(position).name);
        ((RowCell)holder).message.setText(memberDTOS.get(position).message);
    }

    @Override
    public int getItemCount() {

        //카운터
        return memberDTOS.size();
    }
    //소스코드 절약해주는 부분
    private static class RowCell extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView name;
        TextView message;
        public RowCell(View view) {
            super(view);
            circleImageView = (CircleImageView)view.findViewById(R.id.profile_image);
            name = (TextView)view.findViewById(R.id.name);
            message = (TextView)view.findViewById(R.id.message);
        }
    }
}

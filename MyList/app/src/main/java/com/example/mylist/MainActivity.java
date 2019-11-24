package com.example.mylist;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SingerAdapter adapter;
    EditText editText;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);

        ListView listView = (ListView)findViewById(R.id.listView);

        adapter = new SingerAdapter();
        adapter.addItem(new SingerItem("소녀시대","010-1000-1000", R.drawable.bigsolidstar));
        adapter.addItem(new SingerItem("걸스데이","010-2000-2000", R.drawable.bighalfstar));
        adapter.addItem(new SingerItem("여자친구","010-3000-3000", R.drawable.bigemptystar));
        adapter.addItem(new SingerItem("티아라","010-4000-4000", R.drawable.bigsolidstar));
        adapter.addItem(new SingerItem("애프터스쿨","010-5000-5000", R.drawable.bigsolidstar));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SingerItem item = (SingerItem)adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"선택: "+item.getName(), Toast.LENGTH_LONG).show();
            }
        });

        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString();
                String mobile = editText2.getText().toString();
                adapter.addItem(new SingerItem(name,mobile,R.drawable.bigemptystar));
                adapter.notifyDataSetChanged();
            }
        });
    }

    class SingerAdapter extends BaseAdapter{
        ArrayList<SingerItem> items = new ArrayList<SingerItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(SingerItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            SingerItemView view = null;
            if(convertView == null){
                view = new SingerItemView(getApplicationContext());
            }else{
                view = (SingerItemView) convertView;
            }

            SingerItem item = items.get(i);
            view.setName(item.getName());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());

            return view;
        }
    }
}

package com.example.mydatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;

    TextView textView;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String databaseName = editText.getText().toString();
                openDatabase(databaseName);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString();
                createTable(tableName);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText3.getText().toString().trim();
                String ageStr = editText4.getText().toString().trim();
                String mobile = editText5.getText().toString().trim();

                int age = -1;
                try {
                    Integer.parseInt(ageStr);
                } catch (Exception e) {
                }

                insertDate(name, age, mobile);
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tableName = editText2.getText().toString();
                selectData(tableName);
            }
        });
    }

    public void selectData(String tableName) {
        println("selectData() 호출됨.");

        if (database != null) {
            String sql = "select name, age, mobile from " + tableName;
            Cursor cursor = database.rawQuery(sql, null);
            println("조회된 데이터 개수: " + cursor.getCount());

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                println("#" + i + " -> " + name + ", " + age + ", " + mobile);
            }
            cursor.close();
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public void insertDate(String name, int age, String mobile) {
        println("insertData() 호출됨.");

        if (database != null) {
            String sql = "insert into customer(name, age, mobile) values(?,?,?)";
            Object[] params = {name, age, mobile};  //저 위에 물음표들이 여기에 있는 것들로 대체가 된다.

            database.execSQL(sql, params);

            println("데이터 추가됨.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public void createTable(String tableName) {
        println("createTable() 호출됨.");

        if (database != null) {
            String sql = "create table " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            database.execSQL(sql);

            println("테이블 생성됨.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public void openDatabase(String databaseName) {
        println("openDatabase() 호출됨.");

//        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
//        if (database != null) {
//            println("데이터베이스 오픈됨.");
//        }

        DatabaseHelper helper = new DatabaseHelper(this,databaseName,null,3);
        database = helper.getWritableDatabase();
        //이런 식으로 헬퍼객체를 만들어서 사용할 수 있다.
    }

    public void println(String data) {
        textView.append(data + "\n");
    }

    class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //createCustomer를 그대로 구현해주는데 매개변수로 넘어오는 SQLiteDatabase를 이용해서 구현한다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            println("createTable() 호출됨.");

            String tableName = "customer";
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            db.execSQL(sql);
            println("테이블 생성됨.");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("onUpgrade()호출됨: " + oldVersion + ", " + newVersion);
            String tableName = "customer";
            if(newVersion > 1){
                db.execSQL("drop table if exists " + tableName);    //이건 테이블을 삭제하는 건데 보통 이렇게 하면 기존 사용자들 데이터가 다 날라가니까 이러면 안 되고 alter table 명령어를 사용해서 칼럼정보를 수정한다고 함
                println("테이블 삭제함.");
            }
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            db.execSQL(sql);
            println("테이블 새로 생성됨.");

        }
    }
}

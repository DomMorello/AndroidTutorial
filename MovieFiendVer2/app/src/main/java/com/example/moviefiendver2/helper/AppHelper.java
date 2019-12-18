package com.example.moviefiendver2.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;

public class AppHelper {

    public static RequestQueue requestQueue;    //어디서든 접근할 수 있도록 public static으로 requestQueue를 정의
    public static String host = "boostcourse-appapi.connect.or.kr"; //서버 주소
    public static int port = 10000; //서버 포트

    //데이터베이스 관련 코드 시작
    private static SQLiteDatabase database;
    private static final String TAG = "AppHelper";
    //뷰페이저로 보이는 뮤비에 나오는 정보들
    private static String createTableMainMovieSql = "create table if not exists MainMovie" +
            "("+
            "    _id integer PRIMARY KEY autoincrement, " + //자동으로 늘어나는 고유 순서번호
            "     id integer, "+
            "     title text, "+
            "     title_eng text, "+
            "     dateValue text, "+
            "     user_rating float, "+
            "     audience_rating float, "+
            "     reviewer_rating float, "+
            "     reservation_rate float, "+
            "     reservation_grade integer, "+
            "     grade integer, "+
            "     thumb text, "+
            "     image text"+
            " )";
    private static String insertTupleMainMovieSql =
            "insert into MainMovie(" +
                    "id, title, " +
                    "title_eng, " +
                    "dateValue, " +
                    "user_rating, " +
                    "audience_rating, " +
                    "reviewer_rating, " +
                    "reservation_rate, " +
                    "reservation_grade, " +
                    "grade, " +
                    "thumb, " +
                    "image" +
                    ") values(?,?,?,?,?,?,?,?,?,?,?,?)";

    //1단계 데이터베이스를 생성
    public static void openDatabase(Context context, String databaseName){

        try{
            database = context.openOrCreateDatabase(databaseName,Context.MODE_PRIVATE,null);
            if(database != null){
                println("데이터베이스"+ databaseName + "오픈됨.");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //2단계 테이블 생성
    public static void createTable(String tableName){
        println("createTable 호출됨: " + tableName);

        if(database != null){
            //MainMovie table 생성시
            if(tableName.equals("MainMovie")){
                database.execSQL(createTableMainMovieSql);
                println("MainMovie 테이블 생성됨.");
            }
            /* 이 메소도는 테이블 이름마다 각각 다른 테이블을 생성하게 사용할 수 있다. if문 구현해야함. */
        }else{
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    //3단계 데이터 추가(메인뮤비에 대한 데이터만 추가하는 메소드: 최초에 1회만 실시된다. 그 이후로는 update를 할 예정)
    public static void insertMainMovieData(int id, String title, String title_eng, String dateValue, float user_rating, float audience_rating, float reviewer_rating, float reservation_rate, int reservation_grade, int grade, String thumb, String image){

        if(database != null){
            Object[] params = {id,title,title_eng,dateValue,user_rating,audience_rating,reviewer_rating,reservation_rate,reservation_grade,grade,thumb,image};
            database.execSQL(insertTupleMainMovieSql,params);
            println("MainMovie 테이블에 데이터 추가함!");
        }else{
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //4단계 데이터 조회하기 -> tableName에 따라 그 테이블에 있는 데이터들을 조회한다.
    public static void selectMainMovieData(String tableName){
        println("selectData 호출함.");

        if(database != null){
            //MainMovie table data 조회
            if(tableName.equals("MainMovie")){
                String sql = "select id, title, title_eng, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image from " + tableName;
                Cursor cursor = database.rawQuery(sql,null);
                println("조회된 데이터 개수: " + cursor.getCount());

                for(int i=0; i < cursor.getCount(); i++){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String title_eng = cursor.getString(2);
                    String dateValue = cursor.getString(3);
                    float user_rating = cursor.getFloat(4);
                    float audience_rating = cursor.getFloat(5);
                    float reviewer_rating = cursor.getFloat(6);
                    float reservation_rate = cursor.getFloat(7);
                    int reservation_grade  = cursor.getInt(8);
                    int grade  = cursor.getInt(9);
                    String thumb  = cursor.getString(10);
                    String image  = cursor.getString(11);

                    println("#"+i+"번 Data : "+id+", "+title+", "+title_eng+", "+dateValue+", "+user_rating+", "+audience_rating+", "+reviewer_rating+", "+reservation_rate+", "+reservation_grade+", "+grade+", "+thumb+", "+image);
                }//for 끝
            }//MainMovie if 끝
        }else{
            println("database가 null이라 조회할 데이터가 없음!");
        }
    }//메소드 끝

    //데이터베이스 관련 로그찍기
    public static void println(String data){
        Log.d(TAG,data);
    }


}

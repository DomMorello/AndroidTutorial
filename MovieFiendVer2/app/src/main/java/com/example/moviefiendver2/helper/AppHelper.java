package com.example.moviefiendver2.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.moviefiendver2.R;

public class AppHelper {

    public static RequestQueue requestQueue;    //어디서든 접근할 수 있도록 public static으로 requestQueue를 정의
    public static String host = "boostcourse-appapi.connect.or.kr"; //서버 주소
    public static int port = 10000; //서버 포트

    //데이터베이스 관련 코드 시작
    private static SQLiteDatabase database;
    private static final String TAG = "AppHelper";
    public static final String MAIN_MOVIE = "MainMovie";    //메인화면 테이블이름
    public static final String MOVIE_INFO = "MovieInfo";    //상세화면 테이블이름

    //다른 클래스에서 MainMovie table 정보를 접근해서 사용할 수 있도록 선언했다. -> 이 방법이 좋은 건지는 모르겠다.(memory leak?)
    public static String main_title;
    public static String main_dateValue;
    public static float main_reservation_rate;
    public static int main_grade;
    public static String main_image;

    //다른 클래스에서 MovieInfo table 정보를 접근해서 사용할 수 있도록 선언했다.
    public static String info_title;
    public static String info_date;
    public static String info_genre;
    public static int info_duration;
    public static int info_like;
    public static int info_dislike;
    public static int info_reservation_grade;
    public static float info_reservation_rate;
    public static float info_audience_rating;
    public static int info_audience;
    public static String info_synopsis;
    public static String info_director;
    public static String info_actor;
    public static String info_thumb;
    public static int info_grade;

    //뷰페이저로 보이는 뮤비에 나오는 정보관련 sql 명령어
    private static String createTableMainMovieSql = "create table if not exists MainMovie" +
            "(" +
            "    _id integer PRIMARY KEY autoincrement, " + //자동으로 늘어나는 고유 순서번호
            "     id integer, " +
            "     title text, " +
            "     title_eng text, " +
            "     dateValue text, " +
            "     user_rating float, " +
            "     audience_rating float, " +
            "     reviewer_rating float, " +
            "     reservation_rate float, " +
            "     reservation_grade integer, " +
            "     grade integer, " +
            "     thumb text, " +
            "     image text" +
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

    private static String createTableMovieInfoSql = "create table if not exists MovieInfo" +
            "(" +
            "_id integer PRIMARY KEY autoincrement, " +
            "title text, " +
            "id integer, " +
            "date text, " +
            "user_rating float, " +
            "audience_rating float, " +
            "reviewer_rating float, " +
            "reservation_rate float, " +
            "reservation_grade integer, " +
            "grade integer, " +
            "thumb text, " +
            "image text, " +
            "photos text, " +
            "videos text, " +
            "outlinks text, " +
            "genre text, " +
            "duration integer, " +
            "audience integer, " +
            "synopsis text, " +
            "director text, " +
            "actor text, " +
            "likeCount integer, " + //like가 sql예약어라서 likeCount로 이름만 바꿨다.
            "dislike integer)";

    private static String insertTupleMovieInfoSql =
            "insert into MovieInfo(" +
                    "title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, " +
                    "reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience, " +
                    "synopsis, director, actor, likeCount, dislike) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //1단계 데이터베이스를 생성
    public static void openDatabase(Context context, String databaseName) {
        println("openDatabase 호출됨.");
        try {
            database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
            if (database != null) {
                println("데이터베이스" + databaseName + "오픈됨.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2단계 테이블 생성
    public static void createTable(String tableName) {
        println("createTable 호출됨: " + tableName);

        if (database != null) {
            //MainMovie table 생성시
            if (tableName.equals(MAIN_MOVIE)) {
                database.execSQL(createTableMainMovieSql);
                println("MainMovie 테이블 생성됨.");
            } else if (tableName.equals(MOVIE_INFO)) {
                database.execSQL(createTableMovieInfoSql);
                println("MovieInfo 테이블 생성됨.");
            }

        } else {
            println("데이터베이스를 먼저 오픈하세요.");
        }
    }

    //3단계 데이터 추가(메인뮤비에 대한 데이터만 추가하는 메소드: 최초에 1회만 실시된다. 그 이후로는 update를 할 예정)
    public static void insertMainMovieData(int id, String title, String title_eng, String dateValue, float user_rating, float audience_rating, float reviewer_rating, float reservation_rate, int reservation_grade, int grade, String thumb, String image) {

        if (database != null) {
            Object[] params = {id, title, title_eng, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image};
            database.execSQL(insertTupleMainMovieSql, params);
            println("MainMovie 테이블에 데이터 추가함!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //3단계 데이터 추가(뮤비 상세화면 테이블에 대한 데이터만 추가하는 메소드: 최초에 1회만 실시된다. 그 이후로는 update를 할 예정)
    public static void insertMovieInfoData(String title, int id, String date, float user_rating, float audience_rating,
                                           float reviewer_rating, float reservation_rate, int reservation_grade, int grade,
                                           String thumb, String image, String photos, String videos, String outlinks, String genre,
                                           int duration, int audience, String synopsis, String director, String actor, int likeCount, int dislike) {

        if (database != null) {
            Object[] params = {title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade,
                    grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, actor, likeCount, dislike};
            database.execSQL(insertTupleMovieInfoSql, params);
            println("MovieInfo 테이블에 데이터 추가함!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //4단계 데이터 조회하기 -> tableName에 따라 그 테이블에 있는 데이터들을 조회한다.
    //return boolean -> 테이블에 조회할 데이터가 있으면 true, 아니면 false;
    public static boolean selectData(String tableName) {
        println("selectData 호출함.");

        if (database != null) {
            //MainMovie table data 조회
            if (tableName.equals(MAIN_MOVIE)) {
                String sql = "select id, title, title_eng, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image from " + tableName;

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        int id = cursor.getInt(0);
                        String title = cursor.getString(1);
                        String title_eng = cursor.getString(2);
                        String dateValue = cursor.getString(3);
                        float user_rating = cursor.getFloat(4);
                        float audience_rating = cursor.getFloat(5);
                        float reviewer_rating = cursor.getFloat(6);
                        float reservation_rate = cursor.getFloat(7);
                        int reservation_grade = cursor.getInt(8);
                        int grade = cursor.getInt(9);
                        String thumb = cursor.getString(10);
                        String image = cursor.getString(11);

                        println("#" + i + "번 Data : " + id + ", " + title + ", " + title_eng + ", " + dateValue + ", " + user_rating + ", " + audience_rating + ", " + reviewer_rating + ", " + reservation_rate + ", " + reservation_grade + ", " + grade + ", " + thumb + ", " + image);
                    }//for 끝
                    return true;
                }else{
                    println("database가 null이라 조회할 데이터가 없음!");
                    return false;
                }

            } else if (tableName.equals(MOVIE_INFO)) {
                String sql = "select title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, " +
                        "reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, actor, likeCount, dislike from " + tableName;

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        String title = cursor.getString(0);
                        int id = cursor.getInt(1);
                        String date = cursor.getString(2);
                        float user_rating = cursor.getFloat(3);
                        float audience_rating = cursor.getFloat(4);
                        float reviewer_rating = cursor.getFloat(5);
                        float reservation_rate = cursor.getFloat(6);
                        int reservation_grade = cursor.getInt(7);
                        int grade = cursor.getInt(8);
                        String thumb = cursor.getString(9);
                        String image = cursor.getString(10);
                        String photos = cursor.getString(11);
                        String videos = cursor.getString(12);
                        String outlinks = cursor.getString(13);
                        String genre = cursor.getString(14);
                        int duration = cursor.getInt(15);
                        int audience = cursor.getInt(16);
                        String synopsis = cursor.getString(17);
                        String director = cursor.getString(18);
                        String actor = cursor.getString(19);
                        int likeCount = cursor.getInt(20);
                        int dislike = cursor.getInt(21);

                        println("#" + i + "번 Data : " + title + ", " + id + ", " + date + ", " + user_rating + ", " + audience_rating + ", " + reviewer_rating + ", " + reservation_rate + ", " + reservation_grade + ", " + grade + ", " + thumb + ", " + image + ", " + photos + ", "
                                + videos + ", " + outlinks + ", " + genre + ", " + duration + ", " + audience + ", " + synopsis + ", " + director + ", "
                                + actor + ", " + likeCount + ", " + dislike);
                    }//for 끝
                    return true;
                }else{
                    println("database가 null이라 조회할 데이터가 없음!");
                    return false;
                }
            }//else if 끝
        } else {
            println("database가 null이라 조회할 데이터가 없음!");
            return false;
        }
        return false;
    }//메소드 끝

    //메소드 오버로딩
    //데이터베이스에서 특정 아이디값을 가진 정보를 조회한다.-> 인터넷연결 안 돼있을 때 가져와서 쓰기 위함
    //return boolean -> 테이블에 조회할 데이터가 있으면 true, 아니면 false;
    public static boolean selectData(String tableName, int movie_id) {
        println("movie_id가 " + movie_id + "인 데이터를 조회함.");

        if (database != null) {
            //MainMovie table data 조회
            if (tableName.equals(MAIN_MOVIE)) {
                String sql = "select id, title, title_eng, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image from " + tableName + " where id=" + movie_id;  //where조건절이 추가됨.

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    cursor.moveToNext();

                    int id = cursor.getInt(0);
                    main_title = cursor.getString(1);
                    String title_eng = cursor.getString(2);
                    main_dateValue = cursor.getString(3);
                    float user_rating = cursor.getFloat(4);
                    float audience_rating = cursor.getFloat(5);
                    float reviewer_rating = cursor.getFloat(6);
                    main_reservation_rate = cursor.getFloat(7);
                    int reservation_grade = cursor.getInt(8);
                    main_grade = cursor.getInt(9);
                    String thumb = cursor.getString(10);
                    main_image = cursor.getString(11);

                    println("#Data : " + id + ", " + main_title + ", " + title_eng + ", " + main_dateValue + ", " + user_rating + ", " + audience_rating + ", " + reviewer_rating + ", " + main_reservation_rate + ", " + reservation_grade + ", " + main_grade + ", " + thumb + ", " + main_image);
                    return true;
                }else{
                    Log.d(TAG,"데이터베이스에 해당 데이터가 없음!");
                    return false;
                }
            } else if (tableName.equals(MOVIE_INFO)) {
                String sql = "select title, id, date, user_rating, audience_rating, reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, actor, likeCount, dislike from " + tableName + " where id=" + movie_id;  //where조건절이 추가됨.

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    cursor.moveToNext();

                    info_title = cursor.getString(0);
                    int id = cursor.getInt(1);
                    info_date = cursor.getString(2);
                    float user_rating = cursor.getFloat(3);
                    info_audience_rating = cursor.getFloat(4);
                    float reviewer_rating = cursor.getFloat(5);
                    info_reservation_rate = cursor.getFloat(6);
                    info_reservation_grade = cursor.getInt(7);
                    info_grade = cursor.getInt(8);
                    info_thumb = cursor.getString(9);
                    String image = cursor.getString(10);
                    String photos = cursor.getString(11);
                    String videos = cursor.getString(12);
                    String outlinks = cursor.getString(13);
                    info_genre = cursor.getString(14);
                    info_duration = cursor.getInt(15);
                    info_audience = cursor.getInt(16);
                    info_synopsis = cursor.getString(17);
                    info_director = cursor.getString(18);
                    info_actor = cursor.getString(19);
                    info_like = cursor.getInt(20);
                    info_dislike = cursor.getInt(21);

                    println("#Data : " + info_title + ", " + id + ", " + info_date + ", " + user_rating + ", " + info_audience_rating + ", " + reviewer_rating + ", " + info_reservation_rate + ", " + info_reservation_grade + ", " + info_grade + ", " + info_thumb + ", " + image + ", " + photos + ", " + videos + ", " + outlinks + ", " + info_genre + ", " + info_duration + ", " + info_audience + ", " + info_synopsis + ", " + info_director + ", " + info_actor + ", " + info_like + ", " + info_dislike);
                    return true;
                }else{
                    Log.d(TAG,"데이터베이스에 해당 데이터가 없음!");
                    return false;
                }
            }
        } else {
            println("database가 null이라 조회할 데이터가 없음!");
            return false;
        }
        return false;
    }//메소드 끝

    //MainMovie 데이터 업데이트하기
    public static void updateMainMovieData(int movie_id, int id, String title, String title_eng, String dateValue, float user_rating, float audience_rating, float reviewer_rating, float reservation_rate, int reservation_grade, int grade, String thumb, String image) {
        println("updateMainMovieData 데이터 갱신호출!");

        if (database != null) {
            String updateDataSql = "update MainMovie set id=" + id + ", title='" + title + "', title_eng='" + title_eng + "', dateValue='" + dateValue + "', user_rating=" + user_rating + ", audience_rating=" + audience_rating + ", reviewer_rating=" + reviewer_rating + ", reservation_rate=" + reservation_rate + ", reservation_grade=" + reservation_grade + ", grade=" + grade + ", thumb='" + thumb + "', image='" + image + "' where id=" + movie_id;

            database.execSQL(updateDataSql);
            println("movie_id가 " + movie_id + "인 MainMovie table 레코드에 데이터 업데이트함!!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //MovieInfo 데이터 업데이트하기
    public static void updateMovieInfoData(int movie_id, String title, int id, String date, float user_rating, float audience_rating, float reviewer_rating, float reservation_rate, int reservation_grade, int grade, String thumb, String image, String photos, String videos, String outlinks, String genre, int duration, int audience, String synopsis, String director, String actor, int likeCount, int dislike) {
        println("updateMovieInfoData 데이터 갱신호출!");

        if (database != null) {
            String updateDataSql = "update MovieInfo set title='" + title + "', id=" + id + ", date='" + date + "', user_rating=" + user_rating + ", audience_rating=" + audience_rating + ", reviewer_rating=" + reviewer_rating + ", reservation_rate=" + reservation_rate + ", reservation_grade=" + reservation_grade + ", grade=" + grade + ", thumb='" + thumb + "', image='" + image + "', photos='" + photos + "', videos='" + videos + "', outlinks='" + outlinks + "', genre='" + genre + "', duration=" + duration + ", audience=" + audience + ", synopsis='" + synopsis + "', director='" + director + "', actor='" + actor + "', likeCount=" + likeCount + ", dislike=" + dislike + " where id=" + movie_id;

            database.execSQL(updateDataSql);
            println("movie_id가 " + movie_id + "인 MovieInfo table 레코드에 데이터 업데이트함!!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //id값이 ?인 레코드가 테이블에 있나 없나
    public static boolean isMovieExsist(String tableName, int movie_id) {
        String sql = "select count(*) from " + tableName + " where id=" + movie_id;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToNext();
        if (cursor.getInt(0) > 0) {
            return true;
        }
        return false;
    }

    public static void println(String data) {
        Log.d(TAG, data);
    }

}

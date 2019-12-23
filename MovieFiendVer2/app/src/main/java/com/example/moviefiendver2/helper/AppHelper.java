package com.example.moviefiendver2.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.example.moviefiendver2.MovieData.CommentItem;

import java.util.ArrayList;

public class AppHelper {

    public static RequestQueue requestQueue;    //어디서든 접근할 수 있도록 public static으로 requestQueue를 정의
    public static String host = "boostcourse-appapi.connect.or.kr"; //서버 주소
    public static int port = 10000; //서버 포트

    //데이터베이스 관련 코드 시작
    private static SQLiteDatabase database;
    private static final String TAG = "AppHelper";
    public static final String MAIN_MOVIE = "MainMovie";    //메인화면 테이블이름
    public static final String MOVIE_INFO = "MovieInfo";    //상세화면 테이블이름
    public static final String COMMENT = "Comment";    //한줄평 테이블이름
    public static final String TOTAL_COUNT = "TotalCount";  //영화별 평점 참여 인원 테이블이름

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

    //다른 클래스에서 Comment table 정보를 접근해서 사용할 수 있도록 선언했다.
    public static String com_writer;
    public static String com_contents;
    public static float com_rating;
    public static String com_time;
    public static int com_recommend;
    public static int com_id;

    //다른 클래스에서 TotalCount table 정보를 접근해서 사용할 수 있도록 선언했다.
    public static int total_totalCount;

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

    private static String createTableCommentSql = "create table if not exists Comment("
            +"_id integer PRIMARY KEY autoincrement, "+
            "id integer, "+
            "writer text, "+
            "movieId integer, "+
            "writer_image text, "+
            "time text, "+
            "timestamp integer, "+
            "rating float, "+
            "contents text, "+
            "recommend integer)";

    private static String insertTupleCommentSql = "insert into Comment(id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend) values(?,?,?,?,?,?,?,?,?)";

    private static String createTableTotalCountSql = "create table if not exists TotalCount(_id integer PRIMARY KEY autoincrement, id integer, totalCount integer)";

    private static String insertTupleTotalCountSql = "insert into TotalCount(id, totalCount) values(?,?)";

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
            } else if(tableName.equals(COMMENT)){
                database.execSQL(createTableCommentSql);
                println("Comment 테이블 생성됨.");
            } else if(tableName.equals(TOTAL_COUNT)){
                database.execSQL(createTableTotalCountSql);
                println("TotalCount 테이블 생성됨.");
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

    //3단계 데이터 추가(한줄평 테이블에 대한 데이터만 추가하는 메소드: 최초에 1회만 실시된다. 그 이후로는 update를 할 예정)
    public static void insertCommentData(int id, String writer, int movieId, String writer_image, String time, int timestamp, float rating, String contents, int recommend) {

        if (database != null) {
            Object[] params = {id , writer, movieId, writer_image, time, timestamp, rating, contents, recommend};
            database.execSQL(insertTupleCommentSql, params);
            println("Comment 테이블에 데이터 추가함!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    public static void insertTotalCountData(int id, int totalCount){
        if(database != null){
            Object[] params = {id, totalCount};
            database.execSQL(insertTupleTotalCountSql, params);
            println("TotalCount 테이블에 데이터 추가함!");
        }else{
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
            }else if(tableName.equals(COMMENT)){
                String sql = "select id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend from " + tableName;

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        int id = cursor.getInt(0);
                        String writer = cursor.getString(1);
                        int movieId = cursor.getInt(2);
                        String writer_image = cursor.getString(3);
                        String time = cursor.getString(4);
                        int timestamp = cursor.getInt(5);
                        float rating = cursor.getFloat(6);
                        String contents = cursor.getString(7);
                        int recommend = cursor.getInt(8);

                        println("#" + i + "번 Data : " + id + ", " + writer + ", " + movieId + ", " + writer_image + ", " + time + ", " + timestamp + ", " + rating + ", " + contents + ", " + recommend);
                    }//for 끝
                    return true;
                }else{
                    println("database가 null이라 조회할 데이터가 없음!");
                    return false;
                }
            }else if(tableName.equals(TOTAL_COUNT)){
                String sql = "select id, totalCount from " + tableName;

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        int id = cursor.getInt(0);
                        int totalCount = cursor.getInt(1);

                        println("#" + i + "번 Data : " + id + ", " + totalCount);
                    }//for 끝
                    return true;
                }else{
                    println("database가 null이라 조회할 데이터가 없음!");
                    return false;
                }
            }
        } else {
            println("database가 null이라 조회할 데이터가 없음!");
            return false;
        }
        return false;
    }//메소드 끝

    //메소드 오버로딩
    //데이터베이스에서 특정 아이디값을 가진 정보를 조회한다.-> 인터넷연결 안 돼있을 때 가져와서 쓰기 위함
    //return boolean -> 테이블에 조회할 데이터가 있으면 true, 아니면 false;
    //TEST -> movie_id만 있어도 어차피 데이터가 다 저장돼있는 거기 때문에 굳이 comment_id까지 검사할 필요가 없을 것 같은데? 시도해보자.
    public static boolean selectData(String tableName, int movie_id, int comment_id) {
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
            }else if(tableName.equals(COMMENT)){
                String sql = "select id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend from " + tableName + " where id=" + comment_id;  //where조건절이 추가됨. -> 다시 보니까 각 한줄평마다 다 다른 id가 있어서 movie_id는 검사를 하지 않아도 된다.

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    cursor.moveToNext();
                    com_id = cursor.getInt(0);
                    com_writer = cursor.getString(1);
                    int movieId = cursor.getInt(2);
                    String writer_image = cursor.getString(3);
                    com_time = cursor.getString(4);
                    int timestamp = cursor.getInt(5);
                    com_rating = cursor.getFloat(6);
                    com_contents = cursor.getString(7);
                    com_recommend = cursor.getInt(8);

                    println("#Data : " + com_id + ", " + com_writer + ", " + movieId + ", " + writer_image + ", " + com_time + ", " + timestamp + ", " + com_rating + ", " + com_contents + ", " + com_recommend);
                    return true;
                }else{
                    Log.d(TAG,"데이터베이스에 해당 데이터가 없음!");
                    return false;
                }
            }else if(tableName.equals(TOTAL_COUNT)){
                String sql = "select id, totalCount from " + tableName + " where id=" + movie_id;  //where조건절이 추가됨.

                Cursor cursor = database.rawQuery(sql, null);
                println("조회된 데이터 개수: " + cursor.getCount());
                //테이블에 데이터가 존재하면 아래 코드를 실행.
                if(cursor.getCount() > 0){
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    total_totalCount = cursor.getInt(1);

                    println("#Data : " + id + ", " + total_totalCount);
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

    /* 알 수 없는 오류발생: 이 메서드를 실행할 때 다른 영화데이터들은 문제가 없는데 러빙 빈센트만 업데이트를 할 때 syntax error 가 발생한다.
    * 왜일까? 이유를 모르겠다. 안 되면 다같이 안 돼야 되는데 러빙 빈센트만 안 된다. 뭐지... */
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

    //Comment 데이터 업데이트하기
    public static void updateCommentData(int id, String writer, int movieId, String writer_image, String time, int timestamp, float rating, String contents, int recommend) {
        println("updateCommentData 데이터 갱신호출!");

        if (database != null) {
            String updateDataSql = "update Comment set id=" + id + ", writer='" + writer + "', movieId=" + movieId + ", writer_image='" + writer_image + "', time='" + time + "', timestamp=" + timestamp + ", rating=" + rating + ", contents='" + contents + "', recommend=" + recommend +" where id=" + id;   //movie_id가 아닌 커멘트 자체 아이디인 id 를 조건절로 해서 업데이트 해야함. 그리고 movieId도 넣어줘야 완벽한 중복이 제거된다. -> 다시 보니까 각 한줄평마다 다 다른 id가 있어서 movie_id는 검사를 하지 않아도 된다.

            database.execSQL(updateDataSql);
            println("comment_id가 " + id + "인 Comment table 레코드에 데이터 업데이트함!!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //TotalCount 데이터 업데이트하기
    public static void updateTotalCountData(int id, int totalCount) {
        println("updateTotalCountData 데이터 갱신호출!");

        if (database != null) {
            String updateDataSql = "update TotalCount set id=" + id + ", totalCount=" + totalCount + " where id=" + id;

            database.execSQL(updateDataSql);
            println("TotalCount 테이블에서 id가 " + id + "인 TotalCount table 레코드에 데이터 업데이트함!!");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }

    //id값이 ?인 레코드가 테이블에 있나 없나
    public static boolean isDataExsist(String tableName, int id) {   //매개변수가 id인 이유는 영화를 조회할 때는 movie_id 한줄평일 때는 comment_id
        String sql = "select count(*) from " + tableName + " where id=" + id;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToNext();
        if (cursor.getInt(0) > 0) {
            return true;
        }
        return false;
    }

    //어레이리스트를 반환하는 메소드를 만들어서 데이터베이스 내용을 보내준다
    //이 메소드가 존재하는 이유는 기존의 MainMovie나 MovieInfo처럼 position 변수를 넣어줄 수 없고 각 한줄평마다 comment_id를 적용해야해서
    //아예 각 한줄평마다 commentItem을 만들어서 전달해줄 필요가 있었기 때문이다.
    public static ArrayList getCommentFromDatabase(int movie_id){
        String sql = "select id, writer, movieId, writer_image, time, timestamp, rating, contents, recommend from comment where movieId=" + movie_id + " order by id desc";

        ArrayList<CommentItem> list = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, null);
        println("조회된 데이터 개수: " + cursor.getCount());
        //테이블에 데이터가 존재하면 아래 코드를 실행.
        if(cursor.getCount() > 0){
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String writer = cursor.getString(1);
                int movieId = cursor.getInt(2);
                String writer_image = cursor.getString(3);
                String time = cursor.getString(4);
                int timestamp = cursor.getInt(5);
                float rating = cursor.getFloat(6);
                String contents = cursor.getString(7);
                int recommend = cursor.getInt(8);
                println("getCommentDatabase 메소드 안에!!!! id="+id+", writer="+writer+", movieId="+movieId+", contents="+contents);

                CommentItem commentItem = new CommentItem();
                commentItem.setId(id);
                commentItem.setWriter(writer);
                commentItem.setMovieId(movieId);
                commentItem.setWriter_image(writer_image);
                commentItem.setTime(time);
                commentItem.setTimestamp(timestamp);
                commentItem.setRating(rating);
                commentItem.setContents(contents);
                commentItem.setRecommend(recommend);
                list.add(commentItem);

                println("list: " + list.toString());
            }//for 끝
        }else{
            println("database가 null이라 조회할 데이터가 없음!");
        }
        return list;
    }

    public static void println(String data) {
        Log.d(TAG, data);
    }

}

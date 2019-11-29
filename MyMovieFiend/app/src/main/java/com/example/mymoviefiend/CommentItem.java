package com.example.mymoviefiend;

public class CommentItem {

    String id;  //일단 내버려두자
    String comment;

    public CommentItem(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}


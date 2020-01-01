package com.example.moviefiendver2.MovieData;

public class RecyclerItem {

    String photo;
    String video;
    boolean isVideo;

    public RecyclerItem(){}

    public RecyclerItem(String photo, String video, boolean isVideo) {
        this.photo = photo;
        this.video = video;
        this.isVideo = isVideo;
    }

    public boolean getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean video) {
        isVideo = video;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}

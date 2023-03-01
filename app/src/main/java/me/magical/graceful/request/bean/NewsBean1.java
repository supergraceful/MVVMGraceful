package me.magical.graceful.request.bean;

import com.google.gson.annotations.SerializedName;

public class NewsBean1 {

    @SerializedName("path")
    private String path;
    @SerializedName("image")
    private String image;
    @SerializedName("title")
    private String title;
    @SerializedName("passtime")
    private String passtime;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

    @Override
    public String toString() {
        return "NewsBean1{" +
                "path='" + path + '\'' +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", passtime='" + passtime + '\'' +
                '}';
    }
}

package baidubean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jrm on 2017-5-4.
 */
public class InformationsResult implements Serializable{

    private int id;
    private String title;
    private String author_name;
    private String pics;
    private String news_url;
    private String type;
    private int show_type;
    private String publish_time;
    private String source;
    private String thumbnail_pic_s1;
    private String thumbnail_pic_s2;
    private String thumbnail_pic_s3;

    public String getThumbnail_pic_s1() {
        return thumbnail_pic_s1;
    }

    public void setThumbnail_pic_s1(String thumbnail_pic_s1) {
        this.thumbnail_pic_s1 = thumbnail_pic_s1;
    }

    public String getThumbnail_pic_s2() {
        return thumbnail_pic_s2;
    }

    public void setThumbnail_pic_s2(String thumbnail_pic_s2) {
        this.thumbnail_pic_s2 = thumbnail_pic_s2;
    }

    public String getThumbnail_pic_s3() {
        return thumbnail_pic_s3;
    }

    public void setThumbnail_pic_s3(String thumbnail_pic_s3) {
        this.thumbnail_pic_s3 = thumbnail_pic_s3;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

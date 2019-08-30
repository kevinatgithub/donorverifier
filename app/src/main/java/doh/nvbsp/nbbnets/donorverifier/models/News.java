package doh.nvbsp.nbbnets.donorverifier.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject {
    @PrimaryKey
    private int id;
    private String title;
    private String content;
    private String author;
    private String thumbnail;
    private String created_dt;
    private String link;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getCreated_dt() {
        return created_dt;
    }

    public String getLink() {
        return link;
    }
}

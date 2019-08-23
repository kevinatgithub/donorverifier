package app.kevin.dev.donorverifier.models;

public class News {
    private int id;
    private String title;
    private String content;
    private String author;
    private String thumbnail;
    private String created_dt;

    public News(int id, String title, String content, String author, String thumbnail, String created_dt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.thumbnail = thumbnail;
        this.created_dt = created_dt;
    }

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
}

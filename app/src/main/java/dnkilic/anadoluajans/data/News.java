package dnkilic.anadoluajans.data;

public class News {

    private String title;
    private String description;
    private String image;
    private String link;
    private String pubDate;
    private String category;

    public News(String title, String description, String image, String link, String pubDate, String category) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.link = link;
        this.pubDate = pubDate;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getCategory(){ return category;}
}

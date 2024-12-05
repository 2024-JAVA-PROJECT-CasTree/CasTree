package MainLogin;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AladinResponse {
    @SerializedName("item")
    private List<Item> items;

    public List<Item> getItems() {
        return items != null ? items : new ArrayList<>();
    }

    public static class Item {
        @SerializedName("title")
        private String title;
        @SerializedName("author")
        private String author;
        @SerializedName("publisher")
        private String publisher;
        @SerializedName("description")
        private String description;
        @SerializedName("cover")
        private String cover;

        // 생성자
        public Item(String title, String author, String publisher, String description) {
            this.title = title;
            this.author = author;
            this.publisher = publisher;
            this.description = description;
        }

        // Getters
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getPublisher() { return publisher; }
        public String getDescription() { return description; }
        public String getCover() { return cover; }
    }
}
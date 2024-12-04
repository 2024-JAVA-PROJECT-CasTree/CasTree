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
        @SerializedName("description")
        private String description;
        @SerializedName("cover")
        private String cover;

        // Getters
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getDescription() { return description; }
        public String getCover() { return cover; }
    }
}
package pedrojtmartins.com.farfetchmarvel.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Pedro Martins
 * 24/06/2017
 */

public class MarvelDetails {
    // All unused variables are commented for improved serialization performance

    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("results")
        public ArrayList<Result> results = null;
    }

//    public class Events {
//        @SerializedName("available")
//        public Integer available;
//
//        @SerializedName("collectionURI")
//        public String collectionURI;
//
////        @SerializedName("storyItems")
////        public List<Item___> storyItems = null;
//
//        @SerializedName("returned")
//        public Integer returned;
//
//    }


//    public class Image {
//
//        @SerializedName("path")
//        @Expose
//        public String path;
//        @SerializedName("extension")
//        @Expose
//        public String extension;
//
//    }

    public class Result {

//        @SerializedName("id")
//        public Integer id;
//
//        @SerializedName("digitalId")
//        public Integer digitalId;

        @SerializedName("title")
        public String title;

//        @SerializedName("issueNumber")
//        public Integer issueNumber;
//
//        @SerializedName("variantDescription")
//        public String variantDescription;

        @SerializedName("description")
        public String description;

//        @SerializedName("modified")
//        public String modified;
//
//        @SerializedName("isbn")
//        public String isbn;
//
//        @SerializedName("upc")
//        public String upc;
//
//        @SerializedName("diamondCode")
//        public String diamondCode;
//
//        @SerializedName("ean")
//        public String ean;
//
//        @SerializedName("issn")
//        public String issn;
//
//        @SerializedName("format")
//        public String format;
//
//        @SerializedName("pageCount")
//        public Integer pageCount;
//
//        @SerializedName("textObjects")
//        public List<TextObject> textObjects = null;
//
//        @SerializedName("resourceURI")
//        public String resourceURI;
//
//        @SerializedName("urls")
//        public List<Url> urls = null;
//
//        @SerializedName("seriesItems")
//        public Series seriesItems;
//
//        @SerializedName("variants")
//        public List<Variant> variants = null;
//
//        @SerializedName("collections")
//        public List<Object> collections = null;
//
//        @SerializedName("collectedIssues")
//        public List<Object> collectedIssues = null;
//
//        @SerializedName("dates")
//        public List<Date> dates = null;
//        @SerializedName("prices")
//        @Expose
//        public List<Price> prices = null;

        @SerializedName("thumbnail")
        public Thumbnail thumbnail;

//        @SerializedName("images")
//        @Expose
//        public List<Image> images = null;
//        @SerializedName("creators")
//        @Expose
//        public Creators creators;
//        @SerializedName("characters")
//        @Expose
//        public Characters characters;
//        @SerializedName("stories")
//        @Expose
//        public Stories stories;
//        @SerializedName("events")
//        @Expose
//        public Events events;

    }

    public class Thumbnail {
        @SerializedName("path")
        public String path;

        @SerializedName("extension")
        public String extension;

    }

//    public class Series {
//
//        @SerializedName("resourceURI")
//        @Expose
//        public String resourceURI;
//        @SerializedName("name")
//        @Expose
//        public String name;
//
//    }
//
//    public class Stories {
//
//        @SerializedName("available")
//        @Expose
//        public Integer available;
//        @SerializedName("collectionURI")
//        @Expose
//        public String collectionURI;
//        @SerializedName("storyItems")
//        @Expose
//        public List<Story> storyItems = null;
//        @SerializedName("returned")
//        @Expose
//        public Integer returned;
//
//    }
//
//    public class TextObject {
//
//        @SerializedName("type")
//        @Expose
//        public String type;
//        @SerializedName("language")
//        @Expose
//        public String language;
//        @SerializedName("text")
//        @Expose
//        public String text;
//
//    }
}

package pedrojtmartins.com.farfetchmarvel.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pedrojtmartins.com.farfetchmarvel.BR;

/**
 * Pedro Martins
 * 20/06/2017
 */

public class MarvelModel {

    @SerializedName("code")
    public Integer code;

    @SerializedName("status")
    public String status;

    @SerializedName("copyright")
    public String copyright;

    @SerializedName("attributionText")
    public String attributionText;

    @SerializedName("attributionHTML")
    public String attributionHTML;

    @SerializedName("etag")
    public String etag;

    @SerializedName("data")
    public Data data;

    public class Comics {
        @SerializedName("available")
        public Integer available;

        @SerializedName("collectionURI")
        public String collectionURI;

        @SerializedName("items")
        public List<Comic> comicItems;

        @SerializedName("returned")
        public Integer returned;
    }


    public class Data {

        @SerializedName("offset")
        public Integer offset;

        @SerializedName("limit")
        public Integer limit;

        @SerializedName("total")
        public Integer total;

        @SerializedName("count")
        public Integer count;

        @SerializedName("results")
        public List<Character> characters = null;
    }


    public class Events {

        @SerializedName("available")
        public Integer available;

        @SerializedName("collectionURI")
        public String collectionURI;

        @SerializedName("items")
        public List<Event> eventItems = null;

        @SerializedName("returned")
        public Integer returned;

    }

    public class Comic extends BaseObservable {
        @SerializedName("resourceURI")
        public String resourceURI;

        @Bindable
        @SerializedName("name")
        public String name;

        @Bindable
        private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
            notifyPropertyChanged(BR.description);
        }

        @Bindable
        public ObservableBoolean loaded;
        public boolean isLoaded() {
            if (loaded == null)
                loaded = new ObservableBoolean(false);

            return loaded.get();

        }
        public void setLoaded(boolean loaded) {
            if (this.loaded == null)
                this.loaded = new ObservableBoolean(loaded);
            else
                this.loaded.set(loaded);

            notifyPropertyChanged(BR.loaded);
        }

        @Bindable
        private String thumbnail;
        public String getThumbnail() {
            return thumbnail;
        }
        public void setThumbnail(String path, String extension) {
            thumbnail = path + "." + extension;
        }
    }


    public class SeriesItem extends BaseObservable{
        @SerializedName("resourceURI")
        public String resourceURI;

        @SerializedName("name")
        public String name;

        @Bindable
        private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
            notifyPropertyChanged(BR.description);
        }

        @Bindable
        public ObservableBoolean loaded;
        public boolean isLoaded() {
            if (loaded == null)
                loaded = new ObservableBoolean(false);

            return loaded.get();

        }
        public void setLoaded(boolean loaded) {
            if (this.loaded == null)
                this.loaded = new ObservableBoolean(loaded);
            else
                this.loaded.set(loaded);

            notifyPropertyChanged(BR.loaded);
        }

        @Bindable
        private String thumbnail;
        public String getThumbnail() {
            return thumbnail;
        }
        public void setThumbnail(String path, String extension) {
            thumbnail = path + "." + extension;
        }
    }


    public class Story extends BaseObservable{
        @SerializedName("resourceURI")
        public String resourceURI;

        @SerializedName("name")
        public String name;

        @SerializedName("type")
        public String type;

        @Bindable
        private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
            notifyPropertyChanged(BR.description);
        }

        @Bindable
        public ObservableBoolean loaded;
        public boolean isLoaded() {
            if (loaded == null)
                loaded = new ObservableBoolean(false);

            return loaded.get();

        }
        public void setLoaded(boolean loaded) {
            if (this.loaded == null)
                this.loaded = new ObservableBoolean(loaded);
            else
                this.loaded.set(loaded);

            notifyPropertyChanged(BR.loaded);
        }

        @Bindable
        private String thumbnail;
        public String getThumbnail() {
            return thumbnail;
        }
        public void setThumbnail(String path, String extension) {
            thumbnail = path + "." + extension;
        }
    }


    public class Event extends BaseObservable{
        @SerializedName("resourceURI")
        public String resourceURI;

        @SerializedName("name")
        public String name;

        @Bindable
        private String description;
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
            notifyPropertyChanged(BR.description);
        }

        @Bindable
        public ObservableBoolean loaded;
        public boolean isLoaded() {
            if (loaded == null)
                loaded = new ObservableBoolean(false);

            return loaded.get();

        }
        public void setLoaded(boolean loaded) {
            if (this.loaded == null)
                this.loaded = new ObservableBoolean(loaded);
            else
                this.loaded.set(loaded);

            notifyPropertyChanged(BR.loaded);
        }

        @Bindable
        private String thumbnail;
        public String getThumbnail() {
            return thumbnail;
        }
        public void setThumbnail(String path, String extension) {
            thumbnail = path + "." + extension;
        }

    }


    public class Character extends BaseObservable {
        @SerializedName("id")
        public Integer id;

        @SerializedName("name")
        @Bindable
        public String name;

        @SerializedName("description")
        @Bindable
        public String description;

        @SerializedName("modified")
        public String modified;

        @SerializedName("thumbnail")
        public Thumbnail thumbnail;

        @SerializedName("resourceURI")
        public String resourceURI;

        @SerializedName("comics")
        public Comics comics;

        @SerializedName("series")
        public Series series;

        @SerializedName("stories")
        public Stories stories;

        @SerializedName("events")
        public Events events;

        @SerializedName("urls")
        public List<Url> urls = null;
    }


    public class Series {

        @SerializedName("available")
        public Integer available;

        @SerializedName("collectionURI")
        public String collectionURI;

        @SerializedName("items")
        public List<SeriesItem> seriesItems;

        @SerializedName("returned")
        public Integer returned;
    }


    public class Stories {

        @SerializedName("available")
        public Integer available;

        @SerializedName("collectionURI")
        public String collectionURI;

        @SerializedName("items")
        public List<Story> storyItems;

        @SerializedName("returned")
        public Integer returned;

    }


    public class Thumbnail {
        @SerializedName("path")
        public String path;

        @SerializedName("extension")
        public String extension;
    }


    public class Url {
        @SerializedName("type")
        public String type;

        @SerializedName("url")
        public String url;
    }

}
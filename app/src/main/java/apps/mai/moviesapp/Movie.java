package apps.mai.moviesapp;

import java.io.Serializable;

/**
 * Created by Mai_ on 12-Aug-16.
 */
public class Movie implements Serializable{
    String title,description,poster_path,release_date;
    int vote_average;
    byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Movie(String title, String description, String poster_path,
                 String release_date, int vote_average) {
        this.title = title;
        this.description = description;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        //this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getVote_average() {
        return vote_average;
    }

    public void setVote_average(int vote_average) {
        this.vote_average = vote_average;
    }
}

package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import utils.Utils;

public class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<Genre> genres;
    private final LinkedHashMap<String, Integer> userHistory;
    private final ArrayList<String> userFavorites;

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public LinkedHashMap<String, Integer> getUserHistory() {
        return userHistory;
    }

    public ArrayList<String> getUserFavorites() {
        return userFavorites;
    }

    public Video(String title, int year, ArrayList<String> cast, ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = new ArrayList<>(genres.stream().map(Utils::stringToGenre).toList());
        this.userFavorites = new ArrayList<>();
        this.userHistory = new LinkedHashMap<>();
    }

    public Video(ShowInput show) {
        this.title = show.getTitle();
        this.year = show.getYear();
        this.cast = show.getCast();
        this.genres = new ArrayList<>(show.getGenres().stream().map(Utils::stringToGenre).toList());
        this.userFavorites = new ArrayList<>();
        this.userHistory = new LinkedHashMap<>();
    }

    public Video(Video video) {
        this.title = video.title;
        this.year = video.year;
        this.cast = new ArrayList<>(video.cast);
        this.genres = new ArrayList<>(video.genres);
        this.userFavorites = new ArrayList<>(video.userFavorites);
        this.userHistory = new LinkedHashMap<>(video.userHistory);
    }

    public double getAverageRating() {
        return 0;
    }

    public boolean hasRating() {
        return true;
    }

    public int getDuration() { return 0; }

    public int getNumberOfViews() {
        int sum = 0;
        for (Integer num : this.getUserHistory().values()) {
            sum += num;
        }

        return sum;
    }
}

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

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<Genre> getGenres() {
        return genres;
    }

    public final LinkedHashMap<String, Integer> getUserHistory() {
        return userHistory;
    }

    public final ArrayList<String> getUserFavorites() {
        return userFavorites;
    }

    public Video(final String title, final int year, final ArrayList<String> cast,
                 final ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = new ArrayList<>(genres.stream().map(Utils::stringToGenre).toList());
        this.userFavorites = new ArrayList<>();
        this.userHistory = new LinkedHashMap<>();
    }

    public Video(final ShowInput show) {
        this.title = show.getTitle();
        this.year = show.getYear();
        this.cast = show.getCast();
        this.genres = new ArrayList<>(show.getGenres().stream().map(Utils::stringToGenre).toList());
        this.userFavorites = new ArrayList<>();
        this.userHistory = new LinkedHashMap<>();
    }

    public Video(final Video video) {
        this.title = video.title;
        this.year = video.year;
        this.cast = new ArrayList<>(video.cast);
        this.genres = new ArrayList<>(video.genres);
        this.userFavorites = new ArrayList<>(video.userFavorites);
        this.userHistory = new LinkedHashMap<>(video.userHistory);
    }

    /**
     * A method that returns the average rating of a video (this method is overriden in the two
     * subclasses of Video class - Movie and Serial).
     * @return a double value representing the average value of the rating of the given video.
     */
    public double getAverageRating() {
        return 0;
    }

    /**
     * A method that returns if a certain video has a rating (this method is overriden in the two
     * subclasses of the Video class - Movie and Serial).
     * @return a boolean value representing if a video has at least one rating or not.
     */
    public boolean hasRating() {
        return true;
    }

    /**
     * A method that returns the duration of a certain video. This method is overriden in the two
     * subclasses of the Video class - Movie and Serial).
     * @return an integer representing the duration of the given video.
     */
    public int getDuration() {
        return 0;
    }

    /**
     * A method that returns the total number of views based on the number of views of every user.
     * @return an integer representing the total number of views.
     */
    public int getNumberOfViews() {
        int sum = 0;
        for (Integer num : this.getUserHistory().values()) {
            sum += num;
        }

        return sum;
    }
}

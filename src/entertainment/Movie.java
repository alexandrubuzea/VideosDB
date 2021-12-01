package entertainment;

import fileio.*;
import java.util.Map;
import java.util.LinkedHashMap;

public class Movie extends Video {
    private int duration;

    private LinkedHashMap<User, Double> givenRatings;
    private LinkedHashMap<User, Integer> userHistory;

    public LinkedHashMap<User, Double> getGivenRatings() {
        return givenRatings;
    }

    public LinkedHashMap<User, Integer> getUserHistory() {
        return userHistory;
    }

    public int getDuration() {
        return duration;
    }

    public Movie(MovieInputData movieData) {
        super(movieData.getTitle(), movieData.getYear(), movieData.getCast(), movieData.getGenres());
        this.duration = movieData.getDuration();
        this.givenRatings = new LinkedHashMap<>();
        this.userHistory = new LinkedHashMap<>();
    }
}

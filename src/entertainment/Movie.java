package entertainment;

import fileio.*;
import java.util.LinkedHashMap;

public class Movie extends Video {
    private final int duration;

    public int getDuration() {
        return duration;
    }

    private final LinkedHashMap<User, Double> givenRatings;

    public LinkedHashMap<User, Double> getGivenRatings() {
        return givenRatings;
    }

    public Movie(MovieInputData movieData) {
        super(movieData.getTitle(), movieData.getYear(), movieData.getCast(), movieData.getGenres());
        this.duration = movieData.getDuration();
        this.givenRatings = new LinkedHashMap<>();
    }
}

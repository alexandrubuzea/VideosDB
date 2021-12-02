package entertainment;

import fileio.*;
import utils.Utils;

import java.util.LinkedHashMap;
import java.util.ArrayList;

public class Movie extends Video {
    private final int duration;

    @Override
    public int getDuration() {
        return duration;
    }

    private final LinkedHashMap<String, Double> givenRatings;

    public LinkedHashMap<String, Double> getGivenRatings() {
        return givenRatings;
    }

    public Movie(MovieInputData movieData) {
        super(movieData.getTitle(), movieData.getYear(), movieData.getCast(), movieData.getGenres());
        this.duration = movieData.getDuration();
        this.givenRatings = new LinkedHashMap<>();
    }

    public Movie(Video video, int duration, LinkedHashMap<String, Double> givenRatings) {
        super(video);
        this.duration = duration;
        this.givenRatings = givenRatings;
    }

    @Override
    public double getAverageRating() {
        ArrayList<Double> ratings = new ArrayList<>(this.givenRatings.values().stream().toList());

        return Utils.getMean(ratings);
    }

    public boolean hasRating() {
        return (this.getGivenRatings().size() != 0);
    }
}

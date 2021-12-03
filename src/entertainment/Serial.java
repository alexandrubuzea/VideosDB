package entertainment;

import fileio.SerialInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class Serial extends Video {
    private final int numberOfSeasons;

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    private final ArrayList<Season> seasons;

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    private final ArrayList<LinkedHashMap<String, Double>> givenRatings;

    public ArrayList<LinkedHashMap<String, Double>> getGivenRatings() {
        return givenRatings;
    }

    public Serial(final SerialInputData data) {
        super(data.getTitle(), data.getYear(), data.getCast(), data.getGenres());
        this.numberOfSeasons = data.getNumberSeason();
        this.seasons = new ArrayList<>(data.getSeasons());
        this.givenRatings = new ArrayList<>();
        for (int i = 0; i < numberOfSeasons; ++i) {
            this.givenRatings.add(new LinkedHashMap<>());
        }
    }

    /**
     * A method that returns the average value of the ratings given by all users (calculated as the
     * mean value of each season's ratings). If a season does not have a rating, then the average
     * rating is 0.
     * @return A Double value representing the averag rating of the serial.
     */
    public double getAverageRating() {

        ArrayList<Double> seasonRatings = new ArrayList<>();

        for (int i = 0; i < numberOfSeasons; ++i) {
            ArrayList<Double> ratings = new ArrayList<>(this.givenRatings.get(i).
                    values().stream().toList());
            if (ratings.isEmpty()) {
                seasonRatings.add(0.0);
                continue;
            }

            seasonRatings.add(Utils.getMean(ratings));
        }

        return Utils.getMean(seasonRatings);
    }

    /**
     * A method that returns if a certain serial has a rating (i. e. at least one of its seasons
     * have a rating).
     * @return A boolean value representing if the serial has at least one rating or not.
     */
    public boolean hasRating() {
        for (LinkedHashMap<String, Double> map : this.givenRatings) {
            if (map.size() != 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getDuration() {
        int sum = 0;

        for (Season season : this.getSeasons()) {
            sum += season.getDuration();
        }

        return sum;
    }
}

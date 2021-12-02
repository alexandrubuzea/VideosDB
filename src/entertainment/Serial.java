package entertainment;

import fileio.SerialInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Serial extends Video{
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

    public Serial(SerialInputData data) {
        super(data.getTitle(), data.getYear(), data.getCast(), data.getGenres());
        this.numberOfSeasons = data.getNumberSeason();
        this.seasons = new ArrayList<>(data.getSeasons());
        this.givenRatings = new ArrayList<>();
        for (int i = 0; i < numberOfSeasons; ++i) {
            this.givenRatings.add(new LinkedHashMap<>());
        }
    }

    public double getAverageRating() {


        ArrayList<Double> seasonRatings = new ArrayList<>();

        for (int i = 0; i < numberOfSeasons; ++i) {
            ArrayList<Double> ratings = new ArrayList<>(this.givenRatings.get(i).values().stream().toList());
            if (ratings.isEmpty()) {
                seasonRatings.add(0.0);
                continue;
            }

            seasonRatings.add(Utils.getMean(ratings));
        }

        return Utils.getMean(seasonRatings);
    }

    public boolean hasRating() {
        for (LinkedHashMap<String, Double> map : this.givenRatings)
            if (map.size() != 0)
                return true;

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

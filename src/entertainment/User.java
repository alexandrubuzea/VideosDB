package entertainment;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.AbstractMap.SimpleEntry;

public class User {
    /**
     * User's username
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * The history of the movies seen
     */
    private final LinkedHashMap<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private final LinkedHashMap<String, Double> givenMovieRatings;

    private final LinkedHashMap<SimpleEntry<String, Integer>, Double> givenSerialRatings;

    public User(final UserInputData userData) {
        this.username = userData.getUsername();
        this.subscriptionType = userData.getSubscriptionType();
        this.favoriteMovies = new ArrayList<>(userData.getFavoriteMovies());
        this.history = new LinkedHashMap<>(userData.getHistory());
        this.givenSerialRatings = new LinkedHashMap<>();
        this.givenMovieRatings = new LinkedHashMap<>();
    }

    public final String getUsername() {
        return username;
    }

    public final String getSubscriptionType() {
        return subscriptionType;
    }

    public final LinkedHashMap<String, Integer> getHistory() {
        return history;
    }

    public final ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public final LinkedHashMap<String, Double> getGivenMovieRatings() {
        return givenMovieRatings;
    }

    public final LinkedHashMap<SimpleEntry<String, Integer>, Double> getGivenSerialRatings() {
        return givenSerialRatings;
    }

    public User(final User user) {
        this.username = user.username;
        this.subscriptionType = user.subscriptionType;
        this.favoriteMovies = new ArrayList<>(user.favoriteMovies);
        this.history = new LinkedHashMap<>(user.getHistory());
        this.givenSerialRatings = new LinkedHashMap<>();
        this.givenMovieRatings = new LinkedHashMap<>();
    }

    /**
     * A function that returns the total number of ratings given by an user (which is the sum of
     * the total number of ratings given).
     * @return an integer representing the total number of ratings given by the given user.
     */
    public int getNumberOfRatings() {
        return this.givenMovieRatings.size() + this.givenSerialRatings.size();
    }
}

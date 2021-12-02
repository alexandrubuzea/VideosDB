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

    public User(UserInputData userData) {
        this.username = userData.getUsername();
        this.subscriptionType = userData.getSubscriptionType();
        this.favoriteMovies = new ArrayList<>(userData.getFavoriteMovies());
        this.history = new LinkedHashMap<>(userData.getHistory());
        this.givenSerialRatings = new LinkedHashMap<>();
        this.givenMovieRatings = new LinkedHashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public LinkedHashMap<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public LinkedHashMap<String, Double> getGivenMovieRatings() {
        return givenMovieRatings;
    }

    public LinkedHashMap<SimpleEntry<String, Integer>, Double> getGivenSerialRatings() {
        return givenSerialRatings;
    }

    public User(User user) {
        this.username = user.username;
        this.subscriptionType = user.subscriptionType;
        this.favoriteMovies = new ArrayList<>(user.favoriteMovies);
        this.history = new LinkedHashMap<>(user.getHistory());
        this.givenSerialRatings = new LinkedHashMap<>();
        this.givenMovieRatings = new LinkedHashMap<>();
    }

    public int getNumberOfRatings() {
        return this.givenMovieRatings.size() + this.givenSerialRatings.size();
    }
}

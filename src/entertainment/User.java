package entertainment;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
    private final Map<String, Integer> history;
    /**
     * Movies added to favorites
     */
    private final ArrayList<String> favoriteMovies;

    private final Map<Video, Double> givenRatings;

    public User(UserInputData userData) {
        this.username = userData.getUsername();
        this.subscriptionType = userData.getSubscriptionType();
        this.favoriteMovies = userData.getFavoriteMovies();
        this.history = userData.getHistory();
        this.givenRatings = new LinkedHashMap<Video, Double>();
    }
}

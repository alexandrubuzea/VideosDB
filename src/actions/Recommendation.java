package actions;

import database.Database;
import entertainment.Genre;
import entertainment.User;
import entertainment.Video;
import fileio.ActionInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Recommendation {

    private Recommendation() { }

    /**
     * A method that performs a recommendation given on the input data (which contains the
     * recommendation's details: sort criteria, recommendation type, user etc).
     * @param recommendation
     * @return a String representing the answer to the given recommendation
     */
    public static String performRecommendation(final ActionInputData recommendation) {
        String recommendationAnswer = null;

        recommendationAnswer = switch (recommendation.getType()) {
            case "standard" -> Recommendation.standard(recommendation);
            case "best_unseen" -> Recommendation.bestUnseen(recommendation);
            case "popular" -> Recommendation.popular(recommendation);
            case "favorite" -> Recommendation.favorite(recommendation);
            case "search" -> Recommendation.search(recommendation);
            default -> "not yet implemented";
        };

        return recommendationAnswer;
    }

    /**
     * A method that performs a standard recommendation (the first unseen video from the
     * database).
     * @param recommendation
     * @return a string representing the answer to a standard recommendation.
     */
    public static String standard(final ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        String myUsername = recommendation.getUsername();
        User myUser = database.getUserByName(myUsername);

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        if (myVideos.isEmpty()) {
            return "StandardRecommendation cannot be applied!";
        }

        return "StandardRecommendation result: " + myVideos.get(0).getTitle();
    }

    /**
     * A method which performs a bestUnseen recommendation (depending on the given user,
     * the method returns an answer containing the best unseen video - with the highest
     * average rating).
     * @param recommendation
     * @return a string representing the answer to this type of recommendation.
     */
    public static String bestUnseen(final ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        User myUser = database.getUserByName(recommendation.getUsername());

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        if (myVideos.isEmpty()) {
            return "BestRatedUnseenRecommendation cannot be applied!";
        }

        myVideos.sort((o1, o2) -> o1.getAverageRating() > o2.getAverageRating() ? -1 : 1);

        return "BestRatedUnseenRecommendation result: " + myVideos.get(0).getTitle();
    }

    /**
     * A method which performs a popularity recommendation (recommends a video from the most
     * popular genre).
     * @param recommendation
     * @return a string representing the asnwer to this kind of recommendation.
     */
    public static String popular(final ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        if (myUser.getSubscriptionType().equals("BASIC")) {
            return "PopularRecommendation cannot be applied!";
        }

        LinkedHashMap<Genre, Integer> popularity = Database.getGenrePopularity();

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        if (myVideos.isEmpty()) {
            return "PopularRecommendation cannot be applied!";
        }

        ArrayList<Genre> genresArray = new ArrayList<>(popularity.entrySet().stream().
                sorted((o1, o2) -> o1.getValue() >= o2.getValue() ? -1 : 1).
                map(Map.Entry::getKey).toList());

        for (Genre genre : genresArray) {
            for (Video video : myVideos) {
                if (video.getGenres().contains(genre)) {
                    return "PopularRecommendation result: " + video.getTitle();
                }
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    /**
     * A method which performs a favorite recommendation (recommends a video based on how many
     * users added it to favorite).
     * @param recommendation
     * @return a string representing the answer to this type of recommendation.
     */
    public static String favorite(final ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        if (myUser.getSubscriptionType().equals("BASIC")) {
            return "FavoriteRecommendation cannot be applied!";
        }

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        myVideos.sort((o1, o2) -> o1.getUserFavorites().size()
                > o2.getUserFavorites().size() ? -1 : 1);

        if (myVideos.isEmpty()) {
            return "FavoriteRecommendation cannot be applied!";
        }

        return "FavoriteRecommendation result: " + myVideos.get(0).getTitle();
    }

    /**
     * A method which performs a search recommendation based on the user given ratings,
     * corresponding to a year and to a genre.
     * @param recommendation
     * @return a string representing the answer to this type of recommendation.
     */
    public static String search(final ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (myUser.getSubscriptionType().equals("BASIC")) {
            return "SearchRecommendation cannot be applied!";
        }

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        String genre = recommendation.getGenre();

        if (myVideos.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }

        myVideos.removeIf((video) -> !video.getGenres().contains(Utils.stringToGenre(genre)));

        if (myVideos.isEmpty()) {
            return "SearchRecommendation cannot be applied!";
        }

        myVideos.sort((o1, o2) -> {
            if (o1.getAverageRating() != o2.getAverageRating()) {
                return o1.getAverageRating() > o2.getAverageRating() ? 1 : -1;
            } else if (o1.getTitle().compareTo(o2.getTitle()) != 0) {
                return o1.getTitle().compareTo(o2.getTitle()) > 0 ? 1 : -1;
            } else {
                return 0;
            }
        });

        StringBuilder recommendationResult = new StringBuilder("SearchRecommendation result: [");

        for (Video video : myVideos) {
            recommendationResult.append(video.getTitle());
            recommendationResult.append(", ");
        }

        recommendationResult.deleteCharAt(recommendationResult.length() - 1);
        recommendationResult.deleteCharAt(recommendationResult.length() - 1);

        recommendationResult.append("]");
        return recommendationResult.toString();
    }
}

package actions;

import database.Database;
import entertainment.User;
import entertainment.Video;
import fileio.ActionInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;

public class Recommendation {
    public static String performRecommendation(ActionInputData recommendation) {
        String recommendationAnswer = null;

        switch(recommendation.getObjectType()) {
            case "standard" -> recommendationAnswer = Recommendation.standard(recommendation);
            case "best_unseen" -> recommendationAnswer = Recommendation.bestUnseen(recommendation);
            case "popular" -> recommendationAnswer = Recommendation.popular(recommendation);
            case "favorite" -> recommendationAnswer = Recommendation.favorite(recommendation);
            case "search" -> recommendationAnswer = Recommendation.search(recommendation);
        }

        return recommendationAnswer;
    }

    public static String standard(ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        String myUsername = recommendation.getUsername();
        User myUser = database.getUserByName(myUsername);

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        if (myVideos.isEmpty())
            return "StandardRecommendation cannot be applied!";

        return "StandardRecommendation result: " + myVideos.get(0).getTitle();
    }

    public static String bestUnseen(ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        User myUser = database.getUserByName(recommendation.getUsername());

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        myVideos.sort((o1, o2) -> o1.getAverageRating() > o2.getAverageRating() ? -1 : 1);

        return "StandardRecommendation result: " + myVideos.get(0).getTitle();
    }

    public static String popular(ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        if (myUser.getSubscriptionType().equals("BASIC"))
            return "PopularRecommendation cannot be applied!";

        // TODO
        return null;
    }

    public static String favorite(ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        if (myUser.getSubscriptionType().equals("BASIC"))
            return "FavoriteRecommendation cannot be applied!";

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        myVideos.sort((o1, o2) -> o1.getUserFavorites().size() > o2.getUserFavorites().size() ? -1 : 1);

        if (myVideos.get(0).getUserFavorites().size() == 0)
            return "FavoriteRecommendation cannot be applied!";

        return "FavoriteRecommendation result: " + myVideos.get(0).getTitle();
    }

    public static String search(ActionInputData recommendation) {
        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(recommendation.getUsername());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (myUser.getSubscriptionType().equals("BASIC"))
            return "SearchRecommendation cannot be applied!";

        myVideos.removeIf((video) -> myUser.getHistory().containsKey(video.getTitle()));

        String genre = recommendation.getGenre();

        if (myVideos.isEmpty())
            return "SearchRecommendation cannot be applied!";

        myVideos.removeIf((video) -> !video.getGenres().contains(Utils.stringToGenre(genre)));

        if (myVideos.isEmpty())
            return "SearchRecommendation cannot be applied!";

        myVideos.sort((o1, o2) -> {
            if (o1.getAverageRating() != o2.getAverageRating())
                return o1.getAverageRating() > o2.getAverageRating() ? 1 : -1;
            else if (o1.getTitle().compareTo(o2.getTitle()) != 0)
                return o1.getTitle().compareTo(o2.getTitle()) > 0 ? 1 : -1;
            else
                return 0;
        });


        return "SearchRecommendation result: " + myVideos.get(0).getTitle();
    }
}

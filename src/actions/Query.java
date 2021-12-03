package actions;

import entertainment.Video;
import entertainment.Genre;
import entertainment.Serial;
import entertainment.Movie;
import entertainment.User;
import fileio.ActionInputData;
import database.Database;

import java.util.regex.Pattern;
import java.util.ArrayList;
import actor.Actor;
import utils.Utils;
import java.util.AbstractMap.SimpleEntry;
import java.util.Objects;

public final class Query {

    private Query() { }

    /**
     * A method which performs a query based on the query details (stored in query variable).
     * @param query
     * @return a string which is the result of our query.
     */
    public static String performQuery(final ActionInputData query) {
        String queryAnswer = null;

        queryAnswer = switch (query.getObjectType()) {
            case "actors" -> Query.actorQuery(query);
            case "movies", "shows" -> Query.videoQuery(query);
            case "users" -> Query.userQuery(query);
            default -> "Not yet implemented";
        };

        return queryAnswer;
    }

    /**
     * A method which performs an actor query based on the informations given in query
     * variable.
     * @param query
     * @return a string which contains the output of the desired query.
     */
    public static String actorQuery(final ActionInputData query) {
        String queryAnswer = null;

        queryAnswer = switch (query.getCriteria()) {
            case "average" -> Query.averageActorQuery(query);
            case "awards" -> Query.awardsActorQuery(query);
            case "filter_description" -> Query.filterActorQuery(query);
            default -> "Not yet implemented";
        };

        return queryAnswer;
    }

    /**
     * A method which performs an actor query. It returns a sorted list of actor
     * names. The sorting criteria is the average rating of the videos
     * @param query
     * @return
     */
    public static String averageActorQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Actor> myActors = new ArrayList<>(database.getActors());
        ArrayList<SimpleEntry<Actor, Double>> actorRatings = new ArrayList<>();
        for (Actor actor : myActors) {
            ArrayList<String> filmography = new ArrayList<>(actor.getFilmography());

            filmography.removeIf((videoname) -> database.getVideoByName(videoname) == null);
            filmography.removeIf((videoname) -> !database.getVideoByName(videoname).hasRating());
            ArrayList<Double> ratings = new ArrayList<>(filmography.stream().map((videoname) ->
                    database.getVideoByName(videoname).getAverageRating()).toList());

            if (ratings.isEmpty()) {
                continue;
            }

            actorRatings.add(new SimpleEntry<>(actor, Utils.getMean(ratings)));
        }

        if (query.getSortType().equals("asc")) {
            actorRatings.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o1.getValue() > o2.getValue()) ? 1 : -1;
                } else if (o1.getKey().getName().compareTo(o2.getKey().getName()) != 0) {
                    return (o1.getKey().getName().compareTo(o2.getKey().getName()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            actorRatings.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o2.getValue() > o1.getValue()) ? 1 : -1;
                } else if (o2.getKey().getName().compareTo(o1.getKey().getName()) != 0) {
                    return (o2.getKey().getName().compareTo(o1.getKey().getName()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        for (int i = query.getNumber(); i < actorRatings.size();) {
            actorRatings.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (actorRatings.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        ArrayList<Actor> actorArray = new ArrayList<>(actorRatings.stream().
                map(SimpleEntry::getKey).toList());

        for (Actor actor : actorArray) {
            queryResult.append(actor.getName() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method that performs a query based on its details (it filters only the actors with
     * the given awards).
     * @param query
     * @return a string representing the result of the query (a suitable message)
     */
    public static String awardsActorQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Actor> myActors = new ArrayList<>(database.getActors());

        ArrayList<String> myAwards = new ArrayList<>(query.getFilters().get(3));

        for (String award : myAwards) {
            myActors.removeIf((actor) -> !actor.getAwards().containsKey(Utils.
                    stringToAwards(award)));
        }

        ArrayList<SimpleEntry<Actor, Integer>> actorAwards = new ArrayList<>(myActors.
                stream().map((actor) -> new SimpleEntry<>(actor, actor.getNumberOfAwards())).
                toList());

        if (query.getSortType().equals("asc")) {
            actorAwards.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o1.getValue() > o2.getValue()) ? 1 : -1;
                } else if (o1.getKey().getName().compareTo(o2.getKey().getName()) != 0) {
                    return o1.getKey().getName().compareTo(o2.getKey().getName());
                } else {
                    return 0;
                }
            });
        } else {
            actorAwards.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o2.getValue() > o1.getValue()) ? 1 : -1;
                } else if (o1.getKey().getName().compareTo(o2.getKey().getName()) != 0) {
                    return o2.getKey().getName().compareTo(o1.getKey().getName());
                } else {
                    return 0;
                }
            });
        }

        myActors = new ArrayList<>(actorAwards.stream().map(SimpleEntry::getKey).toList());

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myActors.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Actor actor: myActors) {
            queryResult.append(actor.getName() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method that performs a query based on it details (it performs a filter for actor
     * career description by matching some given keywords).
     * @param query
     * @return a string that contains the actor names with the description matching the
     * given keywords
     */
    public static String filterActorQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<String> keywords = new ArrayList<>(query.getFilters().get(2));

        ArrayList<Actor> myActors = new ArrayList<>(database.getActors());

        for (String word : keywords) {
            Pattern pattern = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);

            myActors.removeIf((actor) -> !pattern.matcher(actor.getCareerDescription()).find());
        }

        if (query.getSortType().equals("asc")) {
            myActors.sort((o1, o2) -> {
                if (o1.getName().compareTo(o2.getName()) != 0) {
                    return (o1.getName().compareTo(o2.getName()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            myActors.sort((o1, o2) -> {
                if (o1.getName().compareTo(o2.getName()) != 0) {
                    return (o2.getName().compareTo(o1.getName()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myActors.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Actor actor : myActors) {
            queryResult.append(actor.getName() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method that performs a video query and returns its result. There are more types
     * of queries available.
     * @param query
     * @return a string which describes the result of the query.
     */
    public static String videoQuery(final ActionInputData query) {
        String queryAnswer = null;

        queryAnswer = switch (query.getCriteria()) {
            case "ratings" -> Query.ratingsVideoQuery(query);
            case "favorite" -> Query.favoriteVideoQuery(query);
            case "longest" -> Query.longestVideoQuery(query);
            case "most_viewed" -> Query.mostViewedVideoQuery(query);
            default -> "Not yet implemented";
        };

        return queryAnswer;
    }

    /**
     * A method that performs a video query based on the given filters.
     * @param query
     * @return a string which describes the result of the video query (the list of serials
     * or movies which matches the filters given).
     */
    public static String ratingsVideoQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (query.getObjectType().equals("shows")) {
            myVideos.removeIf((video) -> video instanceof Movie);
        } else {
            myVideos.removeIf((video) -> video instanceof Serial);
        }
        myVideos.removeIf((video) -> !video.hasRating());

        if (myVideos.isEmpty()) {
            return "Query result: []";
        }
        String yearString = query.getFilters().get(0).get(0);

        if (yearString != null) {
            int yearInt = Integer.parseInt(yearString);
            myVideos.removeIf((video) -> video.getYear() != yearInt);
        }

        if (myVideos.isEmpty()) {
            return "Query result: []";
        }

        if (query.getFilters().get(1).get(0) != null) {
            Genre genre = Utils.stringToGenre(query.getFilters().get(1).get(0));
            myVideos.removeIf((video) -> !video.getGenres().contains(genre));
        }

        if (myVideos.isEmpty()) {
            return "Query result: []";
        }

        ArrayList<SimpleEntry<Video, Double>> videoRatings = new ArrayList<>(myVideos.
                stream().map((video) -> new SimpleEntry<>(video, video.getAverageRating())).
                toList());

        if (query.getSortType().equals("asc")) {
            videoRatings.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o1.getValue() > o2.getValue()) ? 1 : -1;
                } else if (o1.getKey().getTitle().compareTo(o2.getKey().getTitle()) != 0) {
                    return (o1.getKey().getTitle().compareTo(o2.getKey().getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            videoRatings.sort((o1, o2) -> {
                if (!Objects.equals(o1.getValue(), o2.getValue())) {
                    return (o2.getValue() > o1.getValue()) ? 1 : -1;
                } else if (o1.getKey().getTitle().compareTo(o2.getKey().getTitle()) != 0) {
                    return (o2.getKey().getTitle().compareTo(o1.getKey().getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        myVideos = new ArrayList<>(videoRatings.stream().map(SimpleEntry::getKey).toList());

        for (int i = query.getNumber(); i < myVideos.size();) {
            myVideos.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myVideos.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Video video : myVideos) {
            queryResult.append(video.getTitle() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method which performs a query about the most favorite videos (returns a list
     * with those videos which are favorite (they exist in user's favorite list)).
     * @param query
     * @return a string which describes the result of the query (the list of serials/movies
     * given)
     */
    public static String favoriteVideoQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (query.getObjectType().equals("shows")) {
            myVideos.removeIf((video) -> video instanceof Movie);
        } else {
            myVideos.removeIf((video) -> video instanceof Serial);
        }

        myVideos.removeIf((video) -> video.getUserFavorites().size() == 0);

        String yearString = query.getFilters().get(0).get(0);

        if (yearString != null) {
            int yearInt = Integer.parseInt(yearString);
            myVideos.removeIf((video) -> video.getYear() != yearInt);
        }

        if (myVideos.isEmpty()) {
            return "Query result: []";
        }

        if (query.getFilters().get(1).get(0) != null) {
            Genre genre = Utils.stringToGenre(query.getFilters().get(1).get(0));
            myVideos.removeIf((video) -> !video.getGenres().contains(genre));
        }

        if (query.getSortType().equals("asc")) {
            myVideos.sort((o1, o2) -> {
                if (o1.getUserFavorites().size() != o2.getUserFavorites().size()) {
                    return o1.getUserFavorites().size() > o2.getUserFavorites().size() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return (o1.getTitle().compareTo(o2.getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            myVideos.sort((o1, o2) -> {
                if (o1.getUserFavorites().size() != o2.getUserFavorites().size()) {
                    return o2.getUserFavorites().size() > o1.getUserFavorites().size() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return (o2.getTitle().compareTo(o1.getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        for (int i = query.getNumber(); i < myVideos.size();) {
            myVideos.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myVideos.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Video video : myVideos) {
            queryResult.append(video.getTitle() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method which performs a query based on the given filters. In addition, the
     * videos are sorted after duration.
     * @param query
     * @return a string which describes the result of the query (the list of serials or
     * movies sorted after duration).
     */
    public static String longestVideoQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (query.getObjectType().equals("shows")) {
            myVideos.removeIf((video) -> video instanceof Movie);
        } else {
            myVideos.removeIf((video) -> video instanceof Serial);
        }

        String genre = query.getFilters().get(1).get(0);

        if (genre != null) {
            myVideos.removeIf((video) -> !video.getGenres().contains(Utils.stringToGenre(genre)));
        }

        String yearString = query.getFilters().get(0).get(0);

        if (yearString != null) {
            Integer year = Integer.parseInt(yearString);
            myVideos.removeIf((video) -> video.getYear() != year);
        }

        if (query.getSortType().equals("asc")) {
            myVideos.sort((o1, o2) -> {
                if (o1.getDuration() != o2.getDuration()) {
                    return o1.getDuration() > o2.getDuration() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return (o1.getTitle().compareTo(o2.getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            myVideos.sort((o1, o2) -> {
                if (o1.getDuration() != o2.getDuration()) {
                    return o2.getDuration() > o1.getDuration() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return (o2.getTitle().compareTo(o1.getTitle()) > 0) ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        for (int i = query.getNumber(); i < myVideos.size();) {
            myVideos.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myVideos.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Video video : myVideos) {
            queryResult.append(video.getTitle() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method which performs a query using the details specified in the query variable.
     * It can use filters for year and genre and the resulting videos are sorted after
     * the number of views.
     * @param query
     * @return a string which describes the result of the query (the filtered list, sorted
     * after the number of views).
     */
    public static String mostViewedVideoQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<Video> myVideos = new ArrayList<>(database.getVideos());

        if (query.getObjectType().equals("shows")) {
            myVideos.removeIf((video) -> video instanceof Movie);
        } else {
            myVideos.removeIf((video) -> video instanceof Serial);
        }

        myVideos.removeIf((video) -> video.getNumberOfViews() == 0);

        String genre = query.getFilters().get(1).get(0);

        if (genre != null) {
            myVideos.removeIf((video) -> !video.getGenres().contains(Utils.stringToGenre(genre)));
        }

        String yearString = query.getFilters().get(0).get(0);

        if (yearString != null) {
            Integer year = Integer.parseInt(yearString);
            myVideos.removeIf((video) -> video.getYear() != year);
        }

        if (query.getSortType().equals("asc")) {
            myVideos.sort((o1, o2) -> {
                if (o1.getNumberOfViews() != o2.getNumberOfViews()) {
                    return o1.getNumberOfViews() > o2.getNumberOfViews() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return o1.getTitle().compareTo(o2.getTitle()) > 0 ? 1 : -1;
                } else {
                    return 0;
                }
            });
        } else {
            myVideos.sort((o1, o2) -> {
                if (o1.getNumberOfViews() != o2.getNumberOfViews()) {
                    return o2.getNumberOfViews() > o1.getNumberOfViews() ? 1 : -1;
                } else if (!o1.getTitle().equals(o2.getTitle())) {
                    return o2.getTitle().compareTo(o1.getTitle()) > 0 ? 1 : -1;
                } else {
                    return 0;
                }
            });
        }

        for (int i = query.getNumber(); i < myVideos.size();) {
            myVideos.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (myVideos.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (Video video : myVideos) {
            queryResult.append(video.getTitle() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }

    /**
     * A method which performs a query using the specified filters for users and sorts the
     * result after number of ratings given (the most active users).
     * @param query
     * @return a string which describes the result of the query (the most active users).
     */
    public static String userQuery(final ActionInputData query) {
        Database database = Database.getDatabase(Database.getInput());

        ArrayList<User> users = database.getUsers();
        ArrayList<User> activeUsers = new ArrayList<>(users);

        activeUsers.removeIf((actor) -> actor.getNumberOfRatings() == 0);

        if (query.getSortType().equals("asc")) {
            activeUsers.sort((o1, o2) -> {
                if (o2.getNumberOfRatings() != o1.getNumberOfRatings()) {
                    return o1.getNumberOfRatings() > o2.getNumberOfRatings() ? 1 : -1;
                } else {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            });
        } else {
            activeUsers.sort((o1, o2) -> {
                if (o2.getNumberOfRatings() != o1.getNumberOfRatings()) {
                    return o2.getNumberOfRatings() > o1.getNumberOfRatings() ? 1 : -1;
                } else {
                    return o2.getUsername().compareTo(o1.getUsername());
                }
            });
        }

        for (int i = query.getNumber(); i < activeUsers.size();) {
            activeUsers.remove(query.getNumber());
        }

        StringBuilder queryResult = new StringBuilder("Query result: [");
        if (activeUsers.isEmpty()) {
            queryResult.append("]");
            return queryResult.toString();
        }

        for (User user : activeUsers) {
            queryResult.append(user.getUsername() + ", ");
        }

        queryResult.deleteCharAt(queryResult.length() - 1);
        queryResult.deleteCharAt(queryResult.length() - 1);

        queryResult.append("]");

        return queryResult.toString();
    }
}

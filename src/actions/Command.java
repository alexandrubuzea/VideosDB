package actions;

import fileio.ActionInputData;
import entertainment.User;
import database.Database;
import entertainment.Video;
import entertainment.Movie;
import entertainment.Serial;
import java.util.AbstractMap.SimpleEntry;

public final class Command {

    private Command() { }

    /**
     * A method that performs one of the three command types described below.
     * @param command
     * @return a string which describes the result of the command.
     */
    public static String performCommand(final ActionInputData command) {
        String commandAnswer = null;
        commandAnswer = switch (command.getType()) {
            case "favorite" -> Command.favoriteCommand(command);
            case "view" -> Command.viewCommand(command);
            case "rating" -> Command.ratingCommand(command);
            default -> "Not yet implemented";
        };

        return commandAnswer;
    }

    /**
     * A method which performs a favorite command (adds a video in the favorite
     * list of an user if the user viewed that video).
     * @param command
     * @return a string which describes the result of the command (adds the video
     * in the favorite list if possible and returns a suitable message).
     */
    public static String favoriteCommand(final ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        if (!myUser.getHistory().containsKey(videoName)) {
            return "error -> " + videoName + " is not seen";
        }

        for (String name : myUser.getFavoriteMovies()) {
            if (name.equals(videoName)) {
                return "error -> " + videoName + " is already in favourite list";
            }
        }

        myUser.getFavoriteMovies().add(videoName);

        Video myVideo = database.getVideoByName(videoName);
        myVideo.getUserFavorites().add(myUser.getUsername());

        return "success -> " + videoName + " was added as favourite";
    }

    /**
     * A method which describes the view command (adds a video to a user history - having in
     * consideration that it is possible for the user to have saw the video multiple times).
     * @param command
     * @return a string which describes the reuslt of the command (adds the video to a user
     * history if possible and returns a suitable message).
     */
    public static String viewCommand(final ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        boolean wasSeen = myUser.getHistory().containsKey(videoName);

        Video myVideo = database.getVideoByName(videoName);
        if (wasSeen) {
            Integer numberOfSeen = myUser.getHistory().get(videoName);
            myUser.getHistory().replace(videoName, numberOfSeen, numberOfSeen + 1);
            myVideo.getUserHistory().replace(username, numberOfSeen, numberOfSeen + 1);

            return "success -> " + videoName + " was viewed with total views of "
                    + (numberOfSeen + 1);
        }

        myUser.getHistory().put(videoName, 1);
        myVideo.getUserHistory().put(username, 1);

        return "success -> " + videoName + " was viewed with total views of 1";
    }

    /**
     * A method that performs a rating command (an user gives a rating to a video -
     * movie or serial, if he already viewed it).
     * @param command
     * @return a string which describes the result of the command (success or fail).
     */
    public static String ratingCommand(final ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        Video myVideo = database.getVideoByName(videoName);

        if (!myUser.getHistory().containsKey(videoName)) {
            return "error -> " + videoName + " is not seen";
        }

        double rating = command.getGrade();

        if (myVideo instanceof Movie) {
            if (myUser.getGivenMovieRatings().containsKey(videoName)) {
                return "error -> " + videoName + " has been already rated";
            }

            myUser.getGivenMovieRatings().put(videoName, rating);
            ((Movie) myVideo).getGivenRatings().put(username, rating);

            return "success -> " + videoName + " was rated with " + rating + " by " + username;

        } else {
            int seasonNumber = command.getSeasonNumber();

            if (myUser.getGivenSerialRatings().containsKey(new SimpleEntry<>(
                    videoName, seasonNumber))) {
                return "error -> " + videoName + " has been already rated";
            }

            myUser.getGivenSerialRatings().put(new SimpleEntry<>(videoName, seasonNumber), rating);
            ((Serial) myVideo).getGivenRatings().get(seasonNumber - 1).put(username, rating);

            return "success -> " + videoName + " was rated with " + rating + " by " + username;
        }
    }
}

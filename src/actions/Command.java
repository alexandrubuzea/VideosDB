package actions;

import fileio.ActionInputData;
import entertainment.User;
import database.Database;
import entertainment.Video;
import entertainment.Movie;
import entertainment.Serial;
import java.util.AbstractMap.SimpleEntry;

public class Command {
    public static String performCommand(ActionInputData command) {
        String commandAnswer = null;
        switch(command.getType()) {
            case "favorite" -> commandAnswer = Command.favoriteCommand(command);
            case "view" -> commandAnswer = Command.viewCommand(command);
            case "rating" -> commandAnswer = Command.ratingCommand(command);
        }

        return commandAnswer;
    }

    public static String favoriteCommand(ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        if (!myUser.getHistory().containsKey(videoName))
            return "error -> " + videoName + " is not seen";

        for (String name : myUser.getFavoriteMovies())
            if (name.equals(videoName))
                return "error -> " + videoName + " is already in favourite list";

        myUser.getFavoriteMovies().add(videoName);

        Video myVideo = database.getVideoByName(videoName);
        myVideo.getUserFavorites().add(myUser.getUsername());

        return "success -> " + videoName + " was added as favourite";
    }

    public static String viewCommand(ActionInputData command) {
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

            return "success -> " + videoName + " with total views of " + (numberOfSeen + 1);
        }

        myUser.getHistory().put(videoName, 1);
        myVideo.getUserHistory().put(username, 1);

        return "success -> " + videoName + " with total views of 1";
    }

    public static String ratingCommand(ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        Video myVideo = database.getVideoByName(videoName);

        if (!myUser.getHistory().containsKey(videoName))
            return "error -> " + videoName + " is not seen";

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

            if(myUser.getGivenSerialRatings().containsKey(new SimpleEntry<>(videoName, seasonNumber))) {
                return "error -> " + videoName + " has been already rated";
            }

            myUser.getGivenSerialRatings().put(new SimpleEntry<>(videoName, seasonNumber), rating);
            ((Serial) myVideo).getGivenRatings().get(seasonNumber - 1).put(username, rating);

            return "success -> " + videoName + " was rated with " + rating + " by " + username;
        }
    }
}

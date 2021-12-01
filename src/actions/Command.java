package actions;

import fileio.ActionInputData;
import entertainment.User;
import database.Database;

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

        return "success -> " + videoName + " was added as favourite";
    }

    public static String viewCommand(ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        boolean wasSeen = myUser.getHistory().containsKey(videoName);

        if (wasSeen) {
            Integer numberOfSeen = myUser.getHistory().get(videoName);
            myUser.getHistory().replace(videoName, numberOfSeen, numberOfSeen + 1);
            return "success -> " + videoName + " with total views of " + (numberOfSeen + 1);
        }

        myUser.getHistory().put(videoName, 1);

        return "success -> " + videoName + " with total views of 1";
    }

    public static String ratingCommand(ActionInputData command) {
        String username = command.getUsername();

        Database database = Database.getDatabase(Database.getInput());

        User myUser = database.getUserByName(username);

        String videoName = command.getTitle();

        if (!myUser.getHistory().containsKey(videoName))
            return "error -> " + videoName + " is not seen";

        double rating = command.getGrade();

        if (myUser.getGivenRatings().containsKey(videoName))
            return "error -> " + videoName + " has been already rated";

        myUser.getGivenRatings().put(videoName, rating);

        return "success -> " + videoName + " was rated with " + rating + " by " + username;
    }
}

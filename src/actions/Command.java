package actions;

import fileio.ActionInputData;
import common.Constants;
import entertainment.User;
import database.Database;

public class Command {
    public static String performCommand(ActionInputData command) {
        String commandAnswer;
        switch(command.getType()) {
            case Constants.FAVORITE_MOVIES -> commandAnswer = Command.favoriteCommand(command);
        };

        return null;
    }

    public static String favoriteCommand(ActionInputData command) {
        String username = command.getUsername();
        Database database = Database.getDatabase(Database.getInput());

        return null;
    }
}

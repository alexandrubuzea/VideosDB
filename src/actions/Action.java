package actions;

import fileio.ActionInputData;
import common.Constants;

public final class Action {

    /**
     * A static method that determines the type of action required and performs that type of
     * action.
     * @param action - which tells us all the action's details (type, user, video, sorting criteria
     *               etc).
     * @return actionAnswer - a String representing the answer to the given action (a command,
     * query or recommendation).
     */
    public static String performTask(final ActionInputData action) {
        String actionAnswer;
        actionAnswer = switch (action.getActionType()) {
            case Constants.COMMAND -> Command.performCommand(action);
            case Constants.QUERY -> Query.performQuery(action);
            case Constants.RECOMMENDATION -> Recommendation.performRecommendation(action);
            default -> "Not yet implemented";
        };

        return actionAnswer;
    }

    private Action() { }
}

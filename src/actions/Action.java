package actions;

import fileio.ActionInputData;
import common.Constants;

public class Action {
    public static String performTask(ActionInputData action) {
        String actionAnswer;
        switch(action.getActionType()) {
            case Constants.COMMAND -> actionAnswer = Command.performCommand(action);
            //case Constants.QUERY -> actionAnswer = Query.performQuery(action);
            //case Constants.RECOMMENDATION -> actionAnswer = Recommendation.performRecommendation(action);
            default -> actionAnswer = new String("Not yet implemented");
        }

        return actionAnswer;
    }
}

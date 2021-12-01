package actions;

import fileio.ActionInputData;

public class Query {
    public static String performQuery(ActionInputData query) {
        String queryAnswer = null;

        switch(query.getType()) {
            case "average" -> queryAnswer = Query.averageCommand(query);
            case "awards" -> queryAnswer = Query.awardsCommand(query);
            case "filter" -> queryAnswer = Query.filterCommand(query);
        }

        return queryAnswer;
    }
}

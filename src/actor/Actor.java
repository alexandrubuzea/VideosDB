package actor;

import java.util.ArrayList;
import fileio.ActorInputData;
import java.util.LinkedHashMap;

public class Actor {
    private final String name;
    private final String careerDescription;
    private final ArrayList<String> filmography;
    private final LinkedHashMap <ActorsAwards, Integer> awards;

    public Actor(ActorInputData actorData) {
        this.name = actorData.getName();
        this.careerDescription = actorData.getCareerDescription();
        this.filmography = actorData.getFilmography();
        this.awards = new LinkedHashMap<> (actorData.getAwards());
    }
}

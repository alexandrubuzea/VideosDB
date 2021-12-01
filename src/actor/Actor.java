package actor;

import java.util.ArrayList;
import fileio.ActorInputData;
import java.util.LinkedHashMap;

public class Actor {
    private final String name;

    public String getName() {
        return name;
    }

    private final String careerDescription;

    public String getCareerDescription() {
        return careerDescription;
    }

    private final ArrayList<String> filmography;

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    private final LinkedHashMap <ActorsAwards, Integer> awards;

    public LinkedHashMap<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Actor(ActorInputData actorData) {
        this.name = actorData.getName();
        this.careerDescription = actorData.getCareerDescription();
        this.filmography = new ArrayList<>(actorData.getFilmography());
        this.awards = new LinkedHashMap<> (actorData.getAwards());
    }
}

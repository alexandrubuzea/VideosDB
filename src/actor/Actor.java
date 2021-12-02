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

    public Actor(Actor actor) {
        this.name = actor.name;
        this.careerDescription = actor.careerDescription;
        this.filmography = new ArrayList<>(actor.filmography);
        this.awards = new LinkedHashMap<>(actor.awards);
    }

    public int getNumberOfAwards() {
        ArrayList<Integer> numberOfAwards = new ArrayList<>(this.getAwards().values());

        int sum = 0;
        for (Integer number : numberOfAwards)
            sum += number;

        return sum;
    }
}

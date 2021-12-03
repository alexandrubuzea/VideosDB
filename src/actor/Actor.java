package actor;

import java.util.ArrayList;
import fileio.ActorInputData;
import java.util.LinkedHashMap;

public class Actor {
    private final String name;

    public final String getName() {
        return name;
    }

    private final String careerDescription;

    public final String getCareerDescription() {
        return careerDescription;
    }

    private final ArrayList<String> filmography;

    public final ArrayList<String> getFilmography() {
        return filmography;
    }

    private final LinkedHashMap<ActorsAwards, Integer> awards;

    public final LinkedHashMap<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public Actor(final ActorInputData actorData) {
        this.name = actorData.getName();
        this.careerDescription = actorData.getCareerDescription();
        this.filmography = new ArrayList<>(actorData.getFilmography());
        this.awards = new LinkedHashMap<>(actorData.getAwards());
    }

    public Actor(final Actor actor) {
        this.name = actor.name;
        this.careerDescription = actor.careerDescription;
        this.filmography = new ArrayList<>(actor.filmography);
        this.awards = new LinkedHashMap<>(actor.awards);
    }

    /**
     * A method which calculates the number of awards of an actor.
     * @return an integer representing the number of awards got by an actor until now.
     */
    public final int getNumberOfAwards() {
        ArrayList<Integer> numberOfAwards = new ArrayList<>(this.getAwards().values());

        int sum = 0;
        for (Integer number : numberOfAwards) {
            sum += number;
        }

        return sum;
    }
}

package database;

import entertainment.Video;
import java.util.List;
import java.util.ArrayList;
import actor.Actor;
import fileio.Input;
import entertainment.Movie;
import entertainment.User;
import entertainment.Serial;

public class Database {
    public List<Video> getVideos() {
        return videos;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public List<User> getUsers() {
        return users;
    }

    private List<Video> videos;
    private List<Actor> actors;
    private List<User> users;
    private static Input input;
    private static Database database = null;

    private Database() {}

    private Database(Input input) {
        database = new Database();
        videos = new ArrayList<Video>();
        actors = new ArrayList<Actor>();
        users = new ArrayList<User>();
        this.input = input;

        videos.addAll(input.getMovies().stream().map((data) -> new Movie(data)).toList());
        videos.addAll(input.getSerials().stream().map((data) -> new Serial(data)).toList());

        actors.addAll(input.getActors().stream().map((data) -> new Actor(data)).toList());
        users.addAll(input.getUsers().stream().map((data) -> new User(data)).toList());

    }

    public static Database getDatabase() {
        if (database == null) {
            database = new Database(input);
        }

        return database;
    }
}

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
    private List<Video> videos;

    public List<Video> getVideos() {
        return videos;
    }

    private List<Actor> actors;

    public List<Actor> getActors() {
        return actors;
    }

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    private static Input input;

    public static Input getInput() {
        return input;
    }

    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    private List<Serial> serials;

    public List<Serial> getSerials() {
        return serials;
    }

    private static Database database = null;

    private Database() {}

    private Database(Input input) {
        database = new Database();
        videos = new ArrayList<>();
        actors = new ArrayList<>();
        users = new ArrayList<>();
        movies = new ArrayList<>();
        serials = new ArrayList<>();
        this.input = input;

        movies.addAll(input.getMovies().stream().map((data) -> new Movie(data)).toList());
        serials.addAll(input.getSerials().stream().map((data) -> new Serial(data)).toList());

        videos.addAll(input.getMovies().stream().map((data) -> new Movie(data)).toList());
        videos.addAll(input.getSerials().stream().map((data) -> new Serial(data)).toList());

        actors.addAll(input.getActors().stream().map((data) -> new Actor(data)).toList());
        users.addAll(input.getUsers().stream().map((data) -> new User(data)).toList());
    }

    public static Database getDatabase(Input input) {
        if (database == null) {
            database = new Database(input);
        }

        return database;
    }
}

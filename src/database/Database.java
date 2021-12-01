package database;

import entertainment.Video;
import java.util.ArrayList;
import actor.Actor;
import fileio.Input;
import entertainment.Movie;
import entertainment.User;
import entertainment.Serial;

public class Database {
    private ArrayList<Video> videos;

    public ArrayList<Video> getVideos() {
        return videos;
    }

    private ArrayList<Actor> actors;

    public ArrayList<Actor> getActors() {
        return actors;
    }

    private ArrayList<User> users;

    public ArrayList<User> getUsers() {
        return users;
    }

    private static Input input;

    public static Input getInput() {
        return input;
    }

    private ArrayList<Movie> movies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    private ArrayList<Serial> serials;

    public ArrayList<Serial> getSerials() {
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
        Database.input = input;

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

    public User getUserByName(String name) {
        ArrayList<User> userList = this.getUsers();
        for (User user : userList)
            if (user.getUsername().equals(name))
                return user;

        return null;
    }

    public Movie getMovieByName(String name) {
        ArrayList<Movie> movieList = this.getMovies();
        for (Movie movie : movieList)
            if (movie.getTitle().equals(name))
                return movie;

        return null;
    }

    public Serial getSerialByName(String name) {
        ArrayList<Serial> serialList = this.getSerials();
        for (Serial serial : serialList)
            if (serial.getTitle().equals(name))
                return serial;

        return null;
    }

    public Video getVideoByName(String name) {
        ArrayList<Video> videoList = this.getVideos();
        for (Video video : videoList)
            if (video.getTitle().equals(name))
                return video;

        return null;
    }

    public Actor getActorByName(String name) {
        ArrayList<Actor> actorList = this.getActors();
        for (Actor actor : actorList)
            if (actor.getName().equals(name))
                return actor;

        return null;
    }
}

package database;

import entertainment.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import actor.Actor;
import fileio.Input;
import utils.Utils;

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

        movies.addAll(input.getMovies().stream().map(Movie::new).toList());
        serials.addAll(input.getSerials().stream().map(Serial::new).toList());

        videos.addAll(input.getMovies().stream().map(Movie::new).toList());
        videos.addAll(input.getSerials().stream().map(Serial::new).toList());

        actors.addAll(input.getActors().stream().map(Actor::new).toList());
        users.addAll(input.getUsers().stream().map(User::new).toList());
    }

    public static Database getDatabase(Input input) {
        if (database == null) {
            database = new Database(input);
        }

        return database;
    }

    public User getUserByName(String name) {
        ArrayList<User> userList = this.getUsers();
        for (User user : userList) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
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

    public void clearDatabase() {
        actors.clear();
        videos.clear();
        movies.clear();
        serials.clear();
        users.clear();
        input = null;
        database = null;
    }

    public static LinkedHashMap<Genre, Integer> getGenrePopularity() {
        LinkedHashMap<Genre, Integer> popularity = new LinkedHashMap<>();

        ArrayList<String> genres = Utils.getGenresAsStrings();

        for (String genre : genres)
            popularity.put(Utils.stringToGenre(genre), 0);

        ArrayList<Video> myVideos = database.getVideos();

        for (Video video : myVideos) {
            int videoPopularity = video.getNumberOfViews();
            for (Genre genre : video.getGenres()) {
                int value = popularity.get(genre);
                popularity.replace(genre, value, value + videoPopularity);
            }
        }

        return popularity;
    }
}

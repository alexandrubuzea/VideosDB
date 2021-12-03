package database;


import entertainment.Movie;
import entertainment.User;
import entertainment.Video;
import entertainment.Serial;
import entertainment.Genre;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import actor.Actor;
import fileio.Input;
import utils.Utils;

public final class Database {
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

    private Database() { }

    private Database(final Input input) {
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

        for (User user : users) {
            for (String videoname : user.getHistory().keySet()) {
                Video video = getVideoByName(videoname);
                if (!video.getUserHistory().containsKey(video.getTitle())) {
                    video.getUserHistory().put(user.getUsername(),
                            user.getHistory().get(videoname));
                } else {
                    int numberOfViews = video.getUserHistory().get(video.getTitle());
                    video.getUserHistory().replace(user.getUsername(), numberOfViews,
                            numberOfViews + user.getHistory().get(videoname));
                }
            }
        }

        for (User user : users) {
            for (String videoname : user.getFavoriteMovies()) {
                Video video = getVideoByName(videoname);
                video.getUserFavorites().add(user.getUsername());
            }
        }
    }

    /**
     * A function that fills our database instance and returns a Database instance based on
     * the given input (Singleton pattern)
     * @param myInput - our given input as defined in the skel
     * @return database - our Database instance
     */
    public static Database getDatabase(final Input myInput) {
        if (database == null) {
            database = new Database(myInput);
        }

        return database;
    }

    /**
     * A method that queries our database and returns the user associated to the given name.
     * Returns null if there is no user with the given name.
     * @param name
     * @return User type - a reference to our user instance.
     */
    public User getUserByName(final String name) {
        ArrayList<User> userList = this.getUsers();
        for (User user : userList) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * A method that queries our database in order to find a movie with the given name.
     * It returns null if there is no movie with the given name.
     * @param name
     * @return Movie - a reference to our found instance.
     */
    public Movie getMovieByName(final String name) {
        ArrayList<Movie> movieList = this.getMovies();
        for (Movie movie : movieList) {
            if (movie.getTitle().equals(name)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * A method that queries our database in order to find a serial with the given name.
     * It returns null if there is no serial with the given name.
     * @param name
     * @return Serial - a reference to our found instance.
     */
    public Serial getSerialByName(final String name) {
        ArrayList<Serial> serialList = this.getSerials();
        for (Serial serial : serialList) {
            if (serial.getTitle().equals(name)) {
                return serial;
            }
        }
        return null;
    }

    /**
     * A method that queries our database in order to find a video with the given name.
     * It returns null if there is no video with the given name.
     * @param name
     * @return Video - a reference to our found instance.
     */
    public Video getVideoByName(final String name) {
        ArrayList<Video> videoList = this.getVideos();
        for (Video video : videoList) {
            if (video.getTitle().equals(name)) {
                return video;
            }
        }

        return null;
    }

    /**
     * A method that queries our database in order to find an actor with the given name.
     * It returns null if there is no actor with the given name.
     * @param name
     * @return Actor - a reference to our found instance.
     */
    public Actor getActorByName(final String name) {
        ArrayList<Actor> actorList = this.getActors();
        for (Actor actor : actorList) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }

        return null;
    }

    /**
     * A function that clears our database instance (prepares it for the next test check).
     */
    public void clearDatabase() {
        actors.clear();
        videos.clear();
        movies.clear();
        serials.clear();
        users.clear();
        input = null;
        database = null;
    }

    /**
     * A function that calculates all our possible genres's popularity. Popularity of a genre is
     * given by the number of views all videos corresponding to a certain genre have.
     * @return a LinkedHashMap containing all our genres (as keys) and their popularities (as
     * integers). This could have been done with an array, but we use Maps because we <3 Maps :).
     */
    public static LinkedHashMap<Genre, Integer> getGenrePopularity() {
        LinkedHashMap<Genre, Integer> popularity = new LinkedHashMap<>();

        ArrayList<String> genres = Utils.getGenresAsStrings();

        for (String genre : genres) {
            popularity.put(Utils.stringToGenre(genre), 0);
        }

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

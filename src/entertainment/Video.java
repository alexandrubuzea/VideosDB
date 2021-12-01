package entertainment;

import fileio.ShowInput;

import java.util.ArrayList;
import utils.Utils;

public class Video {
    private final String title;
    private final int year;
    private final ArrayList<String> cast;
    private final ArrayList<Genre> genres;

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public Video(String title, int year, ArrayList<String> cast, ArrayList<String> genres) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = new ArrayList<>(genres.stream().map((str) -> Utils.stringToGenre(str)).toList());
    }

    public Video(ShowInput show) {
        this.title = show.getTitle();
        this.year = show.getYear();
        this.cast = show.getCast();
        this.genres = new ArrayList<>(show.getGenres().stream().map((str) -> Utils.stringToGenre(str)).toList());
    }
}

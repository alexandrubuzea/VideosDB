package entertainment;

import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Serial extends Video{
    private int numberOfSeasons;
    private ArrayList<Season> seasons;

    public Serial(SerialInputData data) {
        super(data.getTitle(), data.getYear(), data.getCast(), data.getGenres());
        this.numberOfSeasons = data.getNumberSeason();
        this.seasons = new ArrayList<>(data.getSeasons());
    }
}

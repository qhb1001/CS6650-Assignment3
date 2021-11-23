package util;

import lombok.Data;

/**
 * Represents the recording for the skier servlet post request.
 */
@Data
public class SkierServletPostRequest {
    private int resort_id;
    private int season;
    private int day;
    private int skier_id;
    private int time;
    private int lift;

    /**
     * Construct the object instance.
     *
     * @param resort_id the id of the resort
     * @param season the season id
     * @param day the day id
     * @param skier_id the skier id
     */
    public SkierServletPostRequest(int resort_id, int season, int day, int skier_id, int time, int lift) {
        if (resort_id <= 0 || season <= 0 || skier_id <= 0) {
            throw new IllegalArgumentException("The parameter should be larger than zero");
        }

        if (day < 1 || day > 366) {
            throw new IllegalArgumentException("The id of day is out of range [1, 366]");
        }

        this.resort_id = resort_id;
        this.season = season;
        this.day = day;
        this.skier_id = skier_id;
        this.time = time;
        this.lift = lift;
    }

}

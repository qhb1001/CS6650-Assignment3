package datastore.resort;

import lombok.Data;

@Data
public class Resort {
    private int resort_id;
    private int season;
    private int day_in_year;
    private int skier_id;
    private int minute;
    private int lift;
}

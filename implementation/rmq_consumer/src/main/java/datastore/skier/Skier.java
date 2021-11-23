package datastore.skier;

import lombok.Data;

@Data
public class Skier {
    private int skier_id;
    private int day_in_year;
    private int season;
    private int lift;
}

package datastore.skier;

import datastore.Dao;

public class SkierDao extends Dao {
    private Skier skier;

    public SkierDao(Skier skier) {
        this.skier = skier;
    }

    public void initializeSkierRecord() {
        String insertQueryStatement = "INSERT IGNORE skiers(skier_id, season, day_in_year, lift)\n" +
            "values (%d, %d, %d, 0);";
        insertQueryStatement = String.format(insertQueryStatement, skier.getSkier_id(), skier.getSeason(), skier.getDay_in_year());
        execute(insertQueryStatement);
    }

    public void updateSkierRecord() {
        String updateQueryStatement = "UPDATE skiers set lift = lift + %d where skier_id = %d and season = %d and day_in_year = %d;";
        updateQueryStatement = String.format(updateQueryStatement, skier.getLift(), skier.getSkier_id(), skier.getSeason(), skier.getDay_in_year());
        execute(updateQueryStatement);
    }
}

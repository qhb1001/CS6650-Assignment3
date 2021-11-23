package datastore.resort;

import datastore.Dao;

public class ResortDao extends Dao {
    private Resort resort;

    public ResortDao(Resort resort) {
        this.resort = resort;
    }

    public void insertResortRecord() {
        String insertQueryStatement = "INSERT IGNORE resorts(resort_id, season, day_in_year, skier_id, minute, lift)\n" +
            "values (%d, %d, %d, %d, %d, %d);";
        insertQueryStatement = String.format(insertQueryStatement, resort.getResort_id(), resort.getSeason(), resort.getDay_in_year(), resort.getSkier_id(), resort.getMinute(), resort.getLift());
        execute(insertQueryStatement);
    }
}

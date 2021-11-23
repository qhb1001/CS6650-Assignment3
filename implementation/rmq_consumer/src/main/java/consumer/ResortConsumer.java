package consumer;

import datastore.resort.Resort;
import datastore.resort.ResortDao;
import util.SkierServletPostRequest;

/**
 * Consumer for the resort micro-service.
 */
public class ResortConsumer implements Consumer {

    @Override
    public void consume(SkierServletPostRequest request) {
        Resort resort = new Resort();
        resort.setResort_id(request.getResort_id());
        resort.setDay_in_year(request.getDay());
        resort.setLift(request.getLift());
        resort.setMinute(request.getTime());
        resort.setSeason(request.getSeason());
        resort.setSkier_id(request.getSkier_id());

        ResortDao resortDao = new ResortDao(resort);
        resortDao.insertResortRecord();
    }
}

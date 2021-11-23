package consumer;

import datastore.skier.Skier;
import datastore.skier.SkierDao;
import util.SkierServletPostRequest;

/**
 * Consumer for the skier micro-service.
 */
public class SkierConsumer implements Consumer {

    @Override
    public void consume(SkierServletPostRequest request) {
        Skier skier = new Skier();
        skier.setSkier_id(request.getSkier_id());
        skier.setLift(request.getLift());
        skier.setSeason(request.getSeason());
        skier.setDay_in_year(request.getDay());

        SkierDao skierDao = new SkierDao(skier);
        skierDao.initializeSkierRecord();
        skierDao.updateSkierRecord();
    }
}

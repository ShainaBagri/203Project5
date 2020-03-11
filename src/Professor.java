import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Professor extends EntityActive{

    private static final String AGrade_ID_PREFIX = "AGrade -- ";
    private static final String AGrade_KEY = "aGrade";
    private static final int AGrade_PERIOD_SCALE = 1000;


    public Professor(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getPosition());

        if (openPt.isPresent())
        {
            AGrade aGrade = new AGrade(AGrade_ID_PREFIX + this.getId(),
                    openPt.get(), this.getActionPeriod() / AGrade_PERIOD_SCALE,
                    imageStore.getImageList(AGrade_KEY));
            world.addEntity( aGrade);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());

        this.setActionPeriod(this.getActionPeriod() * 10);
    }


}

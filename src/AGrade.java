import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class AGrade extends EntityActive{
 

    private static final Random rand = new Random();

    private static final String Sickness_KEY = "sick";
    private static final String Sickness_ID_SUFFIX = " -- Sickness";
    private static final int Sickness_PERIOD_SCALE = 4;
    private static final int Sickness_ANIMATION_MIN = 50;
    private static final int Sickness_ANIMATION_MAX = 150;

    public AGrade(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
    }

    public void executeActivity( WorldModel world,ImageStore imageStore, EventScheduler scheduler)
    {
//        Point pos = this.getPosition();  // store current position before removing
//
//        world.removeEntity( this);
//        scheduler.unscheduleAllEvents( this);
//
//        Sickness sickness = new Sickness(this.getId() + Sickness_ID_SUFFIX,
//                pos, this.getActionPeriod() / Sickness_PERIOD_SCALE, Sickness_ANIMATION_MIN +
//                        rand.nextInt(Sickness_ANIMATION_MAX - Sickness_ANIMATION_MIN),
//                imageStore.getImageList( Sickness_KEY));
//
//        world.addEntity(sickness);
//        sickness.scheduleActions(scheduler, world, imageStore);
    }

}

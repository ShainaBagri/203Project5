import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FGrade extends EntityActive{
//
//
    public FGrade(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
  }
////

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> target = world.findNearest(this.getPosition(), Player.class);

        int newY = this.getPosition().y+1;
        if(newY>14) {
            newY = 0;
        }
        Point newPos = new Point(this.getPosition().x, newY);
        if (target.isPresent() && newPos.equals(target.get().getPosition()))
        {

            world.setEnd();

            world.removeEntity(target.get());
            scheduler.unscheduleAllEvents( target.get());
        }
        world.moveEntity(this, newPos);

        scheduler.scheduleEvent( this, new Activity(this, world, imageStore), 100);
    }

}

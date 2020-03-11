import processing.core.PImage;

import java.util.List;

public class EntityAnimated extends EntityActive{
    private int animationPeriod;

    public EntityAnimated(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images){
        super(id, position, actionPeriod, images);
        this.animationPeriod = animationPeriod;
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public void nextImage()
    {
        this.setImageIndex((this.getImageIndex() + 1) % this.getImages().size());
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent( this,
                new Animation(this, 0), this.getAnimationPeriod());
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents( this);
        world.removeEntity( this);
    }

}

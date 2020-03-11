import processing.core.PImage;

import java.util.List;

public abstract class EntityActive extends Entity {

    private int actionPeriod;

    public EntityActive(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super (id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod(){
        return actionPeriod;
    }


    public void setActionPeriod(int actionPeriod){
        this.actionPeriod = actionPeriod;
    }

    public abstract void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

}

import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Obstacle extends EntityActive{

    private static final String Sickness_KEY = "sick";
    private static final String Sickness_ID_SUFFIX = " -- Sickness";

    public Obstacle(String id, Point position, List<PImage> images)
    {
        super(id, position, 1, images);
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getPosition());

        if (openPt.isPresent()) {
            Sickness sick = new Sickness(this.getId() + Sickness_ID_SUFFIX,
                    openPt.get(), 1000, 0, imageStore.getImageList(Sickness_KEY));

            world.addEntity(sick);
            sick.scheduleActions(scheduler, world, imageStore);
        }

    }

}


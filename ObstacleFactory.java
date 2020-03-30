import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class ObstacleFactory extends EntityActive{

    public final String Sickness_KEY = "sick";
    public final String Sickness_ID_SUFFIX = " -- Sickness";

    public ObstacleFactory(String id, Point position, List<PImage> images)
    {
        super(id, position, 10, images);
    }

    public abstract void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler);

}


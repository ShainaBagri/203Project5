import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class AGrade extends EntityActive{


    public AGrade(String id, Point position, int actionPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, images);
    }

    public void executeActivity( WorldModel world,ImageStore imageStore, EventScheduler scheduler)
    {

    }

}

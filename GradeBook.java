import processing.core.PImage;

import java.util.List;
import java.util.Random;

public class GradeBook extends EntityAnimated{

    private static final int GradeBook_ANIMATION_REPEAT_COUNT = 7;

    public GradeBook(String id, Point position, List<PImage> images)
    {
        super (id, position, 0, 0, images);
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                new Animation(this, GradeBook_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());

    }


}

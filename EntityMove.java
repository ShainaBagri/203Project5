import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class EntityMove extends EntityAnimated{

    public PathingStrategy strategy;

    public EntityMove(String id,  Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
            super(id, position, actionPeriod, animationPeriod, images);
            //this.strategy = new SingleStepPathing();
            //this.strategy = new AStarPathingStrategy();
            this.strategy = new BestFirstPathing();
            //this.strategy = new SingleStepPathing();

    }

    public abstract boolean moveTo( WorldModel world, Entity target, EventScheduler scheduler);

    public static boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }
}

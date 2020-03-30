import processing.core.PImage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Sickness extends EntityMove{

    public static final String BLACK_IMAGE_NAME = "black";
    public static final int WORLD_WIDTH_SCALE = 1;
    public static final int WORLD_HEIGHT_SCALE = 1;

    public static final int VIEW_WIDTH = 640;
    public static final int VIEW_HEIGHT = 480;
    public static final int TILE_WIDTH = 32;
    public static final int TILE_HEIGHT = 32;
    public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    public Sickness(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        super (id, position, actionPeriod, animationPeriod, images);
    }

    public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> SicknessTarget = world.findNearest(this.getPosition(), Player.class);
        long nextPeriod = this.getActionPeriod();

        if (SicknessTarget.isPresent())
        {

            Point tgtPos = SicknessTarget.get().getPosition();

            if (this.moveTo(world, SicknessTarget.get(), scheduler))
            {

                world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                        new Background(BLACK_IMAGE_NAME,
                                imageStore.getImageList(BLACK_IMAGE_NAME)));

                nextPeriod += this.getActionPeriod();
            }
        }

        scheduler.scheduleEvent( this, new Activity(this, world, imageStore), nextPeriod);
    }

    public boolean moveTo( WorldModel world, Entity target, EventScheduler scheduler)
    {

        if (this.getPosition().adjacent(target.getPosition()))
        {
            world.setEnd();
            world.removeEntity( target);
            scheduler.unscheduleAllEvents( target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity( this, nextPos);
            }
            return false;
        }
    }

    public Point nextPosition(WorldModel world, Point destPos){

        List<Point> points = strategy.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (points.size() == 0) {
            return this.getPosition();
        }

        return points.get(0);
    }



}

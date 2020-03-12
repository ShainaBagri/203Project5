import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

public class ObstacleLevel3 extends ObstacleFactory{
	
	private static final Random rand = new Random();
	private static final Point[] possiblePts = {new Point(19, 3), new Point(19, 11), new Point(3, 1), 
			new Point(3, 14), new Point(1, 7)};

	public ObstacleLevel3(List<PImage> images) {
		super("obstacle_randCol_randRow", possiblePts[rand.nextInt(5)], images);
	}
	
	public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getPosition());

        if (openPt.isPresent()) {
            Sickness sick = new Sickness(this.getId() + super.Sickness_ID_SUFFIX,
                    openPt.get(), this.getActionPeriod()/3, 0, imageStore.getImageList(Sickness_KEY));

            world.addEntity(sick);
            sick.scheduleActions(scheduler, world, imageStore);
        }

    }
}

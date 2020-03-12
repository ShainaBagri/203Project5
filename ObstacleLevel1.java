import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class ObstacleLevel1 extends ObstacleFactory{

	public ObstacleLevel1(List<PImage> images) {
		super("obstacle_19_6", new Point(19, 6), images);
	}
	
	public void executeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround( this.getPosition());

        if (openPt.isPresent()) {
            Sickness sick = new Sickness(this.getId() + super.Sickness_ID_SUFFIX,
                    openPt.get(), this.getActionPeriod(), 0, imageStore.getImageList(Sickness_KEY));

            world.addEntity(sick);
            sick.scheduleActions(scheduler, world, imageStore);
        }

    }
}

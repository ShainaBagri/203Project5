public class Activity implements Action {

    private EntityActive entity;
    private WorldModel world;
    private ImageStore imageStore;


    public Activity(EntityActive entity, WorldModel world,
                  ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }


    public void executeAction(EventScheduler scheduler)
    {

        this.entity.executeActivity( this.world,
                    this.imageStore, scheduler);


    }
}

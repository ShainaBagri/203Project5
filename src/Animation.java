public class Animation implements Action {

    private EntityAnimated entity;
    private int repeatCount;

    public Animation(EntityAnimated entity, int repeatCount)
    {
        this.entity = entity;
        this.repeatCount = repeatCount;

    }

    public void executeAction(EventScheduler scheduler)
    {
        (this.entity).nextImage();

        if (this.repeatCount != 1)
        {
            scheduler.scheduleEvent( (Entity)this.entity,
                    (new Animation(this.entity,
                            Math.max(this.repeatCount - 1, 0))),
                   this.entity.getAnimationPeriod());
        }
    }

}

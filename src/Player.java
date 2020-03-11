import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Player extends EntityAnimated{

    private boolean hasA;

    public Player(String id, Point position, int actionPeriod, int animationPeriod, List<PImage> images)
    {
        super(id, position, actionPeriod, animationPeriod, images);
        hasA = false;
    }

    public boolean getHasA() {
        return hasA;
    }

    public void swapA(){
        this.hasA = !hasA;
    }


}

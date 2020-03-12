import processing.core.PImage;

import java.util.List;

public class Entity{

    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    public String getId(){
        return id;
    }

    public Point getPosition(){
        return this.position;
    }

    public void setPosition(Point p){
        this.position = p;
    }

    public PImage getCurrentImage() { return (this.images.get(imageIndex)); }

    public List<PImage> getImages(){
        return this.images;
    }

    public int getImageIndex(){
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}

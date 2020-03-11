
public class PathNode {
    private Point p;
    private int h;
    private int g;
    private int f;
    private PathNode parent;


    public PathNode(Point p, int h, int g, int f, PathNode parent){
        this.p = p;
        this.h = h;
        this.g = g;
        this.f = f;
        this.parent = parent;
    }

    public PathNode getParent(){
        return parent;
    }

    public void setParent(PathNode p){
        this.parent = p;
    }

    public int getG() { return g;}

    public void setG(int newg) {
        this.f = newg + this.h;
        this.g = newg;
    }

    public int getF() {
        return f;}

    public Point getPoint(){ return p;}




}


public class PathNodeTwo {
    private Point p;
    private int d;
    private PathNodeTwo parent;

    public PathNodeTwo(Point p, int d, PathNodeTwo parent){
        this.p = p;
        this.d = d;
        this.parent = parent;
    }

    public PathNodeTwo getParent2(){
        return parent;
    }

    public void setParent(PathNodeTwo p){
        this.parent = p;
    }

    public int getDist(){
        return d;
    }

    public void setDist(int dist) {
        this.d = dist;
    }

    public Point getPoint(){ return p;}




}

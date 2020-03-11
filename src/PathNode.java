
public class PathNode {
    private Point p;
    private int d;
    private PathNode parent;

    public PathNode(Point p, int d, PathNode parent){
        this.p = p;
        this.d = d;
        this.parent = parent;
    }

    public PathNode getParent(){
        return parent;
    }

    public void setParent(PathNode p){
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

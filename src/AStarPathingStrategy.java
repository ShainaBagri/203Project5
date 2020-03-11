import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy  implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        Comparator<PathNode> comp = (n1, n2) -> n1.getF() - n2.getF();
        PriorityQueue<PathNode> openQ = new PriorityQueue<> (5, comp); //based on the f
        HashMap<Point, PathNode> closedMap = new HashMap<>();
        HashMap<Point, PathNode> openMap = new HashMap<>();
        List<Point> path = new LinkedList<>();

        //add start node to open list and mark it as current node
        int h = Math.abs(start.y - end.y) + Math.abs(start.x - end.x);
        int g = 0;
        int f = h + g;
        PathNode curr = new PathNode(start, h, g, f, null);
        openQ.add(curr);
        openMap.put(start, curr);

        //while loop until path to end is found
        //while(curr.getPoint() != end)
        while(!withinReach.test(curr.getPoint(), end) ) {

            //get list of neighbors using the potential neighbors function
            // filter out if they can't pass through
            // filter if they are in the closed list
            List<Point> neighbors = potentialNeighbors.apply(curr.getPoint())
                    .filter(canPassThrough)
                    .filter(n -> !closedMap.containsKey(n))
                    .collect(Collectors.toList());

            //analyze all valid adjacent nodes, checks if it is in the open list
            // if not add to open list
                // determine the distance from start node g
            // if in the open list
                //if calc g value better than previously calculated g value, replace old g with new one
            //estimate h value
            //add g and h to get an f value
            //mark adjacent nodes parent as the current node
            for(int i = 0; i < neighbors.size(); i++){
                Point newPoint = neighbors.get(i);
                int newG = curr.getG() + 10;
                if (newPoint.x != curr.getPoint().x && newPoint.y != curr.getPoint().y){
                    newG = curr.getG() + 14;
                }
                int newH = (Math.abs(newPoint.y - end.y) + Math.abs(newPoint.x - end.x)) * 10 ;
                int newF = newG + newH;
                PathNode newNode = new PathNode(newPoint, newH, newG, newF, curr);

                if(!openMap.containsKey(neighbors.get(i))){
                    openQ.add(newNode);
                    openMap.put(newNode.getPoint(), newNode);
                }
                else{
                    PathNode oldNode = openMap.get(newPoint);
                    if(oldNode.getG() > newG){
                        oldNode.setG(newG);
                        oldNode.setParent(curr);
                    }
                }
            }
            //move the current node to the closed list and take it off the open lists
            openQ.remove(curr);
            openMap.remove(curr.getPoint());
            closedMap.put(curr.getPoint(), curr);
            //chose node from open queue with the smallest f value and set it to the current node
            if (openQ.size() == 0 ){
                return path;
            }
            else {
                curr = openQ.peek();
            }
        }
        //find the Path by backtracking through to find previous nodes
        while(curr.getParent() != null){
            path.add(0, curr.getPoint());
            curr = curr.getParent();
        }
        return path;

    }

}

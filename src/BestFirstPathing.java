import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestFirstPathing implements PathingStrategy{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Comparator<PathNode> comp = (n1, n2) -> n1.getDist() - n2.getDist();
        PriorityQueue<PathNode> pq = new PriorityQueue<>(5, comp); //based on the f
        PathNode curr = new PathNode(start, 0, null);
        HashMap<Point, PathNode> visited = new HashMap<>();
        pq.add(curr);
        List<Point> path = new LinkedList<>();

        while(!pq.isEmpty() && curr.getDist()<15) {
            curr = pq.remove();
            if(curr.getPoint().equals(end)) {
                while(curr.getParent() != null){
                    path.add(0, curr.getPoint());
                    curr = curr.getParent();
                }
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(curr.getPoint())
                    .filter(canPassThrough)
                    .filter(n -> !visited.containsKey(n))
                    .collect(Collectors.toList());
            for(int i=0; i<neighbors.size(); i++) {
                PathNode newNode = new PathNode(neighbors.get(i), curr.getDist()+1, curr);
                pq.add(newNode);
            }
            visited.put(curr.getPoint(), curr);
        }
        return path;
    }
}

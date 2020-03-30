import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestFirstPathing implements PathingStrategy {

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        Comparator<PathNodeTwo> comp = (n1, n2) -> n1.getDist() - n2.getDist();
        PriorityQueue<PathNodeTwo> pq = new PriorityQueue<>(5, comp); //based on the f
        PathNodeTwo curr2 = new PathNodeTwo(start, 0, null);
        HashMap<Point, PathNodeTwo> visited = new HashMap<>();
        pq.add(curr2);
        List<Point> path = new LinkedList<>();

        while(!pq.isEmpty()){//&& curr2.getDist()<75) {
            curr2 = pq.remove();
            if(curr2.getPoint().equals(end) || curr2.getDist()>15) {
                while(curr2.getParent2() != null){
                    path.add(0, curr2.getPoint());
                    curr2 = curr2.getParent2();
                }
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(curr2.getPoint())
                    .filter(canPassThrough)
                    .filter(n -> !visited.containsKey(n))
                    .collect(Collectors.toList());
            for(int i=0; i<neighbors.size(); i++) {
                PathNodeTwo newNode = new PathNodeTwo(neighbors.get(i), curr2.getDist()+1, curr2);
                pq.add(newNode);
            }
            visited.put(curr2.getPoint(), curr2);
        }
        return path;
    }
}

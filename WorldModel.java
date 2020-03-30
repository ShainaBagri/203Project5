import processing.core.PImage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;
   private boolean end;

   private static final int FISH_REACH = 1;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();
      this.end = false;

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public boolean getEnd() { return end;}

   public void setEnd() { end = true;}

   public Set<Entity> getEntities() {
      return entities;
   }

   public int getNumCols() {
      return numCols;
   }

   public int getNumRows() {
      return numRows;
   }

   public void tryAddEntity(Entity entity)
   {
      if (isOccupied( entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      this.addEntity(entity);
   }

   public boolean withinBounds( Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
              pos.x >= 0 && pos.x < this.numCols;
   }

   public boolean isOccupied( Point pos)
   {
      return this.withinBounds( pos) &&
              this.getOccupancyCell( pos) != null;
   }

   public Optional<Point> findOpenAround( Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (this.withinBounds( newPt) &&
                    !this.isOccupied( newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public Optional<Entity> findNearest(Point pos, Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (kind.isInstance(entity))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }


   private static Optional<Entity> nearestEntity(List<Entity> entities, Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }


   /*
      Assumes that there is no entity currently occupying the
      intended destination cell.
   */
   public void addEntity( Entity entity)
   {
      if (this.withinBounds( entity.getPosition()))
      {
         this.setOccupancyCell( entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public void moveEntity( Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (this.withinBounds( pos) && !pos.equals(oldPos) && !isOccupied(pos)){
         this.setOccupancyCell( oldPos, null);
         this.removeEntityAt( pos);
         this.setOccupancyCell( pos, entity);
         entity.setPosition(pos);
      }

      else if (this.withinBounds(pos) && !pos.equals(oldPos) && entity.getClass() == Player.class) {
         Optional<Entity> ent = getOccupant(pos);
         if (ent!=null && ent.get().getClass() == AGrade.class && !((Player) entity).getHasA()) {
            ((Player) entity).swapA();
            this.setOccupancyCell( oldPos, null);
            this.removeEntityAt( pos);
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos);
         }
         else if(((Player) entity).getHasA()){
            List<Point> neighbors = CARDINAL_NEIGHBORS.apply(oldPos)
                    .filter(n -> ent!=null && ent.get().getClass() == GradeBook.class)
                    .collect(Collectors.toList());
            if(neighbors.size() != 0){
               ((Player) entity).swapA();
            }
         }
         else if(ent!=null && (ent.get().getClass() == FGrade.class || ent.get().getClass() == Sickness.class)) {
            this.removeEntity(entity);
            end = true;
         }
      }
   }

   public void removeEntity( Entity entity)
   {
      this.removeEntityAt( entity.getPosition());
   }

   public void removeEntityAt( Point pos)
   {
      if (this.withinBounds( pos)
              && this.getOccupancyCell( pos) != null)
      {
         Entity entity = this.getOccupancyCell( pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         Point p = new Point(-1, -1);
         entity.setPosition(p);
         this.entities.remove(entity);
         this.setOccupancyCell( pos, null);
      }
   }

   public void setBackground( Point pos, Background background)
   {
      if (this.withinBounds( pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> getOccupant( Point pos)
   {
      if (this.isOccupied( pos))
      {
         return Optional.of(this.getOccupancyCell( pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public Entity getOccupancyCell( Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }

   public void setOccupancyCell( Point pos,
                                       Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public Background getBackgroundCell( Point pos)
   {
      return this.background[pos.y][pos.x];
   }

   public void setBackgroundCell( Point pos,
                                        Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (this.withinBounds( pos))
      {
         return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   static final Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
           point ->
                   Stream.<Point>builder()
                           .add(new Point(point.x, point.y - 1))
                           .add(new Point(point.x, point.y + 1))
                           .add(new Point(point.x - 1, point.y))
                           .add(new Point(point.x + 1, point.y))
                           .build();

}

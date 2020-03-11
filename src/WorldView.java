import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

/*
WorldView ideally mostly controls drawing the current part of the whole world
that we can see based on the viewport
*/

final class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;


   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
   }


   private void drawBackground()
   {
      for (int row = 0; row < this.world.getNumCols(); row++)
      {
         for (int col = 0; col < this.world.getNumCols(); col++)
         {
            Point worldPoint = new Point( col, row);
            Optional<PImage> image = this.world.getBackgroundImage(
                    worldPoint);
            if (image.isPresent())
            {
               this.screen.image(image.get(), col * this.tileWidth,
                       row * this.tileHeight);
            }
         }
      }
   }

//   private void drawEntities()
//   {
//      for (Entity entity : this.world.getEntities())
//      {
//         Point pos = entity.getPosition();
//
//         if (viewport.contains(pos))
//         {
//            Point viewPoint = this.viewport.worldToViewport(pos.x, pos.y);
//            this.screen.image(entity.getCurrentImage(),
//                    viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
//         }
//      }
//   }


   public void drawViewport()
   {
      this.drawBackground();
      //this.drawEntities();
   }


}

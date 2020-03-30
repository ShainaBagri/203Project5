import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;


/*
ImageStore: to ideally keep track of the images used in our virtual world
 */



final class ImageStore
{
   private Map<String, List<PImage>> images;
   private List<PImage> defaultImages;

   private static final String aGrade_KEY = "aGrade";
   private static final int aGrade_NUM_PROPERTIES = 5;
   private static final int aGrade_ID = 1;
   private static final int aGrade_COL = 2;
   private static final int aGrade_ROW = 3;
   private static final int aGrade_ACTION_PERIOD = 4;

   private static final String fGrade_KEY = "fGrade";
   private static final int fGrade_NUM_PROPERTIES = 5;
   private static final int fGrade_ID = 1;
   private static final int fGrade_COL = 2;
   private static final int fGrade_ROW = 3;
   private static final int fGrade_ACTION_PERIOD = 4;

   private static final String GRADEBOOK_KEY = "gradeBook";
   private static final int GRADEBOOK_NUM_PROPERTIES = 4;
   private static final int GRADEBOOK_ID = 1;
   private static final int GRADEBOOK_COL = 2;
   private static final int GRADEBOOK_ROW = 3;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final String Profesor_KEY = "prof";
   private static final int Profesor_NUM_PROPERTIES = 5;
   private static final int Profesor_ID = 1;
   private static final int Profesor_COL = 2;
   private static final int Profesor_ROW = 3;
   private static final int Profesor_ACTION_PERIOD = 4;

   private static final int COLOR_MASK = 0xffffff;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;
   private static final int KEYED_IMAGE_MIN = 5;
   private static final int PROPERTY_KEY = 0;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }


   public List<PImage> getImageList( String key)
   {
      return this.images.getOrDefault(key, this.defaultImages);
   }

   public List<PImage> getImages(String key)
   {
      List<PImage> imgs = images.get(key);
      if (imgs == null)
      {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }

   public void loadImages(Scanner in, PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            processImageLine( in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }

   public void load(Scanner in, WorldModel world)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!this.processLine(in.nextLine(), world))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }


   private void processImageLine(String line, PApplet screen)
   {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2)
      {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1)
         {
            List<PImage> imgs = getImages(key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN)
            {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }

   /*
Called with color for which alpha should be set and alpha value.
setAlpha(img, color(255, 255, 255), 0));
*/
   private static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   private boolean processLine(String line, WorldModel world)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return parseBackground(properties, world);
            case aGrade_KEY:
               return parseAGrade(properties, world);
            case fGrade_KEY:
               return parseFGrade(properties, world);
            case GRADEBOOK_KEY:
               return parseGradeBook(properties, world);
            case Profesor_KEY:
               return parseProfessor(properties, world);
         }
      }

      return false;
   }

   private boolean parseBackground(String [] properties, WorldModel world)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground( pt,
                 new Background(id, this.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseAGrade(String [] properties, WorldModel world)
   {
      if (properties.length == aGrade_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[aGrade_COL]),
                 Integer.parseInt(properties[aGrade_ROW]));
         AGrade aGrade = new AGrade(properties[aGrade_ID],
                 pt, Integer.parseInt(properties[aGrade_ACTION_PERIOD]), this.getImageList(aGrade_KEY));
         world.tryAddEntity(aGrade);
      }

      return properties.length == aGrade_NUM_PROPERTIES;
   }

   private boolean parseFGrade(String [] properties, WorldModel world)
   {
      if (properties.length == fGrade_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[fGrade_COL]),
                 Integer.parseInt(properties[fGrade_ROW]));
         FGrade fGrade = new FGrade(properties[fGrade_ID],
                 pt, Integer.parseInt(properties[fGrade_ACTION_PERIOD]), this.getImageList(fGrade_KEY));
         world.tryAddEntity(fGrade);
      }

      return properties.length == aGrade_NUM_PROPERTIES;
   }

   private boolean parseGradeBook(String [] properties, WorldModel world)
   {
      if (properties.length == GRADEBOOK_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[GRADEBOOK_COL]),
                 Integer.parseInt(properties[GRADEBOOK_ROW]));
         GradeBook gb = new GradeBook(properties[GRADEBOOK_ID],
                 pt, this.getImageList( GRADEBOOK_KEY));
         world.tryAddEntity(gb);
      }

      return properties.length == GRADEBOOK_NUM_PROPERTIES;
   }

   private boolean parseProfessor(String [] properties, WorldModel world)
   {
      if (properties.length == Profesor_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[Profesor_COL]),
                 Integer.parseInt(properties[Profesor_ROW]));
          Professor professor = new Professor(properties[Profesor_ID],
                 pt,
                 Integer.parseInt(properties[Profesor_ACTION_PERIOD]),
                 this.getImageList( Profesor_KEY));
         world.tryAddEntity( professor);
      }

      return properties.length == Profesor_NUM_PROPERTIES;
   }

}

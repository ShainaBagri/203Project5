import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class ImageStore {
    private Map<String, List<PImage>> images;
    private List<PImage> defaultImages;

    private static final String BGND_KEY = "background";
    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

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
                    System.err.println(String.format("Ainvalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e)
            {
                System.err.println(String.format("Binvalid entry on line %d",
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
            //switch (properties[PROPERTY_KEY])
            //{
                //case BGND_KEY:
                    return parseBackground(properties, world);
//                case OCTO_KEY:
//                    return parseOcto(properties, world);
//                case OBSTACLE_KEY:
//                    return parseObstacle(properties, world);
//                case FISH_KEY:
//                    return parseFish(properties, world);
//                case ATLANTIS_KEY:
//                    return pargreyTiletlantis(properties, world);
//                case SGRASS_KEY:
//                    return parseSgrass(properties, world);
            //}
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
            world.setBackground( pt, new Background(id, this.getImageList(id)));
        }
        return properties.length == BGND_NUM_PROPERTIES;
    }

}

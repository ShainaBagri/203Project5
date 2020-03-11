import processing.core.*;

public class KeyIn extends PApplet{

    private Point p;

    public KeyIn(){
        this.p = new Point(0, 0);
    }

    public KeyIn(int x, int y){
        this.p = new Point(x, y);
    }

    public void keyPressed()
    {
        if (key == CODED)
        {
            int dx = 0;
            int dy = 0;

            switch (keyCode)
            {
                case UP:
                    dy = -1;
                    System.out.println("UP");
                    break;
                case DOWN:
                    dy = 1;
                    System.out.println("DOWN");
                    break;
                case LEFT:
                    dx = -1;
                    System.out.println("LEFT");
                    break;
                case RIGHT:
                    dx = 1;
                    System.out.println("RIGHT");
                    break;
            }

            this.p = new Point(dx, dy);

        }
    }

    public Point getPoint(){
        return this.p;
    }


}

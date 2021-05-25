import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Stack;

public class DepthFirstSearchVisualizer extends JPanel {


        public static final int SIMULATION_SPEED = 30;

        public static final int NORTH = 1;
        public static final int EAST = 2;
        public static final int SOUTH = 3;
        public static final int WEST = 4;
        public static final int VISITED = 5;

        private int xOff = 2,yOff = 2;
        private int currentX, currentY;
        private int totalVisited = 0;
        private int gridWidth = 40;
        private int gridHeight = 40;
        private int nodeSize = 16;
        private int grid[];

        private Stack<Point> visitedCoordinates;
        private Thread thread;
        private Point start;
        private Random random;
        private List<Point> path;

        private Color current;
        private Color visited;
        private Color bounds;


                public DepthFirstSearchVisualizer(){
                        setPreferredSize(new Dimension(gridWidth * nodeSize   , gridHeight * nodeSize ));
                        grid = new int[gridWidth * gridHeight];
                        visitedCoordinates = new Stack<>();
                        path = new ArrayList<>();
                        random = new Random();
                        current = new Color(random.nextInt(255) , random.nextInt(255) , random.nextInt(255));
                        visited =  new Color(random.nextInt(255) , random.nextInt(255) , random.nextInt(255));
                        bounds =  new Color(random.nextInt(255) , random.nextInt(255) , random.nextInt(255));
                        intialize();
                    }

                    public void intialize(){

                        thread =  new Thread(new Runnable() {
                            @Override
                            public void run() {

                                start = new Point(0,0);
                                visitedCoordinates.push(start);
                                grid[0 + 0 * gridWidth] = VISITED;
                                totalVisited++;
                                currentX = start.x;
                                currentY = start.y;


                                path.add(start);

                                while(totalVisited < grid.length){
                                    currentX = start.x;
                                    currentY = start.y;
                                    start = pickLocation(start.x,start.y);
                                    try {
                                        Thread.sleep(SIMULATION_SPEED);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.out.println("Terminated !");
                            }
                        });
                  }

                    private boolean inBounds(int x,int y){
                        return (x >= 0 && x < gridWidth && y >= 0 && y < gridHeight);
                    }

                    private Point pickLocation(int x,int y){

                         List<Integer> validLocations = new ArrayList<Integer>();
                          Random random = new Random();

                          if(inBounds(x,y - 1) && (grid[x + (y - 1) * gridWidth] != VISITED)){
                              validLocations.add(NORTH);
                          }

                          if(inBounds(x + 1,y) && (grid[(x + 1) + y * gridWidth] != VISITED)){
                               validLocations.add(EAST);
                          }

                          if(inBounds(x,y + 1) && (grid[x + (y + 1) * gridWidth] != VISITED)){
                               validLocations.add(SOUTH);
                          }

                          if(inBounds(x - 1,y) && (grid[(x - 1) + y * gridWidth] != VISITED)){
                               validLocations.add(WEST);
                          }

                         if(!validLocations.isEmpty()){

                             int location = validLocations.get(random.nextInt(validLocations.size()));
                             Point nextLocation;


                             switch(location){

                                 case NORTH:

                                     grid[x + (y  - 1) * gridWidth] = VISITED;
                                     totalVisited++;
                                     nextLocation = new Point(x , y - 1);
                                     visitedCoordinates.push(nextLocation);

                                     return nextLocation;

                                 case EAST:

                                     grid[(x + 1) + y * gridWidth] = VISITED;
                                     totalVisited++;
                                     nextLocation = new Point(x + 1, y);
                                     visitedCoordinates.push(nextLocation);

                                     return nextLocation;

                                 case SOUTH:

                                     grid[x + (y + 1) * gridWidth] = VISITED;
                                     totalVisited++;
                                     nextLocation = new Point(x , (y + 1));
                                     visitedCoordinates.push(nextLocation);

                                     return nextLocation;

                                 case WEST:

                                     grid[(x - 1) + y * gridWidth] = VISITED;
                                     totalVisited++;
                                     nextLocation = new Point((x - 1),y);
                                     visitedCoordinates.push(nextLocation);

                                     return nextLocation;
                             }
                         }
                                return visitedCoordinates.pop();
                    }


                    private void showMaze(Graphics g){
                        for(int y = 0; y< gridHeight; y++){
                            for(int x = 0; x< gridWidth; x++){
                                if(grid[x + y * gridWidth] == VISITED){
                                   g.setColor(visited);
                                }
                                else{
                                    if(x == 0 || y == 0 || x == gridWidth - 1 || y == gridHeight - 1)
                                        g.setColor(bounds);
                                    else
                                     g.setColor(Color.black);
                                }


                                g.fillRect(x   * nodeSize ,y  * nodeSize,nodeSize - xOff ,nodeSize - yOff);

                                g.setColor(current);

                                g.fillRect(currentX * nodeSize , currentY * nodeSize , nodeSize , nodeSize);

                            }
                        }
                    }



                    public void paint(Graphics g){
                        g.setColor(Color.black);
                        g.fillRect(0,0,getWidth(),getHeight());
                        showMaze(g);
                        repaint();
                    }

                    public void start(){
                        thread.start();
                    }

                    public static void main(String[] args){

                          DepthFirstSearchVisualizer maze = new DepthFirstSearchVisualizer();

                          maze.start();

                          JFrame window = new JFrame("Depth First Search");
                          window.setVisible(true);
                          window.setResizable(false);
                          window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                          window.setLocationRelativeTo(null);
                          window.add(maze);
                          window.pack();
                    }


                    private void showInConsole(){
                        for(int y = 0; y< gridHeight; y++){
                            for(int x = 0; x< gridWidth; x++){
                                if(grid[x + y * gridWidth] == VISITED){
                                    System.out.print("x");
                                }
                                else{
                                    System.out.print(".");
                                }
                            }
                            System.out.println();
                        }
                    }

}

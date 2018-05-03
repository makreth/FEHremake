package feremkae;

import java.util.*;

/**
 * This mess of a class is instantiated whenever a unit is created, and also
 * instantiated as a toolbox class to calculate various movement and attack
 * related mechanics.
*/

public class AStar {
    /**
     * Completely overkill, Cell is a private class that serves as a desperate work-around 
     * to give Tiles, which aren't normally ordered, an overridden compareTo method so it can
     * be used in Priority Queues.
     */
    private static class Cell implements Comparable <Cell>{
        private Tile wrappedTile;
        private int x;
        private int y;
        private double priority;
        
        public Cell(Tile t){
            wrappedTile = t;
            x = wrappedTile.getX();
            y = wrappedTile.getY();
        }
        
        public Tile getWrappedTile(){
            return wrappedTile;
        }
        
        @Override
        public int compareTo(Cell c){
            return Double.compare(priority, c.getPriority());
        }
        
        @Override
        public boolean equals(Object o){
            if(o == null){
                System.out.println("not found.");
                return false;
            }
            if(!Cell.class.isAssignableFrom(o.getClass())){
                System.out.println("unapplicable");
                return false;
            }
            final Cell c = (Cell) o;
            return c.getX() == getX()
                    && c.getY() == getY();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + this.x;
            hash = 53 * hash + this.y;
            return hash;
        }
        
        public int getX(){
            return x;
        }
        
        public int getY(){
            return y;
        }
        
        public double getPriority(){
            return priority;
        }
        
        public void setPriority(double newDouble){
            priority = newDouble;
        }
        
        public String toString(){
            return wrappedTile.toString();
        }
    }

    private Board board;
    private Cell start;
    private Cell goal;
    private HashMap<Cell, Cell> cameFrom;
    private HashMap<Cell, Double> costSoFar;
    private PriorityQueue frontier;
    private Unit selectedUnit;
    
    public AStar(Board b, Tile newStart, Tile newGoal, Unit newUnit){
        start = new Cell(newStart);
        goal = new Cell(newGoal);
        selectedUnit = newUnit;
        start.setPriority(0);
        frontier = new PriorityQueue();
        frontier.add(start);
        cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar = new HashMap<>();
        costSoFar.put(start, 0.0);
        board = b;
    }
    
    public AStar(Unit newUnit){
        selectedUnit = newUnit;
        start = new Cell(selectedUnit.getLocation());
        start.setPriority(0);
        frontier = new PriorityQueue();
        frontier.add(start);
        cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        costSoFar = new HashMap<>();
        costSoFar.put(start, 0.0);
    }
    
    /**
     * This is a safety method since I thought it was somehow okay to have the goal
     * variable bouncing around somewhere. It resets everything to a default state.
    */
    public void resetVariables(){
        start = new Cell(selectedUnit.getLocation());
        start.setPriority(0);
        frontier.clear();
        frontier.add(start);
        cameFrom.clear();
        cameFrom.put(start, null);
        costSoFar.clear();
        costSoFar.put(start, 0.0);
    }
    
    /**
     * This method desperately needs documentation, since it's really confusing.
     * The parameter 'limiter' makes this method function in three different ways.
     * If the limiter is zero, it functions as an unnecessarily convoluted
     * breadthSearch. If the limiter is negative, it incorporates units of a specific
     * color into the algorithm, but otherwise it's the same as when the limiter is positive.
     * If the limiter is positive, it functions as a normal Dijkstra algorithm, where 
     * limiter becomes movement range. I don't think I'll add the AStar part, because
     * I don't know if I'll need a heuristic in this game.
     * @param limiter 
     */
    public void initMovementMap(int limiter){
         while (!frontier.isEmpty()){
            Cell current = (Cell) frontier.poll();
            if(goal != null && current == goal){
                break;
            }
            ArrayList<Tile> proto = current.getWrappedTile().getNeighbors();
            ArrayList<Cell> neighbors = new ArrayList<>();
            for(Tile t : proto){
                Obj currObj = t.getObjectOnTile();
                if(!(currObj instanceof Obstacle)){
                    if(currObj instanceof Unit){
                        Unit currUnit = (Unit) currObj;
                        Unit movingUnit = (Unit) start.getWrappedTile().getObjectOnTile();
                        if(limiter > 0 && !currUnit.getColor().equals(movingUnit.getColor()))
                            continue;
                    }
                    Cell tileWrap = new Cell(t);
                    neighbors.add(tileWrap);
                }
            }
            for(Cell next : neighbors){
                costSoFar.get(current);
                double newCost = costSoFar.get(current) + current.getWrappedTile().getMovementCost();
                if(limiter != 0 && newCost > Math.abs(limiter)){
                    continue;
                }
                if(!costSoFar.keySet().contains(cameFrom.get(next))
                        || newCost < costSoFar.get(next)){
                    costSoFar.put(next, newCost);
                    next.setPriority(newCost + 0); //Djikstra is AStar with h = 0
                    frontier.add(next);
                    cameFrom.put(next, current);
                }
            }
        }
    }
    
    public HashMap<Tile, Tile> getMovementMap(){
        HashMap<Tile, Tile> movementMap = new HashMap<>();
        for(Cell c : cameFrom.keySet()){
            Tile currentTile = c.getWrappedTile();
            if(cameFrom.get(c) != null){
                Tile pointedTile = cameFrom.get(c).getWrappedTile();
                movementMap.put(currentTile, pointedTile);
            }
        }
        return movementMap;
    }
    
    public ArrayList <Tile> createPath(){
        if(!cameFrom.containsValue(goal) && !cameFrom.containsKey(goal)){
            return null;
        }
        Cell current = goal;
        //System.out.println("move area " + cameFrom);
        ArrayList<Tile> path = new ArrayList<>();
        while(!current.getWrappedTile().equals(start.getWrappedTile())){
            path.add(current.getWrappedTile());
            current = cameFrom.get(current);
        }
        //System.out.println(path);
        Collections.reverse(path);
        return path;
    }
    
    public void setGoal(Tile t){
        goal = new Cell(t);
    }
 
    private double heuristic(Cell a, Cell b){
        return Math.abs(a.getWrappedTile().getX() - b.getWrappedTile().getY())
                + Math.abs(a.getWrappedTile().getY() - b.getWrappedTile().getY());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import java.util.HashMap;

/**
 *
 * @author sheph
 */
public class CellsAndHashMap {
    private static class Cell{
        private final int x;
        private final int y;
        
        public Cell(int newX, int newY){
            x = newX;
            y = newY;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 83 * hash + this.x;
            hash = 83 * hash + this.y;
            System.out.println(hash);
            return hash;
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
        
        public int getX(){
            return x;
        }
        
        public int getY(){
            return y;
        }
        
        public String toString(){
            return x + ", " + y;
        }
    }
    
    private HashMap<Cell, Cell> linkedMap;
    
    public CellsAndHashMap(){
        linkedMap = new HashMap<>();
        Cell c1 = new Cell(0,0);
        Cell c2 = new Cell(1,0);
        Cell c3 = new Cell(2,0);
        Cell c4 = new Cell(3,0);
        linkedMap.put(c1, c2);
        linkedMap.put(c2, c3);
        linkedMap.put(c3, c4);
        System.out.println(linkedMap.get(c1));
    }
}

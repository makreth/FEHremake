/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import java.util.ArrayList;

public class Tile {
    private int x;
    private int y;
    private Obj objectOnTile;
    private Obj featureOnTile;
    private boolean selected = false;
    ArrayList<Tile> neighbors;
    private boolean hasPath = false;
    
    public Tile(int newX, int newY){
        x = newX;
        y = newY;
        neighbors = new ArrayList();
    }
    
    public ArrayList<Tile> getNeighbors(){
        return neighbors;
    }
    
    public void connect(Tile t){
        if(t == null){
            return;
        }
        t.checkAndAdd(this);
        checkAndAdd(t);
    }
    
    private void checkAndAdd(Tile t){
        if(t != null){
            neighbors.add(t);
        }
    }
    
    public void printCoordinates(){
        System.out.println(x + ", " + y + ".");
    }
    
    public boolean isEmpty(){
        return objectOnTile == null;
    }
    
    public int getMovementCost(){
        if(featureOnTile != null){
            featureOnTile.getMovementCost();
        }
        return 1;
    }
    
    public Obj getObjectOnTile(){
        return objectOnTile;
    }
    
    public void setObjectOnTile(Obj newObj){
        objectOnTile = newObj;
    }
    
    public Obj getFeatureOnTile(){
        return featureOnTile;
    }
    
    public void setFeatureOnTile(Obj newObj){
        featureOnTile = newObj;
    }
    
    public boolean isCurrentlySelected(){
        return selected;
    }
    
    public void setSelect(boolean b){
        selected = b;
    }
    
    public void setPath(boolean b){
        hasPath = b;
    }
    
    public boolean getPath(){
        return hasPath;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public String toString(){
        return "Tile" + " " + x + ", " + y;
    }
    
    public boolean equals(Object o){
            if(o == null){
                System.out.println("not found.");
                return false;
            }
            if(!Tile.class.isAssignableFrom(o.getClass())){
                System.out.println("unapplicable");
                return false;
            }
            final Tile t = (Tile) o;
            return t.getX() == getX()
                    && t.getY() == getY();
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 57 * hash + this.x;
            hash = 57 * hash + this.y;
            return hash;
        }
    
}

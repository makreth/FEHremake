/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.CharSprite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author sheph
 */
public class Unit extends Obj{
    protected Tile location;
    protected int speed;
    protected AStar movementHandler;
    protected String teamColor;
    protected int[] range;
    protected HashMap<String, Stat> statList;
    protected Boolean inactive = false;
    protected Board board;
    
    public Unit(Tile t){
        location = t;
        t.setObjectOnTile(this);
        movementHandler = new AStar(this);
        range = new int[]{1};
        initStats();
    }

    public void teleport(Tile destination){
        if(destination.getObjectOnTile() != null){
            return;
        }
        location.setObjectOnTile(null);
        location = destination;
        location.setObjectOnTile(this);
    }
    
    public Boolean move(Tile destination){
        Boolean success = canMove(destination);
        if(success){
            teleport(destination);
            inactive = true;
        }        
        return success;
    }
    
    public Boolean canMove(Tile destination){
        HashMap<Tile, Tile> hh = movementHandler.getMovementMap();
        return hh.containsValue(destination) || hh.containsKey(destination);
    }
    
    public Tile findTargetTileInNeighbors(Tile target){
        ArrayList<Tile> possibleTiles = new ArrayList<>();
        HashMap<Tile, Tile> movementMap = movementHandler.getMovementMap();
        for(Tile currTile : movementMap.keySet()){
            if(canAttack(currTile, target) && currTile.getObjectOnTile() == null){
                possibleTiles.add(currTile);
            }
        }
        int closest = range[0] + speed;
        Tile closeTile = null;
        for(Tile currTile : possibleTiles){
            int x = Math.abs(location.getX() - currTile.getX());
            int y = Math.abs(location.getY() - currTile.getY());
            if(x <= y){
                if(x < closest){
                    closest = x;
                    closeTile = currTile;
                }
            }else{
                if(y < closest){
                    closest = y;
                    closeTile = currTile;
                }
            }
        }
        return closeTile;
    }
    
    public boolean attack(Unit enemy, Tile destination){
        Boolean success = false;
        Tile tileOfAtk = null;
        if(destination!= null && canAttack(destination, enemy.getLocation())){
            move(destination);
            success = true;
            tileOfAtk = destination;
        }else{
            if(canAttack(location,enemy.getLocation())){
                move(location);
                success = true;
                tileOfAtk = location;
            }
            else{
                Tile selectedTile = findTargetTileInNeighbors(enemy.location);
                if(canMove(selectedTile)){
                    tileOfAtk = selectedTile;
                    move(selectedTile);
                    success = true;
                }
            }
        }
        if(success){
            inflictDamage(enemy);
        }
        enemy.counterAttack(this);

        inactive = true;
        return success;
    }
    
    public boolean counterAttack(Unit enemy){
        //code to check for counter attack
        if(location != null && canAttack(location, enemy.getLocation())){
            System.out.print(this + " counter-attacks: ");
            inflictDamage(enemy);
            return true;
        }
        return false;
    }
    
    public void inflictDamage(Unit enemy){
        enemy.getStats().get("HP").decreaseBy(statList.get("Atk").getVal());
        System.out.println(this + " hits " + enemy + " for " + statList.get("Atk").getVal() + " points of damage (" + enemy.getStats().get("HP").getVal() + "/" + enemy.getStats().get("HP").getMax() + ")");
        if(enemy.getStats().get("HP").getVal() == 0){
            enemy.destroy();
        }
    }
    
    public Boolean canAttack(Tile currLoc, Tile targetLoc){  
        for(int i = 0; i < range.length; i ++){
            if(Math.abs(currLoc.getX() - targetLoc.getX()) + Math.abs(currLoc.getY() - targetLoc.getY()) == range[i])
                return true;
            }
        return false;
    }
    
    public void initStats(){
        statList = new HashMap<String, Stat>();
        statList.put("HP", new Stat("HP", 10, 10));
        statList.put("Atk", new Stat("Atk", 5, 5));
    }
    
    public HashMap<String,Stat> getStats(){
        return statList;
    }
    
    public ArrayList<Tile> getPath(Tile goal){
        movementHandler.setGoal(goal);
        return movementHandler.createPath();
    }
    
    public HashMap<Tile,Tile> getMovementMap(){
        return movementHandler.getMovementMap();
    }
    
    public ArrayList<Tile> getAttackList(){
        ArrayList<Tile> result = new ArrayList<>();
        result.addAll(movementHandler.getMovementMap().keySet());
        ArrayList<Tile> copy = new ArrayList<>(result);
        for(Tile t : copy){
            if(t.getObjectOnTile() != null && t.getObjectOnTile() instanceof Unit){
                Unit uni = (Unit) t.getObjectOnTile();
                if(!uni.equals(this) && uni.getColor().equals(teamColor)){
                    result.remove(t);
                }
            }
        }
        for(int i = 0; i < range.length; i++)
            for(int j = 0; j < range[i]; j ++){
                result = expand(result, j);
            }
        HashMap<Tile, Tile> movementMap = getMovementMap();
        copy = new ArrayList<>(result);
        for(Tile currTile : copy){
            Boolean keep = false;
            for(Tile movementTile : movementMap.keySet()){
                if(canAttack(movementTile, currTile)){
                    keep = true;
                }
            }
            if(!keep){
                result.remove(currTile);
            }
        }
        return result;
        
    }
    
    protected ArrayList<Tile> expand(ArrayList<Tile> input, int x){
        ArrayList<Tile> output = new ArrayList<>();
        for(Tile currTile : input){
            ArrayList<Tile> neighborsOfCurrTile = currTile.getNeighbors();
            for(Tile nTile : neighborsOfCurrTile){
                if(!output.contains(nTile)){
                    output.add(nTile);
                }
            }
        }
        return output;
    }
    
    public void reinitMap(){
        movementHandler.resetVariables();
        movementHandler.initMovementMap(speed);
    }
    
    public Tile getLocation(){
        return location;
    }
    
    public int getSpeed(){
        return speed;
    }
    
    public Boolean isInactive(){
        return inactive;
    }
    
    public void setInactive(Boolean b){
        inactive = b;
    }
    
    public String getColor(){
        return teamColor;
    }
    
    public void setColor(String s){
        teamColor = s;
        if(sprite instanceof CharSprite){
            CharSprite cSprite = (CharSprite) sprite;
            cSprite.setColor(s);
        }
    }
    
    public void setRange(int[] x){
        range = x;
    }
    
    public String toString(){
        String result = "";
        result += getColor();
        result += super.toString();
        return result;
    }
    
    public void destroy(){
        location.setObjectOnTile(null);
        location = null;
        System.out.println(this + " was destroyed.");
    }
    
    public AStar getMovementHandler(){
        return movementHandler;
    }
    
    public void setBoard(Board b){
        board = b;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sheph
 */
public class AIController {
    String color;
    Team managedTeam;
    
    public AIController(String newColor, Team newTeam){
        color = newColor;
        managedTeam = newTeam;
    }
    
    public void startTurn(){
        for(Unit u : managedTeam.getRoster()){
            ArrayList<Tile> reachableUnits = new ArrayList<Tile>();
            for(int i = 0; i > - 8; i--){
                reachableUnits = scanForEnemy(u, i);
                if(reachableUnits.size() > 0){
                    break;
                }
            }
            if(reachableUnits.isEmpty()){
                System.out.println("failed to find units within range");
                return;
            }
            ArrayList<Unit> attackableUnits = new ArrayList<Unit>();
            for(Tile t : reachableUnits){
                Unit currUnit = (Unit) t.getObjectOnTile();
                if(u.canAttack(u.getLocation(), currUnit.getLocation())){
                    attackableUnits.add(currUnit);
                    return;
                }
            }
            Unit weakest = null;
            for(Unit target : attackableUnits){
                if(weakest == null || target.getStats().get("HP").getVal() < weakest.getStats().get("HP").getVal()){
                    weakest = target;
                }
            }
            if(weakest != null){
                u.attack(weakest, null);
                continue;
            }
            Unit target = (Unit) reachableUnits.get(0).getObjectOnTile();
            u.reinitMap();
            HashMap<Tile,Tile> possibleTiles = u.getMovementMap();
            Tile closeTile = null;
            AStar currScanner = new AStar(u);
            currScanner.setGoal(target.location);
            currScanner.initMovementMap(0);
            ArrayList<Tile> path = currScanner.createPath();
            for(int i = 0; i < path.size(); i++){
                if(possibleTiles.containsKey(path.get(i))){
                    closeTile = path.get(i);
                }
            }
            if(!u.attack(target, null)){
                u.move(closeTile);
            }
        }
        managedTeam.refreshUnits();
    }
    
    public ArrayList<Tile> scanForEnemy(Unit currUnit, int range){
        AStar currScanner = new AStar(currUnit);
        currScanner.initMovementMap(range);
        HashMap<Tile, Tile> breadthSearch = currScanner.getMovementMap();
        ArrayList<Tile> result = new ArrayList<>();
        for(Tile t: breadthSearch.keySet()){
            if(t.getObjectOnTile() != null  && t.getObjectOnTile() instanceof Unit){
                Unit scannedUnit = (Unit) t.getObjectOnTile();
                if(!scannedUnit.getColor().equals(currUnit.getColor())){
                    result.add(t);
                }
            }
        }
        return result;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.OverlayArrow;
import feremkae.rendering.OverlaySquare;
import feremkae.rendering.OverlaySymbol;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;
import javax.swing.SwingUtilities;

/**
 *This class is instantiated whenever a new Board class is created. Boards should only
 * be created when a new level is to be played. When a board is destroyed, all of the pieces
 * and the associated clickAdapter will go with it.
 * @author sheph
 */
public class ClickAdapter extends MouseAdapter {
    private final Board parentBoard;
    private Tile selected;
    private Tile loadedPathTile = null;
    private int cells;
    private Boolean playerControl;
    
    public ClickAdapter(Board newB){
        parentBoard = newB;
        cells = parentBoard.getCellSize();
        playerControl = true;
    }
    
    /**
     * Called whenever the player makes an interaction with the board. It checks
     * whether all units on a team have moved, passing control over to the opposing
     * team if true. It will call itself to activate AI. This method is counterintuitive
     * and confusing.
     */
    public void progressBoardState(){
        HashMap<Team, AIController> teams = parentBoard.getVyingFactions();
        HashMap<Team, AIController> copy = parentBoard.getVyingFactions();
        for(Team t : teams.keySet()){
            t.removeDead();
        }
        if(!playerControl){
            for(Team t : teams.keySet()){
                if(!t.getTeamColor().equals(parentBoard.getPlayerColor())){
                    System.out.println("AI's turn");
                    teams.get(t).startTurn();
                }
            }
            playerControl = true;
            for(Team t : teams.keySet()){
                if(t.getTeamColor().equals(parentBoard.getPlayerColor())){
                    t.refreshUnits();
                }
            }
            System.out.println("Player's turn");
            return;
        }else{
            for(Team t : teams.keySet()){
                if(t.getTeamColor().equals(parentBoard.getPlayerColor())){
                    if(t.checkAllMoved()){
                        playerControl = false;
                        System.out.println("Player's turn over.");
                        progressBoardState();
                        return;
                    }
                }
            }
        }
    }
    /**
     * Overridden mouseReleased method that handles selection, movement, and attack.
     * Also activates boardState checks, activating the AI when necessary.
     * @param e 
     */
    @Override
    public void mouseReleased(MouseEvent e){
        if(!playerControl){
            return;
        }
        Class c = new OverlaySquare(Color.BLUE).getClass();
        int rowR = (e.getX() - 10)/cells;
        int colC = (e.getY() - 10)/cells;
        if(rowR >= parentBoard.getBoardArray().length || colC >= parentBoard.getBoardArray()[rowR].length){
            return;
        }
        Tile clickedTile = parentBoard.getBoardArray()[rowR][colC];
        Obj clickedObj = clickedTile.getObjectOnTile();
        if(e.getButton() == e.BUTTON1){
            parentBoard.removeGraphics(c.getSuperclass());
            if(selected != null){
                if(selected.getObjectOnTile() instanceof Unit){
                    Unit selectedUnit = (Unit) selected.getObjectOnTile();
                    if(selectedUnit.getColor().equals(parentBoard.getPlayerColor())){
                        if(clickedObj != null && clickedObj instanceof Unit){
                            Unit clickedUnit = (Unit) clickedObj;
                            if(clickedUnit.equals(selectedUnit)){
                                clearSelect();
                            }
                            if(!clickedUnit.getColor().equals(parentBoard.getPlayerColor())){
                                if(selectedUnit.attack(clickedUnit, loadedPathTile)){
                                    progressBoardState();
                                }
                            }
                        }else{
                            if(selectedUnit.move(clickedTile)){
                                progressBoardState();
                            }
                        }  
                    }
                    clearSelect();
                }
            }else{
                selectUnit(rowR, colC);
            }
        }        
        if(e.getButton() == e.BUTTON2){
            parentBoard.removeGraphics(c.getSuperclass());
            if(clickedTile.getObjectOnTile() instanceof Obstacle){
                clickedTile.setObjectOnTile(null);
            }else{
            clickedTile.setObjectOnTile(new Obstacle());
            }
            clearSelect();
        }
        loadedPathTile = null;
        if(e.getButton() == e.BUTTON3){
            if(selected != null){
                if(selected.getObjectOnTile() instanceof Unit){
                    Unit selectedUnit = (Unit) selected.getObjectOnTile();
                    if(selectedUnit.getColor().equals(parentBoard.getPlayerColor())){
                        if(selectedUnit.canMove(clickedTile)){
                            loadedPathTile = clickedTile;
                        }
                    }
                }
            }
        }
        parentBoard.repaint();
        
    }
    /**
     * Helper method paired with the overridden mouseReleased method that generates
     * graphics to indicate movement and attack range of a Unit. When a unit is selected,
     * the player may hold down the right mouse button and drag to precisely dictate routes,
     * repeatedly calling the overridden mouseDragged method. The select variable is used for
     * most player interaction.
     * @param row
     * @param column 
     */
    private void selectUnit(int row, int column){
        clearSelect();
        Tile clickedTile = parentBoard.getBoardArray()[row][column];
        Obj clickedObj = clickedTile.getObjectOnTile();
        if(checkIfUnit(clickedObj)){
            selected = clickedTile;
            selected.setSelect(true);
            Unit currUnit = getSelectedUnit();
            if(currUnit.isInactive()){
                clearSelect();
                return;
            }
            currUnit.reinitMap();
            ArrayList spriteList = parentBoard.getDrawingMap().get(clickedTile);
            spriteList.add(new OverlaySymbol("_"));
            HashMap<Tile, Tile> genMap = currUnit.getMovementMap();
            for(Tile t: genMap.keySet()){
                spriteList = parentBoard.getDrawingMap().get(t);
                spriteList.add(new OverlaySquare(Color.BLUE));
            }
            
            for(Tile t: currUnit.getAttackList()){
                spriteList = parentBoard.getDrawingMap().get(t);
                if(spriteList.contains(new OverlaySquare(Color.BLUE)) || spriteList.contains(new OverlaySquare(Color.RED))){
                    continue;
                }else{
                    spriteList.add(new OverlaySquare(Color.RED));
                }
            }
        }
    }
    /**
     * Stub method that is called whenever the selection variable needs to be cleared.
     */
    private void clearSelect(){
        if(selected != null){
            selected.setSelect(false);
            selected = null;
        }
    }
    
    private boolean checkIfUnit(Obj selectedObj){
        return selectedObj != null && selectedObj instanceof Unit;
    }
    
    private Unit getSelectedUnit(){
        Unit selectedUnit = (Unit) selected.getObjectOnTile();
        return selectedUnit;
    }
    
    /**
     * Overridden mouseDragged method of MouseAdapter class that generates an overlay for viewing
     * by the player. The overlay displays movement range and attack range. At the moment, an 
     * arrow is generated by holding down the right-mouse button to manually dictate routes.
     * @param e 
     * @see feremkae.rendering.Overlay
     */
    
    @Override
    public void mouseDragged(MouseEvent e){
        if(!SwingUtilities.isRightMouseButton(e)){
            return;
        }
        parentBoard.removeGraphics(new OverlayArrow(0).getClass());
        int rowR = (e.getX() - 10)/cells;
        int colC = (e.getY() - 10)/cells;
        if(rowR >= parentBoard.getBoardArray().length || colC >= parentBoard.getBoardArray()[rowR].length){
            return;
        }
        Tile draggedTile = parentBoard.getBoardArray()[rowR][colC];
        if(selected != null && selected.getObjectOnTile() instanceof  Unit){
            Unit selectedUnit = (Unit) selected.getObjectOnTile();
            if(selectedUnit.getColor().equals(parentBoard.getPlayerColor())){
                ArrayList<Tile> path = selectedUnit.getPath(draggedTile);
                if(path == null){
                    return;
                }else{
                    HashMap<Tile, Integer> coordinateMap = new HashMap<>();
                    coordinateMap.put(new Tile(0,-1), 1);
                    coordinateMap.put(new Tile(1,0), 2);
                    coordinateMap.put(new Tile(0,1), 3);
                    coordinateMap.put(new Tile(-1,0), 4);
                    for(int i = 0; i < path.size(); i++){
                        ArrayList spriteList = parentBoard.getDrawingMap().get(path.get(i));
                        Tile currTile = path.get(i);
                        Tile adjTile = null;
                        if(i + 1 < path.size()){
                            spriteList.add(new OverlayArrow(0));
                            adjTile = path.get(i+1);
                            spriteList.add(new OverlayArrow(coordinateMap.get(findDirection(currTile, adjTile))));
                            if(i - 1 >= 0){
                                adjTile = path.get(i - 1);
                                spriteList.add(new OverlayArrow(coordinateMap.get(findDirection(currTile, adjTile))));
                            }else{
                                adjTile = selectedUnit.getLocation();
                                spriteList.add(new OverlayArrow(coordinateMap.get(findDirection(currTile, adjTile))));
                            }
                        }else if(i + 1 == path.size()){
                            if(i == 0){
                                adjTile = selectedUnit.getLocation();
                            }else{
                                adjTile = path.get(i-1);
                            }
                            spriteList.add(new OverlayArrow(coordinateMap.get(findDirection(currTile, adjTile)) + 4));
                        }
                    }
                }
            }
        }
        parentBoard.repaint();
        
    }
    /**
     * Helper method specifically paired with overridden MouseDragged method that
     * returns a local variable Tile to be matched with an integer in a local HashMap.
     * That HashMap contains values paired with directional sprites.
     * @param current
     * @param adjacent
     * @return 
     */
    private Tile findDirection(Tile current, Tile adjacent){
        return new Tile(adjacent.getX() - current.getX(), adjacent.getY() - current.getY());
    }
    
    public Boolean getPlayerControl(){
        return playerControl;
    }
    
    public void setPlayerControl(Boolean b){
        playerControl = b;
    }
}
       

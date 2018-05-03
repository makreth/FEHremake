/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;
import feremkae.rendering.Sprite;
import feremkae.rendering.TileSquare;
import feremkae.rendering.CharSprite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import java.awt.Color;



public class Board extends JPanel {
    private int cellSize;
    private int boardSize;
    private Tile[][] boardArray;
    private HashMap<Tile, ArrayList<Sprite>> drawingMap;
    private static final String PLAYER_COLOR = "blue";
    private HashMap<Team, AIController> vyingFactions;
    
    public Board(int newCellSize, int newBoardSize){
        drawingMap = new HashMap<>();
        cellSize = newCellSize;
        boardSize = newBoardSize;
        //ClickAdapter th = new ClickAdapter(this);
        MouseAdapter ma = new ClickAdapter(this);
        ClickAdapter ca = (ClickAdapter) ma;
        addMouseListener(ma);
        addMouseMotionListener(ma);
        boardArray = new Tile[boardSize][boardSize];
        constructBoard();
        vyingFactions = new HashMap<>();
        create2DCathedral();
        createPlayerTeam();
        createTeam("red");
        for(Team t : vyingFactions.keySet()){
            for(Unit u : t.getRoster()){
                u.setBoard(this);
            }
        }
        ca.progressBoardState();
    }
    
    private void create2DCathedral(){
        for(int i = 0; i < 12; i++){
            boardArray[0][i].setObjectOnTile(new Obstacle());
            boardArray[i][0].setObjectOnTile(new Obstacle());
            boardArray[12-1][i].setObjectOnTile(new Obstacle());
            if(i != 5 && i != 6){
                boardArray[i][12-1].setObjectOnTile(new Obstacle());
            }
        }
        for(int i = 0; i < 12; i++){
            if(i != 5 && i != 6){
                boardArray[i][5].setObjectOnTile(new Obstacle());
            }
        }
        boardArray[4][12].setObjectOnTile(new Obstacle());
        boardArray[4][13].setObjectOnTile(new Obstacle());
        boardArray[7][12].setObjectOnTile(new Obstacle());
        boardArray[7][13].setObjectOnTile(new Obstacle());
    }
    
    private Team createPlayerTeam(){
        ArrayList<Unit> playerTeam = new ArrayList<>();
        playerTeam.add(new Archer(boardArray[4][7]));
        playerTeam.add(new Archer(boardArray[8][7]));
        playerTeam.add(new Fighter(boardArray[7][10]));
        Team player = new Team(playerTeam, PLAYER_COLOR);
        vyingFactions.put(player, null);
        return player;
    }
    
    private Team createTeam(String color){
        ArrayList<Unit> teamList = new ArrayList<>();
        teamList.add(new Archer(boardArray[16][16]));
        teamList.add(new Fighter(boardArray[15][13]));
        Team newTeam = new Team(teamList, color);
        vyingFactions.put(newTeam, new AIController("red", newTeam));
        return newTeam;
    }
    
    private void createUnit(int x, int y, String color){
        Archer uni = new Archer(boardArray[x][y]);
        uni.setColor(color);
    }
    
    private void constructBoard(){
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                Tile tileBehind = null;
                Tile tileAbove = null;
                Tile currTile = new Tile(x, y);
                boardArray[x][y] = currTile;
                ArrayList<Sprite> spriteList = new ArrayList<>();
                spriteList.add(new TileSquare());
                drawingMap.put(currTile, spriteList);
                if(x - 1 >= 0){
                    tileBehind = boardArray[x-1][y];
                    currTile.connect(tileBehind);
                }
                if(y - 1 >= 0){
                    tileAbove = boardArray[x][y-1];
                    currTile.connect(tileAbove);
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        System.out.println("yes.");
        for(int y = 0; y < boardSize; y++){
            for(int x = 0; x < boardSize; x++){
                Tile currTile = boardArray[x][y];
                if(!currTile.isEmpty()){
                    currTile.getObjectOnTile().getSprite().drawSprite(g, x*cellSize+10, y*cellSize+5, cellSize);
                    if(currTile.getObjectOnTile() instanceof Unit){
                        Unit uni = (Unit) currTile.getObjectOnTile();
                        if(uni.isInactive()){
                            CharSprite cs = new CharSprite("X");
                            cs.drawSprite(g, x * cellSize, y * cellSize + 5, cellSize);
                        }
                    }
                }
                ArrayList <Sprite> spriteList = drawingMap.get(currTile);
                for(Sprite s: spriteList){
                    s.drawSprite(g, x*cellSize+10, y*cellSize+5, cellSize);
                }
            }
        }
        /*for(Tile t : drawingMap.keySet()){
            ArrayList<Sprite> spriteList = drawingMap.get(t);
            ArrayList<Sprite> iterList = new ArrayList<>(spriteList);
            for(Sprite s : iterList){
                if(!(s instanceof TileSquare)){
                    spriteList.remove(s);
                }
            }
        }*/
    }
    
    public void removeGraphics(Class toRemove){
        for(Tile t: drawingMap.keySet()){
            ArrayList<Sprite> spriteList = drawingMap.get(t);
            ArrayList<Sprite> copyList = new ArrayList<>(spriteList);
            for(Sprite s : copyList){
                if(toRemove.isInstance(s)){
                    spriteList.remove(s);
                }
            }
        }
    }
    
    public void testDraw(ArrayList<Tile> p){
        for(Tile t : p){
            t.setPath(true);
        }
    }
    
    public int getCellSize(){
        return cellSize;
    }
    
    public Tile[][] getBoardArray(){
        return boardArray;
    }
    
    public HashMap<Tile, ArrayList<Sprite>>getDrawingMap(){
        return drawingMap;
    }
    
    public String getPlayerColor(){
        return PLAYER_COLOR;
    }

    HashMap<Team, AIController> getVyingFactions() {
        return vyingFactions;
    }
}

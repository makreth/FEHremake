/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

/**
 *
 * @author sheph
 */
public class CharSprite extends Sprite{
    protected String symbol;
    protected Color color;
    
    public CharSprite(String newSymbol){
        symbol = newSymbol;
    }
    
    public CharSprite(String newSymbol, String newColor){
        symbol = newSymbol;
        setColor(newColor);
    }
    
    @Override
    public void drawSprite(Graphics g, int x, int y, int cellSize){
        Graphics2D g2d = (Graphics2D) g.create();
        if(color != null)
            g2d.setColor(color);
        g2d.drawString(symbol,(x+cellSize/2) - 4, (y+(cellSize- cellSize/2)) + 4);
        g2d.dispose();
    }
    
    public void setColor(String newColor){
        if(newColor.equals("blue")){
            color = Color.BLUE;
        }
        if(newColor.equals("red")){
            color = Color.RED;
        }
    }
    
    public String getSymbol(){
        return symbol;
    }
    
}

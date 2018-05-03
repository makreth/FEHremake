/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author sheph
 */
public class OverlaySymbol extends Overlay{
    
    String symbol;
    
    public OverlaySymbol(String x){
        symbol = x;
    }
    
    @Override
    public void drawSprite(Graphics g, int x, int y, int cellSize){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.drawString(symbol,(x+cellSize/2) - 4, (y+(cellSize- cellSize/2)) + 4);
        g2d.dispose();
    }
}

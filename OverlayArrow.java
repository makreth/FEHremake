/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 *
 * @author sheph
 */
public class OverlayArrow extends Overlay{
    int part;
    
    public OverlayArrow(int s){
        part = s;
    }
    
    @Override
    public void drawSprite(Graphics g, int x, int y, int cellSize){
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.cyan);
        Shape toDraw = null;
        int dX = (cellSize/3);
        int dY = (cellSize/3);
        int dSize = cellSize/3 + 1;
        switch(part){
            case 0: toDraw = new Rectangle(x + dX, y + dY, dSize, dSize);
                    break;
            case 1: toDraw = new Rectangle(x + dX, y, dSize, dSize);
                    break;
            case 2: toDraw = new Rectangle(x + 2*dX, y + dY, dSize, dSize);
                    break;
            case 3: toDraw = new Rectangle(x + dX, y + 2*dY, dSize, dSize);
                    break;
            case 4: toDraw = new Rectangle(x, y + dY, dSize, dSize);
                    break;
            case 5: toDraw = new Polygon(new int[]{x, x + cellSize, x + cellSize/2}, new int[]{y, y, y + cellSize/2}, 3);
                    break;
            case 6: toDraw = new Polygon(new int[]{x + cellSize, x + cellSize/2, x+cellSize}, new int[]{y, y + cellSize/2, y + cellSize}, 3);
                    break;
            case 7: toDraw = new Polygon(new int[]{x + cellSize/2, x, x + cellSize}, new int[]{y + cellSize/2, y + cellSize, y + cellSize}, 3);
                    break;
            case 8: toDraw = new Polygon(new int[]{x, x + cellSize/2, x}, new int[]{y, y + cellSize/2, y + cellSize}, 3);
                    break;
        }
        g2d.fill(toDraw);
        g2d.dispose();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author sheph
 */
public class TileSquare extends Sprite{
    
    @Override
    public void drawSprite(Graphics g, int x, int y, int cellSize){
        Graphics2D g2d = (Graphics2D) g;
        Rectangle r = new Rectangle(x, y, cellSize, cellSize);
        g2d.draw(r);
        
    }
}

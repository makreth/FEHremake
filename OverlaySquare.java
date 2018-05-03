/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Objects;

/**
 *
 * @author sheph
 */
public class OverlaySquare extends Overlay{
    Color color;
    
    public OverlaySquare(Color c){
        color = c;
    }
    
    @Override
    public void drawSprite(Graphics g, int x, int y, int cellSize){
        Graphics2D g2d = (Graphics2D) g.create();
        float opacity = 0.5f;
        int type = AlphaComposite.SRC_OVER;
        AlphaComposite ac = AlphaComposite.getInstance(type, opacity);
        g2d.setComposite(ac);
        g2d.setColor(color);
        Rectangle r = new Rectangle(x, y, cellSize, cellSize);
        g2d.fill(r);
        g2d.dispose();
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null){
                return false;
            }
            if(!OverlaySquare.class.isAssignableFrom(o.getClass())){
                return false;
            }
            final OverlaySquare c = (OverlaySquare) o;
            return color.equals(c.getColor());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.color);
        return hash;
    }
    
    public Color getColor(){
        return color;
    }
}

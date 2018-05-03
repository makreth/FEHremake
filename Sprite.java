/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae.rendering;

import java.awt.Graphics;

public abstract class Sprite {
    public abstract void drawSprite(Graphics g, int x, int y, int cellSize);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.Sprite;
import feremkae.rendering.SolidBlock;

/**
 *
 * @author sheph
 */
public class Obstacle extends Obj{
    
    public Obstacle(){
        sprite = (Sprite) new SolidBlock();
    }
    
    @Override
    public String toString(){
        return "Obstacle";
    }

}

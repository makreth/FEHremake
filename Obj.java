/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.Sprite;

/**
 *
 * @author sheph
 */
public abstract class Obj {
    protected Sprite sprite;
    
    public Sprite getSprite(){
        return sprite;
    }
    
    @Override
    public String toString(){
        return getClass().getName().substring(9);
    }
    
    public int getMovementCost(){
        return 1;
    }

}

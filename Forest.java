/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.CharSprite;

/**
 *
 * @author sheph
 */
public class Forest extends Obj{
    public Forest(){
        sprite = new CharSprite("1:1");
    }
    
    @Override
    public int getMovementCost(){
        return 2;
    }
}

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
public class Fighter extends Unit{
    
    public Fighter(Tile t){
        super(t);
        sprite = new CharSprite("W");
        speed = 4;
    }
    
}

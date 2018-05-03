/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import feremkae.rendering.CharSprite;
import java.util.ArrayList;

/**
 *
 * @author sheph
 */
public class Archer extends Unit{
    
    public Archer(Tile t){
        super(t);
        sprite = new CharSprite("A");
        speed = 3;
        range = new int[]{2};
        t.setObjectOnTile(this);
        movementHandler = new AStar(this);
    }
}

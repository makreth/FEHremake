/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;


public class Stat {
    private int val;
    private int max;
    private String name;
    
    public Stat(String s, int x, int y){
        val = x;
        max = y;
        name = s;
    }
    
    public Stat(String s, int x){
        val = x;
        max = -1;
        name = s;
    }
    
    public int getVal(){
        return val;
    }
    
    public void setVal(int x){
        val = x;
    }
    
    public int getMax(){
        return max;
    }
    
    public void setMax(int x){
        max = x;
    }
    
    public void increaseBy(int x){
        if(val + x <= max){
            val+=x;
        }
        else{
            val = max;
        }
    }
    
    public void decreaseBy(int x){
        if(val - x >= 0){
            val-=x;
        }
        else{
            val = 0;
        }
    }
    
    public String getName(){
        return name;
    }
}

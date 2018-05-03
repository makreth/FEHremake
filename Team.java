/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package feremkae;

import java.util.ArrayList;

/**
 *
 * @author sheph
 */
public class Team {
    private ArrayList<Unit> roster;
    private String teamColor;
    
    public Team(String color){
        roster = new ArrayList<>();
        teamColor = color;
    }
    
    public Team(ArrayList <Unit> newList, String color){
        roster = newList;
        teamColor = color;
        for(Unit u: roster){
            u.setColor(teamColor);
        }
    }
    
    public ArrayList<Unit> getRoster(){
        return roster;
    }
    
    public Boolean checkAllMoved(){
        Boolean result = true;
        for(Unit u : roster){
            if(!u.isInactive()){
                return !result;
            }
        }
        return result;
    }
    
    public void refreshUnits(){
        for(Unit u : roster){
            u.setInactive(false);
        }
    }
    
    public void removeDead(){
        ArrayList<Unit> copy = new ArrayList<>(roster);
        for(Unit u: copy){
            if(u.getStats().get("HP").getVal() == 0){
                roster.remove(u);
            }
        }
    }
    
    public String getTeamColor(){
        return teamColor;
    }
    
    
}

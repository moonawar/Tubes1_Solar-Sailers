package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


public class FireSupernova extends BotState{
     /* ABSTRACT METHOD */
     public float calculatePriorityScore() {
        // System.out.println("fire supernova = " + BotState.supernovaFired);
        if (bot.getSupernovaCount()==1){
            return 1000;
        }
        return 0;
    }
    
    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction = fireSupernova();
        return playerAction;
    }

    /* HELPER METHODS */
    private int getTargetHeading(){
        List <GameObject> enemy = gameState.getPlayerGameObjects()
            .stream().filter(y-> y.getId() != bot.getId()).sorted(Comparator.comparing(x-> x.getSize()))
            .collect(Collectors.toList());
        GameObject biggestEnemy = enemy.get(enemy.size()-1);
        System.out.println(biggestEnemy.getSize());
        return getHeadingBetween(biggestEnemy);
    }

    /* FIRE SUPERNOVA */
    private PlayerAction fireSupernova(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FIRESUPERNOVA;
        playerAction.heading = getTargetHeading();
        BotState.supernovaFired = true;
        return playerAction;
    }
}

package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


public class FireSupernova extends BotState{
     /* ABSTRACT METHOD */
     public float calculatePriorityScore() {
        // Implementasi perhitungan skor prioritas untuk state FireSupernova
        if (bot.getSupernovaCount()==1){
            return 1000;
        }
        return 0;
    }
    
    public PlayerAction calculatePlayerAction(){
        //Implementasi pemilihan aksi bot untuk state FireSupernova
        PlayerAction playerAction = new PlayerAction();
        playerAction = fireSupernova();
        return playerAction;
    }

    /* HELPER METHODS */
    private int getTargetHeading(){
        //Mengembalikan heading pada musuh yang paling besar
        List <GameObject> enemy = gameState.getPlayerGameObjects()
            .stream().filter(y-> y.getId() != bot.getId()).sorted(Comparator.comparing(x-> x.getSize()))
            .collect(Collectors.toList());
        GameObject biggestEnemy = enemy.get(enemy.size()-1);
        System.out.println(biggestEnemy.getSize());
        return getHeadingBetween(biggestEnemy);
    }

    /* FIRE SUPERNOVA */
    private PlayerAction fireSupernova(){
        //Mengembalikan aksi FIRESUPERNOVA dengan heading mengarah ke musuh yang memiliki ukuran paling besar
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FIRESUPERNOVA;
        playerAction.heading = getTargetHeading();
        BotState.supernovaFired = true;
        return playerAction;
    }
}

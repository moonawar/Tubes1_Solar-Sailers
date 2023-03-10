package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class FireTeleport extends BotState{
    private final int LARGE_NUM = 1000000;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore(){
        if (BotState.teleporterFired == true || bot.getTeleporterCount() == 0 || BotState.teleporterAngle != -1) {
            // initial checking for teleporter
            return 0;
        } else {
            return sizeSaveToFire();
        }
    }

    public PlayerAction calculatePlayerAction(){
        List<GameObject> enemySmallList = enemySmall();
        if(enemySmallList.size() != 0){
            // get the smallest enemy to be targeted
            GameObject target = enemySmallest(enemySmallList);
            return fireTeleport(target);
        } else {
            PlayerAction playerAction = new PlayerAction();
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = bot.getCurrHeading();
            return playerAction;
        }   
    }

    /* HELPER METHOD */
    private List<GameObject> enemySmall(){
        int radius = gameState.getWorld().getRadius() - 5;
        List<GameObject> enemySmallList = getPlayersAtBotArea(radius)
            .stream().filter(x -> x.getId() != bot.getId() && x.getSize() <= bot.getSize() - 20)
            .collect(Collectors.toList()); 
        return enemySmallList;
    }

    private GameObject enemySmallest(List<GameObject> enemySmallList){
        int minsize = LARGE_NUM, size;
        int smallestidx = 0;
        for(int i = 0; i < enemySmallList.size(); i++){
            size = enemySmallList.get(i).getSize();
            if(size < minsize){
                minsize = size;
                smallestidx = i;
            }
        }
        return enemySmallList.get(smallestidx);
    }

    private int calcHeadingOffset(GameObject target){
        int heading = getHeadingBetween(target);
        int offset = 0;
        if(heading >= 0 && heading < 90){
            int theading = target.getCurrHeading();
            if(theading >= 45 && theading < 225) offset = 2;
            else offset = -2;
        }
        else if(heading >= 90 && heading < 180){
            int theading = target.getCurrHeading();
            if(theading >= 135 && theading < 315) offset = 2;
            else offset = -2;
        }
        else if(heading >= 180 && heading < 270){
            int theading = target.getCurrHeading();
            if(theading >= 225 && theading < 45) offset = 2;
            else offset = -2;
        }
        else if(heading >= 270 && heading < 360){
            int theading = target.getCurrHeading();
            if(theading >= 315 && theading <= 135) offset = 2;
            else offset = -2;
        }
        return heading + offset;
    }

    private float sizeSaveToFire(){
        int botSize = bot.getSize();
        List<GameObject> enemySmallList = enemySmall();

        // check if there is any smaller enemy in the first place
        if(enemySmallList.size() == 0){
            return 0;
        } else {
            GameObject smallestEnemy = enemySmallest(enemySmallList);
            int smallestEnemySize = smallestEnemy.getSize();

            return (botSize/smallestEnemySize) * 250 * (float) (250/getDistanceToBot(smallestEnemy));
        }
    }

    private PlayerAction fireTeleport(GameObject target){
        PlayerAction playerAction = new PlayerAction();
        
        // calc the heading offset
        int shootDirection = calcHeadingOffset(target);

        // set the player action
        playerAction.action = PlayerActions.FIRETELEPORT;
        playerAction.heading = shootDirection;

        // set the bot state
        BotState.teleporterAngle = shootDirection;
        BotState.teleporterFired = true;

        return playerAction;
    }
}

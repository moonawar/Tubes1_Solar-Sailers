package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.Collectors;

public class TorpedoAttack extends BotState{
    private final int VERY_CLOSE_DISTANCE = 75;
    private final int CLOSE_DISTANCE = 100;
    private final int LARGE_NUM = 1000000;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        int prio;
        if (bot.getTorpedoSalvoCount() == 0 || sizeSaveToAttack() == false) {
            // initial checking for torpedo
            return 0;
        } 
        else{
            List<GameObject> enemyInRangeListClose = enemyInRange(CLOSE_DISTANCE + bot.getSize());
            if(enemyInRangeListClose.size() == 0) return 0; // doesn't have enemy close enough, don't call calculatePlayerAction                else{
            List<GameObject> enemyInRangeListVeryClose = enemyInRange(VERY_CLOSE_DISTANCE + bot.getSize());
            if(enemyInRangeListVeryClose.size() == 0){
                prio = (200 - getObjClosestEnemy(enemyInRangeListClose).getSize());
                return prio;
            }
            else{
                prio = (300 - getObjClosestEnemy(enemyInRangeListVeryClose).getSize());
                return prio;
            }
        }
    }

    public PlayerAction calculatePlayerAction(){
        // bot.getTorpedoSalvoCount() != 0
        List<GameObject> enemyInRangeList = enemyInRange(CLOSE_DISTANCE + bot.getSize());
        if(enemyInRangeList.size() != 0){
            GameObject enemy = getObjClosestEnemy(enemyInRangeList);
            return attackTorpedo(enemy);
        }
        else{
            PlayerAction playerAction = new PlayerAction();
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = 0;
            return playerAction;
        }
    }

    /* HELPER METHOD */
    private List<GameObject> enemyInRange(int radius){
        List<GameObject> enemyInRangeList = getPlayersAtBotArea(radius)
        .stream().filter(x -> (getDistance(x.getPosition(), gameState.getWorld().getCenterPoint()) <= gameState.getWorld().getRadius()))
        .collect(Collectors.toList());
        return enemyInRangeList;
    }

    private GameObject getObjClosestEnemy(List<GameObject> enemyInRangeList){
        // return : closest enemy from the list
        double mindist = LARGE_NUM, dist;
        int closestidx = 0;
        for(int i = 0; i < enemyInRangeList.size(); i++){
            dist = getDistanceToBot(enemyInRangeList.get(i));
            if(dist < mindist){
                mindist = dist;
                closestidx = i;
            }
        }
        return enemyInRangeList.get(closestidx);
    }

    private boolean sizeSaveToAttack(){
        return (bot.getSize() > 15);
    }

    private PlayerAction attackTorpedo(GameObject enemy){
        // return : torpedo attack action
        PlayerAction playerAction = new PlayerAction();

        // set action
        playerAction.action = PlayerActions.FIRETORPEDOES;
        playerAction.heading = getHeadingBetween(enemy);
        return playerAction;
    }
}
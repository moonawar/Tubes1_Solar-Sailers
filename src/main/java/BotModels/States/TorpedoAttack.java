package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;

public class TorpedoAttack extends BotState{
    private final int VERY_CLOSE_DISTANCE = 10;
    private final int CLOSE_DISTANCE = 70;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        if(bot.getTorpedoSalvoCount() == 0) return 0; // doesn't have torpedo salvo, don't call calculatePlayerAction
        else{
            if(sizeSaveToAttack() == 0) return 0; // size not safe, don't call calculatePlayerAction
            else{
                List<GameObject> enemyInRangeListClose = enemyInRange(CLOSE_DISTANCE);
                if(enemyInRangeListClose.size() == 0) return 0; // doesn't have enemy close enough, don't call calculatePlayerAction
                else{
                    List<GameObject> enemyInRangeListVeryClose = enemyInRange(VERY_CLOSE_DISTANCE);
                    if(enemyInRangeListVeryClose.size() == 0){
                        double dist = getDistClosestEnemy(getObjClosestEnemy(enemyInRangeListClose));
                        float prio = ((float) dist/CLOSE_DISTANCE * 80/100) + (sizeSaveToAttack() * 20/100);
                        return prio;
                    }
                    else{
                        double dist = getDistClosestEnemy(getObjClosestEnemy(enemyInRangeListVeryClose));
                        float prio = ((float) dist/VERY_CLOSE_DISTANCE * 80/100) + (sizeSaveToAttack() * 20/100);
                        return prio;
                    }
                }
            }
        }
    }

    public PlayerAction calculatePlayerAction(){
        // enemyInRangeList.size != 0, bot.getTorpedoSalvoCount() != 0
        GameObject enemy = getObjClosestEnemy(enemyInRange(CLOSE_DISTANCE));
        return attackTorpedo(enemy);
    }

    /* HELPER METHOD */
    private List<GameObject> enemyInRange(int radius){
        List<GameObject> enemyInRangeList = getGameObjectsByType(getGameObjectsAtBotArea(radius), ObjectTypes.PLAYER);
        return enemyInRangeList;
    }
    private GameObject getObjClosestEnemy(List<GameObject> enemyInRangeList){
        // enemyInRangeList.size != 0;
        double mindist = 100, dist;
        int closestidx = -1;
        for(int i = 0; i < enemyInRangeList.size(); i++){
            dist = getDistanceToBot(enemyInRangeList.get(i));
            if(dist < mindist){
                mindist = dist;
                closestidx = i;
            }
        }
        return enemyInRangeList.get(closestidx);
    }
    private double getDistClosestEnemy(GameObject closestEnemy){
        return getDistanceToBot(closestEnemy);
    }

    private int sizeSaveToAttack(){
        int botSize = bot.getSize();
        if(botSize < 15) return 0;
        if(botSize < 30) return 25;
        if(botSize < 45) return 50;
        else return 100;
    }

    private PlayerAction attackTorpedo(GameObject enemy){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FIRE_TORPEDOES;
        playerAction.heading = getHeadingBetween(enemy);
        return playerAction;
    }
}
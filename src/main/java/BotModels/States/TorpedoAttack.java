package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;

public class TorpedoAttack extends BotState{
    private final int VERY_CLOSE_DISTANCE = 50;
    private final int CLOSE_DISTANCE = 75;
    private final int LARGE_NUM = 1000000;

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
        // bot.getTorpedoSalvoCount() != 0
        List<GameObject> enemyInRangeList = enemyInRange(CLOSE_DISTANCE);
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
        List<GameObject> enemyInRangeList = getPlayersAtBotArea(radius);
        return enemyInRangeList;
    }
    private GameObject getObjClosestEnemy(List<GameObject> enemyInRangeList){
        // enemyInRangeList.size != 0;
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
        playerAction.action = PlayerActions.FIRETORPEDOES;
        playerAction.heading = getHeadingBetween(enemy) - 5;
        return playerAction;
    }
}

package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;

public class TorpedoAttack extends BotState{
    private final int VERY_CLOSE_DISTANCE = 50;
    private final int CLOSE_DISTANCE = 150;
    private final int LARGE_NUM = 1000000;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        if (bot.getTorpedoSalvoCount() == 0 || sizeSaveToAttack() == 0) {
            // initial checking for torpedo
            return 0;
        } else {
            List<GameObject> enemyInRangeListClose = enemyInRange(CLOSE_DISTANCE + bot.getSize());
            if (enemyInRangeListClose.size() == 0) {
                // no enemy in range
                return 0;
            } else {
                List<GameObject> enemyInRangeListVeryClose = enemyInRange(VERY_CLOSE_DISTANCE + bot.getSize());
                if (enemyInRangeListVeryClose.size() == 0) {
                    GameObject closestEnemy = getObjClosestEnemy(enemyInRangeListClose);
                    double dist = getDistClosestEnemy(closestEnemy);
                    float prio = ((float) (CLOSE_DISTANCE/(dist - closestEnemy.getSize())) * sizeSaveToAttack() * 200);
                    return prio;
                } else {
                    GameObject closestEnemy = getObjClosestEnemy(enemyInRangeListVeryClose);
                    double dist = getDistClosestEnemy(getObjClosestEnemy(enemyInRangeListVeryClose));
                    float prio = ((float) (VERY_CLOSE_DISTANCE/(dist - closestEnemy.getSize())) * sizeSaveToAttack() * 200);
                    return prio;
                }
            }
        }
    }

    public PlayerAction calculatePlayerAction(){
        // bot.getTorpedoSalvoCount() != 0
        List<GameObject> enemyInRangeList = enemyInRange(bot.getSize() + CLOSE_DISTANCE);
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
        // return : list of enemy in range
        List<GameObject> enemyInRangeList = getPlayersAtBotArea(radius);
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

    private double getDistClosestEnemy(GameObject closestEnemy){
        // return : distance to closest enemy
        return getDistanceToBot(closestEnemy);
    }

    private int sizeSaveToAttack(){
        // return 0 : if bot is too small to attack, increase as bot gets bigger
        int botSize = bot.getSize();
        if(botSize < 15) return 0;
        if(botSize < 30) return 1;
        if(botSize < 45) return 2;
        else return 3;
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

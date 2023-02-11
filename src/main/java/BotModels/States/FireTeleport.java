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
        if(bot.getTeleporterAngle() != -1) return 0;
        else{
            if(bot.getTeleporterCount() == 0) return 0;
            else{
                if(sizeSaveToFire() == 0) return 0;
                else{
                    List<GameObject> enemySmallList = enemySmall();
                    if(enemySmallList.size() == 0) return 0;
                    else{
                        return sizeSaveToFire();
                    }
                }
            }
        }
    }

    public PlayerAction calculatePlayerAction(){
        List<GameObject> enemySmallList = enemySmall();
        if(enemySmallList.size() != 0){
            GameObject target = enemySmallest(enemySmallList);
            return fireTeleport(target);
        }
        else{
            PlayerAction playerAction = new PlayerAction();
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = 0;
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

    private int sizeSaveToFire(){
        int botSize = bot.getSize();
        if(botSize <= 20) return 0;
        if(botSize < 30) return 25;
        if(botSize < 45) return 50;
        else return 75;
    }

    private PlayerAction fireTeleport(GameObject target){
        PlayerAction playerAction = new PlayerAction();
        int heading = calcHeadingOffset(target);
        playerAction.action = PlayerActions.FIRETELEPORT;
        playerAction.heading = heading;
        bot.teleporterAngle = heading;
        return playerAction;
    }
}

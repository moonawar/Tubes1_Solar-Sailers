/* package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class FireTeleport extends BotState{
    private final int VERY_SMALL_SHIP= 10;
    private final int SMALL_SHIP = 100000;
    private final int VERY_CLOSE_DISTANCE = 10;
    private final int CLOSE_DISTANCE = 50;
    private final int MEDIUM_DISTANCE = 150;
    private final int FAR_DISTANCE =  200;
    private final int VERY_FAR_DISTANCE = 400;
    private final int LARGE_NUM = 1000000;

    /* ABSTRACT METHOD
    public float calculatePriorityScore(){}
    public PlayerAction calculatePlayerAction(){}

    /* HELPER METHOD
    private List<GameObject> enemySmall(){
        int radius = gameState.getWorld().getRadius();
        List<GameObject> enemySmallList = getGameObjectsByType(getGameObjectsAtBotArea(radius), ObjectTypes.PLAYER)
            .stream().filter(x -> x.getId() != bot.getId() && x.getSize() <= SMALL_SHIP)
            .collect(Collectors.toList()); 
        return enemySmallList;
    }
    private GameObject enemySmallest(List<GameObject> enemySmallList){
        int minsize = LARGE_NUM, size;
        int smallestidx = 0;
        for(int i = 0; i < enemySmallList.size(); i++){
            dist = enemySmallList.get(i).getSize();
            if(dist < mindist){
                mindist = dist;
                closestidx = i;
            }
        }
        return enemyInRangeList.get(closestidx);
    }
} */

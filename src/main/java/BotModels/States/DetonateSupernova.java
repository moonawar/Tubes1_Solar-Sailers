package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class  DetonateSupernova extends BotState{
     /* ABSTRACT METHOD */
     public float calculatePriorityScore() {
        if (isBombExist() && BotState.supernovaFired ){
            // System.out.println("bomb exist");
            // if (bot.supernovaBomb == 1){
            if (isReadytoDetonate()){
                return 2000;
            } else if (distanceSupernovaToBoundary()<=50){
                return 2000;
            }  else {
                return 0;
            }
        } else {
            // System.out.println("bomb doesnt exist");
            return 0;
        }
    }
    
    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction = detonateSupernova();
        return playerAction;
    }

    /* HELPER METHHOD */
    private boolean isBombExist(){
        return !getGameObjectsByType(ObjectTypes.SUPERNOVABOMB).isEmpty();
    }
    private boolean isReadytoDetonate(){
        GameObject supernovaBomb = getGameObjectsByType(ObjectTypes.SUPERNOVABOMB).get(0);
        List <GameObject> enemy = gameState.getPlayerGameObjects()
            .stream().filter(y-> y.getId() != bot.getId()).sorted(Comparator.comparing(x-> x.getSize()))
            .collect(Collectors.toList());
        GameObject biggestEnemy = enemy.get(enemy.size()-1);
        if (getDistanceToBot(supernovaBomb)-bot.getSize()>= 170 && getDistanceBetween(supernovaBomb, biggestEnemy)<=70+biggestEnemy.getSize()){
            return true;
        } else {
            return false;
        }
    }

    private double distanceSupernovaToBoundary(){
        GameObject supernovaBomb = getGameObjectsByType(ObjectTypes.SUPERNOVABOMB).get(0);
        return gameState.getWorld().getRadius()-getDistance(gameState.getWorld().getCenterPoint(), supernovaBomb.getPosition());
    }

    private PlayerAction detonateSupernova(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.DETONATESUPERNOVA;
        playerAction.heading = 0;
        BotState.supernovaFired = false;
        return playerAction;
    }

}
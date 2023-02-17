package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class  DetonateSupernova extends BotState{
     /* ABSTRACT METHOD */
     public float calculatePriorityScore() {
        // Implementasi perhitungan skor prioritas untuk state DetonateSupernova
        if (isBombExist() && BotState.supernovaFired ){

            if (isReadytoDetonate()){
                return 2000;
            } else if (distanceSupernovaToBoundary()<=50){
                return 2000;
            }  else {
                return 0;
            }
        } else {
         
            return 0;
        }
    }
    
    public PlayerAction calculatePlayerAction(){
        // Implementasi pemilihan aksi bot untuk state DetonateSupernova
        PlayerAction playerAction = new PlayerAction();
        playerAction = detonateSupernova();
        return playerAction;
    }

    /* HELPER METHHOD */
    private boolean isBombExist(){
        // Mengembalikan true apabila terdapat supernova bomb pada world
        return !getGameObjectsByType(ObjectTypes.SUPERNOVABOMB).isEmpty();
    }

    private boolean isReadytoDetonate(){
        // Mengembalikan true apabila supernova telah sampai pada musuh dengan ukuran terbesar dan berada pada jarak yang jauh dari bot
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
        // Mengembalikan jarak supernovabomb ke boundary
        GameObject supernovaBomb = getGameObjectsByType(ObjectTypes.SUPERNOVABOMB).get(0);
        return gameState.getWorld().getRadius()-getDistance(gameState.getWorld().getCenterPoint(), supernovaBomb.getPosition());
    }

    private PlayerAction detonateSupernova(){
        // Mengembalikan aksi DETONATESUPERNOVA apabila supernova bomb telah sampai pada sasaran dan berada pada jarak tertentu dari bot
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.DETONATESUPERNOVA;
        playerAction.heading = 0;
        BotState.supernovaFired = false;
        return playerAction;
    }

}
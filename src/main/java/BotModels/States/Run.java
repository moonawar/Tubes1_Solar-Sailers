package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class Run extends BotState{
    /* CONSTANTS */
    private final int MEDIUM_DISTANCE = 150;    
    private final int DISTANCE_TO_TOLERANCE_RATIO = 10;

    private final int RUN_ANGLE = 135;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        // Count the priority score of the state
        int biggerEnemyCount = getBiggerEnemiesInRange(MEDIUM_DISTANCE + bot.getSize()).size();
        int teleportProjectileCount = getTeleporterInRange(MEDIUM_DISTANCE + bot.getSize()).size();
        int supernovaProjectileCount = getSupernovaProjInRange(MEDIUM_DISTANCE + bot.getSize()).size();
        
        float priorityScore = 
            biggerEnemyCount * 15 + 
            teleportProjectileCount * 150 + 
            supernovaProjectileCount * 150;

        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.STARTAFTERBURNER;

        List<GameObject> biggerEnemiesClose = getBiggerEnemiesInRange(MEDIUM_DISTANCE);
        GameObject closestEnemy = biggerEnemiesClose.size() > 0 ? 
            biggerEnemiesClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null ;

        if (closestEnemy == null) {
            playerAction.heading = (bot.currentHeading + 135) % 360;
        } else {
            playerAction.heading =  (getHeadingBetween(closestEnemy) + RUN_ANGLE) % 360;
        }
        return playerAction;
    }    

    /* HELPER METHODS */
    private List<GameObject> getBiggerEnemiesInRange(int distance) {
        // Mengembalikan list musuh yang lebih besar dari ship yang berada di area sekitar ship (MEDIUM DISTANCE)
        List<GameObject> biggerEnemies = getPlayersAtArea(bot.getPosition(), distance)
            .stream().filter(x -> x.getId() != bot.getId() && x.getSize() > bot.getSize() && isObjectHeadingTo(x, bot.getPosition(), distance/DISTANCE_TO_TOLERANCE_RATIO))
            .collect(Collectors.toList()); 
        return biggerEnemies;
    }

    private List<GameObject> getTeleporterInRange(int distance) {
        List<GameObject> teleportProjectiles = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TELEPORTER).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return teleportProjectiles;
    }

    private List<GameObject> getSupernovaProjInRange(int distance){
        List<GameObject> supernovaProjectiles = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.SUPERNOVABOMB).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return supernovaProjectiles;
    }
}

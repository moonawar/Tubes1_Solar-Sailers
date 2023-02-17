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

    private final int RUN_ANGLE = 180;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        // check if bot is in danger
        int biggerEnemyCount = getBiggerEnemiesInRange(MEDIUM_DISTANCE + bot.getSize()).size();
        int teleportProjectileCount = getTeleporterInRange(MEDIUM_DISTANCE + bot.getSize()).size();
        int supernovaProjectileCount = getSupernovaProjInRange(MEDIUM_DISTANCE + bot.getSize()).size();

        
        float priorityScore = 
            biggerEnemyCount * 50 + 
            teleportProjectileCount * 150 + 
            supernovaProjectileCount * 150;

        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();

        // if in gas cloud or boundary
        if (!getGasCloud().isEmpty() &&  bot.getSize() + 50 < distanceToGasCloud() + getGasCloud().get(0).getSize()){
            playerAction = dodgeGasCloud();
        }

        if (bot.getSize() + 200  < distanceToBoundary()){
            playerAction = dodgeBoundary();
        }

        // set action to run
        playerAction.action = PlayerActions.FORWARD;

        List<GameObject> biggerEnemiesClose = getBiggerEnemiesInRange(MEDIUM_DISTANCE);
        GameObject closestEnemy = biggerEnemiesClose.size() > 0 ? 
            biggerEnemiesClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null;

        List<GameObject> teleportProjectileClose = getTeleporterInRange(MEDIUM_DISTANCE);
        GameObject closestTeleportProjectile = teleportProjectileClose.size() > 0 ? 
            teleportProjectileClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null;

        List<GameObject> supernovaProjectileClose = getSupernovaProjInRange(MEDIUM_DISTANCE);
        GameObject closestSupernovaProjectile = supernovaProjectileClose.size() > 0 ? 
            supernovaProjectileClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null;

        if (closestEnemy != null) {
            // run away from bigger enemy
            playerAction.heading = (getHeadingBetween(closestEnemy) + RUN_ANGLE) % 360;
        } else if (closestTeleportProjectile != null) {
            // run away from teleporter projectile
            playerAction.heading = (getHeadingBetween(closestTeleportProjectile) + RUN_ANGLE - 90) % 360;
        } else if (closestSupernovaProjectile != null) {
            // run away from supernova projectile
            playerAction.heading = (getHeadingBetween(closestSupernovaProjectile) + RUN_ANGLE - 90) % 360;
        } else {
            playerAction.heading =  bot.getCurrHeading();
        }

        return playerAction;
    }    

    /* HELPER METHODS */
    private List<GameObject> getBiggerEnemiesInRange(int distance) {
        // return list of bigger enemies within distance that is heading towards bot
        List<GameObject> biggerEnemies = getPlayersAtArea(bot.getPosition(), distance)
            .stream().filter(x -> x.getId() != bot.getId() && x.getSize() > bot.getSize() && isObjectHeadingTo(x, bot.getPosition(), distance/DISTANCE_TO_TOLERANCE_RATIO))
            .collect(Collectors.toList()); 
        return biggerEnemies;
    }

    private List<GameObject> getTeleporterInRange(int distance) {
        // return list of teleporter projectiles within distance that is heading towards bot
        List<GameObject> teleportProjectiles = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TELEPORTER).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return teleportProjectiles;
    }

    private List<GameObject> getSupernovaProjInRange(int distance){
        // return list of supernova projectiles within distance that is heading towards bot
        List<GameObject> supernovaProjectiles = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.SUPERNOVABOMB).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return supernovaProjectiles;
    }

    private PlayerAction dodgeBoundary(){
        // return : player action to dodge the boundary
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = (getHeading(gameState.getWorld().getCenterPoint()) + 60) % 360;

        return playerAction;
    }

    private PlayerAction dodgeGasCloud(){
        // return : player action to dodge the gas cloud
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = (getHeadingToGasCloud() + 180) % 360;
        return playerAction;
    }

    private double distanceToBoundary(){
        // return : distance to the boundary of the world
        return gameState.getWorld().getRadius() - getDistance(gameState.getWorld().getCenterPoint(), bot.getPosition());
    }

    private List<GameObject> getGasCloud(){
        // return : list of gas cloud sorted by distance to bot
        List<GameObject> gasCloud = getGameObjectsByType(ObjectTypes.GASCLOUD)
            .stream().sorted(Comparator
                .comparing(x-> getDistanceToBot(x)))
            .collect(Collectors.toList());
        return gasCloud;
    }

    private double distanceToGasCloud(){
        // return : distance to the closest gas cloud
        if (getGasCloud().isEmpty()){
            return 999;
        } else {
            return getDistanceToBot(getGasCloud().get(0));
        }
    }

    private int getHeadingToGasCloud(){
        // return : heading to the closest gas cloud
        if (getGasCloud().isEmpty()){
            return bot.getCurrHeading();
        } else {
            return getHeadingBetween(getGasCloud().get(0));
        }
    }

}

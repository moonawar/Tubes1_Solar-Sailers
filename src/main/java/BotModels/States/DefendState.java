package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class DefendState extends BotState{
    /* Parameters for priority score
     * 1. Banyaknya musuh yang lebih besar
     * 2. Banyaknya torpedo yang mengarah ke ship
     * 3. Banyaknya teleport projectile yang mengarah ke ship
     * 4. Kecepatan ship
     * 5. Supernova projectile yang mengarah ke ship
     * 
     * Jenis-jenis defense yang bisa dilakukan
     * 1. Shield
     * 2. Afterburner
     * 3. Wormhole
     */

    /* PARAMETER CONSTANTS */
    private final int VERY_CLOSE_DISTANCE = 50;
    private final int CLOSE_DISTANCE = 100;
    private final int MEDIUM_DISTANCE = 200;
    private final int FAR_DISTANCE = 400;
    private final int VERY_FAR_DISTANCE = 600;
    
    private final int DISTANCE_TO_TOLERANCE_RATIO = 10;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        int biggerEnemyCount = getBiggerEnemiesInRange(MEDIUM_DISTANCE).size();
        int torpedoCount = getTorpedosInRange(MEDIUM_DISTANCE).size();
        int teleportProjectileCount = getTeleporterInRange(MEDIUM_DISTANCE).size();
        int supernovaProjectileCount = getSupernovaProjInRange(MEDIUM_DISTANCE).size();
        
        // Will be recalculated later
        float priorityScore = biggerEnemyCount * 15 + torpedoCount * 10 + teleportProjectileCount * 20 + supernovaProjectileCount * 60;

        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        
        double highestActionPriority = shieldPriority();
        playerAction = shieldAction();

        highestActionPriority = Math.max(highestActionPriority, afterburnerPriority());
        if (highestActionPriority == afterburnerPriority()) {
            playerAction = afterburnerAction();
        }

        highestActionPriority = Math.max(highestActionPriority, wormholePriority());
        if (highestActionPriority == wormholePriority()) {
            playerAction = wormholeAction();
        }

        return playerAction;
    }
    
    /* HELPER METHODS */
    private List<GameObject> getBiggerEnemiesInRange(int distance) {
        // Mengembalikan list musuh yang lebih besar dari ship yang berada di area sekitar ship (MEDIUM DISTANCE)
        List<GameObject> biggerEnemies = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.PLAYER)
            .stream().filter(x -> x.getId() != bot.getId() && x.getSize() > bot.getSize())
            .collect(Collectors.toList()); 
        return biggerEnemies;
    }

    private List<GameObject> getTorpedosInRange(int distance) {
        // Mengembalikan list musuh yang lebih besar dari ship yang berada di area sekitar ship (CLOSE DISTANCE)
        List<GameObject> torpedoes = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TORPEDOSALVO).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return torpedoes;
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



    /* SHIELD 
        Akan mengaktifkan shield jika:
            1. Ada banyak torpedo yang mengarah ke ship pada jarak dekat
            2. Kecepatan ship terlalu rendah untuk menghindari torpedo
    */
    private double shieldPriority(){
        List<GameObject> torpedoesVeryClose = getTorpedosInRange(VERY_CLOSE_DISTANCE);
        int torpedoCountVeryClose = torpedoesVeryClose.size();

        List<GameObject> torpedoesClose = getTorpedosInRange(CLOSE_DISTANCE);
        int torpedoCountClose = torpedoesClose.size() - torpedoCountVeryClose;

        int speed = bot.getSpeed();

        double priority = 
            torpedoCountVeryClose * 0.8 + 
            torpedoCountClose * 0.3 - 
            speed * 0.002;
            
        return priority;
    }
    
    private PlayerAction shieldAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.ACTIVATESHIELD;
        playerAction.heading = bot.currentHeading;

        return playerAction;
    }

    /* AFTERBURNER
        Akan mengaktifkan afterburner jika:
            1. Ship berada di jarak dekat dengan musuh yang lebih besar
            2. Ship berada di jarak medium dengan torpedo yang mengarah ke ship
            3. Ship berada di jarak medium dengan teleport projectile yang mengarah ke ship
            4. Kecepatan ship terlalu rendah untuk menghindari torpedo
            5. Supernova projectile yang mengarah ke ship
    */

    private double afterburnerPriority(){
        List<GameObject> biggerEnemiesClose = getBiggerEnemiesInRange(CLOSE_DISTANCE);
        int biggerEnemyCountClose = biggerEnemiesClose.size();

        List<GameObject> torpedoesMedium = getTorpedosInRange(MEDIUM_DISTANCE);
        int torpedoCountMedium = torpedoesMedium.size();

        List<GameObject> teleportProjectilesMedium = getTeleporterInRange(MEDIUM_DISTANCE);
        int teleportProjectileCountMedium = teleportProjectilesMedium.size();

        List<GameObject> supernovaProjectilesMedium = getSupernovaProjInRange(MEDIUM_DISTANCE);
        int supernovaProjectileCountMedium = supernovaProjectilesMedium.size();

        int speed = bot.getSpeed();

        double priority = 
            biggerEnemyCountClose * 0.9 + 
            torpedoCountMedium * 0.05 + 
            teleportProjectileCountMedium * 0.05 +
            supernovaProjectileCountMedium * 1 - 
            speed * 0.002;
        
        return priority;
    }

    private PlayerAction afterburnerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.STARTAFTERBURNER;

        List<GameObject> biggerEnemiesClose = getBiggerEnemiesInRange(CLOSE_DISTANCE);
        GameObject closestEnemy = biggerEnemiesClose.size() > 0 ? 
            biggerEnemiesClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null ;

        if (closestEnemy == null) {
            playerAction.heading = bot.currentHeading;
        } else {
            playerAction.heading =  (getHeadingBetween(closestEnemy) + 120) % 360;
        }
        return playerAction;
    }

    /* WORMHOLE
        Akan mengaktifkan wormhole jika:
            1. Ship berada di jarak dekat dengan wormhole
            2. Ship berada di jarak medium dengan torpedo yang mengarah ke ship
            3. Ship berada di jarak medium dengan teleport projectile yang mengarah ke ship
            4. Ship berada di jarak medium dengan supernova projectile yang mengarah ke ship
    */

    private double wormholePriority(){
        List<GameObject> wormholesClose = getGameObjectsByType(getGameObjectsAtBotArea(CLOSE_DISTANCE), ObjectTypes.WORMHOLE);
        int wormholeCountClose = wormholesClose.size();

        List<GameObject> torpedoesMedium = getTorpedosInRange(MEDIUM_DISTANCE);
        int torpedoCountMedium = torpedoesMedium.size();

        List<GameObject> teleportProjectilesMedium = getTeleporterInRange(MEDIUM_DISTANCE);
        int teleportProjectileCountMedium = teleportProjectilesMedium.size();

        List<GameObject> supernovaProjectilesMedium = getSupernovaProjInRange(MEDIUM_DISTANCE);
        int supernovaProjectileCountMedium = supernovaProjectilesMedium.size();

        double priority = 
            wormholeCountClose * 1.1 + 
            torpedoCountMedium * 0.05 + 
            teleportProjectileCountMedium * 0.05 +
            supernovaProjectileCountMedium * 1.2;
        
        return priority;
    }

    private PlayerAction wormholeAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = bot.getSpeed() < 10 ? PlayerActions.STARTAFTERBURNER : PlayerActions.FORWARD;

        List<GameObject> wormholesClose = getGameObjectsByType(getGameObjectsAtBotArea(CLOSE_DISTANCE), ObjectTypes.WORMHOLE);
        GameObject closestWormhole = wormholesClose.size() > 0 ? 
            wormholesClose.stream().sorted(Comparator.comparing(x -> getDistanceToBot(x))).collect(Collectors.toList()).get(0) : null ;
        
        if (closestWormhole == null) {
            playerAction.heading = bot.currentHeading;
        } else {
            playerAction.heading =  getHeadingBetween(closestWormhole);
        }

        return playerAction;
    }


    
}

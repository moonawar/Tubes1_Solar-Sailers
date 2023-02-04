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
    
    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        int biggerEnemyCount = getBiggerEnemiesInRange(MEDIUM_DISTANCE).size();
        int torpedoCount = getTorpedosInRange(MEDIUM_DISTANCE).size();
        int teleportProjectileCount = getTeleporterInRange(MEDIUM_DISTANCE).size();
        
        // Will be recalculated later
        float priorityScore = biggerEnemyCount * 0.5f + torpedoCount * 0.3f + teleportProjectileCount * 0.2f;

        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
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
        List<GameObject> torpedoes = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TORPEDO_SALVO);
        return torpedoes;
    }

    private List<GameObject> getTeleporterInRange(int distance) {
        List<GameObject> teleportProjectiles = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TELEPORTER);
        return teleportProjectiles;
    }
}


package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class GatherFood extends BotState{
    /* Constants */
    private final int VERY_CLOSE_DISTANCE = 32;
    private final int CLOSE_DISTANCE = 50;
    private final int FAR_DISTANCE = 200;

    private final float BASE_SCORE = 100;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        return BASE_SCORE;
    }

    public PlayerAction calculatePlayerAction(){
        // go to closest food by default
        PlayerAction playerAction = new PlayerAction();

        if (getGameObjectsByType(ObjectTypes.FOOD).size() == 0) {
            // just avoid obstacles
            if (!getGasCloud().isEmpty() &&  bot.getSize() + 50 < distanceToGasCloud() + getGasCloud().get(0).getSize()){
                playerAction = dodgeGasCloud();
            }
    
            if (bot.getSize()  < distanceToBoundary()){
                playerAction = dodgeBoundary();
            }

            return playerAction;
        }

        playerAction = goToClosestFood();

        

        // gather food if safe
        if (bot.getSize() <= 15 || foodDensityInRange(bot.getSize() + VERY_CLOSE_DISTANCE) >= 1){
            playerAction = goToClosestFood();
        } else if (foodDensityInRange(bot.getSize() + CLOSE_DISTANCE) >= 5){
            playerAction = goToClosestFood();
        } else {
            playerAction = goToNewArea();
        }

        // obstacle avoidance
        if (!getGasCloud().isEmpty() &&  bot.getSize() > distanceToGasCloud() + getGasCloud().get(0).getSize()){
            playerAction = dodgeGasCloud();
        }

        if (distanceToBoundary()< 20+bot.getSize()){
            playerAction = dodgeBoundary();
            System.out.println(distanceToBoundary());
        }
        

        return playerAction;
    }

    /* HELPER METHODS */
    private double foodDensityInRange(int distance){
        // return : density score of food and superfood in range
        List<GameObject> food = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.FOOD);
        List<GameObject> superFood = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.SUPERFOOD);
        return food.size() + 1.25*superFood.size();
    }

    private List<GameObject> getGameObjectsByQuadran(float maxQuadrant, float nQuadrant, int distance){
        // return : list of game objects in a quadrant (nQuadrant) of a division (maxQuadrant) in the area around the bot
        List<GameObject> objects = getGameObjectsAtBotArea(distance)
            .stream().filter(x -> (getHeadingBetween(x) >= (360/maxQuadrant*(nQuadrant-1))) && (getHeadingBetween(x)<(360/maxQuadrant*(nQuadrant))))
            .collect(Collectors.toList());
        return objects;
    }

    private int calculateDensity(List<GameObject> objects){
        // return : density score of objects in list
        int food = getGameObjectsByType(objects, ObjectTypes.FOOD).size();
        int superFood = getGameObjectsByType(ObjectTypes.SUPERFOOD).size();
        int gasCloud = getGameObjectsByType(ObjectTypes.GASCLOUD).size();
        int torpedo = getGameObjectsByType(ObjectTypes.TORPEDOSALVO).size();
        int supernova = getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).size();

        return food*10 + superFood*15 + gasCloud * (-30) + torpedo*(-20) + supernova*20;
    }

    private int foodHeatMap(int distance){
        // return : heading to the quadrant with the highest density of food and superfood
        List<Integer> density = new ArrayList<Integer>();
        for (int i = 1; i <= 6; i++){
            density.add(calculateDensity(getGameObjectsByQuadran(6, i, distance)));
        }

        int idx_max = 0;
        for (int i = 1; i < 6; i++){
            if (density.get(idx_max) <= density.get(i)){
                idx_max = i;
            }
        }
        return ((idx_max)*60) + 30;
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


    private boolean isSuperFoodActive(){
        // return : true if superfood is active
        if ((bot.getEffect()>=8 && bot.getEffect() < 16)){
            return true;
        } else if (bot.getEffect()>=24){
            if (bot.getEffect()-16 >= 8 && bot.getEffect()-16<16){
                return true;
            }
        }
        return false;
    }


    /* GATHER FOOD */
    private PlayerAction goToClosestFood(){
        // return : player action to go to the closest food
        PlayerAction playerAction = new PlayerAction();
        List<GameObject> food = getGameObjectsByType(ObjectTypes.FOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBotWithHeading(x)))
            .collect(Collectors.toList());

        List<GameObject> superFood = getGameObjectsByType(ObjectTypes.SUPERFOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBotWithHeading(x)))
            .collect(Collectors.toList());

        if (!food.isEmpty()){ //superfood lebih diutamakan kalau tidak aktif
            if (!superFood.isEmpty()){
                if (getDistanceToBotWithHeading(food.get(0)) >= 0.75 * getDistanceToBotWithHeading(superFood.get(0)) && !isSuperFoodActive()){
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(superFood.get(0));
                } else {
                    playerAction.action = PlayerActions.FORWARD;
                    playerAction.heading = getHeadingBetween(food.get(0));
                }
            } else {
                playerAction.action = PlayerActions.FORWARD;
                playerAction.heading = getHeadingBetween(food.get(0));
            }
        } else {
            playerAction = goToNewArea();
        }
        return playerAction;
    }

    private double getDistanceToBotWithHeading(GameObject x){
        // return : distance to the closest food considering the heading too
        int headToFood = getHeading(x.position);
        int botHeading = bot.getCurrHeading();

        double actualDist = getDistanceToBot(x);
    
        if (Math.abs((headToFood - botHeading) % 360) <= 15) {
            return actualDist;
        } else {
            return actualDist + 50;
        }
    }
       

    private PlayerAction goToNewArea(){
        // return : player action to go to the quadrant with the highest density of food and superfood
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = foodHeatMap(FAR_DISTANCE + bot.getSize());
        return playerAction;
    }

    private PlayerAction dodgeBoundary(){
        // return : player action to dodge the boundary
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = (getHeading(gameState.getWorld().getCenterPoint())) % 360;

        return playerAction;
    }

    private PlayerAction dodgeGasCloud(){
        // return : player action to dodge the gas cloud
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = (getHeadingToGasCloud() + 180) % 360;
        return playerAction;
    }
    
}

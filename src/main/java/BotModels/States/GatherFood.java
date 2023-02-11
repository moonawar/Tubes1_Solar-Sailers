
package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class GatherFood extends BotState{

    private final int VERY_CLOSE_DISTANCE = 32;
    private final int CLOSE_DISTANCE = 50;
    private final int MEDIUM_DISTANCE =150;
    private final int FAR_DISTANCE =  200;
    private final int VERY_FAR_DISTANCE =400;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        //belum
        return 1000;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction = goToClosestFood();

        if (bot.getSize()<=15 || foodDensityInRange(bot.getSize() + VERY_CLOSE_DISTANCE)>=1){
            playerAction = goToClosestFood();
        } else if (foodDensityInRange(bot.getSize() + CLOSE_DISTANCE)>=5){
            playerAction = goToClosestFood();
        } else {
            playerAction = goToNewArea();
        }
        
        if(!getGasCloud().isEmpty() && distanceToGasCloud()-bot.getSize() < 50){
            playerAction = dodgeGasCloud();
        }

        if(distanceToBoundary()-bot.getSize() < 50){
            playerAction = dodgeBoundary();
        }
    

        return playerAction;
    }

    /* HELPER METHODS */
    private double foodDensityInRange(int distance){
        // mengembalikan banyak food & superfood disekitar player
        List<GameObject> food = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.FOOD);
        List<GameObject> superFood = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.SUPERFOOD);
        return food.size() + 1.25*superFood.size();
    }

    private List<GameObject> getGameObjectsByQuadran(float maxQuadrant, float nQuadrant, int distance){
        // membagi objek disekitar player
        List<GameObject> objects = getGameObjectsAtBotArea(distance)
            .stream().filter(x -> (getHeadingBetween(x)>=(360/maxQuadrant*(nQuadrant-1))) && (getHeadingBetween(x)<(360/maxQuadrant*(nQuadrant))))
            .collect(Collectors.toList());
        return objects;
    }

    private int calculateDensity(List<GameObject> objects){
        // menghitung density
        // belum ngitung lawan
        int food = getGameObjectsByType(objects, ObjectTypes.FOOD).size();
        int superFood = getGameObjectsByType(ObjectTypes.SUPERFOOD).size();
        int gasCloud = getGameObjectsByType(ObjectTypes.GASCLOUD).size();
        int torpedo = getGameObjectsByType(ObjectTypes.TORPEDOSALVO).size();
        int supernova = getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).size();

        return food*10 + superFood*15 + gasCloud * (-30) + torpedo*(-20) + supernova*20;
    }

    private int foodHeatMap(int distance){
        List<Integer> density = new ArrayList<Integer>();
        for (int i=1; i<=6; i++){
            density.add(calculateDensity(getGameObjectsByQuadran(6, i, distance)));
        }
        int idx_max = 0;
        for (int i=1; i<6; i++){
            if (density.get(idx_max)<=density.get(i)){
                idx_max = i;
            }
        }
        return ((idx_max)*60) + 30;
    }

    private double distanceToBoundary(){
        return gameState.getWorld().getRadius()-getDistance(gameState.getWorld().getCenterPoint(),bot.getPosition());
    }

    private List<GameObject> getGasCloud(){
        List<GameObject> gasCloud = getGameObjectsByType(ObjectTypes.GASCLOUD)
            .stream().sorted(Comparator
                .comparing(x-> getDistanceToBot(x)))
            .collect(Collectors.toList());
        return gasCloud;
    }

    private double distanceToGasCloud(){
        if (getGasCloud().isEmpty()){
            return -99;
        } else {
            return getDistanceToBot(getGasCloud().get(0));
        }
    }

    private int getHeadingToGasCloud(){
        if (getGasCloud().isEmpty()){
            return 0;
        } else {
            return getHeadingBetween(getGasCloud().get(0));
        }
    }


    private boolean isSuperFoodActive(){
        if ((bot.getEffect()>=8 && bot.getEffect()<16)){
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
        PlayerAction playerAction = new PlayerAction();
        List<GameObject> food = getGameObjectsByType(ObjectTypes.FOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBot(x)))
            .collect(Collectors.toList());

        List<GameObject> superFood = getGameObjectsByType(ObjectTypes.SUPERFOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBot(x)))
            .collect(Collectors.toList());

        if (!food.isEmpty()){ //superfood lebih diutamakan kalau tidak aktif
            if (!superFood.isEmpty()){
                if (getDistanceToBot(food.get(0)) >= 0.75*getDistanceToBot(superFood.get(0))&& !isSuperFoodActive()){
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

    private PlayerAction goToNewArea(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = foodHeatMap(FAR_DISTANCE + bot.getSize());
        return playerAction;
    }

    private PlayerAction dodgeBoundary(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = getHeading(gameState.getWorld().getCenterPoint());
        return playerAction;
    }

    private PlayerAction dodgeGasCloud(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = (getHeadingToGasCloud()+180)%360;
        return playerAction;
    }

    /* GATHER FOOD */
}

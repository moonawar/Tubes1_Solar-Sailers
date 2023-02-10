
package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class GatherFood extends BotState{

    private final int VERY_CLOSE_DISTANCE = 10;
    private final int CLOSE_DISTANCE = 70;
    private final int MEDIUM_DISTANCE =150;
    private final int FAR_DISTANCE =  200;
    private final int VERY_FAR_DISTANCE =400;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        //belum
        return 100;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction = getClosestFood();

        if (foodDensityInRange(bot.getSize() + VERY_CLOSE_DISTANCE)>=1){
            playerAction = getClosestFood();
        } else if (foodDensityInRange(bot.getSize() + CLOSE_DISTANCE)>=7){
            playerAction = getClosestFood();
        } else {
            playerAction = goToNewArea();
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
        // menghitung density (not finished)
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

    /* GATHER FOOD */

    private PlayerAction getClosestFood(){
        PlayerAction playerAction = new PlayerAction();
        List<GameObject> food = getGameObjectsByType(ObjectTypes.FOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBot(x)))
            .collect(Collectors.toList());

        List<GameObject> superFood = getGameObjectsByType(ObjectTypes.SUPERFOOD)
            .stream().sorted(Comparator
                .comparing(x -> getDistanceToBot(x)))
            .collect(Collectors.toList());

        if (!food.isEmpty()){ //superfood lebih diutamakan
            if (!superFood.isEmpty()){
                if (getDistanceToBot(food.get(0)) >= 0.75*getDistanceToBot(superFood.get(0))){
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
        playerAction.heading = foodHeatMap(FAR_DISTANCE);
        return playerAction;
    }


    /* GATHER FOOD */
}

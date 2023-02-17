package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class PickUpSupernova extends BotState{

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        return 100;
    }
    
    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        // System.out.println("supernova distance = "+ supernoveDistance());
        if (supernoveDistance()<=200){
            playerAction = pickUpSupernova();
        } else {
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = 0;
        }
        return playerAction;
    }

    /* HELPER METHODS */
    double supernoveDistance(){
        if (getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).isEmpty()){
            return -99;
        } else {
            return getDistanceToBot(getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).get(0));
        }
    }

    /*PICKUP SUPERNOVA */

    private PlayerAction pickUpSupernova(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = getHeadingBetween(getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).get(0));
        return playerAction;
    }

}

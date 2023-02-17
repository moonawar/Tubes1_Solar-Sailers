package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class PickUpSupernova extends BotState{

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        if (supernoveDistance()<=250-bot.getSize() && supernoveDistance()!=-99){
            return 2000;
        } else {
            return 0;
        }
    }
    
    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
            playerAction = pickUpSupernova();
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

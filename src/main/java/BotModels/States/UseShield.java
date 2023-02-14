package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class UseShield extends BotState {
    /* CONSTANTS */
    private final int CLOSE_DISTANCE = 80;
    private final int DISTANCE_TO_TOLERANCE_RATIO = 10;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        // Count the priority score of the state
        int torpedoCount = getTorpedosInRange(CLOSE_DISTANCE + bot.getSize()).size();
        
        // Will be recalculated later
        float priorityScore = bot.shieldCount * torpedoCount * 50;

        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.ACTIVATESHIELD;
        playerAction.heading = bot.currentHeading;

        return playerAction;
    }

    /* HELPER METHODS */
    private List<GameObject> getTorpedosInRange(int distance) {
        // Return : list of torpedo that is heading to the bot
        List<GameObject> torpedoes = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TORPEDOSALVO).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return torpedoes;
    }
   
}

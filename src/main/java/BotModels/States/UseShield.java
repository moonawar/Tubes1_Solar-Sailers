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
        // check if bot is in danger of torpedo heading to it
        int torpedoCount = getTorpedosInRange(CLOSE_DISTANCE + bot.getSize()).size();

        float priorityScore = bot.shieldCount * torpedoCount * 50;
        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        // activate shield
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.ACTIVATESHIELD;
        playerAction.heading = bot.currentHeading;

        return playerAction;
    }

    /* HELPER METHODS */
    private List<GameObject> getTorpedosInRange(int distance) {
        // return : list of torpedo that is heading to the bot
        List<GameObject> torpedoes = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TORPEDOSALVO).
            stream().filter(x -> isObjectHeadingToBot(x, distance/DISTANCE_TO_TOLERANCE_RATIO)).collect(Collectors.toList());
        return torpedoes;
    }
   
}

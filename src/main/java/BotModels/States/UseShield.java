package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class UseShield extends BotState {
    /* CONSTANTS */
    private final int CLOSE_DISTANCE = 150;

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        // check if bot is in danger of torpedo heading to it
        if (bot.getSize() < 30 || bot.getEffect() >= 16) {
            return 0;
        }
        
        int torpedoCount = getTorpedosInRange(CLOSE_DISTANCE + bot.getSize()).size();
        if (bot.shieldCount <= 0) {
            return 0;
        }

        float priorityScore = torpedoCount * 80;
        return priorityScore;
    }

    public PlayerAction calculatePlayerAction(){
        // activate shield
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.ACTIVATESHIELD;

        int headToCenter = getHeading(gameState.getWorld().getCenterPoint());
        playerAction.heading = headToCenter;

        return playerAction;
    }

    /* HELPER METHODS */
    private List<GameObject> getTorpedosInRange(int distance) {
        // return : list of torpedo that is heading to the bot
        List<GameObject> torpedoes = getGameObjectsByType(getGameObjectsAtBotArea(distance), ObjectTypes.TORPEDOSALVO).
            stream().filter(x -> isObjectHeadingToBot(x, (int) (bot.getSize()/2.5))).collect(Collectors.toList());
        return torpedoes;
    }
   
}

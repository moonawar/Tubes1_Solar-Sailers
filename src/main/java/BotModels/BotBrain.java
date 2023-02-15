package BotModels;

import Models.*;
import Services.*;

import BotModels.States.*;

public class BotBrain {
    public static BotService botService; // Bot Service to receive game state and bot data
    private static BotState currentState; // Current active state
    private static PlayerAction currentPlayerAction; // Action that will be returned to the host

    public static GameState GetGameState(){
        // Return : Game state from the server
        return botService.getGameState();
    }

    public static GameObject GetBot(){
        // Return : Bot data from the server
        return botService.getBot();
    }

    /* State Machine */
    private static BotState GatherFood = new GatherFood();
    private static BotState UseShield = new UseShield();
    private static BotState Run = new Run();
    private static BotState TorpedoAttack = new TorpedoAttack();
    private static BotState FireTeleport = new FireTeleport();
    private static BotState Teleport = new Teleport();

    private static BotState[] states = {GatherFood, FireTeleport, Teleport , Run, UseShield, TorpedoAttack};
    public static PlayerAction GetBotAction(){
        // Return : Action that will be executed by the bot based on the best state
        currentState = GetBestState();
        System.out.println("State : " + currentState.toString());

        currentPlayerAction = currentState.GetPlayerAction();
        return currentPlayerAction;
    }

    private static BotState GetBestState(){
        // Return : Best state based on the priority score
        BotState.gameState = GetGameState();
        BotState.bot = GetBot();

        float highestPriority = -99;
        BotState bestState = null;

        for (BotState state : states) {
            if (state.GetPriorityScore() > highestPriority) {
                highestPriority = state.GetPriorityScore();
                bestState = state;
            }
        }

        return bestState;
    }

}

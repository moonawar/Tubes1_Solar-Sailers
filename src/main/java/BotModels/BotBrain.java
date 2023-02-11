package BotModels;

import Models.*;
import Services.*;

import BotModels.States.*;

public class BotBrain {
    public static BotService botService; // Bot Service untuk menerima data game state dan data bot
    private static BotState currentState; // State yang sedang dijalankan
    private static PlayerAction currentPlayerAction; // Aksi yang akan dilakukan oleh bot dan akan dikirim ke server

    public static GameState GetGameState(){
        // Mengembalikan game state yang diterima dari server untuk diakses oleh state
        return botService.getGameState();
    }

    public static GameObject GetBot(){
        // Mengembalikan data bot (player) yang diterima dari server untuk diakses oleh state
        return botService.getBot();
    }

    /* State Machine */
    // * WAITING TO BE IMPLEMENTED *
    private static BotState GatherFood = new GatherFood();
    private static BotState Defend = new DefendState();
    private static BotState TorpedoAttack = new TorpedoAttack();
    // private static BotState FireTeleport = new FireTeleportState();
    // private static BotState Teleport = new TeleportState();
    // private static BotState GetPowerup = new GetPowerupState();
    // private static BotState FireNova = new FireNovaState();
    // private static BotState DetonateNova = new DetonateNovaState();

    // private BotState[] states = {GatherFood, Defend, TorpedoAttack, FireTeleport, Teleport, GetPowerup, FireNova, DetonateNova};
    // private static BotState[] states = {Defend};
    // private static BotState[] states = {TorpedoAttack};
    private static BotState[] states = {GatherFood};


    public static PlayerAction GetBotAction(){
        // *Waiting for states to be implemented*
        currentState = GetBestState();

        currentPlayerAction = currentState.GetPlayerAction();
        return currentPlayerAction;
    }

    // * WAITING TO BE IMPLEMENTED *
    private static BotState GetBestState(){
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

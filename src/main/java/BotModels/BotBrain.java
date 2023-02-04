package BotModels;

import Models.*;
import Services.*;

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
    // private BotState GatherFood = new GatherFoodState();
    // private BotState Defend = new DefendState();
    // private BotState TorpedoAttack = new TorpedoAttackState();
    // private BotState FireTeleport = new FireTeleportState();
    // private BotState Teleport = new TeleportState();
    // private BotState GetPowerup = new GetPowerupState();
    // private BotState FireNova = new FireNovaState();
    // private BotState DetonateNova = new DetonateNovaState();

    // private BotState[] states = {GatherFood, Defend, TorpedoAttack, FireTeleport, Teleport, GetPowerup, FireNova, DetonateNova};

    public static PlayerAction GetBotAction(){
        // *Waiting for states to be implemented*
        // currentState = GetBestState();

        currentPlayerAction = currentState.GetPlayerAction();
        return currentPlayerAction;
    }

    // * WAITING TO BE IMPLEMENTED *
    // private BotState GetBestState(){
    //     BotState.gameState = GetGameState();
    //     BotState.bot = GetBot();

    //     float highestPriority = 0;
    //     BotState bestState = null;

    //     for (BotState state : states) {
    //         if (state.GetPriorityScore() > highestPriority) {
    //             highestPriority = state.GetPriorityScore();
    //             bestState = state;
    //         }
    //     }
    // }

}

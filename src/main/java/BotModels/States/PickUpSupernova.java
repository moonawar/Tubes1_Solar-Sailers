package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;


public class PickUpSupernova extends BotState{

    /* ABSTRACT METHOD */
    public float calculatePriorityScore() {
        // Implementasi perhitungan skor prioritas untuk state PickUpSuperNova
        if (supernoveDistance()<=250-bot.getSize() && supernoveDistance()!=-99){
            return 2000;
        } else {
            return 0;
        }
    }
    
    public PlayerAction calculatePlayerAction(){
        // Implementasi pemilihan aksi bot untuk state PickUpSupernova
        PlayerAction playerAction = new PlayerAction();
            playerAction = pickUpSupernova();
        return playerAction;
    }

    /* HELPER METHODS */
    double supernoveDistance(){
        // Mengembalikan jarak supernova ke bot, -99 bila pickup supernova tidak ada
        if (getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).isEmpty()){
            return -99;
        } else {
            return getDistanceToBot(getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).get(0));
        }
    }

    /*PICKUP SUPERNOVA */

    private PlayerAction pickUpSupernova(){
        //Mengembalikan aksi FORWARD dengan heading mengarah ke supernova pick up
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.FORWARD;
        playerAction.heading = getHeadingBetween(getGameObjectsByType(ObjectTypes.SUPERNOVAPICKUP).get(0));
        return playerAction;
    }

}

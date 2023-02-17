package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Teleport extends BotState {

    /* ABSTRACT METHODS */
    public float calculatePriorityScore(){
        if(BotState.teleporterFired == false) {
            // no teleporter fired
            //System.out.print("Teleport Priority Score: " + 0 + " #no tele fired " + BotState.teleporterAngle);
            return 0;
        }
        else {
            return decideTeleportScore();
        } 
    }

    public PlayerAction calculatePlayerAction(){
        // teleport
        return teleport();
    }

    /* HELPER METHODS */
    private int decideTeleportScore(){
        // check if bot's teleporter is still alive
        List<GameObject> myTeleporter = 
            getGameObjectsByType(ObjectTypes.TELEPORTER).stream().filter(item -> Math.abs((item.getCurrHeading() - BotState.teleporterAngle) % 360) <= 15).collect(Collectors.toList());
        if (myTeleporter.size() == 0) {
            // teleporter is dead
            //System.out.print("Teleport Priority Score: " + 0 + " #dead " + BotState.teleporterAngle);
            //BotState.teleporterFired = false;
            return 0;
        } else {
            List<GameObject> targetAroundTeleporter = getPlayersAtArea(myTeleporter.get(0).position, bot.getSize() + 100).stream().
                filter(e -> e.getId() != bot.getId()).collect(Collectors.toList());
            if (targetAroundTeleporter.size() == 0) {
                // teleporter is alive but no target around
                //System.out.print("Teleport Priority Score: " + 0 +" #no target ");
                return 0;
            } else {
                for(int i = 0; i < targetAroundTeleporter.size(); i++){
                    if (targetAroundTeleporter.get(i).getSize() >= bot.getSize()) {
                        // target is bigger than bot = DANGER, don't teleport
                        //System.out.print("Teleport Priority Score: " + 0 + " #target bigger ");
                        return 0;
                    }
                }

                for(int i = 0; i < targetAroundTeleporter.size(); i++){
                    if (getDistanceToBot(targetAroundTeleporter.get(i)) <= bot.getSize() + 100) {
                        //System.out.print("Teleport Priority Score: " + 900);
                        return 900;
                    }
                }
                System.out.print("Teleport Priority Score: " + 0);
                return 0;
            }
        }
    }

    private PlayerAction teleport(){
        PlayerAction playerAction = new PlayerAction();

        // set action to teleport
        playerAction.action = PlayerActions.TELEPORT;
        playerAction.heading = bot.getCurrHeading();

        // reset teleporter
        BotState.teleporterAngle = -1;
        BotState.teleporterFired = false;

        return playerAction;
    }
}
package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.Collectors;

public class Teleport extends BotState {

    /* ABSTRACT METHODS */
    public float calculatePriorityScore(){
        if(bot.teleporterAngle == -1) return 0;
        else return decideTeleport();
    }
    public PlayerAction calculatePlayerAction(){
        if(bot.getTeleporterCount() != 0) return teleport();
        else{
            PlayerAction playerAction = new PlayerAction();
            playerAction.action = PlayerActions.STOP;
            playerAction.heading = 0;
            return playerAction;
        }
    }

    /* HELPER METHODS */
    private int decideTeleport(){
        int angle = bot.getTeleporterAngle();
        if(angle == -1) return 0;
        else{
            List<GameObject> myTeleporter = gameState.getGameObjects().stream().filter(item -> item.getGameObjectType() == ObjectTypes.TELEPORTER && item.getCurrHeading() == angle).collect(Collectors.toList());
            if(myTeleporter.size() == 0) return 0;
            else{
                List<GameObject> targetAroundTeleporter = getGameObjectsAtArea(myTeleporter.get(0).position, bot.getSize());
                if(targetAroundTeleporter.size() == 0) return 0;
                else{
                    for(int i = 0; i < targetAroundTeleporter.size(); i++){
                        if(targetAroundTeleporter.get(i).getSize() >= bot.getSize()) return 0;
                    }
                    return 300;
                }
            }
        }
    }

    private PlayerAction teleport(){
        PlayerAction playerAction = new PlayerAction();
        playerAction.action = PlayerActions.TELEPORT;
        bot.setTeleporterAngle(-1);
        return playerAction;
    }
}

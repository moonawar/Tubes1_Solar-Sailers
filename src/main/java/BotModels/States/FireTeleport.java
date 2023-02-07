package BotModels.States;

import BotModels.*;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class FireTeleport extends BotState{
    /* ABSTRACT METHOD */
    public float calculatePriorityScore(){}
    public PlayerAction calculatePlayerAction(){}
    
}

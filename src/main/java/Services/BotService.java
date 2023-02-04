package Services;

import BotModels.BotBrain;

// import Enums.*;
import Models.*;

import java.util.*;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
        BotBrain.botService = this;
    }

    public GameObject getBot() {
        return this.bot;
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public void computeNextPlayerAction(PlayerAction playerAction) {
        // playerAction.action = PlayerActions.FORWARD;
        // playerAction.heading = new Random().nextInt(360);

        // if (!gameState.getGameObjects().isEmpty()) {
        //     var foodList = gameState.getGameObjects()
        //             .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD)
        //             .sorted(Comparator
        //                     .comparing(item -> getDistanceBetween(bot, item)))
        //             .collect(Collectors.toList());

        //     playerAction.heading = getHeadingBetween(foodList.get(0));
        // }

        // this.playerAction = playerAction;
        this.playerAction = BotBrain.GetBotAction();
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }

    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

}

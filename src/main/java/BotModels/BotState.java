package BotModels;

import java.util.List;
import java.util.stream.Collectors;

import Enums.*;
import Models.*;

public abstract class BotState {
    public static GameState gameState;
    public static GameObject bot;
    protected static boolean teleporterFired = false;
    protected static int teleporterAngle = -1;
    protected static boolean supernovaFired = false;

    /* ABSTRACT METHOD (MUST BE IMPLEMENTED BY EACH STATE) */
    abstract public float calculatePriorityScore();
    abstract public PlayerAction calculatePlayerAction();

    /* ACCESSOR */
    public float GetPriorityScore() {
        return this.calculatePriorityScore();
    }

    public PlayerAction GetPlayerAction() {
        return this.calculatePlayerAction();
    }

    /* SOME UTILITY FUNCTION */
    protected double getDistance(Position position1, Position position2) {
        // Mengembalikan jarak antara dua posisi
        var triangleX = Math.abs(position1.x - position2.x);
        var triangleY = Math.abs(position1.y - position2.y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    protected double getDistanceBetween(GameObject object1, GameObject object2) {
        // Mengembalikan jarak antara dua game object
        return getDistance(object1.getPosition(), object2.getPosition());
    }

    protected double getDistanceToBot(GameObject object) {
        // Mengembalikan jarak antara bot dan game object lain
        return getDistance(bot.getPosition(), object.getPosition());
    }

    protected int getHeading(Position position) {
        // Mengembalikan heading antara bot dengan posisi lain
        var direction = toDegrees(Math.atan2(position.y - bot.getPosition().y, position.x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    protected int getHeading2(Position position1, Position position2) {
        // Mengembalikan heading dari posisi 1 ke posisi 2
        var direction = toDegrees(Math.atan2(position2.y - position1.y, position2.x - position1.x));
        return (direction + 360) % 360;
    }
    
    protected int getHeadingBetween(GameObject otherObject) {
        // Mengembalikan heading antara bot dan game object lain
        return getHeading(otherObject.getPosition());
    }

    protected int toDegrees(double v) {
        // Mengubah radian ke derajat
        return (int) (v * (180 / Math.PI));
    }

    protected List<GameObject> getGameObjectsByType(ObjectTypes type) {
        // Mengembalikan list game object berdasarkan tipe
        return gameState.getGameObjects().stream().filter(item -> item.getGameObjectType() == type).collect(Collectors.toList());
    }

    protected List<GameObject> getPlayers(ObjectTypes type) {
        // Mengembalikan list game object berdasarkan tipe
        return gameState.getPlayerGameObjects().stream().collect(Collectors.toList());
    }

    protected List<GameObject> getGameObjectsByType(List<GameObject> gameObjects, ObjectTypes type) {
        // Mengembalikan list game object berdasarkan tipe dari list game object
        return gameObjects.stream().filter(item -> item.getGameObjectType() == type).collect(Collectors.toList());
    }

    protected List<GameObject> getGameObjectsAtArea(Position position, int radius) {
        // Mengembalikan list game object yang berada di area radius dari posisi tertentu
        return gameState.getGameObjects().stream()
                .filter(item -> getDistance(position, item.getPosition()) <= radius).collect(Collectors.toList());
    }
    protected List<GameObject> getPlayersAtArea(Position position, int radius) {
        // Mengembalikan list game object yang berada di area radius dari posisi tertentu
        return gameState.getPlayerGameObjects().stream()
                .filter(item -> getDistance(position, item.getPosition()) <= radius + item.getSize() + bot.getSize() && item.getId() != bot.getId()).collect(Collectors.toList());
    }

    protected List<GameObject> getGameObjectsAtBotArea(int radius) {
        // Mengembalikan list game object yang berada di area radius dari posisi bot
        return getGameObjectsAtArea(bot.getPosition(), radius);
    }

    protected List<GameObject> getPlayersAtBotArea(int radius) {
        // Mengembalikan list game object yang berada di area radius dari posisi bot
        return getPlayersAtArea(bot.getPosition(), radius);
    }

    protected boolean isObjectHeadingTo(GameObject obj, Position position, int degreeTolerance) {
        // Mengembalikan true jika objek tertentu mengarah ke posisi tertentu, dengan toleransi +- distanceTolerance dari posisi
        int objHeading = obj.currentHeading;

        return Math.abs((objHeading - getHeading2(obj.position, position)) % 360) <= degreeTolerance;
    }

    protected boolean isObjectHeadingToBot(GameObject obj, int degreeTolerance) {
        // Mengembalikan true jika heading bot ke game object tertentu
        return isObjectHeadingTo(obj, bot.getPosition(), degreeTolerance);
    }
}

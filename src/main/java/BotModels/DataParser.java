package BotModels;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataParser {
    private JSONObject getData(){
        /* Get the bot state data from data.json */
        JSONParser parser = new JSONParser();

        /* Initialize the JSONObject data */
        JSONObject obj = null;

        /* Read the data from data.json */
        try (FileReader reader = new FileReader("./data.json")) {
            obj = (JSONObject) parser.parse(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return obj;
    }

    public JSONObject getDefendData(){
        /* Get data for defend state */
        JSONObject obj = getData();
        JSONObject defendData = (JSONObject) obj.get("Defend");
        return defendData;
    }

    public JSONObject getGatherFoodData(){
        /* Get data for gather food state */
        JSONObject obj = getData();
        JSONObject gatherFoodData = (JSONObject) obj.get("GatherFood");
        return gatherFoodData;
    }

    public JSONObject getFireTeleportData(){
        /* Get data for fire teleport state */
        JSONObject obj = getData();
        JSONObject fireTeleportData = (JSONObject) obj.get("FireTeleport");
        return fireTeleportData;
    }

    public JSONObject getTeleportData(){
        /* Get data for teleport state */
        JSONObject obj = getData();
        JSONObject teleportData = (JSONObject) obj.get("Teleport");
        return teleportData;
    }

    public JSONObject getTorpedoAttackData(){
        /* Get data for torpedo attack state */
        JSONObject obj = getData();
        JSONObject torpedoAttackData = (JSONObject) obj.get("TorpedoAttack");
        return torpedoAttackData;
    }
}

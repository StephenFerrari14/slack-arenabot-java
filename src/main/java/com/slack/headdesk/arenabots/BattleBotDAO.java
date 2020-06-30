package com.slack.headdesk.arenabots;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class BattleBotDAO {
    public String user;

    public BattleBotDAO(String user) {
        this.user = user;
        this.init();
    }

    private String getFilename() {
        return String.format("%s-profile.json", this.user);
    }

    private String getRequestFilename() { return String.format("%s-requests.txt", this.user); }

    private void init() {
        try {
            String filename = this.getFilename();
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                // fill out file
                JSONObject json = new JSONObject();
                json.put("wins", 0);
                json.put("loses", 0);
                json.put("bots", new JSONArray());
                FileWriter writer = new FileWriter(filename);
                writer.write(json.toJSONString());
                writer.close();
            } else {
                System.out.println("File already exists.");
                // Maybe add some corrupt file validation
            }

            String requestFilename = this.getRequestFilename();
            File requestFile = new File(requestFilename);
            requestFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addBot(String name) {
        BattleBot newBot = new BattleBot(this.user, name);
        // Save to json
        JSONObject jsonBot = new JSONObject();
        jsonBot.put("name", newBot.name);
        jsonBot.put("attack", newBot.attack);
        jsonBot.put("health", newBot.health);
        jsonBot.put("level", newBot.level);
        jsonBot.put("xp", newBot.xp);
        JSONArray jsonBots = new JSONArray();
        jsonBots.add(jsonBot);

        try (FileReader reader = new FileReader(this.getFilename()))
        {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject userProfile = (JSONObject) obj;
            userProfile.put("bots", jsonBots);
            FileWriter writer = new FileWriter(this.getFilename());
            writer.write(userProfile.toJSONString());
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    void saveBot(BattleBot bot) {
        JSONParser jsonParser = new JSONParser();
        JSONObject userProfile = new JSONObject();
        try (FileReader reader = new FileReader(this.getFilename());) {
            Object obj = jsonParser.parse(reader);
            userProfile = (JSONObject) obj;
            JSONArray bots = (JSONArray) userProfile.get("bots");
            JSONObject jsonBot = (JSONObject) bots.get(0);
            jsonBot.put("attack", bot.attack);
            jsonBot.put("health", bot.health);
            jsonBot.put("level", bot.level);
            jsonBot.put("xp", bot.xp);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(this.getFilename(), false)) {
            writer.write(userProfile.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getBot() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(this.getFilename()))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject userProfile = (JSONObject) obj;
            JSONArray bots = (JSONArray)userProfile.get("bots");
            return (JSONObject) bots.get(0);
//            BattleBot bot = new BattleBot(this.user, "loading");
//            int xp = ((Long) jsonBot.get("xp")).intValue();
//            bot.load(this.user, (String) jsonBot.get("name"),
//                    ((Long) jsonBot.get("attack")).intValue(),
//                    ((Long) jsonBot.get("health")).intValue(),
//                    this.getLevelFromXp(xp),
//                    ((Long) jsonBot.get("xp")).intValue());
//            reader.close();
//            return bot;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // Do something since bot doesn't exist
        }
        JSONObject noBot = new JSONObject();
        noBot.put("owner", this.user);
        noBot.put("name", "no bot found");
//        return new BattleBot(this.user, "no bot found");
        return noBot;
    }

    public void deleteBot() {
        JSONArray jsonBots = new JSONArray();

        try (FileReader reader = new FileReader(this.getFilename()))
        {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject userProfile = (JSONObject) obj;
            userProfile.put("bots", jsonBots);
            FileWriter writer = new FileWriter(this.getFilename());
            writer.write(userProfile.toJSONString());
            writer.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // Move to user dao
    public UserProfile getProfile(){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(this.getFilename()))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject jsonProfile = (JSONObject) obj;
            UserProfile profile = new UserProfile();
            profile.name = String.format("<@%s>", this.user);
            profile.wins = ((Long)jsonProfile.get("wins")).intValue();
            profile.loses = ((Long)jsonProfile.get("loses")).intValue();
            reader.close();
            return profile;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // Do something since bot doesn't exist
        }
        return new UserProfile();
    }

    public boolean hasBot() {
        JSONObject bot = this.getBot();
        return !bot.get("name").toString().equals("no bot found"); // Make better api for this case
    }

    // Move this to another dao
    public void saveBattleRequest(String challengee) {
        try {
            // Change back to FileWriter before release, dont actually need BufferedWriter for this and its slower
            String challengeeName = String.format("%s\n", challengee);
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(this.getRequestFilename(), true));
            out.write(challengeeName);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasBattleRequest(String challengee) {
        boolean foundBattleRequest = false;
        try {
            // Change back to FileWriter before release, dont actually need BufferedWriter for this and its slower
            BufferedReader breader = new BufferedReader(new FileReader(this.getRequestFilename()));
            String line = breader.readLine();
            while (line != null) {
                if (line.equals(challengee)) {
                    foundBattleRequest = true;
                }
                line = breader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundBattleRequest;
    }

    public void removeBattleRequest(String challengee) {
        try {
            String requests = new String(Files.readAllBytes(Paths.get(this.getRequestFilename())));
            requests = requests.replace(String.format("%s\n", challengee), "");

            PrintWriter writer = new PrintWriter(this.getRequestFilename());
            writer.print("");
            writer.print(requests);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



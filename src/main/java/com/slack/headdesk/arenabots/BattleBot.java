package com.slack.headdesk.arenabots;

import org.json.simple.JSONObject;

public class BattleBot {
    public int id;
    public String name;
    public int attack;
    public int health;
    public int level;
    public int xp;
    public String owner;
    private BattleBotDAO dao;

    public BattleBot(String owner, String name) {
        this.name = name;
        this.owner = owner;
        this.attack = (int) (Math.random() * 10) + 1;
        this.health = (int) (Math.random() * 10) + 1;
        this.level = 1;
        this.xp = 0;
        this.dao = new BattleBotDAO(owner);
    }

    private void save() {
        this.dao.saveBot(this);
    }

    public void delete() {
        this.dao.deleteBot(); // Add bot name
        // this.dao.deleteBot(this.name)
    }

//    void load(String owner, String name, int attack, int health, int level, int xp) {
//        this.owner = owner;
//        this.name = name;
//        this.attack = attack;
//        this.health = health;
//        this.level = level;
//        this.xp = xp;
//    }

    public void assemble() {
        // Change dao to return bot object
        JSONObject result = this.dao.getBot();
        this.owner = result.get("owner").toString();
        this.name = result.get("name").toString();
        this.attack = ((Long) result.get("attack")).intValue();
        this.health = ((Long) result.get("health")).intValue();
        this.level = this.getLevelFromXp(((Long) result.get("level")).intValue());
        this.xp = ((Long) result.get("xp")).intValue();
    }

    void addXP(int xp) {
        this.xp += xp;
        this.save();
    }

    private int getLevelFromXp(int xp) {
        if (xp == 0) {
            return 1;
        }
        return (int) Math.floor(Math.sqrt(xp));
    }

    public boolean isAssembled() {
        return this.dao.hasBot();
    }
}

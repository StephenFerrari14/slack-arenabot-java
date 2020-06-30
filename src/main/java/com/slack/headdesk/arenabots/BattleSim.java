package com.slack.headdesk.arenabots;

public class BattleSim implements Runnable {
    private BattleBot fighterOne;
    private BattleBot fighterTwo;
    private SlackMessager messenger;


    public BattleSim(BattleBot challengeeBot, BattleBot challengerBot) {
        this.fighterOne = challengerBot;
        this.fighterTwo = challengeeBot;
        this.messenger = new SlackMessager();
    }

    @Override
    public void run() {
        // Entering arena!
        // bot1 win rate = bot1 hp + atk / bot1 hp + atk + bot2 hp + atk
        int statTotalOne = this.fighterOne.attack + this.fighterOne.health;
        int statTotalTwo = this.fighterTwo.attack + this.fighterTwo.health;
        int statTotal = statTotalOne + statTotalTwo;
        double battleChance = (Math.random() * ((statTotal - 1) + 1)) + 1;
        String winner = "No one";
        if (battleChance <= statTotalOne) {
            // Fighter 1 wins!
            winner = this.fighterOne.name;
            this.fighterOne.addXP(1);
        } else {
            // Fighter 2 wins!
            winner = this.fighterTwo.name;
            this.fighterTwo.addXP(1);
        }
        String winMessage = String.format("The winner is %s!", winner);
        this.messenger.sendMessage(winMessage, this.fighterOne.owner);
        this.messenger.sendMessage(winMessage, this.fighterTwo.owner);
    }
}

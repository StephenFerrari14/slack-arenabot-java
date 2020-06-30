package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBot;
import com.slack.headdesk.arenabots.BattleBotDAO;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BattleChallengeRule extends BaseRule {
    public BattleChallengeRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("challenge (.+)");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            this.ruleCaught = true;
            String challengee = crMatcher.group(1).replace("<", "").replace(">", "").replace("@", "");
            System.out.println(challengee);
            // Check if challengee is yourself
            if (challengee.equals(this.entity.event.user)) {
                this.messenger.sendMessage("You can't challenge yourself.", this.entity.event.user);
            }

            // Check you and challengee have bots
            BattleBot bot = new BattleBot(this.entity.event.user, "loading");
            bot.assemble();
            if (!bot.isAssembled()) {
                this.messenger.sendMessage("You need a bot before you can challenge someone.", this.entity.event.user);
                return;
            }

            BattleBot enemyBot = new BattleBot(challengee, "loading");
            enemyBot.assemble();
            if (!enemyBot.isAssembled()) {
                this.messenger.sendMessage("Your opponent needs a bot before they can be challenged.", this.entity.event.user);
                return;
            }

            BattleBotDAO dao = new BattleBotDAO(this.entity.event.user);
            dao.saveBattleRequest(challengee);
            // Send message to challengee
            this.messenger.sendMessage(String.format("<@%s> has challenged you to a battle. Please respond with `accept challenge from <@%s>`.", this.entity.event.user, this.entity.event.user), challengee);
        }
    }
}

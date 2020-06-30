package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBot;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetRobotRule extends BaseRule {
    public GetRobotRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("show my robot|get my robot");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            // Check if user has robot
            BattleBot bot = new BattleBot(this.entity.event.user, "loading");
            bot.assemble();
            if (bot.name.equals("no bot found")) { // Probably use exception handling instead of this
                this.messenger.sendMessage("Bot not found. Try again or create a new one.", this.entity.event.user);
            } else {
                this.messenger.sendMessage(String.format("*%s*\nLevel: %s\nAttack: %s\nHealth: %s\nXP: %s", bot.name, bot.level, bot.attack, bot.health, bot.xp), this.entity.event.user);
            }
            this.ruleCaught = true;
        }
    }
}

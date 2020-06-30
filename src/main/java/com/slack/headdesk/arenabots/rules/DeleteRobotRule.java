package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBot;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteRobotRule extends BaseRule {
    public DeleteRobotRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("delete my robot");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            // Check if user has robot
            BattleBot bot = new BattleBot(this.entity.event.user, "loading");
            bot.assemble();
            bot.delete();
            this.messenger.sendMessage("Bot deleted. Create a new bot with the command `create robot <name>`", this.entity.event.user);
            this.ruleCaught = true;
        }
    }
}

package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBot;
import com.slack.headdesk.arenabots.BattleBotDAO;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateRobotRule extends BaseRule {
    public CreateRobotRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("create robot (.*)");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            String name = crMatcher.group(1);
            // Check if user has robot
            BattleBot bot = new BattleBot(this.entity.event.user, "loading");
            bot.assemble();
            if (bot.name.equals("no bot found")) { // Probably use exception handling instead of this
                BattleBotDAO dao = new BattleBotDAO(this.entity.event.user);
                dao.addBot(name);
                this.messenger.sendMessage(String.format("New bot %s created!", name), this.entity.event.user);
                this.ruleCaught = true;
                return;
            }
            this.messenger.sendMessage("Bot already exists, delete existing bot before trying to create a new one.", this.entity.event.user);
            this.ruleCaught = true;
        }
    }
}

package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBot;
import com.slack.headdesk.arenabots.BattleBotDAO;
import com.slack.headdesk.arenabots.BattleSim;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcceptChallengeRule extends BaseRule {
    public AcceptChallengeRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("accept challenge from (.+)");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            this.ruleCaught = true;
            String challenger = crMatcher.group(1).replace("<", "").replace(">", "").replace("@", "");
            // Check if challenger is yourself
            if (challenger.equals(this.entity.event.user)) {
                this.messenger.sendMessage("You can't challenge yourself.", this.entity.event.user);
            }

            BattleBot bot = new BattleBot(this.entity.event.user, "loading");
            bot.assemble();

            if (!bot.isAssembled()) {
                this.messenger.sendMessage("You need a bot before you can challenge someone.", this.entity.event.user);
                return;
            }

            BattleBot enemyBot = new BattleBot(challenger, "loading");
            enemyBot.assemble();

            if (!enemyBot.isAssembled()) {
                this.messenger.sendMessage("Your opponent needs a bot before they can be challenged.", this.entity.event.user);
                return;
            }

            BattleBotDAO requestDao = new BattleBotDAO(challenger);
            requestDao.removeBattleRequest(this.entity.event.user);

            // Start thread for battle sim
            Thread battleThread = new Thread(new BattleSim(bot, enemyBot));
            battleThread.start();
        }
    }
}

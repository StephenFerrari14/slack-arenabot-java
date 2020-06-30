package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.entities.SlackEntity;

public class SelfRule extends BaseRule {
    private String botId;

    public SelfRule(SlackEntity entity) {
        super(entity);
        this.botId = System.getenv("BOT_ID");
    }

    @Override
    public void run() {
        if (this.entity.event.user.equals(this.botId)) { // Get this from api call?
            this.ruleCaught = true;
        }
    }
}

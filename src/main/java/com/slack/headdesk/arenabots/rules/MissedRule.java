package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.entities.SlackEntity;

public class MissedRule extends BaseRule {
    public MissedRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        this.messenger.sendMessage("I do not recognize that command.", this.entity.event.user);
    }
}

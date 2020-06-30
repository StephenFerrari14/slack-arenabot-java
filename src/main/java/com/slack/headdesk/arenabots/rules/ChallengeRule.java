package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.entities.SlackEntity;

public class ChallengeRule extends BaseRule {
    public ChallengeRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        if (this.entity.challenge != null) {
            this.ruleCaught = true;
            this.result = "{\"challenge\": \"" + this.entity.challenge + "\"}";
        }
    }
}

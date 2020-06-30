package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.SlackMessager;
import com.slack.headdesk.arenabots.entities.SlackEntity;

public class BaseRule implements RulesInterface {
    public SlackEntity entity;
    public Boolean ruleCaught = false;
    public String result = "";
    SlackMessager messenger;

    BaseRule(SlackEntity entity) {
        this.entity = entity;
        this.messenger = new SlackMessager();
    }

    @Override
    public void run() {
    }
}

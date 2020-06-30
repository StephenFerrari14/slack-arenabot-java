package com.slack.headdesk.arenabots;

import com.slack.headdesk.arenabots.entities.SlackEntity;
import com.slack.headdesk.arenabots.rules.*;

import java.util.ArrayList;

/**
 * First idea at a rule based slack framework
 * Think it might be better to make an interface like hubot
 */
class SlackMessageParser {
    private SlackEntity entity;

    SlackMessageParser(SlackEntity entity) {
        this.entity = entity;
    }

    String parse() {
        // It works so move these out at some point to a configuration
        ArrayList<BaseRule> rules = new ArrayList<BaseRule>();
        rules.add(new ChallengeRule(this.entity));
        rules.add(new SelfRule(this.entity));
        rules.add(new PingRule(this.entity));
        rules.add(new HelpRule(this.entity));
        rules.add(new CreateRobotRule(this.entity));
        rules.add(new DeleteRobotRule(this.entity));
        rules.add(new GetProfileRule(this.entity));
        rules.add(new GetRobotRule(this.entity));
        rules.add(new BattleChallengeRule(this.entity));
        rules.add(new AcceptChallengeRule(this.entity));
        rules.add(new MissedRule(this.entity));
        for (BaseRule rule : rules) {
            System.out.println(String.format("Running rule: %s", rule.getClass().getName()));
            rule.run();
            if (rule.ruleCaught) {
                System.out.println(String.format("Rule caught for: %s", rule.getClass().getName()));
                if (!rule.result.equals("")) {
                    return rule.result;
                }
                return String.format("Rule caught for: %s", rule.getClass().getName());
            }
        }
        return "I do not know how to do that. Please try again.";
    }
}

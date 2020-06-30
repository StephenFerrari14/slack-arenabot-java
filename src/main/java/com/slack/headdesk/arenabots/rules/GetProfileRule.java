package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.BattleBotDAO;
import com.slack.headdesk.arenabots.UserProfile;
import com.slack.headdesk.arenabots.entities.SlackEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetProfileRule extends BaseRule {
    public GetProfileRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        Pattern crPattern = Pattern.compile("show my profile");
        Matcher crMatcher = crPattern.matcher(this.entity.event.text);
        boolean matches = crMatcher.matches();
        if (matches) {
            // Check if user has robot
            BattleBotDAO dao = new BattleBotDAO(this.entity.event.user);
            UserProfile profile = dao.getProfile();
            this.messenger.sendMessage(String.format("%s\nWins: %s\nLoses: %s", profile.name, profile.wins, profile.loses), this.entity.event.user);
            this.ruleCaught = true;
        }
    }
}

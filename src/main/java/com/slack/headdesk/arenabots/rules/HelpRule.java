package com.slack.headdesk.arenabots.rules;

import com.slack.headdesk.arenabots.entities.SlackEntity;

/**
 * Rule to return a help message to a user in slack
 *
 * Thinking about how to do it like hubot where it gets the help text from doc comments in files
 * Makes it so this can be more easily added to the framework rules
 */
public class HelpRule extends BaseRule {
    public HelpRule(SlackEntity entity) {
        super(entity);
    }

    @Override
    public void run() {
        System.out.println(this.entity.event);
        if (this.entity.event.text.toLowerCase().equals("help")) {
            String newLine = System.getProperty("line.separator");
            String message = "Hey bot battler, I am the hub for entering into the Battle Bot Arena." + newLine +
                    "Just send me a message of what you want to do while in the arena." + newLine + newLine +
                    "Here are the commands I offer:" + newLine +
                    "`show my profile`" + newLine +
                    "`show my robot`" + newLine +
                    "`delete my robot`" + newLine +
                    "`create robot <name of robot>`" + newLine +
                    "`challenge @user`"
                    ;
            this.messenger.sendMessage(message, this.entity.event.user);
            this.ruleCaught = true;
        }
    }
}

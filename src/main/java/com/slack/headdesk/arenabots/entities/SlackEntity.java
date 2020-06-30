package com.slack.headdesk.arenabots.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slack.headdesk.arenabots.entities.SlackEventEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackEntity {
    @JsonProperty
    public String challenge;
    @JsonProperty
    public SlackEventEntity event;
}
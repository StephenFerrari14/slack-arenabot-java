package com.slack.headdesk.arenabots.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackEventEntity {
    @JsonProperty
    public String user;
    @JsonProperty
    public String text;
}
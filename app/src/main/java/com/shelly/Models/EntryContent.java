package com.shelly.Models;

import java.util.List;

public class EntryContent {
    private StringBuilder Text;
    private List<String> Tags;

    public EntryContent() {

    }

    public EntryContent(StringBuilder text, List<String> tags) {
        Text = text;
        Tags = tags;
    }

    public StringBuilder getText() {
        return Text;
    }

    public void setText(StringBuilder text) {
        Text = text;
    }

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    @Override
    public String toString() {
        return "EntryContent{" +
                "Text=" + Text +
                ", Tags=" + Tags +
                '}';
    }
}

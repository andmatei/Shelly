package com.shelly.Models;

import java.util.List;

public class EntryContent {
    private StringBuffer Text;
    private List<String> Tags;

    public EntryContent() {

    }

    public EntryContent(StringBuffer text, List<String> tags) {
        Text = text;
        Tags = tags;
    }

    public StringBuffer getText() {
        return Text;
    }

    public void setText(StringBuffer text) {
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

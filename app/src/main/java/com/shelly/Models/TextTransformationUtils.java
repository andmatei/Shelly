package com.shelly.Models;

import android.graphics.Typeface;

public class TextTransformationUtils {
    private String FieldValue;
    private String AssignedTag;
    private Typeface TextFont;
    private boolean Selected;

    public TextTransformationUtils() {}

    public TextTransformationUtils(String fieldValue, String assignedTag, Typeface textFont, boolean selected) {
        FieldValue = fieldValue;
        AssignedTag = assignedTag;
        TextFont = textFont;
        Selected = selected;
    }

    public String getFieldValue() {
        return FieldValue;
    }

    public void setFieldValue(String fieldValue) {
        FieldValue = fieldValue;
    }

    public String getAssignedTag() {
        return AssignedTag;
    }

    public void setAssignedTag(String assignedTag) {
        AssignedTag = assignedTag;
    }

    public Typeface getTextFont() {
        return TextFont;
    }

    public void setTextFont(Typeface textFont) {
        TextFont = textFont;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}

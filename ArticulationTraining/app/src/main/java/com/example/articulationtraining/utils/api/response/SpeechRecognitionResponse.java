package com.example.articulationtraining.utils.api.response;

import com.google.gson.annotations.SerializedName;

public class SpeechRecognitionResponse {

    @SerializedName("RecognitionStatus")
    private String recognitionStatus;

    @SerializedName("Offset")
    private long offset;

    @SerializedName("Duration")
    private long duration;

    @SerializedName("DisplayText")
    private String displayText;

    // Getters and Setters
    public String getRecognitionStatus() {
        return recognitionStatus;
    }

    public void setRecognitionStatus(String recognitionStatus) {
        this.recognitionStatus = recognitionStatus;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

}

package com.dnkilic.seslihaber.data;

public class Radio {

    private String channelName;
    private String stream;

    public Radio(String channelName, String stream) {
        this.channelName = channelName;
        this.stream = stream;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}

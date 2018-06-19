package com.lingsatuo.view;

public class LrcLineBean {
    private String lrc;
    private long start;
    private long end;

    public LrcLineBean() {
    }

    public LrcLineBean(String text, long start, long end) {
        this.lrc = text;
        this.start = start;
        this.end = end;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}

package com.jew.coree.treader.util;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class TRPage {
    private long begin;
    private long end;
    private int index;
    private List<String> lines;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
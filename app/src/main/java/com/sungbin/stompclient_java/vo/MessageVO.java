package com.sungbin.stompclient_java.vo;

public class MessageVO {
    String name;
    String content;
    String time;


    int type;

    public MessageVO(String name, String content, String time) {
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {     // 메세지 자신 or 다른사람 구분 위함
        this.type = type;
    }
}

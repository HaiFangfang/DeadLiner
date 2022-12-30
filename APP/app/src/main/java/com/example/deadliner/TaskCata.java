package com.example.deadliner;

public class TaskCata {
    private String id;
    private String cataName;
    private int taskNum;
    private int openNum;
    public void setCataName(String name){
        this.cataName=name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public void setOpenNum(int openNum) {
        this.openNum = openNum;
    }

    public String getId() {
        return id;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public String getCataName() {
        return cataName;
    }

    public int getOpenNum() {
        return openNum;
    }
}

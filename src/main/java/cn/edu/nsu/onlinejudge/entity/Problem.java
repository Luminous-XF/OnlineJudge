package cn.edu.nsu.onlinejudge.entity;

import java.util.Date;

public class Problem {

    private int problemId;

    private int problemNumber;

    private String problemTitle;

    private String problemComment;

    private String input;

    private String output;

    private int spj;

    private String note;

    private int difficulty;

    private int author;

    private int memoryLimit;

    private int timeLimit;

    private String source;

    private int submit;

    private int accepted;

    private Date createTime;

    private int status;


    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber = problemNumber;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getProblemComment() {
        return problemComment;
    }

    public void setProblemComment(String problemComment) {
        this.problemComment = problemComment;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getSpj() {
        return spj;
    }

    public void setSpj(int spj) {
        this.spj = spj;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "problemId=" + problemId +
                ", problemNumber=" + problemNumber +
                ", problemTitle='" + problemTitle + '\'' +
                ", problemComment='" + problemComment + '\'' +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                ", spj=" + spj +
                ", note='" + note + '\'' +
                ", difficulty=" + difficulty +
                ", author=" + author +
                ", memoryLimit=" + memoryLimit +
                ", timeLimit=" + timeLimit +
                ", source='" + source + '\'' +
                ", submit=" + submit +
                ", accepted=" + accepted +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}

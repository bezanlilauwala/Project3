package ssl.pms;

import java.util.Date;

public class Task {

    int taskID;
    int deliverableID;
    int expectedEffort;
    int effortCompleted;
    int actualEffort;
    int percentComplete;
    int type;
    String name;
    String description;
    long expectedDuration;
    long actualDuration;
    Date expectedStartDate;
    Date expectedEndDate;
    Date actualStartDate;
    Date actualEndDate;

    public Task() {
        taskID = 0;
        deliverableID = 0;
        expectedDuration = 0;
        expectedEffort = 0;
        actualDuration = 0;
        effortCompleted = 0;
        actualEffort = 0;
        percentComplete = 0;
        type = 0;
        name = "";
        description = "";
    }

    public Task(int taskID, int deliverableID, String name, String description, java.sql.Date expectedStartDate,
                java.sql.Date expectedEndDate, long expectedDuration, int expectedEffort, java.sql.Date actualStartDate,
                java.sql.Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                int percentComplete, int type) {
        this.taskID = taskID;
        this.deliverableID = deliverableID;
        this.name = name;
        this.description = description;
        this.expectedStartDate = expectedStartDate;
        this.expectedEndDate = expectedEndDate;
        this.expectedDuration = expectedDuration;
        this.expectedEffort = expectedEffort;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
        this.actualDuration = actualDuration;
        this.effortCompleted = effortCompleted;
        this.actualEffort = actualEffort;
        this.percentComplete = percentComplete;
        this.type = type;
    }

    public int getTaskID() { return taskID; }

    public int getDeliverableID() {
        return deliverableID;
    }

    public Date getExpectedStartDate() {
        return expectedStartDate;
    }

    public long getExpectedDuration() {
        return expectedDuration;
    }

    public int getExpectedEffort() {
        return expectedEffort;
    }

    public Date getActualStartDate() {
        return actualStartDate;
    }

    public Date getActualEndDate() {
        return actualEndDate;
    }

    public long getActualDuration() {
        return actualDuration;
    }

    public int getEffortCompleted() {
        return effortCompleted;
    }

    public int getActualEffort() {
        return actualEffort;
    }

    public int getPercentComplete() {
        return percentComplete;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public void setActualDuration(long actualDuration) {
        this.actualDuration = actualDuration;
    }

    public void setActualEffort(int actualEffort) {
        this.actualEffort = actualEffort;
    }

    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public void setDeliverableID(int deliverableID) {
        this.deliverableID = deliverableID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEffortCompleted(int effortCompleted) {
        this.effortCompleted = effortCompleted;
    }

    public void setExpectedDuration(long expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public void setExpectedEffort(int expectedEffort) {
        this.expectedEffort = expectedEffort;
    }

    public void setExpectedStartDate(Date expectedStartDate) {
        this.expectedStartDate = expectedStartDate;
    }

    public void setExpectedEndDate(Date expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    public void setType(int type) {
        this.type = type;
    }
}

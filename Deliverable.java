package ssl.pms;

import java.util.Date;

public class Deliverable {

    private int deliverableID;
    private String name, description;
    private Date dueDate;

    public Deliverable() {
        deliverableID = 0;
        name = "";
        description = "";
    }

    public Deliverable(int deliverableID, String name, String description, Date dueDate) {
        this.deliverableID = deliverableID;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void print() {
        System.out.println(deliverableID + "\t" +
                            name + "\t" +
                            description + "\t" +
                            dueDate);
    }

    public int getDeliverableID() {
        return deliverableID;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDeliverableID(int deliverableID) {
        this.deliverableID = deliverableID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }


}

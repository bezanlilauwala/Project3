package ssl.pms;

import java.sql.*;
import java.util.Vector;

public class Project {
    int projectID;
    String name, description, url;
    Connection conn;            //Connection object is used to made a connection to the database in the file system

    Vector<Deliverable> deliverableVector;
    Vector<Task> taskVector;
    Vector<Issue> issueVector;
    Vector<ActionItem> actionItemVector;
    Vector<Resource> resourceVector;
    Vector<Requirement> requirementVector;

    public Project(int projectID, String name, String description) {
        this.projectID = projectID;
        this.name = name;
        this.description = description;
        url = "jdbc:sqlite:C:/Users/bezan/Documents/SQLiteJDBC/PMS/" + name + ".db";     //Todo: Change to Server URL
        BuildTables.createTables(getConnectionToProjectDB());      //Create database tables and add them to the database at the above url
    }

    int getProjectID() {return projectID;}
    String getName() {return name;}
    String getDescription() {return description;}

    public Connection getConnectionToProjectDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        return conn;
    }

    //Closes the connection to the database that was opened when connectAuthentication() was called.
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Returns the primary key of the most recently added row in a table
    public int getMostRecentTableID(String tableName, String tableIDName) {
        String sql = "SELECT ? FROM ? WHERE ? = (SELECT MAX(?) FROM ?)";
        ResultSet rs;
        Connection conn = getConnectionToProjectDB();
        int id = 0;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tableIDName);
            pstmt.setString(2, tableName);
            pstmt.setString(3, tableIDName);
            pstmt.setString(4, tableIDName);
            pstmt.setString(5, tableName);
            rs = pstmt.executeQuery();
            rs.next();
            id = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Chance to pop-out dialog
        }
        closeConnection(conn);
        return id;
    }

    /*********************************************** DELIVERABLE ******************************************************/
    
    public void createDeliverableButtonClicked(String name, String description, Date dueDate) {
        insertDeliverable(name, description, dueDate);
        int deliverableID = getMostRecentTableID("Deliverable", "DeliverableID");      //Get the id of the deliverable that was just inserted into the database (database handles primary key auto increment)
        addDeliverableToVector(deliverableID, name, description, dueDate);       //Add the newly created Deliverable to the Project's deliverableVector
        //Todo: Navigate back to Deliverable sheet
    }

    //Insert Deliverable into database
    public void insertDeliverable(String name, String description, Date dueDate) {
        String sql = "INSERT INTO Deliverable(Name, Description, Due_Date) VALUES (?, ?, ?)";
        Connection conn = getConnectionToProjectDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setLong(3, PMS.convertDateToLong(dueDate));
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Chance to pop-out dialog
        }
        closeConnection(conn);
    }
    
    public void addDeliverableToVector(int deliverableID, String name, String description, Date dueDate) {
        Deliverable del = new Deliverable(deliverableID, name, description, dueDate);
        deliverableVector.add(del);
    }

    public void updateDeliverableButtonClicked(int deliverableID, String Name, String description, Date due_Date) {
        updateDeliverable(deliverableID, Name, description, due_Date);
        upDateDeliverableInVector(deliverableID, Name, description, due_Date);
    }

    //Updates the Deliverable object in the deliverableVector member variable
    public void upDateDeliverableInVector(int deliverableID, String Name, String description, Date due_Date) {

    }

    //Updates the deliverable in the database
    public void updateDeliverable(int deliverableID, String name, String description, Date due_Date) {
        String sql = "UPDATE Deliverable SET Name = ?, Description = ?, Due_Date = ?";
        Connection conn = getConnectionToProjectDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(1, description);
            pstmt.setLong(1, PMS.convertDateToLong(due_Date));
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Chance to pop-out dialog
        }
        closeConnection(conn);
    }

    //Used for the Deliverable sheet in the Overview page
    public void getAllDeliverables() {
        String sql1 = "SELECT * FROM Deliverable";
        String sql2 = "SELECT * FROM Requirement WHERE Requirement.DeliverableID = Deliverable.DeliverableID AND Deliverable.DeliverableID = ?";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Deliverable del;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = getConnectionToProjectDB();

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql1);        //Execute the sql query and return the results to the ResultSet object
            rs = stmt.executeQuery(sql2);
            //Go through each row of the Deliverable table
            while(rs.next()) {
                del = new Deliverable();
                //Set member variables for deliverable with values from database
                del.setDeliverableID(rs.getInt("DeliverableID"));
                del.setName(rs.getString("Name"));
                del.setDescription(rs.getString("Description"));
                del.setDueDate(PMS.convertLongToDate(rs.getLong("Due_Date")));
                deliverableVector.add(del);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        closeConnection(conn);
    }
    
    /*********************************************** TASK *************************************************************/
    
    public void createTaskButtonClicked(int deliverableID, String name, String description, Date expectedStartDate,
                                        Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                                        Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                        int percentComplete, int type) {

        insertTask(deliverableID, name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type);
        int taskID = getMostRecentTableID("Task", "TaskID");      //Needed for addTaskToVector()
        addTaskToVector(taskID, deliverableID, name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type);       //Add the newly created Deliverable to the Project's deliverableVector
        //Todo: Navigate back to Deliverable sheet
    }

    public void insertTask(int deliverableID, String name, String description, Date expectedStartDate,
                           Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                           Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                           int percentComplete, int type) {
        //Todo: If no deliverable is being linked, pass -1 for DeliverableID
        String sql = "INSERT INTO Task(DeliverableID, Name, Description, Expected_Start_date, Expected_End_Date, " +
                "Expected_Duration, Expected_Effort, Actual_Start_date, Actual_End_Date, Actual_Duration, " +
                "Effort_Completed, Actual_Effort, Percent_Complete, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                "?, ?)";
        Connection conn = getConnectionToProjectDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliverableID);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setLong(4, PMS.convertDateToLong(expectedStartDate));
            pstmt.setLong(5, PMS.convertDateToLong(expectedEndDate));
            pstmt.setLong(6, expectedDuration);
            pstmt.setInt(7, expectedEffort);
            pstmt.setLong(8, PMS.convertDateToLong(actualStartDate));
            pstmt.setLong(9, PMS.convertDateToLong(actualEndDate));
            pstmt.setLong(10, actualDuration);
            pstmt.setInt(11, effortCompleted);
            pstmt.setInt(12, actualEffort);
            pstmt.setInt(13, percentComplete);
            pstmt.setInt(14, type);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        closeConnection(conn);
    }

    public void updateTask() {

    }

    public void addTaskToVector(int taskID, int deliverableID, String name, String description, Date expectedStartDate,
                                Date expectedEndDate, long expectedDuration, int expectedEffort, Date actualStartDate,
                                Date actualEndDate, long actualDuration, int effortCompleted, int actualEffort,
                                int percentComplete, int type) {
        Task task = new Task(taskID, deliverableID, name, description, expectedStartDate, expectedEndDate, expectedDuration,
                expectedEffort, actualStartDate, actualEndDate, actualDuration, effortCompleted, actualEffort,
                percentComplete, type);
        taskVector.add(task);
    }
    
    public void getAllTasks() {
        String sql = "SELECT * FROM Task";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Task task;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = getConnectionToProjectDB();

        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);        //Execute the sql query and return the results to the ResultSet object

            //Goes through each row of the table
            while(rs.next()) {
                task = new Task();
                task.setTaskID(rs.getInt("TaskID"));
                task.setDeliverableID(rs.getInt("DeliverableID"));
                task.setName(rs.getString("Name"));
                task.setDescription(rs.getString("Description"));
                task.setExpectedStartDate(PMS.convertLongToDate(rs.getLong("Expected_Start_Date")));
                task.setExpectedEndDate(PMS.convertLongToDate(rs.getLong("Expected_End_Date")));
                task.setExpectedDuration(rs.getLong("Expected_Duration"));
                task.setExpectedEffort(rs.getInt("Expected_Effort"));
                task.setActualStartDate(PMS.convertLongToDate(rs.getLong("Actual_Start_Date")));
                task.setActualEndDate(PMS.convertLongToDate(rs.getLong("Actual_End_Date")));
                task.setActualDuration(rs.getLong("Actual_Duration"));
                task.setEffortCompleted(rs.getInt("Effort_Completed"));
                task.setActualEffort(rs.getInt("Actual_Effort"));
                task.setPercentComplete(rs.getInt("Percent_Complete"));
                task.setType(rs.getInt("Type"));
                taskVector.add(task);
            }
        } catch(SQLException ex) {
            System.out.println(ex.getMessage());
        }
        closeConnection(conn);
    }

    /*********************************************** ISSUE ************************************************************/



}

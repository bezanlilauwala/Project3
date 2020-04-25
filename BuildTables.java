package ssl.pms;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BuildTables {

    public static void createTables(Connection conn) {
        createDeliverableTable(conn);
        createTaskTable(conn);
        createIssueTable(conn);
        createResourceTable(conn);
        createActionItemTable(conn);
        createRequirementTable(conn);
        createDecisionTable(conn);
        createReferenceDocumentTable(conn);
        createRiskTable(conn);
        createChangeTable(conn);
        createDependentTable(conn);
        createIssueAffectingTaskTable(conn);
        createResourceAssignedToActionItemTable(conn);
        createResourceAssignedToTaskTable(conn);
        createDecisionAboutIssueTable(conn);
        createResourceSkillTable(conn);
        createMeetingNoteTable(conn);
    }

    public static void createDeliverableTable(Connection conn) {
        String sql = "CREATE TABLE Deliverable (" +
                "DeliverableID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Due_Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createTaskTable(Connection conn) {
        String sql = "CREATE TABLE Task (" +
                "TaskID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Expected_Start_Date INTEGER, " +
                "Expected_End_Date INTEGER, " +
                "Expected_Duration INTEGER, " +
                "Expected_Effort INTEGER, " +
                "Actual_Start_Date INTEGER, " +
                "Actual_End_Date INTEGER, " +
                "Actual_Duration INTEGER, " +
                "Effort_Completed INTEGER, " +
                "Actual_Effort INTEGER, " +
                "Percent_Complete INTEGER, " +
                "Type INTEGER, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createIssueTable(Connection conn) {
        String sql = "CREATE TABLE Issue (" +
                "IssueID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Priority TEXT, " +
                "Priority_Defaults TEXT, " +
                "Severity TEXT, " +
                "Severity_Defaults TEXT, " +
                "Date_Raised INTEGER, " +
                "Date_Assigned INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Status TEXT, " +
                "Status_Defaults TEXT, " +
                "Status_Description TEXT, " +
                "Update_Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceTable(Connection conn) {
        String sql = "CREATE TABLE Resource (" +
                "ResourceID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Title TEXT, " +
                "Availability Calendar TEXT, " +
                "Pay Rate REAL);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createActionItemTable(Connection conn) {
        String sql = "CREATE TABLE Action_Item ( " +
                "ActionItemID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "IssueID INTEGER, " +
                "RiskID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Date_Created INTEGER, " +
                "Date_Assigned INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Status INTEGER," +
                "Status_Defaults TEXT," +
                "Status_DescriptionTEXT," +
                "Update_DateTEXT," +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID)," +
                "FOREIGN KEY(RiskID) REFERENCES Risk(RiskID)" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createRequirementTable(Connection conn) {
        String sql = "CREATE TABLE Requirement (" +
                "RequirementID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER, " +
                "Name TEXT, " +
                "Requirement Text TEXT, " +
                "Source Document TEXT, " +
                "Location in Source Document TEXT, " +
                "Client Reference TEXT, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID) " +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDecisionTable(Connection conn) {
        String sql = "CREATE TABLE Decision (" +
                "DecisionID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "ResourceID INTEGER, " +
                "Name TEXT, " +
                "Description TEXT, " +
                "Priority INTEGER, " +
                "Priority_Defaults TEXT, " +
                "Impact INTEGER, " +
                "Impact_Defaults TEXT, " +
                "Date_Created INTEGER, " +
                "Date_Needed INTEGER, " +
                "Date_Made INTEGER, " +
                "Expected_Completion_Date INTEGER, " +
                "Actual_Completion_Date INTEGER, " +
                "Note_Date INTEGER, " +
                "Status INTEGER, " +
                "Status_Defaults TEXT, " +
                "Status_Description TEXT, " +
                "Update_Date INTEGER, " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createReferenceDocumentTable(Connection conn) {
        String sql = "CREATE TABLE Reference_Document (" +
                "ReferenceID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DecisionID INTEGER, " +
                "Name TEXT, " +
                "FOREIGN KEY(DecisionID) REFERENCES Decision(DecisionID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createRiskTable(Connection conn) {
        String sql = "CREATE TABLE Risk (" +
                "RiskID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DeliverableID INTEGER, " +
                "Category INTEGER, " +
                "Category_Defaults TEXT, " +
                "Name TEXT, " +
                "Probability REAL, " +
                "Impact INTEGER, " +
                "Impact_Defaults TEXT, " +
                "Mitigation TEXT, " +
                "Contingency TEXT, " +
                "Action_By INTEGER, " +
                "FOREIGN KEY(DeliverableID) REFERENCES Deliverable(DeliverableID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createChangeTable(Connection conn) {
        String sql = "CREATE TABLE Change (" +
                "ChangeID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "Name TEXT, " +
                "Date_Requested INTEGER, " +
                "Requestor TEXT, " +
                "Status INTEGER, " +
                "Status_Defaults TEXT, " +
                "Update Date INTEGER);";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createIssueAffectingTaskTable(Connection conn) {
        String sql = "CREATE TABLE Issue_Affecting_Task (" +
                "TaskID INTEGER, " +
                "IssueID INTEGER, " +
                "FOREIGN KEY(TaskID) REFERENCES Task(TaskID), " +
                "PRIMARY KEY(TaskID, IssueID), " +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID)" +
                ");";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDependentTable(Connection conn) {
        String sql = "CREATE TABLE Dependent (" +
                "SuccessorID INTEGER NOT NULL, " +
                "PredecessorID INTEGER NOT NULL, " +
                "Dependency INTEGER NOT NULL, " +
                "FOREIGN KEY(SuccessorID) REFERENCES Task(TaskID), " +
                "PRIMARY KEY(SuccessorID,PredecessorID), " +
                "FOREIGN KEY(PredecessorID) REFERENCES Task(TaskID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceAssignedToTaskTable(Connection conn) {
        String sql = "CREATE TABLE Resource_Assigned_To_Task (" +
                "TaskID INTEGER, " +
                "ResourceID INTEGER, " +
                "Date_Assigned INTEGER, " +
                "FOREIGN KEY(TaskID) REFERENCES Task(TaskID), " +
                "PRIMARY KEY(TaskID,ResourceID), " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceAssignedToActionItemTable(Connection conn) {
        String sql = "CREATE TABLE Resource_Assigned_To_Action_Item (" +
                "ActionItemID INTEGER, " +
                "ResourceID INTEGER, " +
                "Date_Assigned INTEGER, " +
                "FOREIGN KEY(ActionItemID) REFERENCES  Action_Item(ActionItemID), " +
                "PRIMARY KEY(ActionItemID, ResourceID)" +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createDecisionAboutIssueTable(Connection conn) {
        String sql = "CREATE TABLE Decision_About_Issue (" +
                "DecisionID INTEGER, " +
                "IssueID INTEGER, " +
                "PRIMARY KEY(DecisionID, IssueID), " +
                "FOREIGN KEY(DecisionID) REFERENCES Decision(DecisionID), " +
                "FOREIGN KEY(IssueID) REFERENCES Issue(IssueID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createResourceSkillTable(Connection conn) {
        String sql = "CREATE TABLE Resource_Skill (" +
                "SkillID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "ResourceID INTEGER, " +
                "Description TEXT, " +
                "FOREIGN KEY(ResourceID) REFERENCES Resource(ResourceID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }

    public static void createMeetingNoteTable(Connection conn) {
        String sql = "CREATE TABLE Meeting_Note (" +
                "MeetingNoteID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DecisionID INTEGER, " +
                "Note_Date INTEGER, " +
                "Note TEXT, " +
                "FOREIGN KEY(DecisionID) REFERENCES Decision(DecisionID));";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
    }
}   //End class

//Run Command: java -classpath ".;sqlite-jdbc-3.30.1.jar" Connect
/*
BUTTON CLICKS (Parameters show information that is available in the form that clicks the button):

public static void loginButtonClicked(String username, String password)
public static void newUserButtonClicked()                                       //Opens a form to add the users username, password & email
public static void createUserButtonClicked(String username, String password, String email)  //Checks if the username or email already exist, then inserts the new user into the database, sends a verification email to the user and opens the Projects page
public static void forgotUsernameButtonClicked(String email)    //Gets the username for the corresponding email from the authentication database and calls sendForgotUsernameEmail()
public static void forgotPasswordButtonClicked(String email)    ""
public static void createNewProjectButtonClicked(int userID, String projectName, String description)  //Inserts the project into the authentication database, creates a new Project obj and opens the Deliverable Overview page
public static void openExistingProjectButtonClicked(Project project)            //Loads all table data from the project's database, creates objects and adds them to the projects Vector members
public static int getMostRecentTableID(String tableName, String tableIDName)    //Used to the the most recent ID added to the table

PMS FUNCTIONS
Utility:
public static Connection getConnectionToAuthenticationDB()                      //Opens a Connection to the SQLite user authentication database and returns the Connection object
public static void closeConnection(Connection conn)                             //Closes the Connection object

//Authentication
public static boolean authenticate(String username, String password)            //Checks the authentication database for the username and checks that it matches the stored password
public static int getUserID(String username)                                    //Returns the userID given the username in the authentication database
public static boolean checkIfUsernameExists(String username)                    //Checks the authentication database for the username. Returns true if it exists.
public static boolean checkPassword(String password)                            //Checks that the password meets microsoft standard password https://docs.microsoft.com/en-us/microsoft-365/admin/misc/password-policy-recommendations?view=o365-worldwide
public static void insertUser(String username, String password, String email)   //Inserts the user into the authentication database
public static void insertProject(String projectName, String description)        //Inserts the project into the Authentication database
public static int getMostRecentProjectID()                                      //Returns the projectID of the most recently added Project in the authentication database
public static void insertProjectAuthorization(int userID, int projectID)        //Inserts the project into the Authentication database
public static Vector<Project> getAllProjects(int userID)                        //Returns a vector containing all the Projects for the user from the authentication database
public static void sendForgotUsernameEmail(String username, String email)       //Sends an email to the user's email with their registered username
public static void sendForgotPasswordEmail(String password, String email)       //Sends an email to the user's email with their registered password
public static long convertDateToLong(Date date)
public static Date convertLongToDate(long dateLong)


*/


package ssl.pms;

import java.sql.*;
import java.util.Vector;
import java.util.Date;

public class PMS {

    //Opens a Connection to the database and returns the Connection object
    public static Connection getConnectionToAuthenticationDB() {
        String url = "jdbc:sqlite:C:/Users/bezan/Documents/SQLiteJDBC/PMS/PmsAuthentication.db";    //Todo: Change url to sql server
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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

    public static void loginButtonClicked(String username, String password) {
        if (checkIfUsernameExists(username)) {
            if (authenticate(username, password)) {
                Vector<Project> projectVector = PMS.getAllProjects(getUserID(username));        //Needed to pass all projects to the Projects page
                for(int i = 0 ; i < projectVector.size(); i++) {
                    //projectVector.elementAt(i).getProjectID();
                    //projectVector.elementAt(i).getName();
                    //projectVector.elementAt(i).getDescription();
                }
                //Todo: Open the Projects page for this user using the information in the loop above
            } else {
                System.out.println("Password does not match Username");     //Todo: Change to pop-out window
            }
        } else {
            System.out.println("Username does not exist");      //Todo: Change to pop-out window
        }
    }

    public static void newUserButtonClicked() {
        //Todo: Open the form to enter the new user's username, password & email (form contains Create User Button)
    }

    public static void createUserButtonClicked(String username, String password, String email) {
        int userID = 0;
        if (checkIfUsernameExists(username)) {
            System.out.println("Username already exists");     //Todo: Change to pop-out dialog
        } else if(!checkPassword(password)) {
            System.out.println("Password must have more than 8 characters");     //Todo: Change to pop-out dialog
        } else {
            PMS.insertUser(username, password, email);
            userID = getMostRecentTableID("User", "UserID");
            //Todo: Open the Projects page for this newly created user
            //Todo: Send email verification to user
        }
    }

    public static void forgotUsernameButtonClicked(String email) {
        String sql = "SELECT Username FROM User WHERE Email = ?";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        ResultSet rs;
        String username = "";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            rs.next();
            username = rs.getString("Username");
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        closeConnection(conn);
        sendForgotUsernameEmail(username, email);
    }

    public static void forgotPasswordButtonClicked(String email) {
        String sql = "SELECT Password FROM User WHERE Email = ?";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        ResultSet rs;
        String password = "";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            rs.next();
            password = rs.getString("Password");
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());     //Todo: Change to pop-out dialog
        }
        closeConnection(conn);
        sendForgotPasswordEmail(password, email);
    }

    public static void createNewProjectButtonClicked(int userID, String projectName, String description) {
        PMS.insertProject(projectName, description);
        int projectID = getMostRecentTableID("Project", "ProjectID");
        insertProjectAuthorization(userID, projectID);
        //Todo: insert project into the projectVector...?
        //Todo: Open Deliverable Overview page for this project (should be blank)
    }

    public static void openExistingProjectButtonClicked(Project project) {
        project.getAllDeliverables();
        project.getAllTasks();
        //Todo: Open Deliverable sheet in the projects Overview page
    }

    public static int getMostRecentTableID(String tableName, String tableIDName) {
        String sql = "SELECT ? FROM ? WHERE ? = (SELECT MAX(?) FROM ?)";
        ResultSet rs;
        Connection conn = getConnectionToAuthenticationDB();
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

    //Untested
    //Returns true if password matches the password in the database for that Username
    public static boolean authenticate(String username, String password) {
        String sql = "SELECT Password FROM User WHERE username = \"?\"";
        ResultSet rs = null;
        String databasePassword = "";       //Used for the password that is saved in the database
        Connection conn = PMS.getConnectionToAuthenticationDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            databasePassword = rs.getString(1);
        } catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
        return databasePassword.equals(password);
    }

    public static int getUserID(String username) {
        String sql = "SELECT UserID FROM User WHERE username = \"?\"";
        ResultSet rs = null;
        int userID = 0;       //Used for the password that is saved in the database
        Connection conn = PMS.getConnectionToAuthenticationDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            userID = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
        return userID;
    }

    public static boolean checkIfUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM User WHERE username = \"?\"";
        ResultSet rs = null;
        Connection conn = PMS.getConnectionToAuthenticationDB();
        int count = 0;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println("Error" + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
        return count > 0;
    }

    public static void sendVerificationEmailToUser(String email) {
        //Todo: for final release
    }

    //Returns true if password meets microsoft standards
    public static boolean checkPassword(String password) {
        return password.length() > 8;
    }

    public static void insertUser(String username, String password, String email) {
        String sql = "INSERT INTO User(Username, Password, Email) VALUES (?, ?, ?)";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
    }

    //Inserts the project into the Authentication database
    public static void insertProject(String projectName, String description) {
        String sql1 = "INSERT INTO Project(Name, Description) VALUES (?, ?)";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, projectName);
            pstmt.setString(2, description);
            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
    }

    public static int getMostRecentProjectID() {
        String sql = "SELECT ProjectID FROM Project WHERE ProjectID = (SELECT MAX(ProjectID) FROM Project);";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        ResultSet rs = null;
        int ret = 0;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            ret = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println("Error: " + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
        return ret;
    }

    public static void insertProjectAuthorization(int userID, int projectID) {
        String sql = "INSERT INTO Project_Authorization(UserID, ProjectID) VALUES (?, ?)";
        Connection conn = PMS.getConnectionToAuthenticationDB();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            pstmt.setInt(2, projectID);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
    }

    public static Vector<Project> getAllProjects(int userID) {
        String sql = "SELECT * FROM Project WHERE Project.ProjectID = Project_Authorization.projectID AND " +
                "Project_Authorization.UserID = ?";
        ResultSet rs;                    //ResultSet object is used to hold the results of the sql query
        Vector<Project> projectVector = new Vector<>();        //Used to hold every deliverable in the database
        Project project;          //Used for the individual Deliverable object before it is added to the deliverables vector
        Connection conn = PMS.getConnectionToAuthenticationDB();
        int projectID = 0;
        String name, description;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userID);
            rs = pstmt.executeQuery();        //Execute the sql query and return the results to the ResultSet object

            //Add each project (row) from the project table to the project vector
            while(rs.next()) {
                projectID = rs.getInt("ProjectID");
                name = rs.getString("Name");
                description = rs.getString("Description");
                project = new Project(projectID, name, description);
                projectVector.add(project);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());     //Todo: Change to pop-out dialog
        }
        PMS.closeConnection(conn);
        return projectVector;
    }

    public static void sendForgotUsernameEmail(String username, String email) {
        //Todo: Send an email to the user with their username
    }

    public static void sendForgotPasswordEmail(String password, String email) {
        //Todo: Send an email to the user with their username
    }

    public static long convertDateToLong(Date date) {
        return date.getTime();
    }

    public static Date convertLongToDate(long dateLong) {
        Date date = new Date();
        date.setTime(dateLong);
        return date;
    }

}

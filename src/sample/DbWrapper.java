package sample;

import java.sql.*;

public class DbWrapper {
    static public Connection connection;
    
    private String URL = "jdbc:mysql:///museum";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String userID = "iv1351";
    private String password = "1234";
    private Statement statement;
    
    public void connect() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(URL, userID, password);
            connection.setAutoCommit(false);
            System.out.println("Connected to " + URL + " using " + driver);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void printAllGuides() throws Exception {
        ResultSet result = statement.executeQuery("select Name from Employee where PersonNr in (select PersonNr from GuidedTour)");
        while(result.next()) {
            System.out.println("Name: " + result.getString("Name"));
        }
    }
    
    public void printAllGuideLanguages(String guideName) throws Exception {
        ResultSet personNrResult = statement.executeQuery("select PersonNr from Employee where Name='"+guideName+"'");
        if(personNrResult.next()) {
            String query = "select LanguageName from EmployeeLanguage Where PersonNr='" + personNrResult.getString("PersonNr") + "'";
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                System.out.println(result.getString("LanguageName"));
            }
        } else {
            System.out.println("No employee with the name " + guideName);
        }
    }
    
    public void addEmployeeLanguage(String employeeName, String languageName) throws Exception {
        ResultSet r = statement.executeQuery("select Name from Language where Name='" + languageName + "'");
        if(r.next()) {
            ResultSet personNrResult = statement.executeQuery("select PersonNr from Employee where Name='" + employeeName + "'");
            if(personNrResult.next()) {
                String personNr = personNrResult.getString("PersonNr");
                try {
                    statement.executeUpdate("insert into EmployeeLanguage values(" + personNr + ", '" + languageName + "')");
                    connection.commit();
                    System.out.println(languageName + " added for employee " + employeeName);
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println(languageName + " already exists for " + employeeName);
                }
            } else {
                System.out.println("No employee with the name " + employeeName);
            }
        } else {
            System.out.println("No language with the name " + languageName);
        }
    }
    
    public void createTheFuckingStatementFromANonStaticContext() throws Exception {
        statement = connection.createStatement();
    }
    
    public static void main(String[] args) {
        DbWrapper wrapper = new DbWrapper();
        wrapper.connect();
        try {
            wrapper.createTheFuckingStatementFromANonStaticContext();
            System.out.println("All Guides: ");
            wrapper.printAllGuides();
            System.out.println();
            
            System.out.println("All Guide Languages for Ghost Face");
            wrapper.printAllGuideLanguages("John Baptiste");
            System.out.println();
            
            System.out.println("Insert Language: ");
            wrapper.addEmployeeLanguage("Ghost Face", "Norwegian");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

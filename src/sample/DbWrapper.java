package sample;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class DbWrapper {
    static public Connection connection;
    
    private String URL = "jdbc:mysql://localhost:3306/museum";
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
            statement = connection.createStatement();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<String> getAllGuides() throws Exception {
        ArrayList<String> guides = new ArrayList<String>();
        ResultSet result = statement.executeQuery("select Name from Employee where PersonNr in (select PersonNr from GuidedTour)");
        while(result.next()) {
            guides.add(result.getString("Name"));
        }
        return guides;
    }
    
    public ArrayList<String> getAllGuideLanguages(String guideName) throws Exception {
        ArrayList<String> guideLanguages = new ArrayList<String>();
        ResultSet personNrResult = statement.executeQuery("select PersonNr from Employee where Name='"+guideName+"'");
        if(personNrResult.next()) {
            String query = "select LanguageName from EmployeeLanguage Where PersonNr='" + personNrResult.getString("PersonNr") + "'";
            ResultSet result = statement.executeQuery(query);
            while(result.next()) {
                guideLanguages.add(result.getString("LanguageName"));
            }
        } else {
            System.out.println("No employee with the name " + guideName);
        }
        return guideLanguages;
    }
    
    public boolean addEmployeeLanguage(String employeeName, String languageName) throws Exception {
        ResultSet r = statement.executeQuery("select Name from Language where Name='" + languageName + "'");
        if(r.next()) {
            ResultSet personNrResult = statement.executeQuery("select PersonNr from Employee where Name='" + employeeName + "'");
            if(personNrResult.next()) {
                String personNr = personNrResult.getString("PersonNr");
                try {
                    statement.executeUpdate("insert into EmployeeLanguage values(" + personNr + ", '" + languageName + "')");
                    connection.commit();
                    System.out.println(languageName + " added for employee " + employeeName);
                    return true;
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println(languageName + " already exists for " + employeeName);
                }
            } else {
                System.out.println("No employee with the name " + employeeName);
            }
        } else {
            System.out.println("No language with the name " + languageName);
        }

        return false;
    }
    
    public boolean removeEmployeeLanguage(String employeeName, String languageName) throws Exception {
        ResultSet personNrResult = statement.executeQuery("select PersonNr from Employee where Name='" + employeeName + "'");
        if(personNrResult.next()) {
            String personNr = personNrResult.getString("PersonNr");
            ResultSet foo = statement.executeQuery("select * from GuidedTour where PersonNr='" + personNr + "' and LanguageName='" + languageName + "'");
            if(!foo.next()) {
                statement.executeUpdate("delete from EmployeeLanguage where PersonNr in (select PersonNr from Employee where Name='" + employeeName + "') and LanguageName='" + languageName + "'");
                connection.commit();
                return true;
            } else {
				System.out.println("There exists a guided tour held by this guide in the language which is to be deleted. Language was not deleted.");
            }
        } else {
            System.out.println("No employee with the name " + employeeName);
        }
        return false;
    }

    public ArrayList<Exhibition> getExhibitionsEmployeeQualifiedFor(String employeeName) throws Exception {
        ArrayList<Exhibition> exhibitions = new ArrayList<Exhibition>();
        ResultSet t = statement.executeQuery("select * from Employee where Name='" + employeeName + "'");
		if(t.next())  {
            ResultSet r = statement.executeQuery("select * from Exhibition where ExhibitionID in (select ExhibitionID from EmployeeQualifiedFor where PersonNr=(select PersonNr from Employee where Name='" + employeeName + "'))");
            while(r.next()) {
                exhibitions.add(new Exhibition(r.getInt("ExhibitionID"), r.getString("Title"), r.getDate("Start"), r.getDate("End"), r.getInt("Space"), r.getDouble("Cost")));
            }
        } else {
            System.out.println("No employee with the name " + employeeName);
        }

        return exhibitions;
    }

    public static void main(String[] args) {
        DbWrapper wrapper = new DbWrapper();
        wrapper.connect();
        try {
            for(String i : wrapper.getAllGuides()) {
                System.out.println(i);
            }
            
            for(String i : wrapper.getAllGuideLanguages("John Baptiste")) {
                System.out.println(i);
            }

            System.out.println();
            for(Exhibition e : wrapper.getExhibitionsEmployeeQualifiedFor("John Baptiste")) {
                System.out.println(e.toString());
            }
            System.out.println();

            if(wrapper.addEmployeeLanguage("Ghost Face", "Norwegian")) {
                // Success
			}

            if(wrapper.removeEmployeeLanguage("Ghost Face", "Polish")) {
				// Success
			}
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

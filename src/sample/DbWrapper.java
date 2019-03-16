package sample;

import java.util.ArrayList;
import java.sql.*;

public class DbWrapper {
    private static Connection connection;

    private String URL = "jdbc:mysql://localhost:3306/museum";
    private String driver = "com.mysql.cj.jdbc.Driver";
    private String userID = "root";
    private String password = "";
    private Statement statement;

    void connect() {
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

    ArrayList<String[]> getAllGuidePersonNrAndName() throws Exception {
        ArrayList<String[]> guides = new ArrayList<String[]>();
        ResultSet result = statement.executeQuery("select PersonNr, Name from Employee");
        while(result.next()) {
            String[] guideTuple = {result.getString("PersonNr"), result.getString("Name")};
            guides.add(guideTuple);
        }
        return guides;
    }

    ArrayList<String> getAllLanguages() throws Exception {
        ArrayList<String> langs = new ArrayList<String>();
        ResultSet result = statement.executeQuery("select Name from Language");
        while(result.next()) {
            langs.add(result.getString("Name"));
        }
        return langs;
    }

    ArrayList<String> getAllGuideLanguages(String guideName) throws Exception {
        ArrayList<String> guideLanguages = new ArrayList<String>();
        PreparedStatement getPersonNr = connection.prepareStatement("select PersonNr from Employee where Name = ?");
        getPersonNr.setString(1, guideName);
        ResultSet personNrResult = getPersonNr.executeQuery();
        if(personNrResult.next()) {
            String query = "select LanguageName from EmployeeLanguage Where PersonNr = ?";
            PreparedStatement getLangs = connection.prepareStatement(query);
            getLangs.setString(1, personNrResult.getString("PersonNr"));
            ResultSet result = getLangs.executeQuery();
            while(result.next()) {
                guideLanguages.add(result.getString("LanguageName"));
            }
        } else {
            System.out.println("No employee with the name " + guideName);
        }
        return guideLanguages;
    }

    boolean addEmployeeLanguage(String employeeName, String languageName) throws Exception {
        PreparedStatement getLangName = connection.prepareStatement("select Name from Language where Name = ?");
        getLangName.setString(1, languageName);
        ResultSet r = getLangName.executeQuery();
        if(r.next()) {
            PreparedStatement getPersonNr = connection.prepareStatement("select PersonNr from Employee where Name = ?");
            getPersonNr.setString(1, employeeName);
            ResultSet personNrResult = getPersonNr.executeQuery();
            if(personNrResult.next()) {
                String personNr = personNrResult.getString("PersonNr");
                try {
                    PreparedStatement updateEmployeeLang = connection.prepareStatement("insert into EmployeeLanguage values(?, ?)");
                    updateEmployeeLang.setString(1, personNr);
                    updateEmployeeLang.setString(2, languageName);
                    updateEmployeeLang.executeUpdate();
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
            System.out.println("No language with the name " + languageName + " in the database, adding...");
            PreparedStatement insertNewLang = connection.prepareStatement("insert into Language values(?)");
            insertNewLang.setString(1, languageName);
            int rowsAffected = insertNewLang.executeUpdate();
            if(rowsAffected > 0) {
                connection.commit();
                return addEmployeeLanguage(employeeName, languageName);
            } else {
                System.out.println("Could not add a new language!");
            }
        }

        return false;
    }

    boolean removeEmployeeLanguage(String employeeName, String languageName) throws Exception {
        PreparedStatement getPersonNr = connection.prepareStatement("select PersonNr from Employee where Name = ?");
        getPersonNr.setString(1, employeeName);
        ResultSet personNrResult = getPersonNr.executeQuery();
        if(personNrResult.next()) {
            String personNr = personNrResult.getString("PersonNr");
            PreparedStatement getGuidedTours = connection.prepareStatement("select * from GuidedTour where PersonNr = ? and LanguageName = ?");
            getGuidedTours.setString(1, personNr);
            getGuidedTours.setString(2, languageName);
            ResultSet foo = getGuidedTours.executeQuery();
            if(!foo.next()) {
                PreparedStatement deleteEmployeeLang = connection.prepareStatement("delete from EmployeeLanguage where PersonNr in (select PersonNr from Employee where Name = ?) and LanguageName = ?");
                deleteEmployeeLang.setString(1, employeeName);
                deleteEmployeeLang.setString(2, languageName);
                deleteEmployeeLang.executeUpdate();
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

    boolean addEmployeeQualification(String employeeName, Integer exhibitionId) throws Exception {
        PreparedStatement getPersonNr = connection.prepareStatement("select PersonNr from Employee where Name = ?");
        getPersonNr.setString(1, employeeName);
        ResultSet personNrResult = getPersonNr.executeQuery();
        if(personNrResult.next()) {
            String personNr = personNrResult.getString("PersonNr");
            try {
                PreparedStatement insertEmployeeQualification = connection.prepareStatement("insert into EmployeeQualifiedFor values(?, ?)");
                insertEmployeeQualification.setString(1, personNr);
                insertEmployeeQualification.setInt(2, exhibitionId);
                insertEmployeeQualification.executeUpdate();
                connection.commit();
                System.out.println("Employee " + employeeName + " is now qualified for exhibition " + exhibitionId);
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.println("Employee " + employeeName + " is already qualified for exhibition " + exhibitionId);
            }
        } else {
            System.out.println("No employee with the name " + employeeName);
        }

        return false;
    }

    boolean removeEmployeeQualification(String employeeName, Integer exhibitionId) throws Exception {
        PreparedStatement getPersonNr = connection.prepareStatement("select PersonNr from Employee where Name = ?");
        getPersonNr.setString(1, employeeName);
        ResultSet personNrResult = getPersonNr.executeQuery();
        if(personNrResult.next()) {
            String personNr = personNrResult.getString("PersonNr");
            PreparedStatement getGuidedTour = connection.prepareStatement("select * from GuidedTour where PersonNr = ? and ExhibitionID = ? and StartTime >= NOW()");
            getGuidedTour.setString(1, personNr);
            getGuidedTour.setInt(2, exhibitionId);
            ResultSet foo = getGuidedTour.executeQuery();
            if(!foo.next()) {
                PreparedStatement deleteEQ = connection.prepareStatement("delete from EmployeeQualifiedFor where PersonNr=? and ExhibitionID=?");
                deleteEQ.setString(1, personNr);
                deleteEQ.setInt(2, exhibitionId);
                deleteEQ.executeUpdate();
                connection.commit();
                return true;
            } else {
				System.out.println("There exists a guided tour held by this guide for this exhibition. Qualification was not deleted.");
            }
        } else {
            System.out.println("No employee with the name " + employeeName);
        }
        return false;
    }

    ArrayList<Exhibition> getExhibitionsEmployeeQualifiedFor(String employeeName) throws Exception {
        ArrayList<Exhibition> exhibitions = new ArrayList<Exhibition>();
        PreparedStatement getEmployees = connection.prepareStatement("select * from Employee where Name = ?");
        getEmployees.setString(1, employeeName);
        ResultSet t = getEmployees.executeQuery();
		if(t.next())  {
		    PreparedStatement getExhibitionByQualification = connection.prepareStatement("select * from Exhibition where ExhibitionID in (select ExhibitionID from EmployeeQualifiedFor where PersonNr=(select PersonNr from Employee where Name = ?))");
            getExhibitionByQualification.setString(1, employeeName);
		    ResultSet r = getExhibitionByQualification.executeQuery();
            while(r.next()) {
                exhibitions.add(new Exhibition(r.getInt("ExhibitionID"), r.getString("Title"), r.getDate("Start"), r.getDate("End"), r.getInt("Space"), r.getDouble("Cost")));
            }
        } else {
            System.out.println("No employee with the name " + employeeName);
        }

        return exhibitions;
    }

    ArrayList<Exhibition> getAllExhibitions() throws Exception {
        ArrayList<Exhibition> exhibitions = new ArrayList<Exhibition>();
        ResultSet r = statement.executeQuery("select * from Exhibition");
        while(r.next()) {
            exhibitions.add(new Exhibition(r.getInt("ExhibitionID"), r.getString("Title"), r.getDate("Start"), r.getDate("End"), r.getInt("Space"), r.getDouble("Cost")));
        }

        return exhibitions;
    }

    public static void main(String[] args) {
        DbWrapper wrapper = new DbWrapper();
        wrapper.connect();
        try {
            for(String i : wrapper.getAllLanguages()) {
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

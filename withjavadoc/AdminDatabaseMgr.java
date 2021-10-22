

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
* <h1>AdminDatabaseManager Entity class</h1>
* The Admin Database manager links the java program to a csv file
* <p>
* the csv file contains all the persistent data of the system such as
* admin data in this case.
* allows users to create, retrieve, update entries
* and creates admin objects from database
* @author  Kam Chin Voon
* @version 1.0
* @since   2020-25-11
*/

public class AdminDatabaseMgr extends DatabaseManager{ 
    /** 
    * file path to admin csv
    */
    final static String FILEOFALLADMINS = "ADMIN.csv"; 
    /** 
    * temporary file created to transfer data
    */
    final static String TEMPFILE = "TEMP.csv"; 
    /** 
    * columns of admin csv
    */
    private static String[] columns = { "Last name", "First name", "Username", "Gender"};
    private static List<String> col = Arrays.asList(columns);
    /** 
    * list of admin objects
    */
    private static List<Admin> admins;
  
    /** 
    * list of admin objects
    */
    public AdminDatabaseMgr(){
        super(FILEOFALLADMINS, col);
        admins = getAllAdmins();
    }

    /**
   * This method gets a list of admins
   * @return List of admin objects
   */
    public static List<Admin> getAllAdmins(){
        if(admins == null){
            admins = readAdminsFromCSV();
        }
        return admins;
    }

    /**
   * This method gets a specific instance of admin
   * @param userName username of admin instance
   * @return an instance of admin
   */
    public static Admin retrieveAdmin(String userName){
        for (Admin admin : admins) {
            if (admin.getUsername().equals(userName)){
                return admin;
            }
        }
        System.out.println("Could not find user in database");
        return null;
    }

    /**
   * This method reads admin data row by row 
   * @return an list of admin objects
   * @exception IOException can not find file
   */
    private static List<Admin> readAdminsFromCSV(){ 
        admins = new ArrayList<>(); 
        Path pathToFile = Paths.get(FILEOFALLADMINS); 
        try(BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){

            String line = br.readLine(); 
            line = br.readLine();
            while (line != null) { 
                String[] attributes = line.split(",",-1); 
                Admin a = createAdmin(attributes); 
                admins.add(a); 
                line = br.readLine(); 
            } 
        } 
        catch (IOException ioe) { 
            ioe.printStackTrace(); 
        } return admins; 
    } 
    /**
   * This method creates admin objects
   * @param metadata string array of data
   * @return an admin object
   */
    private static Admin createAdmin(String[] metadata) { 
        
        String lastName = metadata[0]; 
        String firstName= metadata[1];
        String userName= metadata[2]; 
        String gender= metadata[3]; 
        return new Admin(lastName, firstName, userName, gender); 
    } 
}
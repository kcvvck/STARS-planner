

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.*;

/**
 * <h1>
 *     Student Database Manager
 * </h1>
 * links the java program to a csv file
 * the csv file contains all the persistent data of the system such as
 * student data in this case.
 * allows users to create, retrieve, update entries
 * @author Kim Chae Yoon
 * @version 1.0
 * @since   2020-25-11
 */
public class StudentDatabaseManager extends DatabaseManager {
    /**
     * file path to student CSV
     */
    final static String STUDENTFILE = "STUDENT.csv";
    /**
     * columns in student CSV
     */
    private static String[] columns = { "Last Name", "First Name", "Username", "Gender", "Nationality",
            "Course Of Study", "Matriculation Number", "Start Date", "Start Time", "End Date", "End Time", "Course 1",
            "Index 1", "Course 2", "Index 2", "Course 3", "Index 3", "Course 4", "Index 4", "Course 5", "Index 5",
            "Course 6", "Index 6", "Course 7", "Index 7", "Total AU" };
    private static List<String> col = Arrays.asList(columns);
    /**
     * List of Student objects
     */
    public static List<Student> studentList = new ArrayList<Student>();
    /**
     * initialize variable AU
     */
    private static int AU = 0 ;
    /**
     * default access period of students to be used
     */
    final static String defaultAccessPeriod = "January 1 2020,12:00,December 30 2020,23:59";
    /**
     * instance of Student Database Manager
     */
    private static StudentDatabaseManager sdm = null;

    /**
     * gets list of students from CSV and stores in studentList
     */
    public StudentDatabaseManager() {
        super(STUDENTFILE, col);
        studentList = getAllStudentsFromCSV();
        if (sdm == null){
            sdm = this;
        }
    }

    /**
     * gets an instance of student database manager
     * @return student database manager
     */
    public static StudentDatabaseManager getInstance(){
        if (sdm != null){
            return sdm;
        }else{
            sdm = new StudentDatabaseManager();
            return sdm;
        }
    }

    /**
     * updates the access datetime of all students in a specific course of study
     * changes made to CSV
     * @param course Course to be changed
     * @param newStartDate new Start date (should be in the form: November 20 2020)
     * @param newStartTime new Start time (should be in the form: 09:30)
     * @param newEndDate new End date (should be in the form: November 20 2020)
     * @param newEndTime new Start time (should be in the form: 09:30)
     */
    public void changeStudentAccess(String course, String newStartDate, String newStartTime, String newEndDate,
            String newEndTime) {
                List<String> list = Arrays.asList(newStartDate, newStartTime, newEndDate, newEndTime);
        String replace = String.join(",", list)+",";

        super.updateRow(replace, course, col.indexOf("Course Of Study"));
    }

    /**
     * when admin updates course index, replace course index in student csv
     * @param courseID Course ID to be updated (eg. CZ2002)
     * @param oldIndex old index to update (eg. 10002)
     * @param newIndex new index (eg. 20002)
     */
    public void updateCourseStudent(String courseID, int oldIndex, int newIndex){
        replaceInformationCSV(courseID, Integer.toString(oldIndex), courseID, Integer.toString(newIndex));
    }

    /**
     * when admin updates course index, update AU (minus old, plus new)
     * @param courseID course ID (eg. CZ2002)
     * @param courseIndex course index to update (eg. 10002)
     * @param oldAU old AU int
     * @param newAU new AU int
     */
    public void updateCourseAU(String courseID, int courseIndex, int oldAU, int newAU){
        FileWriter fw = null;
        ArrayList<String[]> reWrite = new ArrayList<String[]>();
        String line = "";
        try(BufferedReader fileReader = new BufferedReader(new FileReader(STUDENTFILE))) {
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",", -1);
                List<String> token = Arrays.asList(tokens);
                if (token.contains(courseID) && token.contains(Integer.toString(courseIndex))) { // if row contains the strings you identify it with
                    int oldTotalAU = Integer.parseInt(tokens[col.indexOf("Total AU")]);
                    int newTotalAU = oldTotalAU - oldAU + newAU;
                    tokens[col.indexOf("Total AU")] = Integer.toString(newTotalAU);
                    reWrite.add(tokens);
                } else reWrite.add(tokens);
            }
        } catch (IOException e) {
            System.out.println("Could not find file "+ STUDENTFILE +" to read (StudentDatabaseManager -> updateCourseAU)");
        }
        try {
            fw = new FileWriter(STUDENTFILE);
            for (String[] l : reWrite) {
                String s = String.join(",",l);
                try {
                    fw.append(s+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not find file "+ STUDENTFILE +" StudentDatabaseManager -> updateCourseAU");
        } 
        finally{
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                System.out.println("Could not close file "+ STUDENTFILE +" StudentDatabaseManager -> updateCourseAU");
            }
        }
    }

    /**
     * checking if student exists by finding matric number (matric number is unique)
     * @param identifier1 unique identifier of student
     * @param identifier2 unique identifier of student
     * @return 1 if student is found in file and 0 otherwise
     */
    public int checkStudentExist(String identifier1, String identifier2){
        try{
            String line = "";
            BufferedReader fileReader = null;
            fileReader = new BufferedReader(new FileReader(STUDENTFILE));
            while ((line = fileReader.readLine()) != null){
                String[] tokens = line.split(",",-1);
                List<String> token = Arrays.asList(tokens);
                if(token.contains(identifier1) && token.contains(identifier2)){
                    fileReader.close();
                    return 1;
                }
            }
            fileReader.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Error checking student exist");
        }
        return 0; //student not found
    }

    /**
     * @return studentList as list of Student objects
     */
    public static List<Student> getAllStudents(){
        if(studentList.isEmpty()){
            studentList = getAllStudentsFromCSV();
        }
        return studentList;
    }

    /**
     * get Student object from username
     * @param userName unique username of student to retrieve
     * @return s Student object if found,
     *          null if not found and print error message
     */
    public static Student retrieveStudent(String userName) {
        // studentList = getAllStudentsFromCSV();
        for (Student s : studentList) {
            if(s.getUsername().equals(userName)){
                return s;
            }
        }System.out.println("Student does not exist in the database(StudentDatabaseManager)");
        return null;

    }

    /**
     * get list of all Students in CSV
     * @return studentList as list of Student objects
     */
    private static List<Student> getAllStudentsFromCSV() {
        studentList = readStudentsFromCSV(STUDENTFILE);
        return studentList;
    }

    /**
     * creates and returns list of all students from the CSV
     * @param fileName student CSV
     * @return studentList as list of Student objects
     * @exception IOException
     */
     private static List<Student> readStudentsFromCSV(String fileName){ 
        studentList = new ArrayList<Student>(); 
        Path pathToFile = Paths.get(fileName); 
        try(BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){
            String line = br.readLine(); // loop until all lines are read 
            line = br.readLine();
            while (line != null) { 
                
                String[] attributes = line.split(",",-1); 
                Student s = createStudent(attributes);  
                if(s != null){
                    studentList.add(s);
                }
                line = br.readLine(); 
            } 
        } 
        catch (IOException ioe) { 
            ioe.printStackTrace(); 
        } 
        return studentList; 
    }

    /**
     * creates Student object from string array of data
     * @param metadata string array of data
     * @return Student object
     *          null if unsuccessful
     */
    private static Student createStudent(String[] metadata) {
        
        List<String> coursesRegistered = new ArrayList<String>();
        if(metadata.length>1){
            String lastName = metadata[0]; 
            String firstName= metadata[1];
            String userName= metadata[2]; 
            String gender= metadata[3]; 
            String nationality= metadata[4]; 
            String courseOfStudy= metadata[5];
            String matricNo= metadata[6];
            if(metadata.length>11){
                /**
                 * adds courses taken by Student
                 */
                for(int i = col.indexOf("Course 1") ; i < col.indexOf("Total AU" ) ; i++){
                    if(!(metadata.length - 1 < i)){
                        if(!metadata[i].equals("") && !metadata[i].equals(" ")){
                            coursesRegistered.add(metadata[i]);
                        
                        }
                    }
                }
                if(!(coursesRegistered.size() ==0)){ // if there are courses registered then, surely the au column also exists
                    AU = Integer.parseInt(metadata[col.indexOf("Total AU" )]);
                }else{
                    AU = 0;
                }
            }
            
            return new Student(lastName, firstName, userName, gender, nationality,courseOfStudy, matricNo, coursesRegistered, AU); 
            
        }return null;
    }


    /**
     * updates CSV when two students swap indexes
     * @param matricNo Student's matriculation number
     * @param peerMatricNo peer's matriculation number
     * @param myCourseIndex Student's course index
     * @param peerCourseIndex peer's course index
     * @param courseID course ID of course to be swapped
     */
    public void swapCourseIndex(String matricNo, String peerMatricNo, int myCourseIndex, int peerCourseIndex, String courseID) {
        int uniqueColumn = col.indexOf("Matriculation Number");

        swapValuesCSV(uniqueColumn, matricNo, peerMatricNo, 
                    Integer.toString(myCourseIndex), Integer.toString(peerCourseIndex), courseID);
     
    }

    /**
     * add a new course to student in csv - input student and
     * courseID and course index to add
     * @param s Student who is adding course
     * @param courseID course ID of course added
     * @param courseIndex course ID of course added
     */
    public void addCourse(Student s, String courseID, int courseIndex) {
        if(sdm == null){
            sdm = new StudentDatabaseManager();
        }
        List<String> list = Arrays.asList(courseID, Integer.toString(courseIndex));
        String toAdd = String.join(",", list);
        replaceInformationCSV(col.indexOf("Total AU"), s.getMatricNo(), s.getFirstName(), Integer.toString(s.getTotalAU()));
        appendEntry(toAdd, col.indexOf("Matriculation Number"), s.getMatricNo());
    }


    /**
     * drops specific course in student CSV
     * changes updated AU
     * @param s Student who is dropping course
     * @param courseID course ID to be dropped
     */
    // working
    public void dropCourse(Student s, String courseID) {
        if(sdm == null){
            sdm = new StudentDatabaseManager();
        }
        replaceInformationCSV(col.indexOf("Total AU"), s.getMatricNo(), s.getFirstName(), Integer.toString(s.getTotalAU()));
        removeRow(col.indexOf("Matriculation Number"), s.getMatricNo(), courseID);
        
    }

   


    /**
     * adds new Student into CSV file when admin adds a new Student
     * adds to login file
     * @param lastName last name of Student to be added
     * @param firstName first name of Student to be added
     * @param userName username of Student to be added
     * @param gender gender of Student (F/M)
     * @param nationality nationality of Student
     * @param courseOfStudy course of study of Student (eg. DSAI)
     * @param matricNo Student's matriculation number
     */
    public void addStudent(String lastName, String firstName, String userName,
                                  String gender, String nationality, String courseOfStudy, String matricNo){
        
        Login.addPassword(userName);
        List<String> list = Arrays.asList(lastName, firstName, userName, gender, nationality, courseOfStudy, matricNo, 
                                            defaultAccessPeriod);
        String replace = String.join(",", list);
        addEntry(replace);
        System.out.println("Student: " + firstName + " " + lastName + " of matriculation number " + matricNo +
                             " has been successfully added.");
        
    }
    
}

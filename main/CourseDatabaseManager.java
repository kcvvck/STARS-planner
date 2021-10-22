

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <h1>CourseDatabaseManager</h1>
 * This class is mainly for everything involving the COURSE.csv file, reading, updating, adding, replacing, deleting information
 * All the changes in the Course objects will call the functions here to update the csv
 * e.g. when Admin changes course index or adds course, when Student adds/drops course and Course vacancy changes etc.
 * This is also where all the already existing courses are stored, every line represents a course,
 * so whenever we run the code we have to read the csv and create the Course objects
 * @author Joelle Thng, Wang Anyi, Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */

public class CourseDatabaseManager extends DatabaseManager{ // CRUD -> Responsible for CREATING, RETRIEVING, UPDATing and DELETEING entries FOR CSV ONLY..
    /**
     * List of all the Course objects
     */
    private static List<Course> courses;
    // private final String[] days = {"","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    // private List<String> day = Arrays.asList(days);
    /**
     * The filepath to access the COURSE.csv file
     */
    final static String FILEOFALLCOURSES = "COURSE.csv";
    /**
     * The file columns (e.g."CourseID", "School",	"Index", "Vac", "AU") etc.
     */
    private static String[] columns = {"CourseID", "School",	"Index",	"Vac",	"Session1",	"Session2",	"Session3",	"Type",	"AU"};
    /**
     * converting the file columns into a List<String>, so it's easier to access
     * instead of using the element line[3] we can use line[col.indexOf("Vac")], 
     * which is useful when we change the columns of the csv file, 
     * such as adding/switching columns, adding a column in between existing columns etc.
     * so we just have to update this List, this avoids having to change all of the 
     * individual numbers of the indexes everytime and potentially mixing up the order
     */
    private static List<String> col = Arrays.asList(columns);
    /**
     * The filepath for a temporary csv file we create when updating the main file
     */
    final static String TEMPFILE = "temp.csv";
    /**
     * CourseDatabaseManager object which we first initialise to null
     */
    private static CourseDatabaseManager cdm = null;
    
    /**
     * Constructor to create new CourseDatabaseManager
     * Condition to check if cdm is null, ensures only one instance of CourseDatabaseManager is called
     */
    public CourseDatabaseManager(){
        super(FILEOFALLCOURSES, col);
        courses = getAllCoursesFromCSV();
        if(cdm == null){
            cdm = this;
        }
    }

    
    /** 
     * Gets the List of all Course objects
     * This method is called by other classes
     * @return List<Course>
     */
    public static List<Course> getAllCourses(){
        if(cdm == null){
            cdm = new CourseDatabaseManager();
        }
        return courses;
    }

    
    /** 
     * Looks through the List of Courses, and returns the Course object with the input courseID and courseIndex
     * @param courseID      The course ID of the Course
     * @param courseIndex   The course index of the Course
     * @return Course object
     */
    public static Course retrieveCourse(String courseID, int courseIndex) {
        courses = getAllCoursesFromCSV();
        for (Course c : courses) {
            if(c.getCourseID().equals(courseID)){
                if(c.getCourseIndex() == courseIndex){
                    return c;
                }
                
            }
        }System.out.println("Course does not exist in the database");
        return null;

    }

    
    /** 
     * Gets the List of all Course objects from readCourseFromCSV
     * @return List<Course> List oc Course objects
     */
    private static List<Course> getAllCoursesFromCSV(){
        
        courses = readCourseFromCSV(FILEOFALLCOURSES);
        return courses;
    }

     
     /** 
      * The actual function that reads through the csv file, 
      * calls createCourse method to create Course objects, then and adds into the List of Courses
      * @param fileName         The filepath of the csv we're reading (COURSE.csv)
      * @return List<Course> List of Course objects
      */
     // https://www.java67.com/2015/08/how-to-load-data-from-csv-file-in-java.html#ixzz6ePfLvsWI
    private static List<Course> readCourseFromCSV(String fileName){ 
        List<Course> courses = new ArrayList<>(); 
        Path pathToFile = Paths.get(fileName); // create an instance of BufferedReader // using try with resource, Java 7 feature to close resources
        try(BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){

             
            // read the first line from the text file 
            String line = br.readLine(); // loop until all lines are read 
            line = br.readLine();
            while (line != null) { 
                String[] attributes = line.split(",",-1); 
                Course c = createCourse(attributes); 
                if(c!=null){
                    courses.add(c); // read next line before looping // if end of file reached, line would be null 
                    
                }line = br.readLine(); 
            } 
        } 
        catch (IOException ioe) { 
            ioe.printStackTrace(); 
        } return courses; 
    } 
    
    
    /** 
     * Creates Course objects from one line of the csv file
     * @param metadata  String[] after reading one line of the csv and splitting it by the commas
     * @return Course object
     */
    private static Course createCourse(String[] metadata) { 
        List<String> sessions = new ArrayList<String>();
        if(metadata.length>1){
            String courseID = metadata[0]; 
            String school = metadata[1];
            int courseIndex= Integer.parseInt(metadata[2]); 
            int vacancy= Integer.parseInt(metadata[3].split("/")[0]); 
            int courseSize = Integer.parseInt(metadata[3].split("/")[1]);
            String courseType= metadata[7]; 
            int AU = Integer.parseInt(metadata[8]);
    
            for(int i = col.indexOf("Session1") ; i < col.indexOf("Session3") + 1 ; i++){
                if(!metadata[i].equals("")){
                    sessions.add(metadata[i]);
                
                }
                
            }
            return new Course(courseID, school, courseIndex, vacancy, courseSize, sessions, courseType, AU); 
        }
        return null;
    } 

    
    /** 
     * Updates the vacancy of Course c in the csv file, gets the info from Course c
     * @param c Course that had a change in vacancy and needs to be reflected in csv
     */
    public void updateVacancy(Course c){
        String vac_over_size = c.getVacancy() +"/"+c.getCourseSize();
        replaceInformationCSV(col.indexOf("Vac"), c.getCourseID(), Integer.toString(c.getCourseIndex()), vac_over_size);
    }

    
    /** 
     * Adds a new line representing the details of the course into the csv
     * details input by Admin user
     * @param courseID      Course ID of the course
     * @param school        School the course is under
     * @param courseIndex   Course index of the course
     * @param courseSize    Maximum capacity of the course
     * @param sessionsList  String[] containing the lesson details e.g. "Lecture/LT2/Friday/11:00/12:00"
     * @param courseType    Course type of the course
     * @param AU            Number of AUs of the course
     */
    public void addCourse(String courseID, String school, int courseIndex, int courseSize, String[] sessionsList, 
                        String courseType, int AU){
       
        String size = courseSize + "/" + courseSize;
        List<String> list = Arrays.asList(courseID, school, Integer.toString(courseIndex), size, 
                                    sessionsList[0], sessionsList[1],sessionsList[2], courseType, Integer.toString(AU));
        String replace = String.join(",", list);
        addEntry(replace);
        System.out.println(school + courseID+"/"+courseIndex+" " +  size+" " +  
                            sessionsList[0]+" " +  sessionsList[1]+" " + sessionsList[2]+" " + 
                             courseType+" " +  AU +" has been added");
        
    }


    
    /** 
     * When Admin updates a course, the changes in the details will be updated in the csv
     * @param courseID          Course ID of the course
     * @param school            School the course is under
     * @param oldCourseIndex    The original course index of the course
     * @param newCourseIndex    The new course index to replace with
     * @param courseSize        The new course size e.g. 20/30
     * @param newLesson         String[] containing lesson details e.g. "Lecture/LT2/Friday/11:00/12:00"
     * @param courseType        Course type of the course
     * @param AU                Number of AUs of the course
     */
    public void updateCourse(String courseID, String school,  int oldCourseIndex, int newCourseIndex, String courseSize, 
                            String[] newLesson, String courseType, int AU){

        BufferedReader fileReader = null;
        PrintWriter pw = null;

        File oldFile = new File(FILEOFALLCOURSES);
        File newFile = new File(TEMPFILE);
        String courseIndex = Integer.toString(oldCourseIndex);
        String line;
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < newLesson.length-1; i++) {
            sb.append(newLesson[i] + ",");
        }
        sb.append(newLesson[newLesson.length-1]);
        String str = sb.toString();

        try(FileWriter fw = new FileWriter(TEMPFILE, true); //append to file
            BufferedWriter bw = new BufferedWriter(fw);){
            
            pw = new PrintWriter(bw);
            fileReader = new BufferedReader(new FileReader(FILEOFALLCOURSES));
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",",-1);
                StringBuilder newLine = new StringBuilder();
                if(!tokens[col.indexOf("CourseID")].equals(courseID)){ //if this is not the course to be updated
                    for(int i=0; i<tokens.length; i++){
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    pw.println(newLine);
                }else{
                    if(tokens[col.indexOf("Index")].equals(courseIndex)){
                        List<String> list = Arrays.asList(courseID, school, Integer.toString(newCourseIndex),
                                                        courseSize, str, courseType, Integer.toString(AU));
                        String replace = String.join(",", list);
                        pw.println(replace);
                    }else{
                        List<String> list = Arrays.asList(courseID, school);
                        String replace = String.join(",", list);
                        newLine.append(replace + ",");
                        for(int i=2; i<col.indexOf("Type"); i++){
                            newLine.append(tokens[i]);
                            newLine.append(",");
                        }
                        newLine.append(courseType+","+AU+",");
                        pw.println(newLine);
                    }
                    
                }
            }

            
        }
        catch(Exception e){
            System.out.println("Error updating course file(CourseDatabaseManager)");
            e.printStackTrace();
        } finally{
            if(pw !=null){
                pw.flush();
                pw.close();
            }
            File name = new File(FILEOFALLCOURSES); //variable to store path
            oldFile.delete();
            newFile.renameTo(name);
            try {
                if(fileReader!=null){
                    fileReader.close();
                }
            } catch (IOException e) {
                System.out.println("error encountered at CourseDatabaseManager when trying to update course information");
                e.printStackTrace();
            }
        }
    }

    // public static void main(String[] args) {
    //     CourseDatabaseManager cdm = new CourseDatabaseManager();
    //     cdm.updateCourse("RA1001", "SPMS", 1202, 12222, "40/40", new String[]{"Lecture/LT2/Friday/11:00/12:08", "Tutorial/TR+3/Tuesday/10:00/11:04","Lab/HWLAB1/Monday/14:30/15:32"}, "UE", 7);
    // }



}

/**
* <h1>Admin Entity class</h1>
* The Admin implements IUser which has a method run() which
* displays the main user interface of Admin
* <p>
* connects to student manager and course manager
* to add and change student objects 
* and add and change course objects
*
* @author  Kam Chin Voon
* @version 1.0
* @since   2020-25-11
*/


public class Admin implements IUser{
    /** 
    * username of admin
    */
    private String userName;
    /** 
    * first name of admin
    */
    private String firstName;
    /** 
    * last name of admin
    */
    private String lastName;
    /** 
    * gender of admin
    */
    private String gender;
    /** 
    * name of admin
    */
    private String name;
    
    /** 
    * initalise student and course control classes
    */

    private static StudentManager sm = new StudentManager();
    private static CourseManager cm = new CourseManager();
    

    /**
     * make admins with first name, last name, username(unique) and gender
     * @param firstName first name of admin
     * @param lastName  last name of admin
     * @param userName  username of admin
     * @param gender    gender of admin
     */
    public Admin(String firstName, String lastName, String userName, String gender) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.name = firstName + " "+ lastName;

    }
    /** 
    * when run, will display the main admin user interface,
    * only for the specific admin who logs in
    */
    @Override
    public void run() {
        AdminApp adminUI = new AdminApp(this); 
        adminUI.display();
    }

    
    
    /** 
     * @return String first name of specific Admin
     */
    public String getFirstName() {
        return firstName;
    }


    
    /** 
     * @return String last name of specific Admin
     */
    public String getLastName() {
        return lastName;
    }


    
    /** 
     * @return String gender of specific admin
     */
    public String getGender() {
        return gender;
    }


    
    /** 
     * @return String name of admin which is combining first name and last name
     */
    public String getName() {
        return name;
    }
    /**
   * This method is used to check if the course exists,
   * it calls the course manager which contains a list
   * of courses
   * @param courseID courseID of course eg. CZ2002
   * @return true if courseID exists in database, false otherwise.
   */
    public boolean ifCourseExists(String courseID){
        return cm.ifCourseExists(courseID);
    }
     /**
   * This method is used to check if the course exists,
   * it calls the course manager which contains a list
   * of courses
   * @param courseID courseID of course eg. CZ2002
   * @param courseIndex course index of course eg 2801
   * @return true if course which have specific courseID and courseIndex exists in database, false otherwise.
   */
    public boolean ifCourseExists(String courseID, int courseIndex) {
        return cm.ifCourseExists(courseID, courseIndex);
    }
    
    
    
    
    /** 
     * @return String of username of the admin
     */
    public String getUsername() {
        return userName;
    }


    /**
   * This method passes the parameters to change 
   * student access period to student manager class
   * @param course a specific specialisation eg. DSAI
   * @param startDate start date of the specialisation
   * @param startTime start time of the specialisation
   * @param endDate end date of the specialisation
   * @param endTime end time of the specialisation
   */
    public  void changeStudentAccess(String course, String startDate, String startTime, String endDate, String endTime){
        sm.changeStudentAccess(course, startDate, startTime, endDate, endTime);
    }

    /**
   * This method passes the parameters to add 
   * student to student manager class
   * @param lastName last name of student
   * @param firstName first name of student
   * @param username username of student
   * @param gender gender of student
   * @param nationality nationality of student
   * @param courseOfStudy specialisation
   * @param matricNum matriculation number of student
   */
    public void addStudent(String lastName, String firstName, String username, String gender, String nationality, String courseOfStudy, String matricNum){
        
        sm.addStudent(lastName, firstName, username, gender, nationality, courseOfStudy, matricNum);
                
    }

     /**
   * This method passes the parameters to change 
   * add course to course manager class
   * @param courseID course ID of the course
   * @param school school of the course eg. SCSE
   * @param courseType course type eg. GERPE/CORE
   * @param AU number of AUs
   * @param courseIndex course index of the course
   * @param courseSize size of the course
   * @param lessonList string array of lessons that the course offers
   */
    public void addCourse(String courseID, String school, String courseType, int AU, int courseIndex, int courseSize, String[] lessonList){
        
        cm.addCourse(courseID, school, courseType, AU, courseIndex, courseSize, lessonList);
        
    }
      /**
   * This method passes the parameters to check 
   * if students exists in student manager class
   * @param userName username of the student
   * @param matricNo matriculation number of the students
   * @return true if students exist, false otherwise
   */
    public boolean ifStudentExists(String userName, String matricNo){
        return (sm.ifStudentExist( userName,  matricNo));
    }
    /**
   * This method communicates with course manager class
   * to print all courses if they have the same course ID
   * @param courseID courseID of the course 
   */
    public void getCourse(String courseID){
        CourseManager.printCoursesBy(courseID);
    }
    /**
   * This method communicates with course manager class
   * to check course vacancy of a particular course
   * @param courseID courseID of the course 
   * @param courseIndex course index of the course 
   */
    public  void checkCourseVacancy(String courseID, int courseIndex)
    {
        cm.checkCourseVacancy(courseID, courseIndex);
    }
    /**
   * This method communicates with student manager class
   * to print all students in a particular courseID and index
   * @param courseID courseID of the course 
   * @param courseIndex course index of the course 
   */
    public  void printStudentByIndex(String courseID, int courseIndex) {
        StudentManager.printStudentByIndex(courseID, courseIndex);
    }
    /**
   * This method communicates with student manager class
   * to print all students in a particular courseID 
   * @param courseID courseID of the course 
   */
    public  void printStudentsByCourse(String courseID)
    {
        sm.printStudentsByCourse(courseID);
    }

    /**
   * This method communicates with course manager class
   * to update courses 
   * @param courseID courseID of the course to be changed
   * @param school school of the course to be changed
   * @param oldCourseIndex course index of the course to be changed
   * @param newCourseIndex course index of the course to be changed to
   * @param courseSize course size of the course to be changed to
   * @param newLesson lessons of the course to be changed to
   * @param courseType course type of the course to be changed to
   * @param AU au of the course to be changed to
   */
    public void updateCourse(String courseID, String school, int oldCourseIndex, int newCourseIndex, int courseSize, String[] newLesson, String courseType, int AU){
        
        if (cm.updateCourse(courseID, school, oldCourseIndex , newCourseIndex, courseSize, courseType, AU,  newLesson)) ;
    }

    

    public void printCoursesByID(){
        CourseManager.printCourses("id");
    }

    public void printCoursesByIndex(){
        CourseManager.printCourses("index");
    }

}



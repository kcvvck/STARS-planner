


import java.time.*;
import java.time.format.*;
import java.util.*;

/**
* <h1>AdminApp Boundary class</h1>
* User interface of admin
* <p>
* 1. Change student access period
* 2. Add student 
* 3. Add course
* 4. Update course
* 5. Check vacancy for course
* 6. Prints student list if students are in courseID e.g. CZ2002
* 7. Prints student list if students are in courseID and course index e.g CZ2002 12345
* 8. Prints course list by ID
* 9. Prints course list by courseID and course index
* @author  Joelle Thng, Wang Anyi, Ong Lee Hui Grace
* @version 1.0
* @since   2020-25-11
*/


public class AdminApp implements IMainInterface{
    /** 
    * admin instance that initialised this interface
    */
    private Admin a;
   /** 
    * available schools for error checking when new course is added
    */
   private String[] listOfSchools = { "SPMS", "SCSE", "CEE", "ADM" ,"SSS"};
   private List<String> schoolsList = Arrays.asList(listOfSchools);
   /** 
    * available specialisations for error checking when new student is added
    */
   private String[] listOfSpecialisations = { "DSAI", "CS", "REP", "CE", "MATH", "CHEM", "MAE" };
   private List<String> specialisationsList = Arrays.asList(listOfSpecialisations);
   /** 
    * available course types for error checking when new course is added
    */
   private String[] listOfCourseTypes = { "GERPE", "UE", "LA", "CORE", "GERPE/LA", "GERPE/UE" };
   private List<String> courseTypesList = Arrays.asList(listOfCourseTypes);

    /** 
    * available days for lesson addition to new courses for error checking when new course is added
    */
    private String[] days = {"","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private List<String> day = Arrays.asList(days);

    /** 
    * available months for error checking when admin changes access period of student
    */
    private List<String> okDateFormat = Arrays.asList("January", "February", "March", "April", "May", "June","July","August","September","October","November","December");
    
      
    /**
     * user interface created from specific instance of admin
     * @param a
     */
    public AdminApp(Admin a){
        
        this.a = a;
    }
    /** 
    * main display page for admins 
    * contains all options that admin can use to 
    * change, add contents into persistent database
    * view part of the admin system
    * @exception InputMismatchException if choice that is not an integer.
    */
    public void display() {
        /** 
        * initialises all variables to be used locally in this method
        */
        String course, startDate, startTime, endDate, endTime, lastName, firstName, username, gender, nationality, 
                courseOfStudy, matricNum, courseType, school, courseID;
        /** 
        * variables initalised to -1 for error checking
        */
        int AU, numOfIndex, courseIndex, courseSize=-1, oldCourseIndex=-1, newCourseIndex=-1;
        boolean validDate, validTime;
       /** 
        * String array of lessons are added when admin adds course,
        * maximum of three lessons for each course
        */
        String[] lessonList= new String[3];
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        System.out.println("Welcome to administrator interface " + a.getName() + ", we are happy to have you here ^^!");
        
        do{
            System.out.println(
                "=================================================\n"+
                "                      ADMIN\n"+
                "=================================================\n"+
                "(1) Edit student access period\n" +
                "(2) Add a student\n" +
                "(3) Add a course\n" +
                "(4) Update a course\n" +
                "(5) Check vacancy for a course index\n" +
                "(6) Print student list by course\n" +
                "(7) Print student list by course and index number\n" +
                "(8) Print course list by ID\n"+
                "(9) Print course list by index\n"+
                "(10) Exit\n"+
                "=================================================");
            
                /** 
                * takes input of choice, selected by user
                * raises error if choice is not an integer
                */
                try{
                    System.out.println("Enter the number of your choice: ");
                    choice = sc.nextInt();
                } catch(InputMismatchException e){
                    System.out.println("Invalid input. Please try again.\n");
                    choice = 42; 
                } finally{
                    sc.nextLine();
                }

            switch (choice){
                case 1: 
                    /** 
                    * takes input of course of study
                    */
                    System.out.println("Enter course of study of students to change their access(eg.DSAI): ");
                    course = sc.next();
                    if(!specialisationsList.contains(course)){
                        System.out.println("There are no students taking this course of study. Please try again.\n");
                        break;
                    }
                    /** 
                    * takes input of start date to change access period to
                    */
                    validDate = false;
                    sc.nextLine();
                    try{ 
                        System.out.println("Enter new start date(eg. January 23 2020): ");
                        startDate = sc.nextLine();
                        validDate = DateCheck(startDate);
                        if(!validDate) {
                            System.out.println("Invalid date format, please try again.\n");
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Invalid date format, please try again.\n");
                        break;
                    }
                    /** 
                    * takes input of start time to change access period to
                    */
                    validTime=false;
                    try{ 
                        System.out.println("Enter new start time(eg. 11:00): ");
                        startTime = sc.nextLine();
                        validTime = timeCheck(startTime);
                        if(!validTime){
                            System.out.println("Invalid time format, please try again.\n");
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Invalid time format, please try again.\n");
                        break;
                    }
                    /** 
                    * takes input of end date to change access period to
                    */
                    validDate = false;
                    try{ 
                        System.out.println("Enter new end date(eg. January 23 2020): ");
                        endDate = sc.nextLine();
                        validDate = DateCheck(endDate);
                        if(!validDate) {
                            System.out.println("Invalid date format, please try again.\n");
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Invalid date format, please try again.");
                        break;
                    }
                    /** 
                    * takes input of end time to change access period to
                    */
                    validTime=false;
                    try{ 
                        System.out.println("Enter new end time(eg. 23:59): ");
                        endTime = sc.nextLine();
                        validTime = timeCheck(endTime);
                        if(!validTime){
                            System.out.println("Invalid time format, please try again.");
                            break;
                        }
                    } catch (Exception e){
                        System.out.println("Invalid time format, please try again.\n");
                        break;
                    }
                    /** 
                    * convert date&time into datetimeformat to check if end is after start
                    */
                    
                    String startDateTime = startDate + ", " + startTime;
                    String endDateTime = endDate + ", " + endTime;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy, HH:mm", Locale.ENGLISH);
                    LocalDateTime startPeriod = LocalDateTime.parse(startDateTime, formatter);
                    formatter = DateTimeFormatter.ofPattern("MMMM d yyyy, HH:mm", Locale.ENGLISH);
                    LocalDateTime endPeriod = LocalDateTime.parse(endDateTime, formatter);
                    
                    if (endPeriod.isBefore(startPeriod)){
                        System.out.println("Start period should be before the end period. Please try again.");
                        break;
                    }
                    /** 
                    * passes all parameters to admin to change student access period
                    */
                    a.changeStudentAccess(course.trim(), startDate, startTime, endDate, endTime);
                   
                    break;

                case 2: 
                    /** 
                    * Add student
                    */
                    System.out.println("Enter student matriculation number: ");
                    matricNum = sc.nextLine();
                    
                    /** 
                    * check if student exists in the database, break if student exists
                    */
                    System.out.println("Enter student username: ");
                    username = sc.nextLine();
                    if(a.ifStudentExists(username, matricNum)){
                        System.out.println("There is already an existing student with this username/ Matric number");
                        break;
                    }
                    /** 
                    * error check to see if last name contains only alphabets
                    */
                    System.out.println("Enter student last name: ");
                    lastName = sc.nextLine();
                    for(int i=0; i<lastName.length(); i++){
                        char c = lastName.charAt(i);
                        int ascii = (int) c;
                        boolean alphabet = (65<=ascii && ascii<91) || (97<=ascii && ascii<123);
                        if(!alphabet){ 
                            System.out.println("Invalid name. Please enter name consisting of alphabets: ");
                            lastName = sc.nextLine();
                        }
                    }
                    /** 
                    * error check to see if first name contains only alphabets
                    */
                    System.out.println("Enter student first name: ");
                    firstName = sc.nextLine();
                    for(int i=0; i<firstName.length(); i++){
                        char c = firstName.charAt(i);
                        int ascii = (int) c;
                        boolean alphabet = (65<=ascii && ascii<91) || (97<=ascii && ascii<123);
                        if(!alphabet){ 
                            System.out.println("Invalid name. Please enter name consisting of alphabets: ");
                            firstName = sc.nextLine();
                        }
                    }
                    /** 
                    * error check for gender, only 'F' and 'M' accepted
                    */
                    System.out.println("Enter student gender: ");
                    gender = sc.nextLine();
                    while(!gender.equals("F") && !gender.equals("M")){
                        
                        System.out.println("Invalid gender input. Please enter (F) or (M): ");
                        gender = sc.nextLine();
                    }
                    
                    System.out.println("Enter student nationality: ");
                    nationality = sc.nextLine();
                    /** 
                    * error check for specialisation
                    */
                    System.out.println("Enter student course of study (eg. DSAI): ");
                    courseOfStudy = sc.nextLine().trim();
                    if(!specialisationsList.contains(courseOfStudy)){
                        System.out.println("Invalid course of study. Please try again.");
                    }

                    /** 
                    * calls admin to add student
                    */
                    
                    a.addStudent(lastName, firstName, username, gender, nationality, courseOfStudy, matricNum);
                    break;

                case 3: 
                    /** 
                    * Adds course
                    */
                    System.out.println("Enter course ID(eg. CZ2002): ");
                    courseID = sc.next();
                    
                    /** 
                    * check school
                    */
                    System.out.println("Enter school(eg. SPMS): ");
                    school = sc.next();
                    if(!schoolsList.contains(school)){
                        
                        System.out.println("This is not one of the current schools. Please try again.");
                        break;
                    }

                    /** 
                    * check course type
                    */
                    System.out.println("Enter course type (GERPE/UE etc): ");
                    courseType = sc.next();
                    if(!courseTypesList.contains(courseType)){
                        
                        System.out.println("Invalid course type. Please try again.\n");
                        break;
                    }
                    /** 
                    * check course AUs (acceptable range: >0)
                    * raises InputMismatchException if AU != integer
                    */
                    System.out.println("Enter course AU: ");
                    try{
                        AU = sc.nextInt();
                        if(AU<=0) {
                            System.out.println("AU should be positive integer. Please try again.");
                            break;
                        }
                    } catch (InputMismatchException e){
                        System.out.println("Invalid AU, please enter an integer.");
                        break;
                    } finally {
                        sc.nextLine();
                    }
                    /** 
                    * number of indexes to add for course
                    * check number of indexes user adds > 0
                    */
                    System.out.println("How many Indexes do you want to add for " + courseID + "? ");
                    try{
                        numOfIndex = sc.nextInt();
                        sc.nextLine();
                        if (numOfIndex<=0){
                            
                            System.out.println("Number of of indexes should be positive integer. Please try again.");
                            break;
                        } else if (numOfIndex>5){ 
                            System.out.println("Are you sure you want to add "+numOfIndex+" courses? (y/n)");
                            String yes = sc.nextLine();
                            if (!yes.toUpperCase().equals("Y")) break;
                        }
                    } catch(InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Invalid input, please enter an integer.");
                        break;
                    }
                    /** 
                    * Enter course details for each courses to add
                    * raises exception if not integer
                    * 3 lessons needs to be added to every course
                    */
                    for(int i=0; i<numOfIndex; i++){
                        try{
                            System.out.println("Enter course index: (e.g.10012)");
                            courseIndex = sc.nextInt();
                        } catch(InputMismatchException e){
                            System.out.println("Invalid course index. Please enter an integer.");
                            break;
                        } finally {
                            sc.nextLine();
                        }
                        
                        try{
                            System.out.println("Enter course size for index "+ courseIndex+ ": ");
                            courseSize =sc.nextInt();
                        } catch(InputMismatchException e){
                            System.out.println("Invalid course size. Please enter an integer.");
                            break;
                        } finally {
                            sc.nextLine();
                        }

                        lessonList = new String[3];
                        boolean validLesson = false;
                        for(int j=0; j<3; j++){ 
                            System.out.println("Enter session " + (j+1) +  " in the following format: \n" +
                                    "Lecture/LT2/Friday/11:00/12:00");

                            lessonList[j] = sc.nextLine();
                            try{
                                String[] lesson = lessonList[j].split("/");
                                new Lesson(lesson[0], lesson[1], day.indexOf(lesson[2]), lesson[3], lesson[4]);
                                validLesson = true;
                            }catch(Exception e){
                                System.out.println("Invalid format. Please try again.");
                                break;
                            }
                        }
                        if (!validLesson) break;
                        /** 
                        * add course after each index has been added
                        */
                        
                        a.addCourse(courseID, school, courseType, AU, courseIndex, courseSize, lessonList);
                    }
                    break;

                case 4: 
                    /** 
                    * updates course 
                    */
                    System.out.println("Enter courseID to update: ");
                    courseID = sc.next();
                    if(!a.ifCourseExists(courseID)) {
                        System.out.println("This course does not exist.");
                        break;
                    }
                    /** 
                    * display all the course information
                    */
                    a.getCourse(courseID); 

                    /** 
                    * @see case3
                    */
                    System.out.println("Enter (new)school: ");
                    school = sc.next();

                    System.out.println("Enter (new)course type (GERPE/UE etc): ");
                    courseType = sc.next();
                    if(!courseTypesList.contains(courseType)){
                        System.out.println("Invalid course type. Please try again.\n");
                        break;
                    }
                    
                    System.out.println("Enter (new)course AU: ");
                    try{
                        AU = sc.nextInt();
                        if(AU<=0) {
                            System.out.println("AU should be positive integer. Please try again.");
                            break;
                        }
                    } catch (InputMismatchException e){
                        System.out.println("Invalid AU, please enter an integer.");
                        break;
                    } finally {
                        sc.nextLine();
                    }
                    
                    System.out.println("Enter number of indexes to update in "+ courseID + " (between 1-3):");
                    try{
                        numOfIndex = sc.nextInt();
                        sc.nextLine();
                        if (numOfIndex<=0 ){
                            System.out.println("Not within acceptable range. Please try again.");
                            break;
                        } else if (numOfIndex>3){ 
                            System.out.println("Input is more than current number of course indexes. Please try again.");
                            break;
                        }
                    } catch(InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Invalid input, please enter an integer.");
                        break;
                    }

                    
                    boolean validLesson = false;
                    for (int i = 0; i < numOfIndex; i++) {
                        System.out.println("Enter index to change "+ (i+1) + " :");                        
                        try{
                            oldCourseIndex = sc.nextInt();
                            if(!a.ifCourseExists(courseID, oldCourseIndex)){
                                System.out.println("This course does not exist.");
                                break;
                            }
                        } catch(InputMismatchException e){
                            System.out.println("Invalid course size. Please enter an integer.");
                            break;
                        } finally {
                            sc.nextLine();
                        }

                        System.out.println("Enter new index "+ (i+1) + " :");
                        try{
                            newCourseIndex = sc.nextInt();
                        } catch(InputMismatchException e){
                            System.out.println("Invalid course index. Please enter an integer.");
                            break;
                        } finally {
                            sc.nextLine();
                        }

                        try{
                            System.out.println("Enter course size for index "+ newCourseIndex+ ": ");
                            courseSize =sc.nextInt();
                        } catch(InputMismatchException e){
                            System.out.println("Invalid course size. Please enter an integer.");
                            break;
                        } finally {
                            sc.nextLine();
                        }
                        
                        validLesson = false;
                        int count = 0;
                        for(int j=0; j<3; j++){ 
                            System.out.println("Enter session " + (j+1) +  " in the following format \'Lecture/LT2/Friday/11:00/12:00': ");

                            lessonList[j] = sc.nextLine();
                            try{
                                String[] lesson = lessonList[j].split("/");
                                new Lesson(lesson[0], lesson[1], day.indexOf(lesson[2]), lesson[3], lesson[4]); 
                                validLesson = true;
                                count = j;
                            }catch(Exception e){
                                System.out.println("Invalid format. Please try again.");
                                break;
                            }
                        }
                        if (validLesson && count==2){
                            /** 
                            * connects to admin to update course 
                            */
                            a.updateCourse(courseID, school,  oldCourseIndex, newCourseIndex, courseSize, lessonList, courseType, AU);
                        }
                        if (!validLesson) break;
                    }
                    if(!validLesson) break;
                    
                    break;
                case 5: 
                    /** 
                    * check course vacancy for a valid course
                    * raises InputMismatchException if not integer
                    */
                    System.out.println("Enter Course ID: ");
                    courseID = sc.next();
                    System.out.println("Enter Course Index: (e.g.1302)");
                    try{
                        courseIndex = sc.nextInt();
                    } catch(InputMismatchException e){
                        System.out.println("Invalid course index. Please enter an integer.");
                        break;
                    } finally {
                        sc.nextLine();
                    }
                    /** 
                    * connects to admin to check course vacancy
                    */
                    a.checkCourseVacancy(courseID, courseIndex);
                    break;

                case 6: 
                    /** 
                    * Print all students in valid course ID
                    */
                    System.out.println("Enter Course ID to print student list:");
                    courseID = sc.next();
                    if(!a.ifCourseExists(courseID)) {
                        System.out.println("This course does not exist.");
                        break;
                    }
                    System.out.println("Students by Course");
                    /** 
                    * connects to admin to print student list
                    */
                    a.printStudentsByCourse(courseID);
                    break;
                case 7: 
                    /** 
                    * Print all students in valid course 
                    * raises InputMismatchException is course index input != integer
                    */
                    System.out.println("Enter Course ID: ");
                    courseID = sc.next();
                    if(!a.ifCourseExists(courseID)) {
                        System.out.println("This course does not exist.");
                        break;
                    }

                    System.out.println("Enter Index: ");
                    try{
                        courseIndex = sc.nextInt();
                        if(!a.ifCourseExists(courseID, courseIndex)){
                            System.out.println("This course index does not exist.");
                            break;
                        }
                    } catch(InputMismatchException e){
                        System.out.println("Invalid course index. Please enter an integer.");
                        break;
                    } finally {
                        sc.nextLine();
                    }

                    System.out.println("Students by Course and Index");
                    /** 
                    * connects to admin to print student list
                    */
                    a.printStudentByIndex(courseID, courseIndex);
                    break;
                case 8: 
                    /** 
                    * Print course list by ID
                    */
                    a.printCoursesByID();
                    break;
                case 9: 
                    /** 
                    * Print course list by index number
                    */
                    a.printCoursesByIndex();
                    break;
                case 42:
                    /** 
                    * special case
                    */
                    break;
                    
            }
            /** 
            * keeping scanner open so when going back to MainUI it will still work
            */
        }while(choice < 10 || choice==42);
        System.out.println("\nLogging out.\n\n\n\n");
    }

    
    /** 
     * checks for date input for changing student access
     * @param dateString e.g "January 23 2020"
     * @return boolean true if date formatted as above, false if not
     */
    private boolean DateCheck(String dateString){
        String[] str = dateString.split(" ");
        if (!okDateFormat.contains(str[0])){
            return false;
        }
        if(Integer.parseInt(str[1])< 1 ||Integer.parseInt(str[1])> 31){
            return false;
        }
        if(Integer.parseInt(str[2]) <2020){
            return false;
        }
        return true;
    }

    
    /** 
     * checks for time input for changing student access 
     * @param timeString e.g 11:00, 23:59
     * @return boolean true if time formatted as above, false if not
     */
    private boolean timeCheck(String timeString){
        String[] str = timeString.split(":");
        if (str.length<2 || str.length>2) return false;
        if (Integer.parseInt(str[0]) < 0 || Integer.parseInt(str[0]) > 24){
            return false;
        }
        return true;
    }

    

}
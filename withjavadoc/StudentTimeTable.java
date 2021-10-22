import java.util.*;
import java.time.*;

/**
 * <h1> Student Timetable entity </h1>
 * Represents the timetable of a student
 * each student has an instance of StudentTimeTable
 * @author Ong Hui Lee Grace
 * @version 1.0
 * @since 2020-25-11
 */
public class StudentTimeTable {
    /**
     * Arraylist of Course objects taken by Student
     */
    private ArrayList<Course> coursesTaken;
    /**
     * Arraylist of CourseTimeTables which contains ArrayList of Lessons
     */
    private ArrayList<CourseTimeTable> courseTimeTables = new ArrayList<CourseTimeTable>();
    /**
     * Arraylist containing ArrayList of LocalTime
     */
    private static ArrayList<ArrayList<LocalTime>> timeIntervals = new ArrayList<ArrayList<LocalTime>>();
    /**
     * Arraylist of Array of strings
     */
    private static ArrayList<String[]> printedTimeTable = new ArrayList<String[]>();
    /**
     * Hashmap storing lesson object [l1:l2, l3:l5]] ==> l1 clash with l2, l3 clash with l5 etc.
     */
    private static HashMap<Lesson, Lesson> clashingLessons = new HashMap<Lesson,Lesson>();

    /**
     * adds coursestaken and compiles them
     * @param coursesTaken Arraylist of course objects taken by student
     */
    public StudentTimeTable(ArrayList<Course> coursesTaken) {
        this.coursesTaken = coursesTaken;
        compileTimeTable();
        // checkCoursesClash(); //store clashing courses in hashmap clashingLessons
    }

    /**
     * compiles timetables of courses taken
     * compiles time table every time changes are made to lessons taken
     */
    private void compileTimeTable() {
        this.courseTimeTables = new ArrayList<CourseTimeTable>();
        for (Course course : coursesTaken) {
            this.courseTimeTables.add(course.getTimeTable());
        }
    }


    /**
     * updates timetable when student adds course
     * @param c Course added
     */
    public void addCourse(Course c){
        this.coursesTaken.add(c);
        compileTimeTable();
    }

    /**
     * updates timetable when student drops course
     * @param c Course dropped
     */
    public void dropCourse(Course c){
        this.coursesTaken.remove(c);
        compileTimeTable();
    }

    /**
     * checking if courses clash and updating the hashmap of clashingLessons
     */
    public void checkCoursesClash(){
        clashingLessons = new HashMap<Lesson,Lesson>(); //initialise to nothing, then update everytime
        if (clashingLessons.size()==0){
            for (CourseTimeTable ctt : courseTimeTables){
                for (Lesson l : ctt.getTimetable()){
                    for (CourseTimeTable ctt2 : courseTimeTables){
                        for (Lesson l2 : ctt2.getTimetable()){
                            if(!l.equals(l2)){
                                if(l.checkClash(l2)) {
                                    if (clashingLessons.get(l2)==null || !clashingLessons.get(l2).equals(l)) {
                                        clashingLessons.put(l,l2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * print courses
     */
    public void printCourses(){
        for (Course c : coursesTaken) System.out.println(c.getCourseID()+"|"+c.getCourseIndex()+"~"+c.getVacancy());
        if (clashingLessons.size()==0){
            
            for (CourseTimeTable ctt : courseTimeTables){
                System.out.println(ctt.getCourseID()+"/"+ctt.getCourseIndex());
                
            }
        }
    }


    /**
     * prints lessons that clash
     */
    public void printClashingCourses(){
        checkCoursesClash();
        if (clashingLessons.size()!=0){
            System.out.println("The following courses clash:\n");
            for (Lesson l : clashingLessons.keySet()){
                Lesson l2 = clashingLessons.get(l);
                System.out.println(l.courseID+"/"+l.courseIndex+"\tclashes with\t" + l2.courseID+"/"+l2.courseIndex);
                System.out.println(l.day+"\t\t\t\t"+l2.day);
                System.out.println(l.lesson_type+"/"+l.venue+" \t\t\t"+l2.lesson_type+"/"+l.venue);
                System.out.println(l.start+"-"+l.end+"\t\t\t"+l2.start+"-"+l2.end);
                System.out.println();
            }
        }else System.out.println("No clashing courses. Good timetable.");
    }

    /**
     * prints weekly timetable of a student
     * consists of monday to saturday and multiple 30min time intervals each day
     */
    public void printTimeTable() {
        checkCoursesClash();
        printedTimeTable = new ArrayList<String[]>();
        createTimeIntervals();
        initialisePrintTimeTable();
        System.out.println(
            "\nYour Timetable:\n"+
            "============================================================================================================================================================\n"+
            "| TIME\\DAY |          MON          |         TUES          |          WED          |          THURS        |          FRI          |          SAT          |\n"+
            "============================================================================================================================================================");
        for (ArrayList<LocalTime> timeInterval : timeIntervals) { //line by line (each time interval)
            for (CourseTimeTable ctt : courseTimeTables) {
                for (Lesson l : ctt.getTimetable()){    //check all timetable lessons
                    for (int day=1; day<6; day++){  //check column by column  
                        int row = timeIntervals.indexOf(timeInterval);                                                    
                        if (l.start.equals(timeInterval.get(1)) && l.day.getValue()==day) { //lesson start
                            if (row!=0) printedTimeTable.get(row)[day]=     "_______________________";
                        } else if (l.start.equals(timeInterval.get(0)) && l.day.getValue()==day) { //if lesson starts at this time
                            if (clashingLessons.keySet().contains(l)){ //write clashing lesson details in next row
                                Lesson l2 = clashingLessons.get(l);
                                printedTimeTable.get(row+1)[day] = centerString(23, "!!"+printDetails(l2)+"!!");
                            }
                            if (!printedTimeTable.get(row)[day].contains("/")){ //don't overwrite if prev row alr has stuff
                                printedTimeTable.get(row)[day] = centerString(23, printDetails(l));
                            }
                        } else if ((l.end.equals(timeInterval.get(1))|| l.end.isAfter(timeInterval.get(0))&&l.end.isBefore(timeInterval.get(1)) ) && l.day.getValue()==day) {
                            if (!printedTimeTable.get(row)[day].contains("/")){
                                String overline ="";
                                for (int i=0; i<23; i++) overline+=("\u203E"); //opposite of underscore character
                                printedTimeTable.get(row)[day]=overline;
                            }
                        }
                    }
                }
            }
        }
        for(String[] line : printedTimeTable) System.out.println(String.join("|",line)+"|");        
        System.out.println("============================================================================================================================================================");
    }

//======these functions are just to make and print the timetable=============================================================

    /**
     * create list of timeintervals {{08:30,09:30}, {09:30,10:00}, {10:00,10:30}} etc.
     */
    private static void createTimeIntervals() {
        ArrayList<LocalTime> interval = new ArrayList<LocalTime>();
        interval.add(LocalTime.parse("08:30"));
        interval.add(LocalTime.parse("09:00"));

        timeIntervals.add(interval);
        for (int i = 1; i < 17; i++) {
            LocalTime startInterval = timeIntervals.get(i - 1).get(0).plusMinutes(30);
            LocalTime endInterval = timeIntervals.get(i - 1).get(1).plusMinutes(30);
            timeIntervals.add(new ArrayList<LocalTime>(Arrays.asList(startInterval, endInterval)));
        }
    }

    /**
     * initialise empty timetable
     * each line is just "08:30-09:00" and a bunch of empty strings "            "
     */
    private static void initialisePrintTimeTable(){
        if (printedTimeTable.size()==0){
            for (int i =0; i<17; i++){
                String[] line = new String[7];
                String timeinterval = timeIntervals.get(i).get(0).toString() + "-" + timeIntervals.get(i).get(1).toString();
                line[0] = timeinterval;
                for (int j=1; j<7; j++){
                    line[j]= "                       ";
                }
                printedTimeTable.add(line);
            }
        }
    }

    /**
     * prints details of a specific lesson
     * @param l Lesson to be printed
     * @return details of lesson (eg.RA1001/LAB/HWLAB1)
     */
    private static String printDetails(Lesson l){
        String lesson = l.lesson_type.equals("Tutorial") ? "TUT" : (l.lesson_type.equals("Lab") ? "LAB" : "LEC");
        String details = l.courseID+"/"+lesson+"/"+l.venue;
        return details;
    }

    /**
     * formats a string and centralise it e.g. "   hello    "
     * @param width width of block(spaces+string)
     * @param s text string to include
     * @return string of centralised text (eg. "   hello    ")
     */
    private static String centerString (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
//======================================================================================================

    
    
}

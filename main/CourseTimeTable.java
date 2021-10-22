import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.Iterator;

/**
 * <h1>CourseTimeTable Entity Class</h1>
 * This represents the Course timetable of the course, Every course has it's own timetable
 * contains lesson details, along with course ID and index to identify it
 * Actually the lesson details are inside the Lesson class, and this timetable contains an array of Lesson objects
 * Each lesson represents a Lecture or Lab or Tutorial session etc., and CourseTimeTable contains all of them in an array
 * e.g. 
 * Lesson 1: "Lecture/LT2/Friday/11:00/12:00", 
 * Lesson 2: "Lab/LT2/Wednesday/11:00/12:00", 
 * Lesson 3: "Tutorial/LT2/Saturday/11:00/12:00"
 * @author Ong Hui Lee Grace
 * @version 1.0
 * @since 2020-25-11
 */

public class CourseTimeTable{
    /**
     * Course index of the Course that this timetable belongs to
     */
    private int courseIndex;
    /**
     * Course ID of the Course that this timetable belongs to
     */
    private String courseID;
    /**
     * The actual timetable containing an ArrayList of Lesson objects, which contains the details like time, venue, day etc.
     */
    private ArrayList<Lesson> timeTable; //Set of lab,tut,lec Lessons(contains venue, day, time)
    /**
     * String[] containing the days of the week "Monday","Tuesday","Wednesday" etc.
     */
    String[] days = {"","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    /**
     * Converting the String[] of days into a List<String>
     * Lesson class takes in the integer value of the day (it uses java.time which parses an integer to dayOfWeek format)
     * but in the CSV file we store the details as "Tutorial/LT2/Saturday/11:00/12:00", as "Saturday" is easier to read and understand than 6
     * so when creating Lesson objects we use day.indexOf("Monday") 
     */
    List<String> day = Arrays.asList(days);
    
    /**
     * Constructor for CourseTimeTable
     * @param c Course the CourseTimeTable belongs to
     */
// input from admin
    public CourseTimeTable(Course c){
        this.courseIndex = c.getCourseIndex();
        this.courseID = c.getCourseID();
    }
    /**
     * Another constructor for CourseTimeTable, input is the line from the csv,
     * splits "Tutorial/LT2/Saturday/11:00/12:00" into String array, then create Lesson objects to add into the timetable
     * @param c         Course that this timetable belongs to
     * @param sessions  e.g. {"Lecture/LT2/Friday/11:00/12:00", "Tutorial/LT2/Friday/11:00/12:00", "Lab/LT2/Friday/11:00/12:00"}
     */
// input from csv
    public CourseTimeTable(Course c, List<String> sessions){ 
        this.courseIndex = c.getCourseIndex();
        this.courseID = c.getCourseID();
        int i=0, j=1, k=2;
            //if input is just the String[3]={"Lecture/LT2/Friday/11:00/12:00", "Tutorial/LT2/Friday/11:00/12:00", "Lab/LT2/Friday/11:00/12:00"}
            
        ArrayList<Lesson> timetable = new ArrayList<Lesson>();
        String[] Session1 = sessions.get(i).split("/");
        Lesson lesson1 = new Lesson(Session1[0], Session1[1], day.indexOf(Session1[2]), Session1[3], Session1[4]);
        String[] Session2 = sessions.get(j).split("/");
        Lesson lesson2 = new Lesson(Session2[0], Session2[1], day.indexOf(Session2[2]), Session2[3], Session2[4]);
        String[] Session3 = sessions.get(k).split("/");
        Lesson lesson3 = new Lesson(Session3[0], Session3[1], day.indexOf(Session3[2]), Session3[3], Session3[4]);
        timetable.add(lesson1);
        timetable.add(lesson2);
        timetable.add(lesson3);
        this.timeTable = timetable;
        setTimeTableCourseID_Index();
    }


    /**
     * Constructor using an ArrayList of Lesson objects, it just sets the timetable as the ArrayList 
     * @param c         Course that this CourseTimeTable belongs to
     * @param timeTable ArrayList of Lesson objects that the timetable will contain
     */
    public CourseTimeTable(Course c, ArrayList<Lesson> timeTable){ // can put all the info into excel sheet with each row having a specific duration 
        // so if the input duration is within those ranges, we know exactly which row it is to check
        this.timeTable = timeTable; // each row have columns representing the venue, type of class etc
        setTimeTableCourseID_Index();
    }

    /**
     * Sets the Lesson object's courseID and courseIndex to the same as the Course
     */
    // public Lesson 
    public void setTimeTableCourseID_Index(){
        for (Lesson l : timeTable){
            l.setCourseID(courseID);
            l.setCourseIndex(courseIndex);
        }
    }

    /**
     * Empty the timetable, used when updating the Course details
     */
    public void removeAll(){
        Iterator<Lesson> l = timeTable.iterator();
        while(l.hasNext()) {
            l.next();
            l.remove();
        }
        
    }

    
    /** 
     * Gets the course index of this CourseTimetable, so can identify which Course object this CourseTimeTable belongs to
     * @return int value of the course index
     */
    public int getCourseIndex() {
        return courseIndex;
    }
    
    
    /** 
     * Sets the course ID of the CourseTimeTable
     * @param courseID The course ID that you want to set
     */
    public void setcourseID(String courseID){
        this.courseID = courseID;
    }

    
    /** 
     * Gets the course ID of the CourseTimeTable
     * @return String value of course ID
     */
    public String getCourseID(){
        return courseID;
    }

    
    /** 
     * When admin tries to add a new timeslot/lesson, this method checks if the details clash with any of the existing lessons in the Course's timetable
     * Loops through all of the Lesson objects and compares the timing
     * @param lesson    Lesson type e.g. Lecture/Lab/Tutorial
     * @param venue     Venue where the lesson will be held, e.g. LT2, TR+30 etc.
     * @param day       integer representing the day e.g. 1=Monday, 2=Tuesday, 7=Sunday etc.
     * @param startTime What time the lesson starts (String, will be parsed into LocalTime format)
     * @param endTime   What time the lesson ends (String, will be parsed into LocalTime format)
     * @return boolean true if clash, else false
     */
    private boolean checkClash(String lesson, String venue, int day, String startTime, String endTime){
 
        DayOfWeek dWeek = DayOfWeek.of(day);
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        for (Lesson l : timeTable){
            if(l.getDay().equals(dWeek)){
                if(!l.getStart().isAfter(end) && !l.getEnd().isBefore(start)){
                    return true;
                }
            }
        }
        return false;
    }


    
    /** 
     * Adding a new Lesson object to the timetable
     * @param lesson    Lesson type e.g. Lecture/Lab/Tutorial
     * @param venue     Venue where the lesson will be held, e.g. LT2, TR+30 etc.
     * @param dayOfWeek String day e.g."Monday", "Tuesday" when the lesson is held
     * @param startTime What time the lesson starts (String, will be parsed into LocalTime format)
     * @param endTime   What time the lesson ends (String, will be parsed into LocalTime format)
     */
    public void addTimeSlot(String lesson, String venue, String dayOfWeek, String startTime, String endTime) { // time format must be 24 hr hh:mm format
        int dayInt = day.indexOf(dayOfWeek);
        // System.out.println("day INt in add time slot is : " + dayInt);
        if(!checkClash( lesson,  venue,  dayInt,  startTime,  endTime)){
            timeTable.add(new Lesson( lesson,  venue,  dayInt,  startTime,  endTime));
            System.out.println("Time slot added successfully!");
        }else{
            System.out.println("Oops, there is a timetable clash with the other lessons in "+ courseID+"/"+courseIndex+"!");
        }
        
    }

    
    /** 
     * Remove a Lesson from the timetable list of lessons
     * @param lesson    lesson type e.g.Lab/Lecture/Tutorial
     * @param venue     venue where the lesson is held, e.g. LT2, TR+30 etc.
     */
    public void removeTimeSlot(String lesson, String venue) { // time format must be 24 hr hh:mm format
        Lesson temp = getLesson(lesson, venue);
        if(temp !=null){
            timeTable.remove(temp);
            System.out.println("time slot successfully removed!");
        }
        
        
    }

    
    /** 
     * Get Lesson with a specified lesson type and venue
     * @param lesson    lesson type e.g. Lab/Lecture/Tutorial
     * @param venue     venue where the lesson is held, e.g. LT2, TR+30 etc.
     * @return Lesson object with the given lesson type and venue
     */
    private Lesson getLesson(String lesson, String venue){
        for (Lesson l : timeTable) {
            if(l.getVenue().equals(venue)){
                if(l.getLessonType().equals(lesson)){
                    return l;
                }
            }
        }System.out.println("Lesson does not exist!");
        return null;
    }
    

    /**
     * Prints the schedule of the CourseTimeTable, all the lesson details of each Lesson
     */
    public void printSchedule(){
        for (int j=0; j<timeTable.size(); j++){  //usually 3 lessons (lab,lec,tut)
            System.out.print(timeTable.get(j).lesson_type+ "\t");
            System.out.print(timeTable.get(j).venue + "\t");
            System.out.print(timeTable.get(j).day + "\t");
            System.out.print(timeTable.get(j).start + "\t");
            System.out.print(timeTable.get(j).end + "\n");
        }
    
    }

    
    /** 
     * Gets the timetable of the CourseTimeTable
     * @return ArrayList&lt;Lesson> list of Lesson objects containing the lesson details
     */
    public ArrayList<Lesson> getTimetable(){
        return timeTable;
    }


}

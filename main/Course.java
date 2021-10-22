

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
/**
 * <h1>Course Entity Class</h1>
 * Represents a single course index of a module, so according to our naming convention, 
 * there would usually be multiple Course objects, with same Course name and ID, but different Course index
 * @author Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */


public class Course { 
	/**
	 * Course ID of the Course (e.g.CZ2002)
	 */
	private String courseID; // CZ2002  
	/**
	 * Course Index of the Course (e.g. 10113), it is unique for all Courses with the same name
	 * e.g. there are 3 Course objects, with CourseID=CZ2002, but Course indexes 10113,10114,10115
	 * and each Course has different students enrolled in it
	 */
	private int courseIndex;
	/**
	 * Timetable of the course, which calls the CourseTimeTable class, basically contains all the lesson details
	 */
	private CourseTimeTable timetable;
	/**
	 * How many AUs this module is
	 */
	private Integer AU;
	/**
	 * The course has its own waitlist that queues students when they add/drop a Course
	 */
	private WaitList wl;
	/**
	 * Course Size, capacity for how many students the course can take, vacancy is managed by the waitlist wl
	 */
	private int courseSize;
	/**
	 *The course type, e.g. "GERPE", "UE", "LA", "CORE", "GERPE/LA", "GERPE/UE" etc.
	 */
	private String courseType;
	/**
	 * School the course is under, e.g. SPMS,SSS,SCSE etc.
	 */
	private String school;
	/**
	 * Arraylist containing all the Student objects which are registered to this course
	 */
	private ArrayList<Student> registeredStudents;

	/**
	 * Creates a new Course by inputting a row from the csv
	 * @param courseID 		The course ID of this course (e.g. PH1900)
	 * @param school 		The school the course is under (e.g. SPMS)
	 * @param courseIndex	The course index of this course (e.g. 10113)
	 * @param vacancy		The vacancy of the course
	 * @param courseSize	The max capacity of the course
	 * @param sessions		List of lesson details to call the CourseTimeTable constructor and create timetable
	 * @param courseType	The course type of the course (e.g. UE/GERPE/CORE etc.)
	 * @param AU			The number of AUs this module is
	 */
	// from csv
	public Course(String courseID, String school, int courseIndex, int vacancy, int courseSize, List<String> sessions, String courseType, int AU){
		this.courseID = courseID;
		this.courseIndex = courseIndex;
		this.courseType = courseType;
		this.setSchool(school);
		this.setAU(AU);
		this.courseSize = courseSize;
		this.registeredStudents = new ArrayList<Student>(courseSize);
		this.wl = new WaitList(this, courseSize);
		this.timetable = new CourseTimeTable(this, sessions);
		this.setVacancy(vacancy);
	}
	
	/**
	 * Overloading another constructor to create Course
	 * Similar to the previous, just that when Admin creates a new course,
	 * vacancy=courseSize, hence no need the extra parameter vacancy
	 * @param courseID		The course ID of this course (e.g. PH1900)
	 * @param school		The school the course is under (e.g. SPMS)
	 * @param courseType	The course type of the course (e.g. UE/GERPE/CORE etc.)
	 * @param AU			The number of AUs this module is
	 * @param courseIndex	The course index of this course (e.g. 10113)
	 * @param courseSize	The max capacity of the course
	 * @param lessonList	String[] of lessons e.g. {"Lecture/LT2/Friday/11:00/12:00", "Tut/LT2/Tuesday/11:00/12:00","Lab/LT2/Wednesday/11:00/12:00",}
	 */
// from admin
	public Course(String courseID, String school, String courseType, int AU, int courseIndex, 
	int courseSize, String[] lessonList) {
		this.courseID = courseID;
		this.courseType = courseType;
		this.setSchool(school);
		this.courseSize = courseSize;
		this.setAU(AU);
		this.courseIndex = courseIndex;
		this.registeredStudents = new ArrayList<Student>(courseSize);
		this.wl = new WaitList(this, courseSize);
		List<String> sessions = Arrays.asList(lessonList);
		this.timetable = new CourseTimeTable(this, sessions); 
	}


	
	/** 
	 * Gets the school of the course (e.g.SPMS)
	 * @return String value of School e.g."SPMS"
	 */
	public String getSchool() {
		return school;
	}

	
	/** 
	 * Sets the school of the course (e.g.SPMS)
	 * @param school String value of school e.g."SBS"
	 */
	public void setSchool(String school) {

		this.school = school;
	}


	
	/** 
	 * Gets the number of AUs of the Course
	 * @return int value of AU
	 */
	public int getAU() {
		return AU;
	}

	
	/** 
	 * Sets the number of AUs of the course
	 * @param aU The AU to be set 
	 */
	public void setAU(int aU) {
		this.AU = aU;
	}
	
	
	/** 
	 * Prints the timetable's schedule (all the lesson details, e.g. when lab, lecture, tutorial is)
	 * @param courseindex The course index of this Course
	 */
	public void printSchedule(int courseindex) {
		timetable.printSchedule();
	}

	
	/** 
	 * Gets the course index of the Course
	 * @return int value of course index
	 */
	public int getCourseIndex() {
		return courseIndex;
	}

	
	/** 
	 * Gets the CourseTimeTable object of the Course
	 * mainly used in creating the student's timetable
	 * @return CourseTimeTable that belongs to the Course
	 */
	public CourseTimeTable getTimeTable(){
		return timetable;
	}

	
	/** 
	 * Sets the course index of the Course
	 * @param i Course index to be set
	 */
	public void setCourseIndex(int i) {
		this.courseIndex = i;
	}

	
	/** 
	 * Adding a new lesson into the Course's CourseTimeTable
	 * @param lessons String[] containing the lesson details, e.g. {"Lecture","LT2","Friday","11:00","12:00"}
	 */
	public void addTimeSlot(String[] lessons){
		timetable.addTimeSlot(lessons[0], lessons[1], lessons[2], lessons[3], lessons[4]);
	}

	/**
	 * Empty the Course's CourseTimeTable, used when Admin is updating the Course
	 */
	public void removeAllTimeSlot(){
		timetable.removeAll();
	}

	
	
	
	/** 
	 * Gets the vacancy of the Course, which is stored in the Course's Waitlist wl
	 * @return int value of vacancy
	 */
	public int getVacancy() {
		return wl.getVacancy();
	}
	
	/** 
	 * Sets the vacancy of the Course
	 * @param newVacancy vacancy to be set
	 */
	public void setVacancy(int newVacancy){
		wl.setVacancy(newVacancy);
	}

	
	/** 
	 * Adding a Student s into the list of registered Students
	 * @param s Student to be added
	 */
	public void addStudent(Student s){
		registeredStudents.add(s);
		
	}

	
	/** 
	 * Removing Student s from the list of registered Students
	 * @param s Student to be removed
	 */
	public void removeStudent(Student s){
		registeredStudents.remove(s);
		
	}

	
	/** 
	 * Gets the list of registered Students
	 * @return ArrayList&lt;Student> List of Student objects representing students registered to the course
	 */
	public ArrayList<Student> getRegisteredStudentsList(){ // get names of students registered
		return registeredStudents;
	}

	
	/** 
	 * Sets the course size of the Course
	 * @param newCourseSize The course size to be set
	 */
	public void setCourseSize(int newCourseSize) {
		this.courseSize = newCourseSize;

	}
	
	/** 
	 * Gets the course size of the Course
	 * @return int value of course size
	 */
	public int getCourseSize() {
		return this.courseSize;
	}

	
	/** 
	 * Gets the course ID of the course (e.g. CZ2002)
	 * @return String of courseID
	 */
	public String getCourseID() {
		return courseID;
	}
	
	/** 
	 * sets the course ID of the course
	 * @param courseID The courseID to be set
	 */
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	
	/** 
	 * Adds the Student s into the Course's WaitList wl
	 * @param s Student that is being added to waitlist
	 */
	public void addToWaitList(Student s){
		wl.addToWaitList(s); 
	}
	
	/** 
	 * Removes Student s from the Course's WaitList wl
	 * @param s Student that is being removed from waitlist
	 */
	public void removeFromWaitList(Student s){
		wl.removeFromWaitList(s);
	}

	

	
	/** 
	 * Swap students taking the course but different index
	 * @param s1	Student taking the current course
	 * @param s2	Student who wants to take the course
	 */
	public void swapStudents(Student s1, Student s2){
		registeredStudents.remove(s1);
		registeredStudents.add(s2);
	}

	
	/** 
	 * Gets the size of the waitlist
	 * @return int waitlist size
	 */
	public int getWaitListSize(){
		return wl.getWaitListSize();
	}

	
	/** 
	 * Gets the course type of the Course
	 * @return String courseType (e.g.UE/GERPE etc.)
	 */
	public String getCourseType() {
		return courseType;
	}
	
	/** 
	 * Sets the course type of the course
	 * @param courseType The course type to be set
	 */
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	
	/** 
	 * Unregister the student from the course
	 * @param s Student to be unregistered
	 */
	public void unregisterStudent(Student s){
		wl.unregisterStudent(s);
	}

	
	/** 
	 * Check if a student is in the waitlist
	 * @param s Student to be checked
	 * @return boolean true/false to confirm if student is indeed in waitlist
	 */
	public boolean checkStudentsInWaitList(Student s){
		return wl.checkStudentsInWaitList(s);
	}

	/**
	 * Comparator to compare course IDs of the Course objects
	 * mainly used in printing the courses available, sorting them by course ID
	 */
	public static Comparator<Course> courseIDComparator = new Comparator<Course>() {

		public int compare(Course c1, Course c2) {
		   String courseID1 = c1.getCourseID();
		   String courseID2 = c2.getCourseID();
	
		   return courseID1.compareTo(courseID2);
	
		
	}};

	/**
	 * Comparator to compare course index of the Course objects
	 * mainly used in printing the courses available, sorting them by course index
	 */
	public static Comparator<Course> courseIndexComparator = new Comparator<Course>() {

		public int compare(Course c1, Course c2) {
	
		   int courseIndex1 = c1.getCourseIndex();
		   int courseIndex2 = c2.getCourseIndex();
	
		   return courseIndex1-courseIndex2;
	
	   }};

	
	/** 
	 * Overrides the Object class
	 * Returns string representation of the Course, and makes it easier to print out the Course details
	 * {@inheritDoc}
	 * @return String
	 */
	@Override
    public String toString() {
        return  "\t" + courseIndex + "\t" + getVacancy() + "/"
		+ courseSize + "\t " + courseType + "\t " + AU;
    }


}


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <h1>CourseManager</h1>
 * This class manages the course objects. Any changes made by admin/student to courses through the
 * application will call the functions to update the objects
 * @author Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */

public class CourseManager { // control class
	
	private static CourseDatabaseManager cdm = new CourseDatabaseManager();
	private static List<Course> courses=null;
	private static int result;
	private static Course newCourse;

	/**
	 * Constructor for the CourseManager
	 */
	public CourseManager() {
		if(courses == null){
			courses = CourseDatabaseManager.getAllCourses();
		}
	}

	
	/** 
	 * Returns all courses in List<Course> format, sorted by id
	 * @return List&lt;Course>
	 */

	public static List<Course> getAllCourses(){
		sortBy("id");

		return courses;
	}

	
	/** 
	 * Sorts courses using the Comparators defined in Course
	 * @param index_or_id Which comparator to use. Accepts "index","id", or any string. Defaults to "id".
	 */
	private static void sortBy(String index_or_id){ // sorting
		if (courses == null) {
			courses = CourseDatabaseManager.getAllCourses(); // if we have not initialised courses, then we get it now

		}
		if(index_or_id.equals("index")){
			Collections.sort(courses, Course.courseIndexComparator);
		}else{
			Collections.sort(courses, Course.courseIDComparator);
		}
	}


	
	/** 
	 * Method to create and add a new course if course does not yet exist. (see checkCourses)
	 * The new course is added to the list of Course objects and saved to the CSV using CourseDatabaseManager
	 * @param courseID          Course ID of the course
	 * @param school            School the course is under
	 * @param courseType        Course type of the course (GERPE, UE, CORE)
	 * @param AU                Number of AUs of the course
	 * @param courseIndex		Course index of the course
	 * @param courseSize        The new course size e.g. 20/30
	 * @param lessonList		String array with lesson details
	 */

	 
	public void addCourse(String courseID, String school, String courseType, int AU, int courseIndex, int courseSize,
			String[] lessonList) {
		if (courses == null) {
			courses = CourseDatabaseManager.getAllCourses();

		}
		if (checkCourses(courseID, courseIndex) == -1 || checkCourses(courseID, courseIndex) == 1) { // if course don't
																										// exist
			cdm.addCourse(courseID, school, courseIndex, courseSize, lessonList, courseType, AU);
			System.out.println(courseID+"/"+courseIndex+" created");
			Course temp = new Course(courseID, school, courseType, AU, courseIndex, courseSize, lessonList);
			courses.add(temp);
			
			System.out.println("Course Index added: " + temp.getCourseIndex());
			newCourse = temp; // store it in new courses
			printCourses("id");
		} else
			System.out.println(courseIndex + " already exists.");
	}

	
	/** 
	 * Returns number of vacancies in the course, if course with courseID, and index with courseIndex exists
	 * Else returns -1
	 * @param courseID Course ID of the course
	 * @param courseIndex Course index of the course
	 * @return Number of vacancies. -1 if any input fields are invalid
	 */
	public int checkCourseVacancy(String courseID, int courseIndex) {
		Course course = getCourse(courseID, courseIndex);
		if(course !=null){
			System.out.println("Number of vacancies in " + courseID + " " + courseIndex + " is "
			+ course.getVacancy() + "/" + course.getCourseSize());
			return course.getVacancy();
		}
		System.out.println("We searched high and low but still cannot find the course");
		return -1;
	}

	
	/** 
	 * Updates the course object with the new details
	 * @param courseID			Course ID of the course
	 * @param school			School the course in under
	 * @param oldCourseIndex	Previous course index
	 * @param newCourseIndex	New course index 
	 * @param courseSize 		New course size. Must be more than number of registered students, else the course will not update.
	 * @param courseType		Course type of course (GERPE, UE, CORE)
	 * @param AU				Number of AUs in the course
	 * @param newLesson 		String array of new lesson details
	 * @return boolean whether course was successfully updated. False if unsuccessful, true otherwise.
	 */
	public boolean updateCourse(String courseID, String school, int oldCourseIndex, int newCourseIndex, int courseSize,
			String courseType, int AU, String[] newLesson) {

		//////////////////////////////////
		Course temp = getCourse(courseID, oldCourseIndex);
		if(getCourse(courseID, newCourseIndex)!= null){
			System.out.println("Course you wish to update already exists!");
			return false; 
		}

		//////////Check that new courseSize is more than registered students/////////
		int registeredStudents = temp.getCourseSize() - temp.getVacancy();
		if (registeredStudents > courseSize){
			System.out.println("Course size is smaller than number of registered students. Please enter a larger course size, or drop some students.");
			return false;
		}

		temp.setSchool(school);
		temp.setCourseIndex(newCourseIndex);
		temp.setVacancy(courseSize - registeredStudents);
		temp.setCourseSize(courseSize);
		temp.setCourseType(courseType);
		temp.setAU(AU);
		temp.removeAllTimeSlot();
		for(String lessons : newLesson){
			String[] lessonsSplit = lessons.split("/");
			temp.addTimeSlot(lessonsSplit);
		}
		String courseSizeStr = Integer.toString(courseSize - registeredStudents)+ "/"
				+ Integer.toString(courseSize);
		cdm.updateCourse(courseID, school, oldCourseIndex, newCourseIndex, courseSizeStr, newLesson, courseType, AU);
		return true;
	}

	
	/** 
	 * Update number of vacancy in the course (eg. when student adds/drop course)
	 * @param c				Course to be updated
	 * @param drop_or_add	Whether the student dropped or added the course. Accepts "drop" or "add", otherwise defaults to "add"
	 */
	public void updateVacancy(Course c, String drop_or_add) {
		if(drop_or_add.equals("drop")){
			c.setVacancy(c.getVacancy()+1);
		}else{
			c.setVacancy(c.getVacancy()-1);
		}
		cdm.updateVacancy(c);
	}

	
	/** 
	 * Check if course exists
	 * @param courseID 			Course ID of the course
	 * @param courseIndex 		Course index of the course
	 * @return int 0 if courseID and courseIndex both exist, referring to the same Course object. Else 1 if courseID exists, and -1 otherwise.
	 */
	private int checkCourses(String courseID, int courseIndex) { // if course exists
		result = -1; // result=-1 if don't exist
		for (Course course : courses) {
			if(course.getCourseID().equals(courseID)){
				result += 2; // result=1 if contain courseID
				if (course.getCourseIndex() == courseIndex) {
					result = 0; // result=0 if contain index
					return result;
				}
				
			}
		}
		return result;
	}

	
	/** 
	 * Check if courseID AND courseIndex exists (referring to the same Course object) using checkCourses
	 * @param courseID 			Course ID of the course
	 * @param courseIndex 		Course index of the course
	 * @return boolean True if both courseID and courseIndex exist (referring to the same Course object), else false 
	 */
	public  boolean ifCourseExists(String courseID, int courseIndex) { // check specific course exists

		if (checkCourses(courseID, courseIndex) == 0) {
			return true;
		}
		return false;
	}

	
	/** 
	 * Check if courseID exists using checkCourses
	 * @param courseID 		Course ID of the course
	 * @return boolean True if courseID exists, else false.
	 */
	public boolean ifCourseExists(String courseID) { // check course exist with the name ie "CZ2002"
		if (checkCourses(courseID, 0) > 1) {
			return true;
		} else {
			return false;
		}
	}

	
	/** 
	 * Method to get the number of courses
	 * @return int Number of courses
	 */
	public int getNumberOfCourses() {
		return courses.size();
	}


	/**
	 * Method to print courses, sorted by index or id
	 * Eg. BE1004  10001   18/40    CORE    3
	 * @param index_or_id Whether to sort by "index" or "id". Defaults to "id".
	 */
	public static void printCourses(String index_or_id) { // print courses, sorted by either id or index
		sortBy(index_or_id);
		System.out.println("\n\t\tAll courses offered: \n" + "	    --------------------------------------------\n"
				+ "	    |   Course   | Index | Vacancy | Type | AU |");
		for (Course course : courses) {
			if (newCourse != null && course.equals(newCourse)){
				System.out.print("	*New*	"); // new course indicator
			}else{
				System.out.print("    		");
			}
			System.out.println(course.getCourseID()+"\t" + course.getCourseIndex() + "\t" + course.getVacancy() + "/"
						+ course.getCourseSize() + "\t " + course.getCourseType() + "\t " + course.getAU());
			
		}newCourse = null;
	}



	
	/** 
	 * Method to print course information of a specific courseID
	 * Eg. 10001   18/40    CORE    3
	 * @param courseID courseID of course to print
	 */
	public static void printCoursesBy(String courseID) {

		System.out.println("\nAll courses offered: \n" + " ------------------------------------------\n"
				+ "|   Index | Vacancy | Type | AU |\n"+" ------------------------------------------");
		for (Course course : courses) {
			if(course.getCourseID().equals(courseID)){
				
				System.out.println(course.toString());
			}
			
		}

	}

	
	/** 
	 * Method to get Course object
	 * @param courseID 		Course ID of the course
	 * @param courseIndex	Course index of the course
	 * @return Course object with specified course ID and course index.
	 */
	public Course getCourse(String courseID, int courseIndex) {
		if(courses==null){
			courses = CourseDatabaseManager.getAllCourses();
			
		}
		if (ifCourseExists(courseID, courseIndex)) {
			for (Course temp : courses) {
				if(temp.getCourseID().equals(courseID)){
					if(temp.getCourseIndex() == courseIndex){
						return temp;
						
					}
				}
				
			}
		}else{
			System.out.println("course does not exist!");
			
		}
		return null;
	}

	

	
	/** 
	 * Method to get ArrayList of Course objects with specified courseID
	 * @param courseID 		Course ID of courses to return
	 * @return 				ArrayList&lt;Course> of all courses with the same course ID (eg. BE1004: 10001, 10002, 10003)
	 */
	public ArrayList<Course> getCourse(String courseID) {
		ArrayList<Course> courseList = new ArrayList<Course>();
		if (ifCourseExists(courseID)) {
			for (Course course : courses) {
				if(course.getCourseID().equals(courseID)){
					courseList.add(course);
				}
			}return courseList;
		}else{
			System.out.println("course does not exist!");
		}
		return null;
	}

	
	/** 
	 * Method to print time table of the Course
	 * @param courseID		Course ID of course
	 * @param courseIndex	Course index of course
	 */
	public void getTimeTable(String courseID, int courseIndex){ // get timetable of particular course
		if (ifCourseExists(courseID, courseIndex)==true) {// check courseIndex and courseID exists
			for(Course c: courses){
				if (c.getCourseIndex()==courseIndex){
					c.printSchedule(courseIndex);
				}
			}
		}
	}

	
	/** 
	 * Method to add a new lesson timeslot to Course
	 * @param courseID Course ID of course
	 * @param timeSlot String of timeslot to add e.g. "Lecture/LT2/Friday/11:00/12:00"
	 */
	public void addSlot(String courseID, String timeSlot){ // add time slot for a particular course eg CZ2002
		ArrayList<Course> temp = getCourse(courseID);
		String[] replace = timeSlot.split("/");
		for (Course course : temp) {
			course.addTimeSlot(replace);
		}
		
	}

	// public void register(Student s, String courseID, int courseIndex){
	// 	Course placeholder = getCourse(courseID, courseIndex); // not sure about the argument
	// 	placeholder.addToWaitList(s);
	// 	System.out.println(s.getFirstName() + " has been added into the waitList.");
	// }

	// public void deregister(Student s, String courseID, int courseIndex){
	// 	Course placeholder = getCourse(courseID, courseIndex);
	// 	placeholder.removeFromWaitList(s);
	// 	System.out.println(s.getFirstName() + " has been removed from the waitList.");
	// }
	
	// public void getWaitList(String courseID, int courseIndex){
	// 	Course placeholder = getCourse(courseID, courseIndex);
	// 	System.out.println("Number of waitlisted students in " + courseID + " is " + placeholder.getWaitListSize());
		
	// }
	
}

import java.util.List;
/**
 * <h1>
 *     Student Manager control class
 * </h1>
 * manages logic behind modifications of all student objects
 * connects with course manager, student database manager and student objects to carry out tasks specified by user
 * @author
 * @version 1.0
 * @since 2020-25-11
 */
public class StudentManager {
	/**
	 * List of Student objects
	 */
	protected static List<Student> studentList=null;
	/**
	 * initialise instance of Student database manager
	 */
	private static StudentDatabaseManager sdm;
	/**
	 * initialise instance of Course manager
	 */
	private static CourseManager cm = null;

	/**
	 * constructor to create new Student Manager
	 * instantiates new Course Manager as attribute
	 * reads in list of Students from csv via Student Database manager
	 */
	public StudentManager() {
		sdm = StudentDatabaseManager.getInstance();
		if(studentList==null){
			studentList = StudentDatabaseManager.getAllStudents();
		}
		if(cm == null){
			cm = new CourseManager();
		}
	}

	/**
	 * gets a course from its specified ID and index
	 * @param courseID course ID of course to get
	 * @param courseIndex course Index of course to get
	 * @return Course object
	 */
	public static Course getCourse(String courseID, int courseIndex){
		if(cm == null){
			cm = new CourseManager();
		}
		return cm.getCourse(courseID, courseIndex);
	}


	/**
	 * gets student from matric number
	 * @param matricNumber Matriculation number of student to retrieve
	 * @return Student object if successful
	 * 			null otherwise
	 */
   public static Student retrieveStudent(String matricNumber) {
	   
		for(Student s : studentList) {
			if(s.getMatricNo().equals(matricNumber)){
				return s;
			}
		} System.out.println("Student does not exist.");
		return null;
	}

	/**
	 * @param matricNumber matriculation number of student to check
	 * @return true if student exists, false otherwise
	 */
	public boolean studentExists(String matricNumber){
		return studentList.contains(retrieveStudent(matricNumber));
	}


	/**
	 * calls Student database manager to change student access time
	 * of all students in a specified course of study
	 * @param course course of study (eg. DSAI)
	 * @param startDate start date to change to (should be in the form: November 20 2020)
	 * @param startTime start time to change to (should be in the form: 09:30)
	 * @param endDate end time to change to (should be in the form: November 20 2020)
	 * @param endTime end date to change to (should be in the form: 09:30)
	 */
	public void changeStudentAccess(String course, String startDate, String startTime, String endDate, String endTime){
		sdm.changeStudentAccess(course, startDate, startTime, endDate, endTime);
		System.out.println("Access period of students in " + course + " has been changed to " + startTime + ", " + startDate
                + " till " + endTime + ", " + endDate);
	}


	/**
	 * Prints basic information of all students in list
	 * prints name, matriculation number, gender, nationality
	 */
    public void printStudentInfo(){
		System.out.println("Name: \t\t Matric Num\t Gender\t Nationality");
		if (studentList != null){
			for (Student student : studentList) {
				System.out.println(student.getName() + "\t" + student.getMatricNo()  + " " 
			+ student.getGender() + " " + student.getNationality());		
			}
		}else{ // if no students are registered yet
			System.out.println("no registered students yet");
		}
	}

	/**
	 * adds Student to this studentList
	 * calls Student Database Manager to add Student into CSV
	 * prints all existing students
	 * @param lastName last name of Student to add
	 * @param firstName first name of Student to add
	 * @param userName username of Student to add
	 * @param gender Student's gender (F/M)
	 * @param nationality Student's nationality
	 * @param courseOfStudy Student's course of study (eg. DSAI)
	 * @param matricNo Student's matriculation number
	 */
	public void addStudent(String lastName, String firstName, String userName, String gender, String nationality, String courseOfStudy, String matricNo){
		// add student, if success, is here
		studentList.add(new Student(lastName, firstName, userName, gender, nationality, courseOfStudy, matricNo));
		sdm.addStudent(lastName, firstName, userName, gender, nationality, courseOfStudy, matricNo);

		System.out.println("Name: \t Matriculation Number:");
		for (Student s : studentList) {
			System.out.println(s.getName() + "\t" + s.getMatricNo());
			
		}
	}

	/**
	 * Traverse through student list to check if student exists
	 * @param userName unique username of Student to check
	 * @param matricNo unique matriculation number of Student to check
	 * @return true if student exists in system and false otherwise
	 */
	public boolean ifStudentExist(String userName, String matricNo){
		for(Student s : studentList){
			if(s.getUsername().equals(userName) || s.getMatricNo().equals(matricNo)){
				System.out.println("Student already exists in database");
				return true;
			}
		}return false;
	}

	/**
	 * Getting all students in list
	 * @return studentList as a List of Student objects
	 */
    public List<Student> getAllStudents(){
        return studentList;
    }

	/**
	 * Getting number of students in system
	 * @return int size
	 */
    public int getNumberOfStudents(){
        return studentList.size();
	}

	/**
	 * Traverse through student list to print name and matriculation number of students taking particular course
	 * @param courseID course ID of course to print students of
	 */
	public void printStudentsByCourse(String courseID){
		System.out.println("Name: \t\tMatriculation Number:");
		for (Student s : studentList) {
			if(s.courseExists(courseID)){ // if student takes the course,
				System.out.println(s.getName() + " \t" + s.getMatricNo());
			}
		}
	}

	/**
	 * calls Student Database Manager to add course to student from CSV
	 * decrease vacancy of this Course by 1
	 * @param s Student who is adding course
	 * @param c Course to add
	 */
	public static void addCourse(Student s, Course c){ // to csv from student
		if(cm == null){
			cm = new CourseManager();
		}
		sdm.addCourse(s, c.getCourseID(), c.getCourseIndex());
		cm.updateVacancy(c, "add");
	}

	/**
	 * adds Course for Student
	 * @param s Student who is adding Course
	 * @param courseID course ID to add
	 * @param courseIndex course index to add
	 */
	public void addCourse(Student s, String courseID, int courseIndex){
		Course temp = cm.getCourse(courseID, courseIndex);
		if(temp != null){
			s.addCourse(temp);
			return;
		}
		return;
		
	}

	/**
	 * calls Student Database Manager to remove course in student from CSV
	 * increase vacancy of this Course by 1
	 * @param s Student whose course to drop
	 * @param c Course to drop
	 */
	public  static void dropCourse(Student s, Course c){ // to csv from student
		if(cm == null){
			cm = new CourseManager();
		}
		if(sdm == null){
			sdm = StudentDatabaseManager.getInstance();
		}
		sdm.dropCourse(s, c.getCourseID());
		cm.updateVacancy(c, "drop");
	}

	/**
	 * drops course from Student
	 * @param s Student whose course to drop
	 * @param courseID course ID to drop
	 * @param courseIndex course index to drop
	 */
	public void dropCourse(Student s, String courseID, int courseIndex) {
		Course temp = cm.getCourse(courseID, courseIndex);
		if(temp != null){
			s.dropCourse(temp);
			return;
		}
	}

	/**
	 * Traverse through student list to print name and matriculation number of students taking particular index
	 * @param courseID course index to print students from
	 * @param courseIndex course ID to print students from
	 */
	public static void printStudentByIndex(String courseID, int courseIndex){
		System.out.println("Name: \t\tMatriculation Number:");
		for (Student s : studentList) {
			if(s.courseIndexExists(courseIndex)){
				System.out.println(s.getName() + " \t" + s.getMatricNo());
			}
		}
	}

	/**
	 * For swapping of index of Student with a peer
	 * verifies that both student and peer are taking course index as specified
	 * verifies login of peer from password keyed in in StudentApp
	 * prints lessons of both students
	 * @param s Student doing the swapping of index
	 * @param peerMatricNum Matriculation number of peer
	 * @param peerCourseIndex course index of peer
	 * @param myCourseIndex course index of this Student
	 * @param courseID course ID of this course
	 * @param peerPassword password of peer
	 * @return peer as a Student object
	 * 			, otherwise show error message and return null if peer or student is not taking specifed course index
	 */
	public Student swapCourseIndex(Student s, String peerMatricNum, int peerCourseIndex, int myCourseIndex, String courseID, String peerPassword) {
		Student peer = retrieveStudent(peerMatricNum);

		if(peer!=null && peer.courseIndexExists(peerCourseIndex)){
			Course peerCourse = peer.getCourseUsingIndex(peerCourseIndex);
			Course myCourse = s.getCourseUsingIndex(myCourseIndex);
			if(peerCourse != null && myCourse != null){
				if(Login.verifyLogin(peer.getUsername(), peerPassword, "student")){
					System.out.println("lessons for my course index " + myCourseIndex + ": \n");
					myCourse.printSchedule(myCourseIndex);
					System.out.println();
					System.out.println("lessons for peer course index " + myCourseIndex + ": \n");
					peerCourse.printSchedule(peerCourseIndex);
					return peer;
				}
			}
		}System.out.println("could not find the course in the matric number specified.");
		return null;
	}

	/**
	 * Confirms swapping of indexes
	 * replaces the relevant course indexes in both student and peer objects
	 * swap the 2 students in the course
	 * calls Student Database Manager to perform swapping in CSV
	 * @param s Student doing the swapping of index
	 * @param peer peer Student to swap with
	 * @param myCourseIndex course index of this Student
	 * @param peerCourseIndex course index of peer
	 */
	public void confirmSwap(Student s, Student peer, int myCourseIndex, int peerCourseIndex){
		Course peerCourse = peer.getCourseUsingIndex(peerCourseIndex);
		Course myCourse = s.getCourseUsingIndex(myCourseIndex);
		peer.replaceCourse(peerCourse, myCourse);
		s.replaceCourse(myCourse, peerCourse);
		peerCourse.swapStudents(peer, s);
		myCourse.swapStudents(s, peer);
		sdm.swapCourseIndex(s.getMatricNo(), peer.getMatricNo(), myCourseIndex, peerCourseIndex, myCourse.getCourseID());
		System.out.println("swap completed");
	}

	/**
	 * calls course manager to check vacancy of specified course index
	 * @param courseID Course ID of course to check vacancy
	 * @param courseIndex Course index of course to check vacancy
	 */
	public static void checkCourseVacancy(String courseID, int courseIndex){
		if(cm == null){
			cm = new CourseManager();
		}
		cm.checkCourseVacancy(courseID, courseIndex);
	}

	/**
	 * calls Student object to check/print courses registered
	 * prints list of registered and waitlisted courses, total AU
	 * prints Student timetable and any clashes
	 * @param s Student to check
	 */
	public void checkCoursesReg(Student s){
		s.checkCoursesReg();
	}

	/**
	 * replaces index number of the course that student takes.
	 * if student takes course, drops course and adds new index. If index is full, added to waitlist
	 * else if student does not take course, prints statement, system does not change csv
	 * @param s specific instance of student to change index number of
	 * @param courseID courseID to change index number
	 * @param courseIndex course index student currently has
	 * @param newCourseIndex course index that hte student wants to change to
	 */
	public void replaceCourse(Student s, String courseID, int courseIndex,int newCourseIndex){
		if(!s.courseIndexExists(newCourseIndex)){
			dropCourse(s, courseID, courseIndex);
			addCourse(s, courseID, newCourseIndex);
			return;
		}else{
			System.out.println("You are already enrolled in this course!");
		}

	}
}

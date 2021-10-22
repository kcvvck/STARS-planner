import java.util.ArrayList;
import java.util.*;

/**
 * <h1>Student Entity class</h1>
 * Represents all students in NTU
 * @author 
 * @version 1.0
 * @since 2020-25-11
 */
public class Student implements IUser {

	/**
	 * The firstName, lastName, matriculation number, account username, password, gender, nationality, course of study of student
	 */
	private String firstName, lastName, matricNo, userName, password, gender, nationality, courseOfStudy;
	/**
	 * arraylist of courses taken by student
	 */
	public ArrayList<Course> coursesTaken = new ArrayList<Course>();
	/**
	 * arraylist of courses wailisted by Student
	 */
	public ArrayList<Course> coursesWaitlisted = new ArrayList<Course>();
	/**
	 * The email of this Student
	 */
	private String email;
	/**
	 * attribute to send message to Student
	 */
	private SendNotification sent;
	/**
	 * The total AUs of the courses taken by this Student
	 */
	private int totalAU;
	/**
	 * This Student's timetable
	 */
	private StudentTimeTable studentTimeTable;
	/**
	 * This Student's contact type
	 * can be phone, email or both
	 */
	private String contactType;
	/**
	 * notification to be sent to Student
	 */
	private String notifications = null;

	/**
	 * {@inheritDoc}
	 * Creates new instance of StudentApp and displays the student menu
	 */
	@Override
	public void run() {
		StudentApp studentUI = new StudentApp(this);
		studentUI.display();
	}

	/**
	 * This constructor allows admin to create a new Student by calling student manager
	 * methodToSendMessage is both sms and email by default
	 * @param lastName This Student's last name
	 * @param firstName This Student's first name
	 * @param userName This Student's username. also sets an email for the student based on username
	 * @param gender This Student's gender (F/M)
	 * @param nationality This Student's nationality
	 * @param courseOfStudy Course Student is enrolled in (eg. DSAI)
	 * @param matricNo This Student's matriculation number
	 */
	public Student(String lastName, String firstName, String userName, String gender, String nationality,
			String courseOfStudy, String matricNo) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.matricNo = matricNo;
		this.userName = userName;
		this.gender = gender;
		this.nationality = nationality;
		this.courseOfStudy = courseOfStudy;
		this.setEmail(userName + "@e.ntu.edu.sg");
		methodToSendMessage("both");
	}

	/**
	 * This constructor creates new Student from csv
	 * registers Student into courses from student csv
	 * creates timetable for Student from the list of registered courses
	 * @param lastName This Student's last name
	 * @param firstName This Student's first name
	 * @param userName This Student's username
	 * @param gender This Student's gender (F/M)
	 * @param nationality This Student's nationality
	 * @param courseOfStudy Course Student is enrolled in (eg. DSAI)
	 * @param matricNo This Student's matriculation number
	 * @param coursesRegistered Courses Student is already enrolled in (from CSV file)
	 * @param AU Total AU of courses taken by Student
	 */
	public Student(String lastName, String firstName, String userName, String gender, String nationality,
			String courseOfStudy, String matricNo, List<String> coursesRegistered, int AU) {
		
		this.firstName = firstName;
		this.lastName = lastName;
		this.matricNo = matricNo;
		this.userName = userName;
		this.gender = gender;
		this.nationality = nationality;
		this.courseOfStudy = courseOfStudy;
		this.totalAU = AU;
		this.setEmail(userName + "@e.ntu.edu.sg");
		alreadyRegistered(coursesRegistered);
		this.studentTimeTable = new StudentTimeTable(coursesTaken);
		methodToSendMessage("both");
	}

	/**
	 * Registers Students into each Course in array list of registered courses from CSV
	 * @param coursesRegistered List of String of courses the student is registered to
	 */
	private void alreadyRegistered(List<String> coursesRegistered) {
		if (coursesRegistered != null) {
			for (int i = 0; i < coursesRegistered.size(); i += 2) {
				Course temp = StudentManager.getCourse(coursesRegistered.get(i),
						Integer.parseInt(coursesRegistered.get(i + 1)));
				coursesTaken.add(temp);
				temp.addStudent(this);
			}
		}
	}

	/**
	 * Set the mode to send notifications to Student
	 * @param sms_or_email "sms" or "email" or "both", default="both"
	 */
	private void methodToSendMessage(String sms_or_email){
		if(sms_or_email.equals("email")){
			this.sent = new SendNotification(new Email());
			this.contactType = "email";
		}else if (sms_or_email.equals("sms")){
			this.sent = new SendNotification(new Phone());
			this.contactType = "sms";
		}else{
			this.sent = new SendNotification(new Phone(), new Email());
			this.contactType = "both";
		}
	}

	/**
	 * Change this Student's method to send notifications
	 * @param methodToSend "sms" or "email" or "both"
	 */
	public void changeContactTo(String methodToSend){ // change contact type
		methodToSendMessage(methodToSend);
		System.out.println("Method of sending notifications has been changed to " + methodToSend);
	}

	/**
	 * Gets the preferred mode of sending notifications
	 * @return contact type
	 */
	public String getContactType(){
		return contactType;
	}


	/**
	 * check hashmap of available courses and add the course into student's arraylist
	 * @param coursesAvailable	ArrayList of all the Course Objects
	 * @param coursesTaken		ArrayList of Courses the student takes
	 * @param courseID			CourseID of Course
	 * @param courseIndex		course Index of Course
	 */
	public void addCourseToCoursesTaken(HashMap<String, ArrayList<Course>> coursesAvailable, ArrayList<Course> coursesTaken, String courseID, int courseIndex){
		if (coursesAvailable.keySet().contains(courseID)){
			for (Course course : coursesAvailable.get(courseID)){
				if (course.getCourseIndex()==courseIndex && !coursesTaken.contains(course)) {coursesTaken.add(course);}
			}
		}
	}

	/**
	 * Confirm registration of Course
	 * Student is removed from waitlist and added to course
	 * Message is sent to student to notify confirmation of registration
	 * @param c Course that the Student is registering to
	 */
	public void confirmRegistration(Course c){
		studentTimeTable.addCourse(c);
		coursesWaitlisted.remove(c);
		setTotalAU(getTotalAU()+c.getAU());
		/**
		 * update new AU
		 */
		sent.sendMessageTo(this, "congratulations you have been accepted to " + c.getCourseID() + ", " + c.getCourseIndex(), "reg");
		StudentManager.addCourse(this, c);
		/**
		 * add to csv
		 */
		System.out.println("confirmed registration!");

	}

	/**
	 * deregister Student from Course c
	 * @param c Course that the student is deregistering from
	 */
	public void removeFrom(Course c){
		coursesTaken.remove(c);
	}

	/**
	 * When student wants to change index of the course they are taking
	 * @param courseID		Course ID of the Course
	 * @param courseIndex	Current course index of the student
	 * @param newCourseIndex	New course index the student wants to change to
	 */
	public void swapCourse(String courseID, int courseIndex, int newCourseIndex){
		Course iHave = StudentManager.getCourse(courseID, courseIndex);
		if (iHave==null) return;
		Course iWantToSwap = StudentManager.getCourse(courseID, newCourseIndex);
		if (iWantToSwap==null) return;
		
		if(!iHave.equals(iWantToSwap) && coursesTaken.contains(iHave) && !coursesTaken.contains(iWantToSwap) && iWantToSwap!=null && iHave !=null){
			replaceCourse(iHave, iWantToSwap);
			checkCoursesReg();
			return;
		}else{
			System.out.println("swap course index failed :(");
		}
		
	}

	/**
	 * Prints notification to Student
	 */
	public void getNotifications(){
		if(notifications!=null){
			System.out.println(notifications);
			return;
		}
	}

	/**
	 * Adds Student to waitlist
	 * waitlist will call confirmRegistration and notify Student upon successful registration
	 * @param c Course the Student wants to add
	 */
	public void addCourse(Course c) {
		if(!courseExists(c.getCourseID()) && !checkOverload(c.getAU())){
			/**
			 * Adds student to waitlist if Course has not been taken by Student
			 * and Student is not overloading
			 */
			coursesWaitlisted.add(c);
			System.out.println("added to waitlist");
			c.addToWaitList(this);
			return;
		}else if(courseExists(c.getCourseID())){
			System.out.println("You are already enrolled in this course!");
			return;
		}else if(!checkOverload(c.getAU())){
			/**
			 * message shown if overload
			 */
			System.out.println("you are overloaded! Please drop some courses!");
			return;
		}else{
			return;
		}
		
	}

	/**
	 * Checks if this Student is overloading (more than 21 AU)
	 * @param au AU of the Course the student wants to take
	 * @return true or false
	 */
	private boolean checkOverload(int au){
		if(this.totalAU + au > 21){
			return true;
		}return false;
	}


	/**
	 * Unregister Student from course if Course is taken by Student
	 * Removes Student from waitlist of Course if Student is on waitlist
	 * Display error message if Student is neither registered nor waitlisted
	 * @param c Course the student wants to drop
	 */
  public void dropCourse(Course c) {
		if(coursesTaken.remove(c)){
			System.out.println(c.getCourseID() + " removed from registered!");
			c.unregisterStudent(this);
			setTotalAU(totalAU - c.getAU()); //update total au
			StudentManager.dropCourse(this, c);
			studentTimeTable.dropCourse(c);
			sent.sendMessageTo(this, "You have been successfully deregistered from " + c.getCourseID() + ", " + c.getCourseIndex(), "reg");
			return;
		}else if(coursesWaitlisted.remove(c)){
			System.out.println(c.getCourseID() + " removed from waitlist!");
			c.removeFromWaitList(this);
			return;
		}else{
			System.out.println("we could not find the course in your waitlist and/or registered courses.");
		}
	}

	/**
	 * calls student manager to check vacancy of course index
	 * @param courseIndex	course index of the Course
	 * @param courseID		course ID of the course
	 */
	public void checkVacancy(int courseIndex, String courseID) {
		StudentManager.checkCourseVacancy(courseID, courseIndex);
	}

	/**
	 * Check if Student takes this course index
	 * @param courseIndex (eg. 10002)
	 * @return true if course index taken by Student, false otherwise
	 */
	public boolean courseIndexExists(int courseIndex){
		for (Course c : coursesTaken){
			if (c.getCourseIndex()==courseIndex) return true; 
		} return false;
	}

	/**
	 * Check if Student takes this course ID
	 * @param courseID (eg. CZ2002)
	 * @return true if course ID taken by Student, false otherwise
	 */
	public boolean courseExists(String courseID){
		for (Course c : coursesTaken){
			if (c.getCourseID().equals(courseID)){
				return true; 
			}
		}
		return false;
	}

	/**
	 * Returns the Course by passing in course index that this Student is taking
	 * @param courseIndex (eg. 10002)
	 * @return Course c
	 */
	public Course getCourseUsingIndex(int courseIndex){
		for (Course c : coursesTaken) {
			if(c.getCourseIndex() == courseIndex){
				return c;
			}
		}return null;
	}

	/**
	 * Replaces a course taken by this Student with a peer's course when swapping courses
	 * Remove current course from coursesTaken ArrayList, then adds other course into it
	 * @param myCourse		Course Student is taking
	 * @param peerCourse	Course the peer is taking
	 * @return true if successful, else false
	 */
	public boolean replaceCourse(Course myCourse, Course peerCourse){
		try{
			coursesTaken.remove(myCourse);
			coursesTaken.add(peerCourse);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * Changes Student's password in STUDENTLOGIN csv file
	 * @param userPass The new password, input by Student user
	 */
	public void changePassword(String userPass){
		Login.changePassword(getUsername(), userPass);
	}


	/**
	 * Prints Student's registered and waitlisted courses, total AU of registered courses
	 * Prints Student's weekly timetable
	 * Prints clashing courses
	 */
	public void checkCoursesReg() {
		System.out.println("Your courses registered are: ");
		System.out.println("Course:\t\tType:\tAU:");
		for(Course course : coursesTaken) {
			System.out.println(course.getCourseID()+"/"+ course.getCourseIndex()+"\t"+course.getCourseType()+"\t"+course.getAU());
		}
		System.out.println("\nYour courses waitlisted are: ");
		System.out.println("Course:\t\tType:\tAU:");
		for(Course course : coursesWaitlisted) {
			System.out.println(course.getCourseID()+"/"+ course.getCourseIndex()+"\t"+course.getCourseType()+"\t"+course.getAU());
		}
		System.out.println("\nTotal AU:"+ getTotalAU()+"\n");
		studentTimeTable.printTimeTable();
		studentTimeTable.printClashingCourses();
	}

	/**
	 * Gets Student's name
	 * @return Student's name (first and last name)
	 */
	public String getName(){
		return firstName + " " + lastName;
	}

	/**
	 * Changes total AU of this Student
	 * @param totalAU Total AU to be set
	 */
	public void setTotalAU(int totalAU) {
		this.totalAU = totalAU;
	}

	/**
	 * Gets the total AU the Student has
	 * @return total AU of this Student
	 */
	public int getTotalAU() {
		return totalAU;
	}

	/**
	 * Gets the email of the student
	 * @return email of this Student
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Changes email of this Student
	 * @param email String email e.g. "DON001@e.ntu.edu.sg"
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets first name of the student
	 * @return first name of this Student
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Changes first name of this Student
	 * @param firstName first name to be set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name of the student
	 * @return last name of this Student
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Changes of this Student
	 * @param lastName last name to be set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return matriculation number of this Student
	 */
	public String getMatricNo() {
		return matricNo;
	}

	/**
	 * Changes matriculation number of this Student
	 * @param matricNo matriculation number to be set
	 */
	public void setMatricNo(String matricNo) {
		this.matricNo = matricNo;
	}

	/**
	 * @return username of this Student
	 */
	public String getUsername() {
		return userName;
	}

	/**
	 * Changes username of this Student
	 * @param userName username to be set
	 */
	public void setUsername(String userName) {
		this.userName = userName;
	}

	/**
	 * @return Student's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Changes Student's password
	 * @param password password to be set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return this Student's gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return nationality of this Student
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Changes nationality of this Student
	 * @param nationality nationality of the student
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return course of study of this Student (eg. DSAI)
	 */
	public String getCourseOfStudy() {
		return courseOfStudy;
	}

	/**
	 * Changes course of study of this Student (eg. DSAI)
	 * should be one of available courses in ntu
	 * @param courseOfStudy the course of study to be set
	 */
	public void setCourseOfStudy(String courseOfStudy) {
		this.courseOfStudy = courseOfStudy;
	}

	/**
	 * @return array list of courses taken by this Student
	 */
	public ArrayList<Course> getCoursesTaken() {
		return coursesTaken;
	}

	/**
	 * Changes courses taken by this Student
	 * @param coursesTaken list of Course objects that the student is taking
	 */
	public void setCoursesTaken(ArrayList<Course> coursesTaken) {
		this.coursesTaken = coursesTaken;
	}


	/**
	 * @return arraylist of courses taken by this Student
	 */
	public ArrayList<Course> getCourses() {
		return coursesTaken;
	}

	/**
	 * Changes courses taken by this Student
	 * @param courses arraylist of courses
	 */
	public void setCourses(ArrayList<Course> courses) {
		this.coursesTaken = courses;
	}

	/**
	 * Prints courses registered and waitlisted by student
	 */
	public void printCoursesTaken(){

		for(Course c : coursesWaitlisted){
			System.out.println(c.getCourseID() +"\tWaitlisted"  );
		}
		for(Course c : coursesTaken){
			System.out.println(c.getCourseID() +"\tRegistered"  );
		}
		
	}
}

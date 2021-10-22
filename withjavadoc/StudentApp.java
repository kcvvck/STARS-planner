import java.util.Scanner;
import java.io.Console;
import java.util.*;

/**
 * <h1>StudentApp Boundary class</h1>
 * User interface of Student
 * <p>
 * (1) Add Course
 * (2) Drop Course
 * (3) Check/Print Courses Registered
 * (4) Check Vacancies Available
 * (5) Change Index Number of Course
 * (6) Swap Index Number With Another Student
 * (7) Print All Available Courses
 * (8) Change password
 * (9) Change mode of contact (Phone/Email/Both)
 * (10) Exit
 * </p>
 * @author
 * @version 1.0
 * @since   2020-25-11
 */

public class StudentApp implements IMainInterface{
	/**
	 * instance of student that initialised this interface
	 */
	private final Student s;
	/**
	 * user password for changing of password in option 8
	 * peer password for swapping indexes
	 * response for user input
	 */
	String userPass, peerPassword, response;
	/**
	 * initialisation of variables
	 * courseIndex, newCourseIndex, peerCourseIndex, myCourseIndex, method
	 * to store indexes when swapping or changing course indexes and changing method of contact etc
	 */
	int courseIndex, newCourseIndex, peerCourseIndex, myCourseIndex, method;
	/**
	 * console for entering hidden password
	 */
	Console cnsl = null;
	/**
	 * instance of Student Manager to be called
	 */
	private StudentManager sm = new StudentManager();
	// private CourseManager cm = new CourseManager();
	/**
	 * user interface created from specific instance of student
	 * @param s Student using this StudentApp
	 */
	public StudentApp(Student s){
		this.s = s;
	}

	/**
	 * display page showing menu options to Student
	 * @exception InputMismatchException if user enters non-integer when we try to Scanner.nextInt()
	 */
	@Override
	public void display(){
		Scanner sc = new Scanner(System.in);
		int choice;
		System.out.println("Welcome to student UI page, " + s.getName() +"!");
		s.getNotifications();

		do {
			System.out.println(
				"=================================================\n"+
                "                     STUDENT\n"+ 
                "=================================================\n"+
				"(1) Add Course\n" + 
				"(2) Drop Course\n" + 
				"(3) Check/Print Courses Registered\n" + 
				"(4) Check Vacancies Available\n" + 
				"(5) Change Index Number of Course\n" + 
				"(6) Swap Index Number With Another Student\n" +
				"(7) Print All Available Courses\n"+
				"(8) Change password\n"+
				"(9) Change mode of contact (Phone/Email/Both)\n"+
				"(10) Exit");

			/**
			 * takes input of choice, selected by user
			 * raises error if choice is not an integer
			 */
			try{
				System.out.println("Enter the number of your choice: ");
				choice = sc.nextInt();
			} catch(InputMismatchException e){
				System.out.println("Invalid input. Please try again.\n");
				choice = 42; //special case
			} finally{
				sc.nextLine();
			}

			switch (choice) {
				case 1:
					/**
					 * For Student to add course
					 * shows available courses
					 * reads input of course ID and index
					 */
					CourseManager.printCourses("id");
					System.out.println("Enter ID of course wanted: ");
					String courseID = sc.next();

					System.out.println("Enter wanted index: ");
					try{
						courseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					sm.addCourse(s, courseID, courseIndex); //a bit long.
					break;

				case 2:
					/**
					 * for Student to drop course
					 * calls student manager to print courses already registered and waitlisted by Student
					 * Prints AU, timetable and any clashes
					 * Reads user input for course to be dropped
					 */
					sm.checkCoursesReg(s);
					System.out.println("Enter course ID you want to drop: ");
					courseID = sc.next();
					System.out.println("Enter course index: ");
					try{
						courseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					/**
					 * calls student manager to update Student object to drop course
					 */
					sm.dropCourse(s, courseID, courseIndex);
					break;
				case 3:
					/**
					 * for Student to check/print courses registered
					 * prints courses already registered and waitlisted by Student
					 * prints AU, timetable and any clashes
					 */
					sm.checkCoursesReg(s);
					break;
				case 4:
					/**
					 * for Student to check vacancies available
					 * read input for courseID and index
					 * calls student manager to check vacancy
					 */
					System.out.println("Enter course ID");
					courseID = sc.next();

					System.out.println("Enter course index wanted to check vacancy of: ");
					try{
						courseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					
					StudentManager.checkCourseVacancy(courseID, courseIndex);
					break;
				case 5:
					/**
					 * for Student to change index number
					 * calls Student object to print Student's courses, AU an timetable first
					 * read input for course ID and index to change
					 * calls Student Manager to change index
					 * Message will be sent to notify successful registration into new course
					 */
					s.checkCoursesReg();
					System.out.println("Enter the course ID of the course you want to change: ");
					courseID = sc.next();
					CourseManager.printCoursesBy(courseID);
					System.out.println("Enter course index you currently have ");
					try{
						courseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					System.out.println("Enter course index you want to switch to: ");
					try{
						newCourseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					sm.replaceCourse(s, courseID, courseIndex,newCourseIndex);
					break;
				case 6:
					/**
					 * for Student to swap index number with peer
					 * input peer index number, courseID and index number of both parties
					 */
					System.out.println("Enter your peer's matriculation number: ");
					String peerMatricNum = sc.next();
					System.out.println("Enter course ID");
					courseID = sc.next();
					System.out.println("Enter your course index number: ");
					try{
						myCourseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					System.out.println("Enter your peer's course index number: ");
					try{
						peerCourseIndex = sc.nextInt();
					} catch(InputMismatchException e){
						System.out.println("Please enter an integer.");
						break;
					}
					/**
					 * peer enters password in console
					 */
					cnsl = System.console();
					if (cnsl != null) {
						// read password into the char array
						System.out.println("Nothing is shown, password is hidden, just key in password and press enter.");
						char[] pwd = cnsl.readPassword("Enter peer password: ");
						peerPassword = new String(pwd);
						// prints
						
					}
					Student peer = sm.swapCourseIndex(s, peerMatricNum, peerCourseIndex, myCourseIndex, courseID, peerPassword);
					if(peer !=null){
						System.out.println("Confirm swap? (yes/no only)");
						response = sc.next().trim().toLowerCase();
						if(response.equals("yes")){
							sm.confirmSwap(s, peer,myCourseIndex,  peerCourseIndex);
							s.printCoursesTaken();
						}
					}
					break;
				case 7:
					/**
					 * for Student to view all available courses offered
					 * prints course ID, indexes, vacancies for each index, course type(eg. GERPE) and AU
					 */
					CourseManager.printCourses("id");
					break;

				case 8:
					/**
					 * for Student to change password
					 * takes input in console
					 */
					try{
						cnsl = System.console();
						if (cnsl != null) {
							// read password into the char array
							System.out.println("Nothing is shown, password is hidden, just key in password and press enter.");
							char[] pwd = cnsl.readPassword("Password: ");
							userPass = new String(pwd);
							
						}
					} catch (Exception e){
						System.out.println("Enter password: (Since this is not a terminal, password is not hidden)");
						userPass = sc.nextLine();
					}
					s.changePassword(userPass);
					break;
				case 9:
					/**
					 * for Student to change mode of contact
					 * to email or phone or both
					 * shows error message if input is not an int or not valid int
					 */
					System.out.println("Your current contact mode is " + s.getContactType());
					System.out.println("Choose from the list below: ");
					System.out.println("1. Email\n2. Phone\n3. Both");
					try{
						method = sc.nextInt();
					}catch(Exception e){
						System.out.println("wrong input type");
						continue;
					}
					if(method != -1){
						if(method == 1){
							s.changeContactTo("email");
						}else if(method == 2){
							s.changeContactTo("sms");
						}else if (method ==3){
							s.changeContactTo("both");
						}else{
							System.out.println("no such options!");
							continue;
						}
					}
					break;
				case 42:
					/**
					 * special case, invalid choice
					 */
					break;
			}
		} while (choice < 10 || choice==42);
		/**
		 * keeping scanner open so can go back to MainUI and sign in
		 */
		System.out.println("\nLogging out.\n\n\n\n");
	}
}

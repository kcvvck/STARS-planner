import java.util.LinkedList;
import java.util.Queue;

/**
 * <h1>WaitList Entity Class</h1>
 * This class represents the waitlist of a Course, every Course has its own waitlist
 * When Students add/drop a Course, they will be added/removed from the waitlist
 * The waitlist manages the Course's vacancy
 * @author Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */
public class WaitList {
	/**
	 * Vacancy of the Course, how many slots are available for Students to register
	 */
	private int vacancy;
	/**
	 * The Course this waitlist belongs to
	 */
	private Course c;
	/**
	 * Queue containing all the waitlisted Students
	 */
	private Queue<Student> waitListQ = new LinkedList<Student>();
	
	public WaitList(Course c, int vacancy) {
		this.c = c;
		this.vacancy = vacancy;
	}
	
	/** 
	 * Add Student s into the Queue waitListQ
	 * @param s Student to be added to waitlist
	 */
	public void addToWaitList(Student s) {
		waitListQ.add(s);
		update();
	}
	
	/** 
	 * If there are any vacancies, register the next student in the Queue waitListQ into the course
	 */
	private void update(){ // registering students
		if (vacancy != 0) {
			if(waitListQ.peek() != null){
				Student next = waitListQ.remove();
				registerStudent(next);
			}
		}
	}
	
	
	/** 
	 * Register Student s into the Course, updates both Course and Student objects
	 * @param s Student to be registered
	 */
	private void registerStudent(Student s) {
		c.addStudent(s);
		s.confirmRegistration(this.c);
	}

	
	/** 
	 * Remove Student s from the Queue waitListQ
	 * @param s Student to be removed
	 */
	public void removeFromWaitList(Student s) {
		if (waitListQ.contains(s)) { //it was ! at first
			waitListQ.remove(s);
		} else System.out.println("Student is not in the waitlist and not registered in the course");
	}

	
	/** 
	 * Unregister Student s from the Course, updates Course and Student objects
	 * @param s Student to be unregistered
	 */
	public void unregisterStudent(Student s) {
		c.removeStudent(s);
		s.removeFrom(this.c);
		update();
	}

	
	/** 
	 * Gets the vacancy of the waitlist
	 * @return int value of vacancy
	 */
	public int getVacancy() {
		return this.vacancy;
	}
	
	/** 
	 * Set vacancy of the waitlist
	 * @param vacancy vacancy to be set
	 */
	public void setVacancy(int vacancy) {
		this.vacancy = vacancy;
	}

	
	/** 
	 * Gets the size of the Queue waitListQ
	 * @return int value of queue size
	 */
	public int getWaitListSize(){
		return waitListQ.size();
	}

	
	/** 
	 * Check if Student s is in the waitlist
	 * @param s Student to be checked
	 * @return boolean true if Student is in the waitlist, else false
	 */
	public boolean checkStudentsInWaitList(Student s){
		if(waitListQ.contains(s)){
			return true;
		}return false;
	}

	
}

import java.time.*;

/**
 * <h1>Lesson Entity Class</h1>
 * This class represents a single lesson of a Course, e.g. CZ2001 Lecture
 * contains the details e.g. venue, day, start time, end time etc.
 * CourseTimeTable contains an ArrayList of Lesson objects (different Lesson objects representing Lecture, Lab or Tutorial sessions)
 * @author Ong Hui Lee Grace
 * @version 1.0
 * @since 2020-25-11
 */
public class Lesson {
	/**
	 * Venue where the lesson will be held e.g.LT2, HWLAB, TR+30 etc.
	 */
	public String venue;
	/**
	 * The type of lesson e.g. Lab, Lecture, Tutorial etc.
	 */
	public String lesson_type; //LAB/LEC/TUT
	/**
	 * The day the lesson is held
	 */
	public DayOfWeek day;
	/**
	 * The start time of the lesson
	 */
	public LocalTime start;
	/**
	 * The end time of the lesson
	 */
	public LocalTime end;
	
	/**
	 * The course id of the Course it is under
	 */
	public String courseID;

	/**
	 * The course index of the Course it is under
	 */
	public int courseIndex;
	
	/**
	 * Constructor for Lesson, sets all the attributes
	 * @param lesson	the lesson type e.g.Lecture/Lab
	 * @param venue		venue where the lesson will be held
	 * @param dayEnum	the day in integer form e.g.1=Monday, 2=Tuesday, 5=Friday etc.
	 * @param startTime	what time the lesson starts
	 * @param endTime	what time the lesson ends
	 */
	public Lesson (String lesson, String venue, int dayEnum, String startTime, String endTime) {
                                                //dayEnum:1=monday;7=sunday
		this.venue = venue;
        this.lesson_type = lesson;
        this.day = DayOfWeek.of(dayEnum);
		this.start = LocalTime.parse(startTime);
		this.end = LocalTime.parse(endTime);
	}

	/**
	 * Checks if this lesson clashes timing with another lesson
	 * @param l The other Lesson to compare this lesson against
	 * @return boolean true if clash, else false
	 */
	public boolean checkClash(Lesson l){

		if (start.compareTo(l.end) >0 && day.equals(l.day)){
			return false;
		}else if (end.compareTo(l.start) <0 && day.equals(l.day)){
			return false;
		}else if ((end.compareTo(l.start) ==0 || start.compareTo(l.end) ==0 )&& day.equals(l.day)){
			return false;
		}else if (day.equals(l.day)) {
			return true;
		} else return false;
	}
	
	/**
	 * Sets the courseID of the Lesson
	 * @param courseID course ID to be set
	 */
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	/**
	 * Sets the course index of the Lesson
	 * @param courseIndex course index to be set
	 */
	public void setCourseIndex(int courseIndex) {
		this.courseIndex = courseIndex;
	}

	
	/** 
	 * Gets the lesson type of the Lesson
	 * @return String e.g. "Lecture"
	 */
	public String getLessonType(){
		return lesson_type;
	}
	
	/** 
	 * Gets the venue where the Lesson is held
	 * @return String e.g. "LT2"
	 */
	public String getVenue(){
		return venue;
	}

	
	/** 
	 * Gets the day the Lesson is held
	 * @return DayOfWeek e.g. MONDAY
	 */
	public DayOfWeek getDay(){
		return day;
	}

	
	/** 
	 * Gets the time the Lesson starts
	 * @return LocalTime e.g. 11:30
	 */
	public LocalTime getStart(){
		return start;
	}
	
	/** 
	 * Gets the time the Lesson ends
	 * @return LocalTime e.g. 13:00
	 */
	public LocalTime getEnd(){
		return end;
	}

}

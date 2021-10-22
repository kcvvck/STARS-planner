

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.*;
import java.util.*;
import java.time.format.DateTimeFormatter;

/**
* <h1>CurrentDateTime control class</h1>
* Check student access period in csv by 
* looking for student using their username 
* @author  Joelle Thng
* @version 1.0
* @since   2020-25-11
*/

public class CurrentDateTime {
	/** 
     * file to look for students
     */
	private static String csvFile = "STUDENT.csv";
	
	
    /** 
     * checks access period of students
     * @param username String of specific student
     * @return boolean true if access period is now, false if not
     */
    public static boolean checkAccess(String username)
	{
        boolean success = false;
        LocalDateTime now = LocalDateTime.now();  
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
           
            while ((line = br.readLine()) != null) {
                String[] student = line.split(cvsSplitBy);

                if (line.contains(username))
                {
                	String startDateTime = student[7] + ", " + student[8];
                	String endDateTime = student[9] + ", " + student[10];
                	
                    
                    /** 
                     * Convert String startDateTime and endDateTime to actual date time format
                     */
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy, HH:mm", Locale.ENGLISH);
                    LocalDateTime startPeriod = LocalDateTime.parse(startDateTime, formatter);
                    
                    formatter = DateTimeFormatter.ofPattern("MMMM d yyyy, HH:mm", Locale.ENGLISH);
                    LocalDateTime endPeriod = LocalDateTime.parse(endDateTime, formatter);
                    
                    if (now.isAfter(startPeriod) && now.isBefore(endPeriod))
                    {
                        System.out.println("\n"+username + " ACCESS GRANTED");
                        success= true;
                    }
                    else
                    {
                        System.out.println("\n"+username + " ACCESS DENIED. Please access this during your allocated period.");
                        
                    }
                    
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
	}

}


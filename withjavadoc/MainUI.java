

import java.util.Scanner;
import java.io.Console;

/** 
 * MySTARS program that allows students to log in during add/drop period and register/unregister for courses
 * during allocated access period
 * system is manager by admin who can log in any time to make changes such as adding of students
 * adding of courses
 * updating of courses and printing of student and course list.
 */

public class MainUI{
     static int k = 0;
    
    /** 
     * @param args command-line arguments
     * @exception NullPointerException for absence of slash in &lt;DOMAIN_NAME&gt;/&lt;USER_NAME&gt;
     */
    public static void main(String[] args) {
        IUser u = null;
        UserFactory userFactory= null;

        /** 
         * create all objects
         */
        new StudentDatabaseManager();
        new AdminDatabaseMgr();
        new CourseDatabaseManager();
        
        
        Scanner sc = new Scanner(System.in);
        /** 
         * main running loop 3 tries
         */
        while(k < 3){
            String userDomain = null;
            String userName = null;
            String userPass = null;
            Console cnsl = null;
            boolean verified = false;
            int counter = 0; 
            System.out.println(
            "==============================================================\n"+
            "                             LOGIN\n"+
            "==============================================================");
            while (verified == false && counter < 3) { 
                System.out.println(
                    "Enter username: (in the form Student/D738283 or Admin/673248)");
                String userInputName = sc.nextLine();
                
                try {
                    
                    
                    userDomain = userInputName.substring(0, userInputName.lastIndexOf("/")).toLowerCase();
                    userName = userInputName.substring(userInputName.lastIndexOf("/") + 1, userInputName.length()); 
                    cnsl = System.console();
                    if (cnsl != null) {
                        char[] pwd = cnsl.readPassword("Password: (password is hidden, case-sensitive)\n");
                        userPass = new String(pwd);
                        
                    }
                    
                    
                } catch(NullPointerException e){
                    System.out.println("Please input <DOMAIN_NAME>/<USERNAME>(MainUI)");
                    System.out.println("You have " + (3-(counter+1)) +" tries left");
                    System.out.println("--------------------------------------------------------------");
                    continue;
                } catch (Exception e) {
                    System.out.println("There was an error during input.(MainUI)");
                    System.out.println("You have " + (3-(counter+1)) +" tries left");
                    System.out.println("--------------------------------------------------------------");
                    continue;
                }
                finally{
                    counter++;
                }
                /** 
                 * verifies user
                 */
                verified = Login.verifyLogin(userName, userPass, userDomain); 
                if(!verified){
                    
                    System.out.println("You have " + (3-counter) +" tries left");
                    System.out.println("--------------------------------------------------------------");
                    continue;
                }else{
                    System.out.println("Login successful.");
                }
                
            }
    
            if (verified) {
                /** 
                 * gets instance of user
                 */
                userFactory = new UserFactory(userDomain, userName);
                /** 
                 * runs user interface
                 */
                u = userFactory.getUser(); 
                u.run();
            }
            
        }sc.close();
        
        
        
    }
    
}
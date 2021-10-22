/**
* <h1>UserFactory</h1>
* gets specific instance of user from objects of users
* @author  Kam Chin Voon
* @version 1.0
* @since   2020-25-11
*/

public class UserFactory{
   /** 
    * domain of user (admin/student)
    */
   private String userDomain;
   /**
    * username of the user
    */
   private String userName;
   /**
    * filename, depending on the userdomain, we will locate the file (ADMIN.csv or STUDENT.csv)
    */
   private String fileName;
   /** 
    * creates user factory using user's domain and username
    * @param userDomain user domain e.g. "admin" or "student"
    * @param userName   username of the user
    */
   public UserFactory(String userDomain, String userName){
      this.userDomain = userDomain;
      this.userName = userName;
      /** 
       * file names are in all caps for easy differentiation
      */
      this.fileName = userDomain.toUpperCase().concat(".csv");
   }
    
    /** 
     * connects to database manager to get the string[] of the row containing the user you want
     * gets the object by calling either student database manager or admin database managers
     * @return IUser object (Admin/Student)
     */
    public IUser getUser(){ 
      String[] row = DatabaseManager.getRow(fileName, 2, userName);
      if (userDomain.equals("student")){
         return StudentDatabaseManager.retrieveStudent(row[2]);
     }else{
         return AdminDatabaseMgr.retrieveAdmin(row[2]);
     }
   }
}



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
* <h1>Login control class</h1>
* Check student access period in csv by 
* looking for student using their username 
* @author  Ong Hui Lee Grace, Wang Anyi
* @version 1.0
* @since   2020-25-11
*/

public class Login {
	/** 
    * file of admin login 
    */
	final static String ADMINLOGIN = "ADMINLOGIN.csv";
	/** 
    * file of student login 
    */
	final static String STUDENTLOGIN = "STUDENTLOGIN.csv";
	/** 
    * login file selected in this instance
    */
	private static String LOGINFILE;
	/** 
    * columns in the login file
    */
    static enum columns {
		Username,
		Password,
        Salt,
        Hash;
	}

	
	/** 
	 * verifies access period and changes the file based on whether admin or student
	 * @param userName String of student/admin username--> identifier
	 * @param userDomain String of student/admin --> identify login file to use
	 * @return boolean true if admin or student logs in during access period, false otherwise
	 */
	private static boolean verifyAccessPeriod(String userName, String userDomain){
		if(userDomain.equals("admin")){
			LOGINFILE = ADMINLOGIN;
		}else if (userDomain.equals("student")){
			LOGINFILE = STUDENTLOGIN;
			if(!CurrentDateTime.checkAccess(userName)){
				return false;
			}
		}else{
			System.out.println("error, no such domains!(Login)");
			return false;
		}
		
		return true;
	}

	
    
	/** 
	 * verifies that the user has the logged in during their access period(student)
	 * and verifies that the user has logged in with correct password (admin and student)
	 * this is by converting password to byte array and use hash algorithm 
	 * PBKDF2WithHmacSHA512 to authenticate login
	 * @param userName String to identify user
	 * @param userPass String password of user 
	 * @param userDomain String either student or admin
	 * @return boolean true if password, domain, access period is correct, false otherwise.
	 * @exception IOException if cannot close file
	 */
	public static boolean verifyLogin(String userName, String userPass, String userDomain){ 
		boolean success = false;
		if(verifyAccessPeriod( userName,  userDomain)){
			BufferedReader fileReader = null;

			try { 
				String line = "";
				fileReader = new BufferedReader(new FileReader(LOGINFILE));
				fileReader.readLine();
				while ((line = fileReader.readLine()) != null) {
					String[] tokens = line.split(",",-1);
					String salt = tokens[columns.Salt.ordinal()];
					String hash = tokens[columns.Hash.ordinal()];
					byte[] s = Base64.getDecoder().decode(salt.trim()); 
					byte[] h = Base64.getDecoder().decode(hash.trim()); 
					boolean valid = PBKDF2WithHmacSHA512.authenticate(userPass, s, h);
					if (tokens[columns.Username.ordinal()].compareTo(userName) == 0 && valid) {
						success = true;
					}
				}
				

			} catch (Exception e) {
				System.out.println("Error in CsvFileReader!(Login)");
				e.printStackTrace();
			} finally {
				try {
					fileReader.close();
				} catch (IOException e) {
					System.out.println("Error closing fileReader!(Login)");
					e.printStackTrace();
				}
			}
		}
		if(!success){
			System.out.println("Your password or username was wrong");
		}
		return success;

	}

	
	/** 
	 * default password added for newly added students
	 * @param userName String identifier for new student entry in csv
	 */
	public static void addPassword(String userName) {
		PrintWriter pw = null;
		try {
			String password = "login";
			FileWriter fw = new FileWriter(STUDENTLOGIN, true); 
			BufferedWriter bw = new BufferedWriter(fw);
			pw= new PrintWriter(bw);

			byte[] salt = PBKDF2WithHmacSHA512.salt(); 
			String saltString = Base64.getEncoder().encodeToString(salt); 
			byte[] hash = PBKDF2WithHmacSHA512.hash(password, salt); 
			String hashString = Base64.getEncoder().encodeToString(hash); 

			StringBuilder newLine = new StringBuilder();
			newLine.append(userName + "," +  "," + saltString + "," + hashString);
			pw.println(newLine);

			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error changing main file(Login)");
		}finally{
			pw.flush();
			pw.close();
		}
	}

	
	/** 
	 * Change password for specific student, changes password into byte array and hashed with
	 * PBKDF2WithHmacSHA512 for security
	 * @param userName String username of student (identifier)
	 * @param password String new password of student 
	 */
	public static void changePassword(String userName, String password) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(STUDENTLOGIN, true); 
			BufferedReader fileReader = null;
			ArrayList<String[]> reWrite = new ArrayList<String[]>();
			fileReader = new BufferedReader(new FileReader(STUDENTLOGIN));
			
			byte[] salt = PBKDF2WithHmacSHA512.salt(); 
			String saltString = Base64.getEncoder().encodeToString(salt); 
			byte[] hash = PBKDF2WithHmacSHA512.hash(password, salt); 
			String hashString = Base64.getEncoder().encodeToString(hash); 
			String line = "";
			try {
				while ((line = fileReader.readLine()) != null) {
					String[] tokens = line.split(",", -1);
					List<String> token = Arrays.asList(tokens);
					if (token.contains(userName)) { 
						tokens[1] = "";
						tokens[2] = saltString;
						tokens[3] = hashString;
						reWrite.add(tokens);
					} else
						reWrite.add(tokens);
				}
				System.out.println("Changing of password successful.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fw = new FileWriter(STUDENTLOGIN); 
			} catch (IOException e) {
				e.printStackTrace();
			}
			for (String[] l : reWrite) {
				String s = String.join(",", l);
				try {
					fw.append(s + "\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (fw!= null){
				try {
					fw.flush();
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	
}


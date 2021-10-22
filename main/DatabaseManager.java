
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
* <h1>Database Manager class</h1>
* Course Manager is the logic class to verify that all inputs 
* from user interface is correct before passing information to 
* entity course class.
* <p>
* contains methods such as adding course, dropping course, updates course
* links to course database manager to update course csv file
* @author  Joelle Thng, Wang Anyi
* @version 1.0
* @since   2020-25-11
*/


public abstract class DatabaseManager {
    /** 
    * creates logger instance of database manager to log exceptions and other errors
    */
    private static Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    /** 
    * file of super class
    */
    private String FILE;
    /** 
    * temporary file to store data 
    */
    private static String tempData = "temp.csv";
    /** 
    * columns that are specific to different instance of FILE
    */
    private static List<String> COLUMNS;

    /** 
    * creates instance of superclass database manager using the file path and columns of child classes
    * @param fileName file path to read csv
    * @param Columns number of columns that are in the csv 
    */
    public DatabaseManager(String fileName, List<String> Columns) {
        FILE = fileName;
        COLUMNS = Columns;
    } 

    
    /** 
     * @return String gets file path of file in this instance
     */
    public String getFILE(){
        return FILE;
    }

    
    /** 
     * gets a String[] of data that is identified by the column number anf the unique identifier of "lookingFor"
     * @param fileName file path to read
     * @param col column number to look for unique identifier eg. Username
     * @param lookingFor item to look for eg. "Hermione"
     * @return String[] for the entire row
     * @exception IOException if error closing/opening file
     */
    public static String[] getRow(String fileName, int col, String lookingFor) {
        BufferedReader fileReader = null;
        String line = "";
        try {
            fileReader = new BufferedReader(new FileReader(fileName));
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",",-1); 
                if (tokens[col].equals(lookingFor)) { 
                    fileReader.close();
                    return tokens;
                }
            }
            
        } catch (IOException e) {
            logger.log(Level.FINE, "Could not open/find "+ fileName +" to get row (DatabaseManager -> getRow)", e);
        } finally{
            try {
                fileReader.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Could not close "+ fileName +"(DatabaseManager -> getRow)", e);
            }
        }
        System.out.println("could not find the entry you were looking for");
        return null;
    }

    
    /** 
     * changes the access period of the students if they are in a particular specialisation
     * @param replace data to replace csv
     * @param identifier item to look for eg. "DSAI"
     * @param uniqueColumn column number where the identifier can be found 
     * @return boolean true if access period can be changed, false otherwise 
     * @exception IOException if error closing/opening file
     */
    public boolean updateRow(String replace, String identifier, int uniqueColumn) {
        boolean success = false;
        File oldFile = new File(FILE);
        File newFile = new File(tempData);
        PrintWriter pw = null;
        BufferedReader fileReader = null;

        try (FileWriter fw = new FileWriter(tempData, true); 
            BufferedWriter bw = new BufferedWriter(fw);){
            pw = new PrintWriter(bw);

            String line = "";
            fileReader = new BufferedReader(new FileReader(FILE));

            while ((line = fileReader.readLine()) != null) { 
                StringBuilder newLine = new StringBuilder();
                String[] tokens = line.split(",",-1);
                if (tokens[uniqueColumn].equals(identifier)) { 
                    for (int i = 0; i < 7; i++) { 
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    newLine.append(replace);
                    for (int i = 11; i < tokens.length; i++) {
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    newLine.append(" ");
                    pw.println(newLine);
                    success = true;
                } else { 
                    for (int i = 0; i < tokens.length; i++) { 
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    newLine.append(" ");
                    pw.println(newLine);
                }
            }

        } catch (Exception e) {
            logger.log(Level.FINE, "Could not open/find "+ FILE +" to read/write (DatabaseManager -> updateRow)", e);
        }finally{
            if(pw!=null){
                pw.flush();
                pw.close();
            }
            File name = new File(FILE); 
            oldFile.delete();
            newFile.renameTo(name);
            try {
                fileReader.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Could not close "+ FILE +"(DatabaseManager -> updateRow)", e);
        
            }
            
        }return success;
    }

    
    /** 
     * appends entry to csv file
     * @param toAdd data to add to csv row
     * @param uniqueColumn column number where the identifier can be found 
     * @param identifier item to look for eg. "DSAI"
     * @exception IOException if error closing/opening file
     */
    public void appendEntry(String toAdd, int uniqueColumn, String identifier) {

        File oldFile = new File(FILE);
        File newFile = new File(tempData);
        PrintWriter pw= null;
        BufferedReader fileReader= null;
        

        try(FileWriter fw = new FileWriter(tempData, true);
            BufferedWriter bw = new BufferedWriter(fw);){
            
            pw = new PrintWriter(bw);

            String line;
            fileReader = new BufferedReader(new FileReader(FILE));
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",",-1);
                if (tokens[uniqueColumn].equals(identifier)) { 
                    StringBuilder newLine = new StringBuilder();
                    int i=0;
                    while(!tokens[i].equals("")){
                        newLine.append(tokens[i]);
                        newLine.append(",");
                        i++;
                    }
                    newLine.append(toAdd);
                    for(int j = i+1; j<25; j++){
                        newLine.append(",");
                    }
                    newLine.append(tokens[25]+",");
                    pw.println(newLine);
                }
                else{ 
                    StringBuilder newLine = new StringBuilder();
                    for(int i=0; i<tokens.length; i++){
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    pw.println(newLine);
                }
            }
            
        }
        catch(Exception e){
            logger.log(Level.FINE, "Could not open/find "+ FILE +" to read/write (DatabaseManager -> addEntry)", e);
        }finally{
            if(pw!=null){
                pw.flush();
                pw.close();
            }
            File name = new File(FILE); 
            oldFile.delete();
            newFile.renameTo(name);
            try {
                fileReader.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Could not close "+ FILE +"(DatabaseManager -> addEntry)", e);
        
            }
            
        }
    }


    
    /** 
     * replaces information in csv file 
     * @param uniqueColumn column number where the identifier can be found 
     * @param identify1 identifier 1
     * @param identify2 identifier 2
     * @param information data to replace csv data with
     * @return true if data is replaces successfully, false otherwise
     * @exception IOException if error closing/opening file
     */
    protected boolean replaceInformationCSV(int uniqueColumn, String identify1, String identify2, String information) { 
        boolean result = false;
        FileWriter fw = null;
        ArrayList<String[]> reWrite = new ArrayList<String[]>();
        String line = "";
        try(BufferedReader fileReader = new BufferedReader(new FileReader(FILE))) {
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",", -1);
                List<String> token = Arrays.asList(tokens);
                if (token.contains(identify1) && token.contains(identify2)) { 
                    tokens[uniqueColumn] = information;
                    reWrite.add(tokens);
                    result = true;
                } else reWrite.add(tokens);
            }
        } catch (IOException e) {
            logger.log(Level.FINE, "Could not find file "+ FILE +" to read (DatabaseManager -> replaceInformationinCSV)", e);
        }
        try {
            fw = new FileWriter(FILE);
            for (String[] l : reWrite) {
                String s = String.join(",",l);
                try {
                    fw.append(s+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            logger.log(Level.FINE, "Could not find file "+ FILE +" to write (DatabaseManager -> replaceInformationinCSV)", e);
        } 
        finally{
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Could not close file "+ FILE +"(DatabaseManager -> replaceInformationinCSV)", e);
            }
        }
        return result;
    }

    
    /** 
     * replaces information in csv file 
     * @param identify1 identifier 1
     * @param identify2 identifier 2
     * @param information1 data to replace identify1 in csv with
     * @param information2 data to replace identify2 in csv with
     * @return true if data is replaces successfully, false otherwise
     * @exception IOException if error closing/opening file
     */
    protected boolean replaceInformationCSV(String identify1, String identify2, String information1, String information2) { 
        boolean result = false;
        FileWriter fw = null;
        ArrayList<String[]> reWrite = new ArrayList<String[]>();
        String line = "";
        try(BufferedReader fileReader = new BufferedReader(new FileReader(FILE))) {
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",", -1);
                List<String> token = Arrays.asList(tokens);
                if (token.contains(identify1) && token.contains(identify2)) { 
                    tokens[token.indexOf(identify1)] = information1;
                    tokens[token.indexOf(identify2)] = information2;
                    reWrite.add(tokens);
                    result = true;
                } else reWrite.add(tokens);
            }
        } catch (IOException e) {
            logger.log(Level.FINE, "Could not find file "+ FILE +" to read (DatabaseManager -> replaceInformationinCSV)", e);
        }
        try {
            fw = new FileWriter(FILE);
            for (String[] l : reWrite) {
                String s = String.join(",",l);
                try {
                    fw.append(s+"\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            logger.log(Level.FINE, "Could not find file "+ FILE +" to write (DatabaseManager -> replaceInformationinCSV)", e);
        } 
        finally{
            try {
                fw.flush();
                fw.close();
            } catch (IOException e) {
                logger.log(Level.FINE, "Could not close file "+ FILE +"(DatabaseManager -> replaceInformationinCSV)", e);
            }
        }
        return result;
    }


    /** 
     * removes 2 consecutive values in csv (used for removing courseID and course index)
     * @param uniqueColumn column number where the identifier can be found  
     * @param identifier item to look for eg. "DSAI"
     * @param valueToRemove data to remove in csv
     * @exception IOException if error closing/opening file
     */
    public void removeRow(int uniqueColumn, String identifier, String valueToRemove){
        File oldFile = new File(FILE);
        File newFile = new File(tempData);
        PrintWriter pw=null;
        BufferedReader fileReader=null;


        try{
            FileWriter fw = new FileWriter(tempData, true);
            BufferedWriter bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            String line;
            fileReader = new BufferedReader(new FileReader(FILE));
            while ((line = fileReader.readLine()) != null) {
                String[] tokens = line.split(",",-1);
                if (tokens[uniqueColumn].equals(identifier)) { 
                    StringBuilder newLine = new StringBuilder();
                    for(int i=0; i<11; i++){ 
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    for(int i=11; i<25; i+=2){
                        if(!tokens[i].equals(valueToRemove)){ 
                            newLine.append(tokens[i] + "," + tokens[i+1]+ ",");
                        }
                    }
                    newLine.append("," + "," + tokens[25]);
                    pw.println(newLine);
                }
                else{ 
                    StringBuilder newLine = new StringBuilder();
                    for(int i=0; i<tokens.length; i++){
                        newLine.append(tokens[i]);
                        newLine.append(",");
                    }
                    pw.println(newLine);
                }
            }
            
        }
        catch(IOException e){
            logger.log(Level.FINE, "Error changing student file(DatabaseManager)", e);
        }finally{
            if(pw!=null){
                pw.flush();
                pw.close();
            }
            
            File name = new File(FILE); 
            oldFile.delete();
            newFile.renameTo(name);
            if(fileReader!=null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    logger.log(Level.FINE, "Error closing file(DatabaseManager -> removeRow)", e);
                }
            }
        }
    }

 
    /** 
     * swaps two consecutive values with another 2 consecutive values
     * used to swap student courseID and course index
    * @param uniqueColumn column number where the identifier can be found  
    * @param identifier1 row 1 where the swapping occurs
    * @param identifier2 row 2 where the swapping occurs
    * @param dataToChange1 data to change in csv eg. "987655" --&gt; course index
    * @param dataToChange2 data to change in csv eg. "127834"--&gt; course index
    * @param courseID courseID of course to swap
    * @exception Exception if interrupted execution for timeunit
    */
    protected void swapValuesCSV(int uniqueColumn, String identifier1, String identifier2, String dataToChange1, String dataToChange2, String courseID) { // write information to csv
      
        try {
            List<String> temp = Arrays.asList(courseID, dataToChange1);
            String index1 = String.join(",", temp); 
            List<String> tempList = Arrays.asList(courseID, dataToChange2);
            String index2 = String.join(",", tempList); 
            
            removeRow(uniqueColumn, identifier1, courseID); 
            removeRow(uniqueColumn, identifier2, courseID); 
            TimeUnit.SECONDS.sleep(1);
            appendEntry(index2, uniqueColumn, identifier1); 
            appendEntry(index1, uniqueColumn, identifier2); 
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e) {
            logger.log(Level.FINE, "Error swapping values in csv (DatabaseManager -> swapValuesCSV)", e);

        }
        
    }

    
    /** 
     * adds entry into the csv, inserted at the last row
     * @param rowToAdd String of information to add
     * @exception IOException if error closing/opening file
     */
    public void addEntry(String rowToAdd){
        File file = new File(FILE);
        PrintWriter pw = null;

        try(FileWriter fw = new FileWriter(file, true); 
            BufferedWriter bw = new BufferedWriter(fw);){
            
            pw = new PrintWriter(bw);

            StringBuilder newLine = new StringBuilder();
            newLine.append("\n");
            newLine.append(rowToAdd);
            for (int i=rowToAdd.split(",",-1).length-1; i<COLUMNS.size(); i++){ 
                newLine.append("");
                newLine.append(",");
            }
            System.out.println(" ");
            pw.print(newLine);
        } catch(Exception e){
            logger.log(Level.FINE, "Error closing file(DatabaseManager -> addEntry)", e);
        } finally{
            if(pw != null){
                pw.flush();
                pw.close();
                
            }
        }
    }


}

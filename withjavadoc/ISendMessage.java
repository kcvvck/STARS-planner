/**
* <h1>Interface ISendMessage</h1>
* sends message to students when they are registered/deregistered for the course
* by specified classes
* @author  
* @version 1.0
* @since   2020-25-11
*/

/** 
 * @param s specific instance of student
 * @param MESSAGE message to send to student
 * @param reg_or_dereg message to send to student (reg = registered, dereg = deregistered)
 */

interface ISendMessage {
    public void sendMessage(Student s, String Message, String reg_or_dereg);

}


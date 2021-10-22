/**
* <h1>Phone entity class</h1>
* sends message to students when they are registered for the course
* by sms
* @author  Kam Chin Voon
* @version 1.0
* @since   2020-25-11
*/

public class Phone implements ISendMessage{
    
	/**
	 * {@inheritDoc} @see {@link ISendMessage#sendMessage(Student, String, String)}
	*/
    @Override
    public void sendMessage(Student s, String message, String reg_or_dereg) {
        if(reg_or_dereg.equals("reg")){
            System.out.println("sms has been sent to: " + s.getFirstName() + " to indicate successful registration.");
        }else if(reg_or_dereg.equals("dereg")){
            System.out.println("sms has been sent to: " + s.getFirstName() + " to indicate successful deregistration.");
        }else{;
            System.out.println("Error, no such MESSAGES");
        }
        
        
    }

}

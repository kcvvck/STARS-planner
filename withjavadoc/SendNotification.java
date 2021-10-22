/**
* <h1>SendNotification class</h1>
* sends message to students when they are registered/deregistered for the course
* by sms
* @author  Kam Chin Voon
* @version 1.0
* @since   2020-25-11
*/

public class SendNotification {
   
   private ISendMessage methodToSend;
   private ISendMessage methodToSend2;

   /** 
    * creates send notification object that is able to use one method to send message
    * @param methodToSend instance 1 of ISendMessage
    */
   public SendNotification(ISendMessage methodToSend) {
      this.methodToSend = methodToSend; // get method to send
      
   }
   /** 
    * creates send notification object that is able to use both methods to send message
    * @param methodToSend instance 1 of ISendMessage
    * @param methodToSend2 instance 2 of ISendMessage
    */
   public SendNotification(ISendMessage methodToSend, ISendMessage methodToSend2) { // if method is both
     this.methodToSend = methodToSend;
     this.methodToSend2 = methodToSend2;
  }

   
   /** 
    * sends message to students when they are registered/deregistered for the course
    * if there are 2 methods, send via both email and phone
    * @param s specific instance of student
    * @param message message to send to student
    * @param reg_or_dereg message to send to student (reg = registered, dereg = deregistered)
    */
   public void sendMessageTo(Student s, String message, String reg_or_dereg){ // send message to both if specified
      methodToSend.sendMessage(s, message, reg_or_dereg); 
      if(methodToSend2!=null){
         methodToSend2.sendMessage(s, message, reg_or_dereg);
      }
   }

   


}
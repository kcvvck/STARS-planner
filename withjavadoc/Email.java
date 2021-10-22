


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;


import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
* <h1>Email class</h1>
* sends email to students when they are registered/deregistered for the course
* from cz2002dsai2grp5@gmail.com
* <p>
* code adapted from NTU CZ2002 course materials
* @author  
* @version 1.0
* @since   2020-25-11
*/

public class Email implements ISendMessage{

	
	/**
	 * {@inheritDoc} @see {@link ISendMessage#sendMessage(Student, String, String)}
	 * @exception MessagingException if server cannot be connected
	* @exception RuntimeException when above exception is thrown
	*/
	@Override
	public void sendMessage(Student s, String MESSAGE, String reg_or_dereg) {

		final String username = "cz2002dsai2grp5@gmail.com"; 
		final String password = "@forCZ2002"; 

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(s.getEmail())); 

			if (reg_or_dereg=="reg"){
				message.setSubject(s.getFirstName() + ", confirmed Registration!");
			} else if(reg_or_dereg =="dereg"){
				message.setSubject(s.getFirstName() + ", confirmed DeRegistration!");
			}else{
				System.out.println("Not acceptable MESSAGE");
				return;
			}
			message.setText(MESSAGE);

			Transport.send(message);

			System.out.println("Email sent to "+ s.getName());

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}

/**
 * <h1>Interface IUser</h1>
 * Interface that Student and Admin implements, extensible to other potential users e.g. Professor
 * Used in MainUI and UserFactory to display different user interfaces depending on whether the user is student/admin
 * Child classes will be entity classes
 * @author Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */
public interface IUser {
    /**
     * Both Student and Admin classes has to implement this method, and display their respective Apps
     */
    void run();
 }

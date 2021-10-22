/**
 * <h1>Interface IMainInterface</h1>
 * Interface that StudentApp and AdminApp implements,
 * Represents the display/user interface the user will see
 * Child classes will be boundary classes to get input from user and display information
 * @author Kam Chin Voon
 * @version 1.0
 * @since 2020-25-11
 */
public interface IMainInterface {
    /**
     * Both StudentApp and AdminApp implements this, just displays all the different options that they can choose from,
     * depending on user input, methods from the other classes will be called.
     */
    void display();
}

/**
 * <p>Title: PasswordEvaluationGUITestbed Class.</p>
 * 
 * <p>Description: A JavaFX demonstration application and baseline for a sequence of projects.</p>
 * 
 * <p>Copyright: Lynn Robert Carter ©️ 2022</p>
 * 
 * This class initializes and displays a JavaFX GUI for password evaluation testing.
 * 
 * @version 4.00	2017-10-16 The mainline of a JavaFX-based GUI implementation of a User 
 *                   Interface testbed.
 * 
 * @author Lynn Robert Carter
 */
public class PasswordEvaluationGUITestbed extends Application {
    
    /** The width of the window in pixels */
    public final static double WINDOW_WIDTH = 500;

    /** The height of the window in pixels */
    public final static double WINDOW_HEIGHT = 430;

    /** The GUI object that handles all UI-related elements for the application */
    public UserInterface theGUI;

    /**
     * The start method is the main entry point for any JavaFX application.
     * It sets up the window (stage), its contents (scene), and the GUI layout.
     *
     * @param theStage The primary window of the application
     * @throws Exception If an error occurs during GUI initialization
     */
    @Override
    public void start(Stage theStage) throws Exception {
        
        // Set the title displayed in the window's title bar
        theStage.setTitle("Lynn Robert Carter");
        
        // Create the root pane which holds all UI elements
        Pane theRoot = new Pane();
        
        // Initialize and attach the user interface components to the root pane
        theGUI = new UserInterface(theRoot);
        
        // Create the scene (visual container) with the specified root and dimensions
        Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        // Assign the scene to the stage (window)
        theStage.setScene(theScene);
        
        // Show the stage (make the window visible)
        theStage.show();
    }

    /**
     * The main method launches the JavaFX application.
     * This is required when launching the app outside of a JavaFX-aware IDE.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Launch the JavaFX application; this calls the start() method internally
        launch(args);
    }
}
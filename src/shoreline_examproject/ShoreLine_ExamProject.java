package shoreline_examproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shoreline_examproject.BLL.DirectoryWatcher;

/**
 *
 * @author Dominik
 */
public class ShoreLine_ExamProject extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        DirectoryWatcher dw = new DirectoryWatcher();
        dw.start();
        
        Parent root = FXMLLoader.load(getClass().getResource("GUI/View/MainWindow.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("ShoreLine Exam Project");
        stage.setResizable(false);
        stage.show();
        
        stage.setOnCloseRequest((WindowEvent event) -> { // Close all windows on main window exit.
            Platform.exit();
            System.exit(0);
        } 
        );
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

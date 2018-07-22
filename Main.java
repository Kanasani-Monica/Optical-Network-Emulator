package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		try {
			
			Scene scene = new Scene(root,640,480);
			scene.getStylesheets().add("application.css");
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Optical Network Emulator");
			primaryStage.show();
			
		} catch(NullPointerException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
		root.setCenter(new RootLayout());
		} catch(RuntimeException e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void main(String[] args) {
		
		launch(args);
	}
}

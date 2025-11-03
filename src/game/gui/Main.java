package game.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	Image iconImage = new Image(getClass().getResourceAsStream("icon.png"));
        primaryStage.getIcons().add(iconImage);
        
        SceneController sceneController = new SceneController(primaryStage);
        sceneController.switchScenes();
        
        primaryStage.setTitle("Attack on Titan: Utopia");
    	
    }

    public static void main(String[] args) {
        launch(args);
    }
}

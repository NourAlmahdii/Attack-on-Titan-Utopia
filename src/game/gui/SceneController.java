package game.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import game.engine.Battle;
import game.engine.BattlePhase;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SceneController {
    private Stage primaryStage;
    private MediaPlayer mediaPlayer1; 
    private MediaPlayer mediaPlayer2; 
    private Battle battle;
    private boolean mode = true;
    private int weaponCode;
    private int weaponprice;

    public SceneController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeMediaPlayer1();
        initializeMediaPlayer2(); 
        playBackgroundSound();
    }

    public void switchScenes() {
        showIntro();
    }

    private void initializeMediaPlayer1() {
        String soundFile1 = getClass().getResource("AOTSoundtrack1.mp3").toExternalForm();
        Media sound1 = new Media(soundFile1);
        mediaPlayer1 = new MediaPlayer(sound1);
        mediaPlayer1.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer1.setOnEndOfMedia(() -> mediaPlayer1.seek(Duration.ZERO));
    }

    private void initializeMediaPlayer2() {
        String soundFile2 = getClass().getResource("AOTSoundtrack2.mp3").toExternalForm();
        Media sound2 = new Media(soundFile2);
        mediaPlayer2 = new MediaPlayer(sound2);
        mediaPlayer2.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer2.setOnEndOfMedia(() -> mediaPlayer2.seek(Duration.ZERO)); 
    }


    public void playBackgroundSound() {
        mediaPlayer1.play(); 
    }

    private void showIntro() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: black;");

        Text text = new Text("GIU Anime Team Presents...");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Copperplate Gothic Bold", 70));
        StackPane.setAlignment(text, Pos.CENTER);
        text.setTranslateX((screenWidth / 2 - text.getLayoutBounds().getWidth() / 2) - 50);
        root.getChildren().add(text);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        text.wrappingWidthProperty().bind(scene.widthProperty().multiply(0.9));

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> showStartingScreen());
        pause.play();
    }

    private void showStartingScreen() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Image image = new Image(getClass().getResourceAsStream("startscreen2.png"));
        ImageView imageView = new ImageView(image);

        Text text1 = new Text("Attack on\n Titan: Utopia");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Copperplate Gothic Bold", 100));
        text1.setTextAlignment(TextAlignment.CENTER);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_LEFT);
        vbox.setPadding(new Insets(0, 0, 30, 50));
        vbox.getChildren().add(text1);

        StackPane root = new StackPane(imageView, vbox);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> showStartingScreen2());
        pause.play();
    }

    private void showStartingScreen2() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Image image = new Image(getClass().getResourceAsStream("startscreen2.png"));
        ImageView imageView = new ImageView(image);

        Button startButton = new Button("Start Game");
        Button howToPlayButton = new Button("How To Play");
        Button settingsButton = new Button("Settings");

        String buttonStyle = "-fx-font-size: 38px; -fx-font-family: 'Copperplate Gothic Bold'; "
                + "-fx-background-color: transparent; -fx-text-fill: white;";
        startButton.setStyle(buttonStyle);
        howToPlayButton.setStyle(buttonStyle);
        settingsButton.setStyle(buttonStyle);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, howToPlayButton, settingsButton);

        StackPane root = new StackPane();
        root.getChildren().addAll(imageView, buttonBox);

        howToPlayButton.setOnAction(e -> showHowToPlay());
        startButton.setOnAction(e -> showGamemode());

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void showHowToPlay() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth() / 2;
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() / 2;

        Stage howToPlayStage = new Stage();
        howToPlayStage.initModality(Modality.APPLICATION_MODAL);
        howToPlayStage.initOwner(primaryStage);

        String intro = "This is a tower defence game, players’ main goal is to protect their"
                + " lane walls from the approaching titans by buying weapons into the lanes "
                + "to attack the titans present in it.\n";

        String instructions = "How to Play:\n\n"
                + "1. Defeat titans to gain their resources.\n"
                + "2. Protect the walls from the titans using the weapons.\n"
                + "3. Buy weapons from the weapons shop using the resources gained.\n"
                + "4. This game never ends, only if you get defeated\n"
                + "5. Have fun!";

        Image image = new Image(getClass().getResourceAsStream("howtoplay.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        imageView.setFitHeight(Screen.getPrimary().getVisualBounds().getHeight() / 2);

        Group root = new Group();

        Text introductoryText = new Text(intro);
        introductoryText.setFill(Color.WHITE);
        introductoryText.setFont(Font.font("Copperplate Gothic Bold", 26));
        introductoryText.setWrappingWidth(screenWidth * 0.9);
        introductoryText.setTextAlignment(TextAlignment.CENTER);

        Text instructionsText = new Text(instructions);
        instructionsText.setFill(Color.WHITE);
        instructionsText.setFont(Font.font("Copperplate Gothic Bold", 22));
        instructionsText.setWrappingWidth(screenWidth * 0.9);
        instructionsText.setTextAlignment(TextAlignment.CENTER);

        double introductoryTextX = (screenWidth - introductoryText.getLayoutBounds().getWidth()) / 2;
        double introductoryTextY = (screenHeight - introductoryText.getLayoutBounds().getHeight()
                - instructionsText.getLayoutBounds().getHeight()) / 2;
        double instructionsTextX = (screenWidth - instructionsText.getLayoutBounds().getWidth()) / 2;
        double instructionsTextY = introductoryTextY + introductoryText.getLayoutBounds().getHeight();

        introductoryText.setLayoutX(introductoryTextX);
        introductoryText.setLayoutY(introductoryTextY);
        instructionsText.setLayoutX(instructionsTextX);
        instructionsText.setLayoutY(instructionsTextY);
        imageView.setX((screenWidth - imageView.getBoundsInLocal().getWidth()) / 2);
        imageView.setY((screenHeight - imageView.getBoundsInLocal().getHeight()) / 2);

        Button closeButton = new Button("X");
        closeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 50px; -fx-font-family: 'Copperplate Gothic Bold';");
        closeButton.setOnAction(e -> howToPlayStage.close());
        closeButton.setLayoutX(screenWidth - 110); 
        closeButton.setLayoutY(7); 

        root.getChildren().addAll(imageView, introductoryText, instructionsText, closeButton);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        howToPlayStage.setScene(scene);
        howToPlayStage.setTitle("How to Play");
        howToPlayStage.initStyle(StageStyle.UNDECORATED);
        howToPlayStage.setFullScreen(false);
        howToPlayStage.show();
    }

    private void showGamemode() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        String intro = "Choose a game mode";

        Image image = new Image(getClass().getResourceAsStream("startscreen1.png"));
        ImageView imageView = new ImageView(image);

        Group root = new Group();

        Text introductoryText = new Text(intro);
        introductoryText.setFill(Color.WHITE);
        introductoryText.setFont(Font.font("Copperplate Gothic Bold", 70));
        introductoryText.setWrappingWidth(screenWidth * 0.3);
        introductoryText.setTextAlignment(TextAlignment.LEFT);

        Button easyModeButton = new Button("Easy");
        easyModeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 70; -fx-font-family: 'Copperplate Gothic Bold';");
        easyModeButton.setOnAction(e -> {
            mode = true;
            showEasyMode();
        });

        Tooltip easyModeTooltip = new Tooltip("3 initial lanes\n250 initial resources per lane");
        easyModeTooltip.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold';");
        easyModeButton.setTooltip(easyModeTooltip);

        Button hardModeButton = new Button("Hard");
        hardModeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 70; -fx-font-family: 'Copperplate Gothic Bold';");
        hardModeButton.setOnAction(e -> {
            mode = false;
            showHardMode();
        });

        Tooltip hardModeTooltip = new Tooltip("5 initial lanes\n125 initial resources per lane");
        hardModeTooltip.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold';");
        hardModeButton.setTooltip(hardModeTooltip);

        VBox buttonsContainer = new VBox(20);
        buttonsContainer.getChildren().addAll(easyModeButton, hardModeButton);
        buttonsContainer.setLayoutX(screenWidth * 0.20);
        buttonsContainer.setLayoutY(screenHeight * 0.1 + 330);

        introductoryText.setLayoutX(screenWidth * 0.05);
        introductoryText.setLayoutY(screenHeight * 0.1 + 30);

        imageView.setX((screenWidth - imageView.getBoundsInLocal().getWidth()) / 2);
        imageView.setY((screenHeight - imageView.getBoundsInLocal().getHeight()) / 2);

        root.getChildren().addAll(imageView, introductoryText, buttonsContainer);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    
    private void showEasyMode() {
        mediaPlayer1.pause();
        mediaPlayer2.play();

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        VBox outVBox = new VBox();
        outVBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        outVBox.setPrefSize(screenWidth * 0.20, screenHeight * 0.20); 

        try {
        	initializingEasy();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label scoreLabel = new Label("Score: " + showScore());
        scoreLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        scoreLabel.setTranslateY(90);
        scoreLabel.setTranslateX(30);

        Label turnLabel = new Label("Turn: " + showTurn());
        turnLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        turnLabel.setTranslateY(90);
        turnLabel.setTranslateX(130);

        Label phaseLabel = new Label("Phase: " + showPhase());
        phaseLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        phaseLabel.setTranslateY(90);
        phaseLabel.setTranslateX(190);

        Label recourcesLabel = new Label("Resources: " + showRecources());
        recourcesLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        recourcesLabel.setTranslateY(90);
        recourcesLabel.setTranslateX(320);

        Label lanesLabel = new Label("Lanes alive: " + showLanes());
        lanesLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        lanesLabel.setTranslateY(90);
        lanesLabel.setTranslateX(410);

        Button weaponShopButton = new Button("Weapon Shop");
        weaponShopButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        weaponShopButton.setOnAction(e -> {
        	showWeaponShop(phaseLabel,turnLabel,recourcesLabel,lanesLabel);
        	recourcesLabel.setText("Resources: " + showRecources());
        });
        weaponShopButton.setTranslateY(50);

        Button turnPassingButton = new Button("Next Turn");
        turnPassingButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        turnPassingButton.setOnAction(e -> {
        	showPassingTurn(phaseLabel, turnLabel, recourcesLabel, lanesLabel);
        	HashMap <Titan,Rectangle> titanShape = new HashMap();
        	for(Lane lane : battle.getLanes()){
        		for(Titan titan : lane.getTitans()){
        			if(titanShape.containsKey(titan)){
        				Rectangle rectangle = titanShape.get(titan);
        				rectangle.setTranslateX(titan.getSpeed()*60);
        			}
        			else{
        				Rectangle rc;
        				double h = 0;
        				Color c = null;
        				if(titan instanceof PureTitan){
        					h= 20;
        					c = Color.PINK;
        				}
        				else{
        					if(titan instanceof AbnormalTitan){
            					h= 40;
            					c = Color.PURPLE;
            				}
        					else{
        						if(titan instanceof ArmoredTitan){
                					h= 60;
                					c = Color.BEIGE;
                				}
        						else{
        							if(titan instanceof ColossalTitan){
        	        					h= 80;
        	        					c = Color.GREEN;
        	        				}
        						}
        					}
        				}
        				rc= new Rectangle(10,h, c);
        				rc.setRotate(180);
        				titanShape.put(titan, rc);
        			}
        		}
        	}
        });
        turnPassingButton.setTranslateY(50);

        HBox hboxLabels = new HBox();
        hboxLabels.setSpacing(10);
        hboxLabels.getChildren().addAll(weaponShopButton, turnPassingButton, scoreLabel, turnLabel, phaseLabel, recourcesLabel, lanesLabel);

        outVBox.getChildren().addAll(hboxLabels, turnPassingButton);

        VBox inVBox = new VBox();
        inVBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        inVBox.setPrefSize(screenWidth, screenHeight);
        inVBox.setPadding(new Insets(10)); 

        HBox Lane1 = new HBox();
        HBox Lane2 = new HBox();
        HBox Lane3 = new HBox();
        
        turnPassingButton.setOnAction(e -> {
        	showPassingTurn(phaseLabel, turnLabel, recourcesLabel, lanesLabel);
        	HashMap <Titan,Rectangle> titanShape = new HashMap();
        	for(Lane lane : battle.getLanes()){
        		for(Titan titan : lane.getTitans()){
        			if(titanShape.containsKey(titan)){
        				Rectangle rectangle = titanShape.get(titan);
        				rectangle.setTranslateX(titan.getSpeed()*60);
        			}
        			else{
        				Rectangle rc;
        				double h = 0;
        				Color c = null;
        				if(titan instanceof PureTitan){
        					h= 20;
        					c = Color.PINK;
        				}
        				else{
        					if(titan instanceof AbnormalTitan){
            					h= 40;
            					c = Color.PURPLE;
            				}
        					else{
        						if(titan instanceof ArmoredTitan){
                					h= 60;
                					c = Color.BEIGE;
                				}
        						else{
        							if(titan instanceof ColossalTitan){
        	        					h= 80;
        	        					c = Color.GREEN;
        	        				}
        						}
        					}
        				}
        				rc= new Rectangle(10,h, c);
        				rc.setRotate(180);
        				Lane1.getChildren().add(rc);
        				titanShape.put(titan, rc);
        			}
        		}
        	}
        });

        Lane1.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane2.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane3.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");

        if(battle.getOriginalLanes().get(0).getLaneWall().getCurrentHealth()<0){
        	Lane1.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(1).getLaneWall().getCurrentHealth()<0){
        	Lane2.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(2).getLaneWall().getCurrentHealth()<0){
        	Lane3.setStyle("-fx-background-color: black;");
        }
        
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();
        VBox vbox3 = new VBox();

        vbox1.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox2.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox3.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        
        Label l1= new Label("Wall 1");
        l1.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info1 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(0).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(0).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(0).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(0).getTitans().size());
        info1.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l1.setTooltip(info1);
        Label l2= new Label("Wall 2");
        l2.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info2 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(1).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(1).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(1).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(1).getTitans().size());
        info2.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l2.setTooltip(info2);
        Label l3= new Label("Wall 3");
        l3.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info3 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(2).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(2).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(2).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(2).getTitans().size());
        info3.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l3.setTooltip(info3);
        
        vbox1.getChildren().addAll(l1);
        vbox2.getChildren().addAll(l2);
        vbox3.getChildren().addAll(l3);
        
        double vboxWidth = screenWidth * 0.07;

        vbox1.setPrefWidth(vboxWidth);
        vbox2.setPrefWidth(vboxWidth);
        vbox3.setPrefWidth(vboxWidth);

        Lane1.getChildren().add(vbox1);
        Lane2.getChildren().add(vbox2);
        Lane3.getChildren().add(vbox3);

        Region spacer1 = new Region();
        spacer1.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer1.setPrefHeight(10);

        Region spacer2 = new Region();
        spacer2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer2.setPrefHeight(10);

        inVBox.getChildren().addAll(Lane1, spacer1, Lane2, spacer2, Lane3);
        VBox.setVgrow(Lane1, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane2, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane3, javafx.scene.layout.Priority.ALWAYS);

        inVBox.getChildren().add(outVBox);

        Scene scene = new Scene(inVBox, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        
        if(battle.isGameOver()){
        	showEnd();
        }
        
    }


    private void showHardMode() {
    	mediaPlayer1.pause();
        mediaPlayer2.play();

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        VBox outVBox = new VBox();
        outVBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        outVBox.setPrefSize(screenWidth * 0.20, screenHeight * 0.20); 

        try {
        	initializingHard();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label scoreLabel = new Label("Score: " + showScore());
        scoreLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        scoreLabel.setTranslateY(90);
        scoreLabel.setTranslateX(30);

        Label turnLabel = new Label("Turn: " + showTurn());
        turnLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        turnLabel.setTranslateY(90);
        turnLabel.setTranslateX(130);

        Label phaseLabel = new Label("Phase: " + showPhase());
        phaseLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        phaseLabel.setTranslateY(90);
        phaseLabel.setTranslateX(190);

        Label recourcesLabel = new Label("Resources: " + showRecources());
        recourcesLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        recourcesLabel.setTranslateY(90);
        recourcesLabel.setTranslateX(320);

        Label lanesLabel = new Label("Lanes alive: " + showLanes());
        lanesLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        lanesLabel.setTranslateY(90);
        lanesLabel.setTranslateX(410);

        Button weaponShopButton = new Button("Weapon Shop");
        weaponShopButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        weaponShopButton.setOnAction(e -> {
        	showWeaponShop(phaseLabel,turnLabel,recourcesLabel,lanesLabel);
        	recourcesLabel.setText("Resources: " + showRecources());
        });
        weaponShopButton.setTranslateY(50);

        Button turnPassingButton = new Button("Next Turn");
        turnPassingButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 30px; -fx-font-family: 'Copperplate Gothic Bold';");
        turnPassingButton.setOnAction(e -> {
        	showPassingTurn(phaseLabel, turnLabel, recourcesLabel, lanesLabel);
        	HashMap <Titan,Rectangle> titanShape = new HashMap();
        	for(Lane lane : battle.getLanes()){
        		for(Titan titan : lane.getTitans()){
        			if(titanShape.containsKey(titan)){
        				Rectangle rectangle = titanShape.get(titan);
        				rectangle.setTranslateX(titan.getSpeed()*60);
        			}
        			else{
        				Rectangle rc;
        				double h = 0;
        				Color c = null;
        				if(titan instanceof PureTitan){
        					h= 20;
        					c = Color.PINK;
        				}
        				else{
        					if(titan instanceof AbnormalTitan){
            					h= 40;
            					c = Color.PURPLE;
            				}
        					else{
        						if(titan instanceof ArmoredTitan){
                					h= 60;
                					c = Color.BEIGE;
                				}
        						else{
        							if(titan instanceof ColossalTitan){
        	        					h= 80;
        	        					c = Color.GREEN;
        	        				}
        						}
        					}
        				}
        				rc= new Rectangle(10,h, c);
        				rc.setRotate(180);
        				titanShape.put(titan, rc);
        			}
        		}
        	}
        });
        turnPassingButton.setTranslateY(50);

        HBox hboxLabels = new HBox();
        hboxLabels.setSpacing(10);
        hboxLabels.getChildren().addAll(weaponShopButton, turnPassingButton, scoreLabel, turnLabel, phaseLabel, recourcesLabel, lanesLabel);

        outVBox.getChildren().addAll(hboxLabels, turnPassingButton);

        VBox inVBox = new VBox();
        inVBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        inVBox.setPrefSize(screenWidth, screenHeight);
        inVBox.setPadding(new Insets(10)); 

        HBox Lane1 = new HBox();
        HBox Lane2 = new HBox();
        HBox Lane3 = new HBox();
        HBox Lane4 = new HBox();
        HBox Lane5 = new HBox();
        
        turnPassingButton.setOnAction(e -> {
        	showPassingTurn(phaseLabel, turnLabel, recourcesLabel, lanesLabel);
        	HashMap <Titan,Rectangle> titanShape = new HashMap();
        	for(Lane lane : battle.getLanes()){
        		for(Titan titan : lane.getTitans()){
        			if(titanShape.containsKey(titan)){
        				Rectangle rectangle = titanShape.get(titan);
        				rectangle.setTranslateX(titan.getSpeed()*60);
        			}
        			else{
        				Rectangle rc;
        				double h = 0;
        				Color c = null;
        				if(titan instanceof PureTitan){
        					h= 20;
        					c = Color.PINK;
        				}
        				else{
        					if(titan instanceof AbnormalTitan){
            					h= 40;
            					c = Color.PURPLE;
            				}
        					else{
        						if(titan instanceof ArmoredTitan){
                					h= 60;
                					c = Color.BEIGE;
                				}
        						else{
        							if(titan instanceof ColossalTitan){
        	        					h= 80;
        	        					c = Color.GREEN;
        	        				}
        						}
        					}
        				}
        				rc= new Rectangle(10,h, c);
        				rc.setRotate(180);
        				Lane1.getChildren().add(rc);
        				titanShape.put(titan, rc);
        			}
        		}
        	}
        });

        Lane1.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane2.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane3.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane4.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");
        Lane5.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/bluesand1.png');");

        if(battle.getOriginalLanes().get(0).getLaneWall().getCurrentHealth()<0){
        	Lane1.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(1).getLaneWall().getCurrentHealth()<0){
        	Lane2.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(2).getLaneWall().getCurrentHealth()<0){
        	Lane3.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(3).getLaneWall().getCurrentHealth()<0){
        	Lane4.setStyle("-fx-background-color: black;");
        }
        if(battle.getOriginalLanes().get(4).getLaneWall().getCurrentHealth()<0){
        	Lane5.setStyle("-fx-background-color: black;");
        }
        
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();
        VBox vbox3 = new VBox();
        VBox vbox4 = new VBox();
        VBox vbox5 = new VBox();

        vbox1.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox2.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox3.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox4.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        vbox5.setStyle("-fx-background-color: transparent; -fx-background-image: url('file:///D:/wall1.png'); -fx-border-color: black;");
        
        Label l1= new Label("Wall 1");
        l1.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info1 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(0).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(0).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(0).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(0).getTitans().size());
        info1.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l1.setTooltip(info1);
        Label l2= new Label("Wall 2");
        l2.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info2 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(1).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(1).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(1).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(1).getTitans().size());
        info2.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l2.setTooltip(info2);
        Label l3= new Label("Wall 3");
        l3.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info3 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(2).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(2).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(2).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(2).getTitans().size());
        info3.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l3.setTooltip(info3);
        Label l4= new Label("Wall");
        l4.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info4 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(3).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(3).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(3).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(3).getTitans().size());
        info4.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l4.setTooltip(info4);
        Label l5= new Label("Wall 5");
        l5.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        Tooltip info5 = new Tooltip("Current Health: " + battle.getOriginalLanes().get(4).getLaneWall().getCurrentHealth() + "\n"
        		+ "Danger Level: " + battle.getOriginalLanes().get(4).getDangerLevel() + "\n"
        		+ "Available Weapons: "+battle.getOriginalLanes().get(4).getWeapons().size() + "\n"
                		+ "Active lanes: "+battle.getOriginalLanes().get(4).getTitans().size());
        info5.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        l5.setTooltip(info5);
        
        vbox1.getChildren().addAll(l1);
        vbox2.getChildren().addAll(l2);
        vbox3.getChildren().addAll(l3);
        vbox4.getChildren().addAll(l4);
        vbox5.getChildren().addAll(l5);
        
        double vboxWidth = screenWidth * 0.07;

        vbox1.setPrefWidth(vboxWidth);
        vbox2.setPrefWidth(vboxWidth);
        vbox3.setPrefWidth(vboxWidth);
        vbox4.setPrefWidth(vboxWidth);
        vbox5.setPrefWidth(vboxWidth);

        Lane1.getChildren().add(vbox1);
        Lane2.getChildren().add(vbox2);
        Lane3.getChildren().add(vbox3);
        Lane4.getChildren().add(vbox4);
        Lane5.getChildren().add(vbox5);

        Region spacer1 = new Region();
        spacer1.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer1.setPrefHeight(10);
        
        Region spacer2 = new Region();
        spacer2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer2.setPrefHeight(10);
        
        Region spacer3 = new Region();
        spacer3.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer3.setPrefHeight(10);
        
        Region spacer4 = new Region();
        spacer4.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
        spacer4.setPrefHeight(10);

        inVBox.getChildren().addAll(Lane1, spacer1, Lane2, spacer2, Lane3, spacer3, Lane4, spacer4, Lane5);

        VBox.setVgrow(Lane1, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane2, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane3, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane4, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(Lane5, javafx.scene.layout.Priority.ALWAYS);

        inVBox.getChildren().add(outVBox);

        Scene scene = new Scene(inVBox, screenWidth, screenHeight);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        
        if(battle.isGameOver()){
        	showEnd();
        }
    }

    public void initializingEasy() throws IOException{ 
    	int screenWidth =(int) Screen.getPrimary().getVisualBounds().getWidth();
    	battle= new Battle(1, 0 , screenWidth/2, 3, 250);
    }
    
    public void initializingHard() throws IOException{ 
    	int screenWidth =(int) Screen.getPrimary().getVisualBounds().getWidth();
    	battle= new Battle(1, 0 , screenWidth/2, 5, 125);
    }
    
    public int showScore(){
    	return battle.getScore();
    }
    
    public int showTurn(){
    	return battle.getNumberOfTurns();
    }
    
    public BattlePhase showPhase(){
    	return battle.getBattlePhase();
    }
    
    public int showRecources(){
    	return battle.getResourcesGathered();
    }
    
    public int showLanes(){
    	return battle.getLanes().size();
    }
    
    private void showWeaponShop(Label phaseLabel, Label turnLabel, Label recourcesLabel, Label lanesLabel) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth() / 1.7;
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() / 1.7;

        Stage showWeaponShop = new Stage();
        showWeaponShop.initModality(Modality.APPLICATION_MODAL);
        showWeaponShop.initOwner(primaryStage);

        String intro = "Welcome to the weapon shop";

        Image image = new Image(getClass().getResourceAsStream("shelf.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(screenWidth);
        imageView.setFitHeight(screenHeight);

        Group root = new Group();

        Text introductoryText = new Text(intro);
        introductoryText.setFill(Color.WHITE);
        introductoryText.setFont(Font.font("Copperplate Gothic Bold", 26));
        introductoryText.setTextAlignment(TextAlignment.CENTER);

        double introductoryTextX = (screenWidth - introductoryText.getLayoutBounds().getWidth()) / 2;
        double introductoryTextY = 30;

        introductoryText.setLayoutX(introductoryTextX);
        introductoryText.setLayoutY(introductoryTextY);

        imageView.setX((screenWidth - imageView.getBoundsInLocal().getWidth()) / 2);
        imageView.setY((screenHeight - imageView.getBoundsInLocal().getHeight()) / 2);
        
        HBox hbox1 = new HBox();
        hbox1.setLayoutY(0);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.setPrefSize(screenWidth, screenHeight / 4);
        Label piercingCanon = new Label("Anti-Titan Shell");
        ImageView pc= new ImageView(new Image(new File("piercingCanon.png").toURI().toString()));
        weaponprice= 25;
        weaponCode=1;
        if(showRecources()>= weaponprice){
        	piercingCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        }else{
        	if(showRecources()< weaponprice){   
        		try {
                    if (showRecources() < weaponprice) {
                    	piercingCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: red;");
                        String error= "Not enough resources.";
                        throw new InsufficientResourcesException(error, showRecources());
                    }
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                }
        	}
        }
        Tooltip piercingCanons = new Tooltip("Type: Piercing Cannon\nPrice: 25\nDamage: 10");
        piercingCanons.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        piercingCanon.setTooltip(piercingCanons);
        if(mode==true){
        	hbox1.getChildren().addAll(piercingCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel));
        }else{
        	hbox1.getChildren().addAll(piercingCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel), button4(phaseLabel, recourcesLabel, lanesLabel),
            		button5(phaseLabel, recourcesLabel, lanesLabel));
        }
        
        HBox hbox2 = new HBox();
        hbox2.setLayoutY(screenHeight / 4 - 50);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setPrefSize(screenWidth, screenHeight / 4);
        Label sniperCanon = new Label("Long Range Spear");
        ImageView sc= new ImageView(new Image(new File("sniperCanon.png").toURI().toString()));
        weaponprice= 25;
        weaponCode=2;
        if(showRecources()>= weaponprice){
        	sniperCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        }else{
        	if(showRecources()< weaponprice){   
        		try {
                    if (showRecources() < weaponprice) {
                    	sniperCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: red;");
                        String error= "Not enough resources.";
                        throw new InsufficientResourcesException(error, showRecources());
                    }
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                }
        	}
        }
        
        Tooltip sniperCanons = new Tooltip("Type: Sniper Canon\nPrice: 25\nDamage: 35");
        sniperCanons.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        sniperCanon.setTooltip(sniperCanons);
        if(mode==true){
        	hbox2.getChildren().addAll(sniperCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel));
        }else{
        	hbox2.getChildren().addAll(sniperCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel), button4(phaseLabel, recourcesLabel, lanesLabel),
            		button5(phaseLabel, recourcesLabel, lanesLabel));
        }
        
        HBox hbox3 = new HBox();
        hbox3.setLayoutY((screenHeight / 4) * 2 - 50);
        hbox3.setAlignment(Pos.CENTER);
        hbox3.setPrefSize(screenWidth, screenHeight / 4);
        Label volleySpreadCanon = new Label("Wall Spread Cannon");
        ImageView vsc= new ImageView(new Image(new File("volleySpreadCanon.png").toURI().toString()));
        weaponprice= 100;
        weaponCode=3;
        if(showRecources()>= weaponprice){
        	volleySpreadCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        }else{
        	if(showRecources()< weaponprice){   
        		try {
                    if (showRecources() < weaponprice) {
                    	volleySpreadCanon.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: red;");
                        String error= "Not enough resources.";
                        throw new InsufficientResourcesException(error, showRecources());
                    }
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                }
        	}
        }
        Tooltip volleySpreadCanons = new Tooltip("Type: Volley Spread Canon\nPrice: 100\nDamage: 5\nMin range: 20\nMax range: 50");
        volleySpreadCanons.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        volleySpreadCanon.setTooltip(volleySpreadCanons);
        if(mode==true){
        	hbox3.getChildren().addAll(volleySpreadCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
        		button3(phaseLabel, recourcesLabel, lanesLabel));
        }else{
        	hbox3.getChildren().addAll(volleySpreadCanon, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel), button4(phaseLabel, recourcesLabel, lanesLabel),
            		button5(phaseLabel, recourcesLabel, lanesLabel));
        }

        HBox hbox4 = new HBox();
        hbox4.setLayoutY((screenHeight / 4) * 3 - 50); 
        hbox4.setAlignment(Pos.CENTER);
        hbox4.setPrefSize(screenWidth, screenHeight / 4);
        Label wallTrap = new Label("Long Range Spear");
        ImageView wt= new ImageView(new Image(new File("wallTrap.png").toURI().toString()));
        weaponprice= 25;
        weaponCode=4;
        if(showRecources()>= weaponprice){
        	wallTrap.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        }else{
        	if(showRecources()< weaponprice){   
        		try {
                    if (showRecources() < weaponprice) {
                    	wallTrap.setStyle("-fx-font-size: 50; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: red;");
                        String error= "Not enough resources.";
                        throw new InsufficientResourcesException(error, showRecources());
                    }
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                }
        	}
        }
        Tooltip wallTraps = new Tooltip("Type: Sniper Canon\nPrice: 25\nDamage: 35");
        wallTraps.setStyle("-fx-font-size: 30; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;");
        wallTrap.setTooltip(wallTraps);
        if(mode==true){
        	hbox4.getChildren().addAll(wallTrap, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
        		button3(phaseLabel, recourcesLabel, lanesLabel));
        }else{
        	hbox4.getChildren().addAll(wallTrap, button1(phaseLabel, recourcesLabel, lanesLabel), button2(phaseLabel, recourcesLabel, lanesLabel),
            		button3(phaseLabel, recourcesLabel, lanesLabel), button4(phaseLabel, recourcesLabel, lanesLabel),
            		button5(phaseLabel, recourcesLabel, lanesLabel));
        }
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);

        Button closeButton = new Button("X");
        closeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: black; -fx-font-size: 35px; -fx-font-family: 'Copperplate Gothic Bold';");
        closeButton.setOnAction(e -> showWeaponShop.close());
        closeButton.setLayoutX(screenWidth - 110);
        closeButton.setLayoutY(7);

        root.getChildren().addAll(imageView, introductoryText, vBox, closeButton);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        showWeaponShop.setScene(scene);
        showWeaponShop.initStyle(StageStyle.UNDECORATED);
        showWeaponShop.setFullScreen(false);
        showWeaponShop.show();
    }
    
    public Button button1(Label phaseLabel, Label resourcesLabel, Label lanesLabel) {
        Button button1 = new Button("Wall 1");
        button1.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Copperplate Gothic Bold';");
        button1.setOnAction(event -> {
            try {
                battle.purchaseWeapon(weaponCode, battle.getOriginalLanes().get(0));
            } catch (InsufficientResourcesException | InvalidLaneException e) {
                e.printStackTrace();
            }
            phaseLabel.setText("Phase: " + showPhase());
            resourcesLabel.setText("Resources: " + showRecources());
            lanesLabel.setText("Lanes alive: " + showLanes());
        });
        return button1;
    }
    
    public Button button2(Label phaseLabel, Label resourcesLabel, Label lanesLabel) {
        Button button2 = new Button("Wall 2");
        button2.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Copperplate Gothic Bold';");
        button2.setOnAction(event -> {
            try {
                battle.purchaseWeapon(weaponCode, battle.getOriginalLanes().get(1));
            } catch (InsufficientResourcesException | InvalidLaneException e) {
                e.printStackTrace();
            }
            phaseLabel.setText("Phase: " + showPhase());
            resourcesLabel.setText("Resources: " + showRecources());
            lanesLabel.setText("Lanes alive: " + showLanes());
        });
		return button2;
    }

    
    public Button button3(Label phaseLabel, Label resourcesLabel, Label lanesLabel) {
        Button button3 = new Button("Wall 3");
        button3.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Copperplate Gothic Bold';");
        button3.setOnAction(event -> {
            try {
                battle.purchaseWeapon(weaponCode, battle.getOriginalLanes().get(0));
            } catch (InsufficientResourcesException | InvalidLaneException e) {
                e.printStackTrace();
            }
            phaseLabel.setText("Phase: " + showPhase());
            resourcesLabel.setText("Resources: " + showRecources());
            lanesLabel.setText("Lanes alive: " + showLanes());
        });
		return button3;
    }
    
    public Button button4(Label phaseLabel, Label resourcesLabel, Label lanesLabel) {
    	Button button4= new Button("Wall 4");
    	button4.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Copperplate Gothic Bold';");
    	button4.setOnAction(event -> {
            try {
                battle.purchaseWeapon(weaponCode, battle.getOriginalLanes().get(0));
            } catch (InsufficientResourcesException | InvalidLaneException e) {
                e.printStackTrace();
            }
            phaseLabel.setText("Phase: " + showPhase());
            resourcesLabel.setText("Resources: " + showRecources());
            lanesLabel.setText("Lanes alive: " + showLanes());
        });
		return button4;
    }
    
    public Button button5(Label phaseLabel, Label resourcesLabel, Label lanesLabel) {
    	Button button5= new Button("Wall 5");
    	button5.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-family: 'Copperplate Gothic Bold';");
    	button5.setOnAction(event -> {
            try {
                battle.purchaseWeapon(weaponCode, battle.getOriginalLanes().get(0));
            } catch (InsufficientResourcesException | InvalidLaneException e) {
                e.printStackTrace();
            }
            phaseLabel.setText("Phase: " + showPhase());
            resourcesLabel.setText("Resources: " + showRecources());
            lanesLabel.setText("Lanes alive: " + showLanes());
        });
		return button5;
    }
    
    public void showPassingTurn(Label phaseLabel, Label turnLabel, Label resourcesLabel, Label lanesLabel){
        battle.passTurn();
    	phaseLabel.setText("Phase: " + showPhase());
    	turnLabel.setText("Turns: "+battle.getNumberOfTurns());
    	resourcesLabel.setText("Resources: " + showRecources());
    	lanesLabel.setText("Lanes alive: " + showLanes());
    	HashMap <Titan,Rectangle> titanShape = new HashMap();
    	for(Lane lane : battle.getLanes()){
    		for(Titan titan : lane.getTitans()){
    			if(titanShape.containsKey(titan)){
    				Rectangle rectangle = titanShape.get(titan);
    				rectangle.setTranslateX(titan.getSpeed()*60);
    			}
    			else{
    				Rectangle rc;
    				double h;
    				if(titan instanceof PureTitan){
    					h= 50;
    				}
    				else{
    					h= 60;
    				}
    				rc= new Rectangle(30,h, Color.ALICEBLUE);
    				titanShape.put(titan, rc);
    			}
    		}
    	}
    }
    
    public void showEnd() {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Label messageLabel = new Label("You lost!");
        Label scoreLabel = new Label("Score: " + showScore());
        Button restartButton = new Button("Restart");

        String labelStyle = "-fx-font-size: 38px; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;";
        String buttonStyle = "-fx-font-size: 24px; -fx-font-family: 'Copperplate Gothic Bold'; -fx-text-fill: white;";

        messageLabel.setStyle(labelStyle);
        scoreLabel.setStyle(labelStyle);
        restartButton.setStyle(buttonStyle);

        restartButton.setOnAction(e -> showStartingScreen2());

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(messageLabel, scoreLabel, restartButton);

        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        root.setStyle("-fx-background-color: black;"); 

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    
    
}

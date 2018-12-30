import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws InterruptedException {

		Battle battle = new Battle();
		Pane pane= new Pane();
		pane.getChildren().add(battle.getLand());
		
		Image backGroundImage = new Image(getClass().getResource("bg.png").toExternalForm());
		Scene scene=new Scene(pane,backGroundImage.getWidth(), backGroundImage.getHeight());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        battle.battleInit();
        scene.setOnKeyPressed(input -> {
            if(input.getCode() == KeyCode.SPACE) {
            	battle.battleStart();
            }else if(input.getCode() == KeyCode.R){
            	battle.battleInit();
            }else if(input.getCode() == KeyCode.L){
            	battle.loadRecord(primaryStage);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("battle start!");
        primaryStage.show();	
	}

	public static void main(String[] args) {
		launch(args);
	}
}

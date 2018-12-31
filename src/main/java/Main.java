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
		//加载背景并设置大小
		Image backGroundImage = new Image(getClass().getResource("bg.png").toExternalForm());
		Scene scene=new Scene(pane,backGroundImage.getWidth(), backGroundImage.getHeight());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        //初始化战斗
        battle.battleInit();
        //按空格开始战斗，按F5刷新，按R回放上一场战斗
        scene.setOnKeyPressed(input -> {
            if(input.getCode() == KeyCode.SPACE) {
            	battle.battleStart();
            }else if(input.getCode() == KeyCode.F5){
            	battle.battleInit();
            }else if(input.getCode() == KeyCode.R){
            	battle.battleReplay(primaryStage);
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

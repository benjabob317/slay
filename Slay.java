import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import java.util.ArrayList;
import javafx.scene.control.Button;


public class Slay extends Application
{
    public Scene homeMenu;
    public void start (Stage primary)
    {
        primary.setTitle("Slay");

        BorderPane bp = new BorderPane();
        this.homeMenu = new Scene(bp);

        ScrollPane levelList = new ScrollPane();
        levelList.setPrefSize(500, 500);
        levelList.setContent(buildLevelList(primary));
        bp.setCenter(levelList);

        HBox topBar = new HBox();
        Button openLevelEditor = new Button("Open Level Editor");
        openLevelEditor.setOnAction( e -> {
            primary.setScene(new Scene(new LevelEditor(700, 600, primary, homeMenu)));
        });
        Button refreshLevels = new Button("Refresh Levels");
        refreshLevels.setOnAction( e -> {
            levelList.setContent(buildLevelList(primary));
        });

        topBar.getChildren().addAll(openLevelEditor, refreshLevels);
        bp.setTop(topBar);
        
        primary.setScene(homeMenu);
        primary.show();
    }
    public VBox buildLevelList(Stage primary)
    {
        VBox scrollContent = new VBox();
        File[] files = new File("./Levels/").listFiles();
        ArrayList<LevelPreview> previews = new ArrayList<LevelPreview>();
        ArrayList<LevelData> dataList = new ArrayList<LevelData>();
        ArrayList<String> fileNames = new ArrayList<String>();
       
        for (File file: files) // iterates through files to extract necessary things and build lists
        {
            try 
            {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                LevelData data = (LevelData) ois.readObject();
                ois.close();
                previews.add(new LevelPreview(data.playerData, 50));
                dataList.add(data);
                fileNames.add((file.toString().split("\\.")[file.toString().split("\\.").length - 2]).split("/")[2]);
            }
            catch(FileNotFoundException ex)
            {
                ex.printStackTrace();
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
            catch(ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
       
        scrollContent.getChildren().add(new HBox(20));
        int runningWidth = 0; // used for line breaks
        for (int i = 0; i < previews.size(); i++) // builds level selector things
        {
            runningWidth += previews.get(i).width;
            runningWidth += 20;
            if (runningWidth > 500)
            {
                scrollContent.getChildren().add(new HBox(20));
            }
            ((HBox) scrollContent.getChildren().get(scrollContent.getChildren().size()-1)).getChildren().add(new LevelSelector(previews.get(i), fileNames.get(i), dataList.get(i), primary));

        }
        return scrollContent;
    }

    public class LevelSelector extends VBox
    {
        public LevelSelector(LevelPreview preview, String name, LevelData data, Stage primary)
        {
            setAlignment(Pos.CENTER);
            getChildren().addAll(preview, new Text(name));
            setOnMouseClicked( e -> {
                primary.setScene(new Scene(new Level(600, 600, data, primary, homeMenu)));
            });

        }
    }

}
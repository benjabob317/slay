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

public class HexTest extends Application
{
    public void start(Stage primary)
    {
        primary.setTitle("Slay!");
        
        try {
            FileInputStream fis = new FileInputStream(new File("./levels/fragment.lvl"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            LevelData data = (LevelData) ois.readObject();
            ois.close();
            
            Level level = new Level(500, 500, data);

            int[][] level1 = new int[][] {
                {1, 2, 3, 4, 5, 6, 4, 2, 1},
                {1, 4, 3, 2, 3, 4, 1, 1, 6},
                {1, 1, 1, 1, 1, 1, 4, 1, 2},
                {0, 1, 0, 1, 0, 1, 4, 1, 0},
                {6, 6, 5, 4, 3, 2, 1, 4, 0},
                {3, 3, 3, 3, 5, 4, 3, 2, 1},
                {3, 3, 3, 2, 1, 2, 3, 4, 5},
                {3, 3, 0, 0, 5, 5, 0, 0, 4},
                {3, 3, 0, 0, 2, 2, 0, 0, 1},
            };
            

            primary.setScene(new Scene(level));
            primary.show();
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
}

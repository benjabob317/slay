import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;


public class LevelEditorTest extends Application
{
    public void start (Stage primary)
    {
        LevelEditor editor = new LevelEditor(700, 600, primary);
        Pane drawingBoard = new Pane();
        EditableGrid grid = new EditableGrid(0, 0, 30, 30, 40, drawingBoard, editor);
        editor.setGrid(grid);
        grid.draw();
        

        primary.setScene(new Scene(editor));
        primary.show();
    }
}
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class HexTest extends Application
{
    public void start(Stage primary)
    {
        primary.setTitle("Hexagons!");
        Pane drawingBoard = new Pane();
        
        Level level = new Level();
        HexGrid grid = new HexGrid(0, 0, 9, 9, 40, drawingBoard, level);

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
        grid.setPlayers(level1);
        grid.get(2).get(1).setContents(new Troop(grid.get(2).get(1), 2));
        grid.get(1).get(7).setContents(new Troop(grid.get(1).get(7), 1));
        grid.get(3).get(7).setContents(new Troop(grid.get(3).get(7), 1));

        grid.initializeTerritories();
        grid.initializeCapitals();
        

        level.newTurn(1);

        grid.draw();
        level.topBar.money.setText("");
        level.topBar.adjustPlayerDisplay();
        primary.setScene(new Scene(level));
        primary.show();
    }
}

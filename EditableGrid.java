import java.util.ArrayList;
import javafx.scene.layout.Pane;

public class EditableGrid extends ArrayList<ArrayList<EditableTile>>
{
    public double topLeftX;
    public double topLeftY;
    public double hexSize;
    public Pane canvas;
    public int width;
    public int height;
    public LevelEditor editor;

    public EditableGrid(double topLeftX, double topLeftY, int width, int height, double hexSize, Pane canvas, LevelEditor editor)
    {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.width = width;
        this.height = height;
        this.hexSize = hexSize;
        this.canvas = canvas;
        this.editor = editor;

        canvas.setPrefSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));
        canvas.setMinSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));
        canvas.setMaxSize(hexSize*2*.75*(width+(1.0/3.0)), hexSize*Math.sqrt(3)*(height+0.5));

        for (int x = 0; x < width; x++)
        {
            this.add(new ArrayList<EditableTile>());
            for (int y = 0; y < height; y++)
            {
                this.get(x).add(new EditableTile(this, x, y, 0, false));
            }
        }

    }
    public void draw()
    {
        for (ArrayList<EditableTile> column: this)
        {
            for (EditableTile tile: column)
            {
                tile.draw();
            }
        }
    }
}
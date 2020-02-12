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
    public EditableGrid(double topLeftX, double topLeftY, double hexSize, LevelData data, Pane canvas, LevelEditor editor)
    {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.hexSize = hexSize;
        this.canvas = canvas;
        this.editor = editor;
        for (int x = 0; x < data.playerData.length; x++)
        {
            this.add(new ArrayList<EditableTile>());
            for (int y = 0; y < data.playerData[0].length; y++)
            {
                this.get(x).add(new EditableTile(this, x, y, data.playerData[x][y], false));
            }
        }
        for (int[] i: data.treeData)
        {
            this.get(i[0]).get(i[1]).hasTree = true;
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
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class LevelPreview extends Pane
{
    private int[][] playerData;
    private double hexSize;
    private Hexagon[][] HexArray;
    public double width;
    public double height;


    public LevelPreview(int[][] playerData, double height)
    {
        this.playerData = playerData;
        this.hexSize = height/(playerData[0].length+0.5)/(Math.sqrt(3));
        this.HexArray = new Hexagon[playerData.length][playerData[0].length];

        this.width = hexSize*2*.75*(playerData.length+(1.0/3.0));
        this.height = height;

        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        for (int x = 0; x < playerData.length; x++)
        {
            for (int y = 0; y < playerData[0].length; y++)
            {
                Color color = Color.color(0, 0, 1);
                
                switch (playerData[x][y])
                {
                    case 1:
                        color = Color.web("0x94CF8A");
                        break;
                    case 2:
                        color = Color.web("0xDCCA98");
                        break;
                    case 3:
                        color = Color.web("0x016E1E");
                        break;
                    case 4:
                        color = Color.web("0xE6E174");
                        break;
                    case 5:
                        color = Color.web("0x56A319");
                        break;
                    case 6:
                        color = Color.web("0x786319");
                        break;
                    default:
                        color = Color.web("0x0000FF");
                }
                HexArray[x][y] = new Hexagon(hexSize*(1 + 1.5*x), hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2)), hexSize, color, Color.BLACK);
                HexArray[x][y].setStrokeWidth(0);
                this.getChildren().add(HexArray[x][y]);
            }
        }

    }
}
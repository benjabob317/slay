import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class EditableTile
{
    public EditableGrid container;
    public int x;
    public int y;
    public int player;
    public boolean hasTree;
    public ImageView contentImage;
    public Hexagon hex;

    public EditableTile(EditableGrid container, int x, int y, int player, boolean hasTree)
    {
        this.container = container;
        this.x = x;
        this.y = y;
        this.player = player;
       
        if (isWater())
        {
            this.player = 0;
        }

        this.hasTree = hasTree;

        this.contentImage = new ImageView();
        if (hasTree)
        {
            contentImage.setImage(new Image("./assets/tree.png"));
        }
        contentImage.setX(container.topLeftX + container.hexSize*(.65 + 1.5*x));
        contentImage.setY(container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2)) - container.hexSize*0.4);

        // HEXAGON STUFF

        double centerX = container.topLeftX + container.hexSize*(1 + 1.5*x);
        double centerY = container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2));
        
        Color color = Color.color(0, 0, 1);

        if (player == 1) {
            color = Color.web("0x94CF8A");
        } else if (player == 2) {
            color = Color.web("0xDCCA98");
        } else if (player == 3) {
            color = Color.web("0x016E1E");
        } else if (player == 4) {
            color = Color.web("0xE6E174");
        } else if (player == 5) {
            color = Color.web("0x56A319");
        } else if (player == 6) {
            color = Color.web("0x786319");
        }

        this.hex = new Hexagon(centerX, centerY, container.hexSize, color, Color.BLACK);
        
        hex.setOnMouseEntered( e -> {
            hex.setFill(Color.color(hex.red*.9, hex.green*.9, hex.blue*.9));
        });
        hex.setOnMouseExited( e -> {
            hex.setFill(hex.getColor());
        });
        hex.setOnMouseClicked( e -> {
            runOnMouseClicked();
        });

    }

    public void draw()
    {
        contentImage = new ImageView();
        if (hasTree)
        {
            contentImage.setImage(new Image("./assets/tree.png"));
        }
        contentImage.setX(container.topLeftX + container.hexSize*(.65 + 1.5*x));
        contentImage.setY(container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2)) - container.hexSize*0.4);

        container.canvas.getChildren().remove(hex);

        double centerX = container.topLeftX + container.hexSize*(1 + 1.5*x);
        double centerY = container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2));
        
        Color color = Color.color(0, 0, 1);

        if (player == 1) {
            color = Color.web("0x94CF8A");
        } else if (player == 2) {
            color = Color.web("0xDCCA98");
        } else if (player == 3) {
            color = Color.web("0x016E1E");
        } else if (player == 4) {
            color = Color.web("0xE6E174");
        } else if (player == 5) {
            color = Color.web("0x56A319");
        } else if (player == 6) {
            color = Color.web("0x786319");
        }

        this.hex = new Hexagon(centerX, centerY, container.hexSize, color, Color.BLACK);
        
        hex.setOnMouseEntered( e -> {
            hex.setFill(Color.color(hex.red*.9, hex.green*.9, hex.blue*.9));
        });
        hex.setOnMouseExited( e -> {
            hex.setFill(hex.getColor());
        });
        hex.setOnMouseClicked( e -> {
            runOnMouseClicked();
        });

        container.canvas.getChildren().add(this.hex);
        container.canvas.getChildren().add(contentImage);

    }

    public boolean isWater()
    {
        return ((this.player < 1) || (this.player > 6));
    }
    public void runOnMouseClicked()
    {
        if (container.editor.topBar.treeState == 0)
        {
            player = container.editor.selectedPlayer;
        } else if ((container.editor.topBar.treeState == 1) && !(isWater()))
        {
            hasTree = true;
        } else if (container.editor.topBar.treeState == -1)
        {
            hasTree = false;
        }
        if (isWater())
        {
            hasTree = false;
        }

        this.draw();
    }
}
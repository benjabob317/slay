import javafx.scene.image.ImageView;

public class Tree extends TileContent
{
    public Tree(HexTile tile)
    {
        super(tile);
        this.protectionLevel = 0;
    }
    public ImageView getImage()
    {
        return new ImageView("./assets/tree.png");
    }
    public String getType()
    {
        return "Tree";
    }
}
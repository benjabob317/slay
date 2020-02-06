import javafx.scene.image.ImageView;

public abstract class TileContent
{
    protected HexTile tile;
    protected int protectionLevel;

    public TileContent(HexTile tile)
    {
        this.tile = tile;
    }
    public void setTile(HexTile newTile)
    {
        tile = newTile;
    }
    public HexTile getTile()
    {
        return tile;
    }
    public int getProtectionLevel()
    {
        return this.protectionLevel;
    }
    public void setProtectionLevel(int level)
    {
        this.protectionLevel = level;
    }
    public abstract ImageView getImage();
}
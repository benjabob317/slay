import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.image.ImageView;

public class HexTile
{
    public HexGrid container;
    private int x; // relative position in grid, starts at 0
    private int y; // relative position in grid, starts at 0
    private int player; // 1 to 6
    public boolean isWater;
    private Territory currentTerritory;
    private TileContent contents;
    public ImageView contentImage;
    public Hexagon hex;
    
    public HexTile(HexGrid container, int x, int y, int player) // player from 1-6 makes land, else makes water
    {
        this.container = container;
        this.x = x;
        this.y = y;
        this.currentTerritory = new Territory(container, player);
        this.contents = new EmptyTile(this);
        

        if ((player >= 1) && (player <= 6)) {
            this.player = player;
            this.isWater = false;
        } else {
            this.player = 0;
            this.isWater = true;
        }
        this.contentImage = getContents().getImage();
        contentImage.setX(container.topLeftX + container.hexSize*(.65 + 1.5*getX()));
        contentImage.setY(container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + getY() + .5*(getX()%2)) - container.hexSize*0.4);

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
        
        if (!(isWater)) 
        {
            hex.setOnMouseEntered( e -> {
                if (container.level.currentPlayer == player)
                {
                    hex.setFill(Color.color(hex.red*.9, hex.green*.9, hex.blue*.9));
                }
            });
            hex.setOnMouseExited( e -> {
                if (container.level.currentPlayer == player)
                {
                    hex.setFill(hex.getColor());
                }
            });
            hex.setOnMouseClicked( e -> {
                runOnMouseClicked();
            });
        }


        

    }
    public HexTile(HexGrid container, int x, int y) // water tiles 
    {
        this.container = container;
        this.x = x;
        this.y = y;
        this.player = 0;
        this.isWater = true;
        this.currentTerritory = new Territory(container, 0);
        this.contentImage = new ImageView();

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

    }
    public void draw()
    {

        container.canvas.getChildren().remove(this.hex);

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

        container.canvas.getChildren().remove(contentImage);
        contentImage = getContents().getImage();
        contentImage.setX(container.topLeftX + container.hexSize*(.65 + 1.5*getX()));
        contentImage.setY(container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + getY() + .5*(getX()%2)) - container.hexSize*0.4);
        
        if ((getContents() instanceof Capital) || (getContents() instanceof Castle) || (getContents() instanceof Troop) || (getContents() instanceof Tree) || (getContents() instanceof Gravestone)) // scales content widths
        {
            contentImage.setFitWidth(contentImage.getImage().getWidth()*container.hexSize/40);
            contentImage.setFitHeight(contentImage.getImage().getHeight()*container.hexSize/40);
        }
        if ((getContents() instanceof Troop) || (getContents() instanceof Tree))
        {
            contentImage.setFitHeight(contentImage.getFitHeight() * 1.5);
            contentImage.setFitWidth(contentImage.getFitWidth() * 1.5);
        }

        hex.setOnMouseEntered( e -> {
            if (container.level.currentPlayer == player)
            {
                hex.setFill(Color.color(hex.red*.9, hex.green*.9, hex.blue*.9));
            }
        });
        hex.setOnMouseExited( e -> {
            if (container.level.currentPlayer == player)
            {
                hex.setFill(hex.getColor());
            }
        });
        hex.setOnMouseClicked( e -> {
            runOnMouseClicked();
        });

        container.canvas.getChildren().add(this.hex);
        container.canvas.getChildren().add(contentImage);

    }
    public void setPlayer(int player)
    {
        this.player = player;

        Color color = Color.BLACK;
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
        hex.red = color.getRed();
        hex.green = color.getGreen();
        hex.blue = color.getBlue();
        this.draw();
    }   
    public int getPlayer()
    {
        return player;
    }
    public ArrayList<HexTile> getNeighbors()
    {
        ArrayList<HexTile> neighbors = new ArrayList<HexTile>();
        if (y > 0) {
            neighbors.add(container.get(x).get(y-1));
        }
        if (y < container.height-1) {
            neighbors.add(container.get(x).get(y+1));
        }
        if (x > 0) {
            neighbors.add(container.get(x-1).get(y));
        }
        if (x < container.width-1) {
            neighbors.add(container.get(x+1).get(y));
        }
        if ((x%2 == 0) && (y > 0)) {
            if (x > 0) {
                neighbors.add(container.get(x-1).get(y-1));
            }
            if (x < container.width-1) {
                neighbors.add(container.get(x+1).get(y-1));
            }
        } 
        if ((x%2 == 1) && (y < container.height-1)) {
            if (x > 0) {
                neighbors.add(container.get(x-1).get(y+1));
            }
            if (x < container.width-1) {
                neighbors.add(container.get(x+1).get(y+1));
            }
        }   
        return neighbors;     
    }
    public ArrayList<double[]> getSegments() // coordinates of all line segments on the hexagon, used for k
    {
        Hexagon hex = new Hexagon((container.topLeftX + container.hexSize*(1 + 1.5*x)), (container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + y + .5*(x%2))), container.hexSize, Color.WHITE, Color.BLACK);
        ArrayList<double[]> segments = new ArrayList<double[]>();
        for (int i = 0; i <= 8; i += 2)
        {
            segments.add(new double[] {hex.getPoints().get(i), hex.getPoints().get(i+1), hex.getPoints().get(i+2), hex.getPoints().get(i+3)});
        }
        segments.add(new double[] {hex.getPoints().get(10), hex.getPoints().get(11), hex.getPoints().get(0), hex.getPoints().get(1)});
        return segments;
    }
    public void setTerritory(Territory territory)
    {
        this.getTerritory().remove(this);
        this.currentTerritory = territory;
    }
    public Territory getTerritory()
    {
        return currentTerritory;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public int getProtectionLevel() {
        ArrayList<Integer> protectionLevels = new ArrayList<Integer>();
        protectionLevels.add(this.contents.getProtectionLevel());
       
        for (HexTile tile: this.getNeighbors())
        {
            if (tile.getPlayer() == this.getPlayer()) 
            {
                protectionLevels.add(tile.contents.getProtectionLevel());
            }
        }

        return Collections.max(protectionLevels);
    }

    public void setContents(TileContent newContents)
    {
        contents = newContents;
        contentImage = getContents().getImage();
        contentImage.setX(container.topLeftX + container.hexSize*(.65 + 1.5*getX()));
        contentImage.setY(container.topLeftY + container.hexSize*Math.sqrt(3)*(.5 + getY() + .5*(getX()%2)) - container.hexSize*0.4);
    }
    public TileContent getContents()
    {
        return contents;
    }
    public void runOnMouseClicked()
    {
        if (this.container.level.troopPickedUp)
        {
            ((Troop) container.level.heldTroopSource.getContents()).moveToTile(this);
            this.draw();
            container.level.hideHand();
            this.container.level.troopPickedUp = false;
        } else if (this.container.level.troopSpawnedIn) 
        {
            if (container.currentTerritory.contains(this))
            {
                this.container.currentTerritory.newTroopAtTile(this, 1);
                container.level.hideHand();
                this.container.level.troopSpawnedIn = false;
                this.container.setCurrentTerritory(this.container.currentTerritory);
            }
            else if ((container.currentTerritory.getHostileTileNeighbors().contains(this)) && (this.getProtectionLevel() == 0))
            {
                this.container.currentTerritory.newTroopAtTile(this, 1);
                container.level.hideHand();
                this.container.level.troopSpawnedIn = false;
            }

        }
        if (container.level.currentPlayer == player)
        {
            if ((this.container.level.castleSpawnedIn) && (contents instanceof EmptyTile))
            {
                if ((container.currentTerritory.contains(this)))
                {
                    this.setContents(new Castle(this));
                    this.currentTerritory.money -= 15;
                    container.level.hideHand();
                    container.setCurrentTerritory(this.getTerritory());
                    this.container.level.castleSpawnedIn = false;
                }

            }
            else if (this.getTerritory().size() > 1) {
                if (!((this.getTerritory() != container.currentTerritory) && container.level.troopSpawnedIn))
                {
                    container.setCurrentTerritory(this.getTerritory());
                }
            }
        }
        this.draw();
        
    }
}
package puzzlegame;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Tile {
	// *******************************************************************************************
	// attribute(s)
	private GameStage gameStage;

	private int number;
	private Image numberImg;
	private ImageView imgView;

	private final static int WIDTH = 100;


	// *******************************************************************************************
	// constructor(s)
	public Tile (GameStage gameStage, int number) {
		this.gameStage = gameStage;
		this.number = number;

		// set up image for our number tile
		switch(this.number) {
			case 1:
				this.numberImg = new Image("numbers/1.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 2:
				this.numberImg = new Image("numbers/2.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 3:
				this.numberImg = new Image("numbers/3.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 4:
				this.numberImg = new Image("numbers/4.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 5:
				this.numberImg = new Image("numbers/5.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 6:
				this.numberImg = new Image("numbers/6.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 7:
				this.numberImg = new Image("numbers/7.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			case 8:
				this.numberImg = new Image("numbers/8.png", Tile.WIDTH, Tile.WIDTH, false, false);
				break;
			default:
				this.numberImg = new Image("numbers/0.png", Tile.WIDTH, Tile.WIDTH, false, false);
		}

		// set ImageView for the value/number of the tile
		this.imgView = new ImageView();
		this.imgView.setImage(this.numberImg);

		// handle mouse events
		this.setHandler();
	}


	// *******************************************************************************************
	// method(s)
	// event handlers for mouse clicks
	private void setHandler() {
		this.imgView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				swap();							// try to perform swapping of tiles
				gameStage.checkGameState();		// check the current game state
//				System.out.println("CLICK!");	// uncomment for testing
			}
		});
	}

	// ===========================================================================================
	// swaps the clicked tile to the position of the empty tile (if it is non-diagonally adjacent to it)
	private void swap() {
		int[] positionOfEmptyTile = this.gameStage.getPosition(0);
		int blankX = positionOfEmptyTile[0];
		int blankY = positionOfEmptyTile[1];

		int[] positionOfThisTile = this.gameStage.getPosition(this.number);
		int numX = positionOfThisTile[0];
		int numY = positionOfThisTile[1];

		// proceed if the clicked tile is not the current empty tile
		if (blankX != numX || blankY != numY) {
			// proceed if the clicked tile is adjacent the empty tile
			if (Math.abs(blankX-numX) <= 1 && Math.abs(blankY-numY) <= 1) {
				// proceed if the clicked tile is not diagonally adjacent to the empty tile
				if (Math.abs(blankX-numX) == 1 && Math.abs(blankY-numY) == 1);
				else {
					// swap the positions of the clicked tile and the empty tile
					if (gameStage.allowSwapping()) this.gameStage.swapTiles(this.number, positionOfThisTile, 0, positionOfEmptyTile);
				}
			}
		} // end of if statement
	}

	// ===========================================================================================
	// getter for the imgView attribute
	public ImageView getImgView() {
		return this.imgView;
	}

}

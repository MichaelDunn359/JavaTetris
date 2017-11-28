

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TetrisMain {
	
	static int gridWidth = 14; // for now just make sure this number is always ne
	static int gridHeight = 20;

	public static void main(String[] args) {
		JFrame frame = new JFrame("MoogTetris");
		
		TetrisJPanel panel = new TetrisJPanel();
		
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(gridWidth * 20 + 125, gridHeight * 20 + 150));
		frame.pack();
		frame.setVisible(true);
		
		JButton resetButton = new JButton("restart");
		frame.add(resetButton, BorderLayout.SOUTH);
		//resetButton.setPreferredSize(new Dimension( 100 , 40));
		
		resetButton.addActionListener(new ActionListener() { 
		    public void actionPerformed(ActionEvent e) { 
		    	//Thread.currentThread().interrupt();
		        panel.gameOver = true;
		        //Thread.currentThread().resume();
		    } 
		});
		
		//panel.addKeyListener(panel);
		
		frame.requestFocus();
		panel.playGame();
		
		
	}
	

	 @SuppressWarnings("serial")
	static class TetrisJPanel extends JPanel implements KeyListener{
		 
		 boolean resetPressed = false;
		
		 public void playGame() {
			 
			 matrix = new int[matrix.length][matrix[0].length];
			 resetPressed = false;
			 gameOver = false;
			 
			//============== GAME LOOP COMMENTED OUT CODE IS FOR RESTARD AS THAT IS CURRENTLY DEAD 21/11/2017
				while(!gameOver) {
					//panel.printMatrix();
					
					
						
						this.revalidate();
						this.repaint();
						this.printMatrix();
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(!gameOver) {
							this.rotateIfPos();
							this.sidewaysMovePiece();
							this.fallPiece();
						}
						/*
						if(panel.gameOver) {
							while(true) {try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("is game over? " + panel.gameOver);
								if(!panel.gameOver) {
									panel.printMatrix();
									break;
									
									
								}
							}
						}
						*/
					

						if(this.newPieceNeeded && !gameOver){
							this.checkRows();
							System.out.println(" ============ spawn piece");
							this.spawnPiece();
						}
				}
				
				System.out.println("GAME OVER");
				
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(resetPressed) {
						this.playGame();
					}
				}
				
				
				
			 
		 }
		 
		 class IntPair {
				int a = 0;
				int b = 0;
				
				IntPair(int a, int b){
					this.a = a;
					this.b = b;
				}
			}
		 
			class FallingObject{
				ArrayList<IntPair> parts = new ArrayList<IntPair>();
				
				Color colour = null;
				int objectType = 0;
				int rotationValu = 0; //inc for right,+ 3 for left used with modulus
				ArrayList<ArrayList<IntPair>>  lOneArray;
				ArrayList<ArrayList<IntPair>>  lTwoArray;
				ArrayList<ArrayList<IntPair>>  TArray;
				ArrayList<ArrayList<IntPair>>  lineArray;
				ArrayList<ArrayList<IntPair>>  zigArray;
				ArrayList<ArrayList<IntPair>>  zagArray;
				
				public FallingObject(){
					//populate roation array
					//-======================================================	SHAPE ROTATION ARRAYS
					//THESE ARRAYS CONTAIN TRANSLATIONS MADE TO A CENTER POINT IN ORDER TO PRODUCE TETRIS SHAPES IN DIFFERNT ROTATIONAL POSITIONS
					
					lOneArray = new ArrayList<ArrayList<IntPair>>();
					
					lOneArray.add(new ArrayList());
					
					// L one, first inversion
					lOneArray.get(0).add(new IntPair(0,-1));
					lOneArray.get(0).add(new IntPair(0,0)); // very important the center piece is second
					lOneArray.get(0).add(new IntPair(0,1));
					lOneArray.get(0).add(new IntPair(1, 1));
					
					lOneArray.add(new ArrayList());
					
					//L one second inversion
					lOneArray.get(1).add(new IntPair(-1,-1));
					lOneArray.get(1).add(new IntPair(0,0)); // very important the center piece is second
					lOneArray.get(1).add(new IntPair(-1,0));
					lOneArray.get(1).add(new IntPair(1,0));
					
					lOneArray.add(new ArrayList());
					
					lOneArray.get(2).add(new IntPair(-1,1));
					lOneArray.get(2).add(new IntPair(0,0));
					lOneArray.get(2).add(new IntPair(0,-1));
					lOneArray.get(2).add(new IntPair(0,1));
					
					lOneArray.add(new ArrayList());
					
					lOneArray.get(3).add(new IntPair(-1,0));
					lOneArray.get(3).add(new IntPair(0,0));
					lOneArray.get(3).add(new IntPair(1,0));
					lOneArray.get(3).add(new IntPair(1,1));
					
					//===============================================	L two
					
					lTwoArray = new ArrayList<ArrayList<IntPair>>();
					
					lTwoArray.add(new ArrayList());
					
					// L one, first inversion
					lTwoArray.get(0).add(new IntPair(0,-1));
					lTwoArray.get(0).add(new IntPair(0,0)); // very important the center piece is second
					lTwoArray.get(0).add(new IntPair(0,1));
					lTwoArray.get(0).add(new IntPair(-1, 1));
					
					lTwoArray.add(new ArrayList());
					
					//L one second inversion
					lTwoArray.get(1).add(new IntPair(1,-1));
					lTwoArray.get(1).add(new IntPair(0,0)); // very important the center piece is second
					lTwoArray.get(1).add(new IntPair(-1,0));
					lTwoArray.get(1).add(new IntPair(1,0));
					
					lTwoArray.add(new ArrayList());
					
					lTwoArray.get(2).add(new IntPair(1,-1));
					lTwoArray.get(2).add(new IntPair(0,0));
					lTwoArray.get(2).add(new IntPair(0,-1));
					lTwoArray.get(2).add(new IntPair(0,1));
					
					lTwoArray.add(new ArrayList());
					
					lTwoArray.get(3).add(new IntPair(-1,-1));
					lTwoArray.get(3).add(new IntPair(0,0));
					lTwoArray.get(3).add(new IntPair(1,0));
					lTwoArray.get(3).add(new IntPair(-1,0));
					
					//====================================T ARRAY
					
					TArray = new ArrayList<ArrayList<IntPair>>();
					
					TArray.add(new ArrayList());
					
					// L one, first inversion
					TArray.get(0).add(new IntPair(0,-1));
					TArray.get(0).add(new IntPair(0,0)); // very important the center piece is second
					TArray.get(0).add(new IntPair(0,1));
					TArray.get(0).add(new IntPair(1, 0));
					
					TArray.add(new ArrayList());
					
					//L one second inversion
					TArray.get(1).add(new IntPair(0,-1));
					TArray.get(1).add(new IntPair(0,0)); // very important the center piece is second
					TArray.get(1).add(new IntPair(-1,0));
					TArray.get(1).add(new IntPair(1,0));
					
					TArray.add(new ArrayList());
					
					TArray.get(2).add(new IntPair(0,-1));
					TArray.get(2).add(new IntPair(0,0));
					TArray.get(2).add(new IntPair(0,1));
					TArray.get(2).add(new IntPair(-1,0));
					
					TArray.add(new ArrayList());
					
					TArray.get(3).add(new IntPair(0,1));
					TArray.get(3).add(new IntPair(0,0));
					TArray.get(3).add(new IntPair(1,0));
					TArray.get(3).add(new IntPair(-1,0));
					
					//========================================LINE ARRAY
					
					lineArray = new ArrayList<ArrayList<IntPair>>();
					
					lineArray.add(new ArrayList());
					
					lineArray.get(0).add(new IntPair(0,-1));
					lineArray.get(0).add(new IntPair(0,0)); // very important the center piece is second
					lineArray.get(0).add(new IntPair(0,1));
					
					lineArray.add(new ArrayList());
					
					lineArray.get(1).add(new IntPair(-1,0));
					lineArray.get(1).add(new IntPair(0,0)); // very important the center piece is second
					lineArray.get(1).add(new IntPair(1,0));
					
					//GETTO COPYPASTA
					lineArray.add(new ArrayList());
					
					lineArray.get(2).add(new IntPair(0,-1));
					lineArray.get(2).add(new IntPair(0,0)); // very important the center piece is second
					lineArray.get(2).add(new IntPair(0,1));
					
					lineArray.add(new ArrayList());
					
					lineArray.get(3).add(new IntPair(-1,0));
					lineArray.get(3).add(new IntPair(0,0)); // very important the center piece is second
					lineArray.get(3).add(new IntPair(1,0));
					
					//=========================================ZIG ARRAY
					
					
					zigArray = new ArrayList<ArrayList<IntPair>>();
					
					zigArray.add(new ArrayList());
					
					zigArray.get(0).add(new IntPair(0,-1));
					zigArray.get(0).add(new IntPair(0,0));
					zigArray.get(0).add(new IntPair(1,0));
					zigArray.get(0).add(new IntPair(1,1));
					
					zigArray.add(new ArrayList());
					
					zigArray.get(1).add(new IntPair(-1,0));
					zigArray.get(1).add(new IntPair(0,0));
					zigArray.get(1).add(new IntPair(0,-1));
					zigArray.get(1).add(new IntPair(1,-1));
					
					zigArray.add(new ArrayList());
					
					zigArray.get(2).add(new IntPair(0,-1));
					zigArray.get(2).add(new IntPair(0,0));
					zigArray.get(2).add(new IntPair(1,0));
					zigArray.get(2).add(new IntPair(1,1));
					
					zigArray.add(new ArrayList());
					
					zigArray.get(3).add(new IntPair(-1,0));
					zigArray.get(3).add(new IntPair(0,0));
					zigArray.get(3).add(new IntPair(0,-1));
					zigArray.get(3).add(new IntPair(1,-1));
					
					//=======================================================ZAGARRAY
					
					
					
					zagArray = new ArrayList<ArrayList<IntPair>>();
					
					zagArray.add(new ArrayList());
					
					zagArray.get(0).add(new IntPair(0,-1));
					zagArray.get(0).add(new IntPair(0,0));
					zagArray.get(0).add(new IntPair(-1,0));
					zagArray.get(0).add(new IntPair(-1,1));
					
					zagArray.add(new ArrayList());
					
					zagArray.get(1).add(new IntPair(-1,0));
					zagArray.get(1).add(new IntPair(0,0));
					zagArray.get(1).add(new IntPair(0,1));
					zagArray.get(1).add(new IntPair(1,1));
					
					zagArray.add(new ArrayList());
					
					zagArray.get(2).add(new IntPair(0,-1));
					zagArray.get(2).add(new IntPair(0,0));
					zagArray.get(2).add(new IntPair(-1,0));
					zagArray.get(2).add(new IntPair(-1,1));
					
					zagArray.add(new ArrayList());
					
					zagArray.get(3).add(new IntPair(-1,0));
					zagArray.get(3).add(new IntPair(0,0));
					zagArray.get(3).add(new IntPair(0,1));
					zagArray.get(3).add(new IntPair(1,1));
					
					
					/*
					 parts.add(new IntPair(1 + centerCol,0));
						parts.add(new IntPair(1 + centerCol,1));//center
						parts.add(new IntPair(0 + centerCol,1));
						parts.add(new IntPair(0 + centerCol,2));
					 */
				
					
					//======================================RANDOMLY PROJECT PIECE COLOUR
					Random rand = new Random();
					
					int value = 0 + (int)(Math.random() * ((4 - 0) + 1));
					if(value == 0) {
						colour = Color.RED;
					}else if (value == 1) {
						colour = Color.BLUE;
					}else if (value == 2) {
						colour = Color.ORANGE;
					}else if (value == 3) {
						colour = Color.YELLOW;
					}else {
						colour = Color.CYAN;
					}
					
					objectType = 0 + (int)(Math.random() * ((6 - 0) + 1)); //=========== RANDOMLY DETERMINE OBJECT TYPE
					//System.out.println("ot = " + ot);
					
					//objectType = 6; //---------------------------------------------------temp
					
					int centerCol = gridWidth /2 -1;
					
					if(objectType == 0) {
					System.out.println(" start of ot 0");
						parts.add(new IntPair(0 + centerCol ,0));
						parts.add(new IntPair(0 + centerCol,1)); //center
						parts.add(new IntPair(0 + centerCol,2));
						parts.add(new IntPair(1 + centerCol,0));
						
						System.out.println(" ot 0 parts added");
					}else if(objectType == 1) {
						// L 2
						
						parts.add(new IntPair(0 + centerCol,0));
						parts.add(new IntPair(0 + centerCol,1));//center
						parts.add(new IntPair(0 + centerCol,2));
						parts.add(new IntPair(1 + centerCol,2));
					}else if (objectType == 2) {
						//square
						
						parts.add(new IntPair(0 + centerCol,0));
						parts.add(new IntPair(0 + centerCol,1));//center
						parts.add(new IntPair(1 + centerCol,1));
						parts.add(new IntPair(1 + centerCol,0));
						
					}else if (objectType == 3) {
						//T
						parts.add(new IntPair(0 + centerCol,0));
						parts.add(new IntPair(0 + centerCol,1));//center
						parts.add(new IntPair(0 + centerCol,2));
						parts.add(new IntPair(1 + centerCol,1));
						
					}else if (objectType == 4) {
						//line 
						parts.add(new IntPair(0 + centerCol,0));
						parts.add(new IntPair(0 + centerCol,1)); //center
						parts.add(new IntPair(0+ centerCol,2));
						//parts.add(new IntPair(0,3));
						
					}else if (objectType == 5) {
						//zig
						parts.add(new IntPair(0 + centerCol,0));
						parts.add(new IntPair(0 + centerCol,1)); //center
						parts.add(new IntPair(1 + centerCol,1));
						parts.add(new IntPair(1 + centerCol,2));
						
					}else if (objectType == 6) {
						//zag
						
						parts.add(new IntPair(1 + centerCol,0));
						parts.add(new IntPair(1 + centerCol,1));//center
						parts.add(new IntPair(0 + centerCol,1));
						parts.add(new IntPair(0 + centerCol,2));
						
					}else {
						System.err.println("you should never see this");
					}			
				}	
			}
		
		public boolean leftPressed = false;
		public boolean rightPressed = false;
		public boolean upPressed = false;
		boolean downPressed = false;
		boolean newPieceNeeded = false;
		int[][] matrix = new int [gridWidth][gridHeight]; //13 20
		FallingObject f;
		boolean gameOver = false;
		int score = 0;
		JLabel scoreLabel = new JLabel("Score = 0");
		//JButton resetButton = new JButton("restart");
		
		public void spawnPiece() { // CREATES NEW TETRIS PIECE, PAINTS IT ON MATRIX
			
			f = new FallingObject();
			
			for(IntPair ip : f.parts) {
				matrix[ip.a][ip.b] = f.colour.getRGB();
				
			}
			this.upPressed = false; //rogue
			this.downPressed = false;
			
			this.newPieceNeeded = false;
			System.out.println("piece spawned");
			
		}
		
		
		public void printRow(int rowNum) {
			for(int i = 0; i < matrix.length; i ++) {
				
				System.out.println(matrix[i][rowNum]);
					
			}
		}
		
		public boolean arrayRowContainsZero(int rowNum) { //RETURNS TRUE WHEN ROW IS NOT "FULL"
			
			for(int i = 0; i < matrix.length; i ++) {
				
				if(matrix[i][rowNum] == 0) {
					//System.err.println("returning true");
					return true;
					
				}
			}
			System.out.println("returning false");
			return false;
		}
		
		
		
		public void checkRows() { 
			
			int counter = 0; // tracks number of rows skipped
			int[][] newMatrix = new int[matrix.length][matrix[0].length];
			
			
			
			for(int i = matrix[0].length -1; i > 0; i--) {
				
				if(!this.arrayRowContainsZero(i)) {
					//copy the array into a new one bar this row
					System.out.println("line to remove");
					score++;
					counter++;

				}
				
				for(int j = 0; j < matrix.length; j ++) { //COPY ROW FROM MATRIX INTO NEW MATRIX, ONCE A FULL ROW IS FOUND YOU COPY 
										//FROM THE ROW ABOVE FOR ALL REMAING ROWS REMOVING THE ROW AND PUSHING ALL ROWS DOWN  

					//if(i - counter >= 0 && i + counter < matrix[0].length) {
					if(i - counter >= 0) {
						newMatrix[j][i] = matrix[j][i - counter];
					}else {
						System.out.println("once per cycle");
					}
				}
			}
			
			matrix = newMatrix;		
			
			if(counter != 0) {
				checkRows();
			}
		}
		
		public boolean canPieceMove() { // for downward motion only 	// if no and part of the piece is on the top row set game failed to true
			
			//check tiles directly below piece if there is a cube that is not part of the shape, set true and break
			for(IntPair ip : f.parts) { // for each cube in piece
				if(ip.b +1 != matrix[1].length) { // if any piece at bottom of table, return false
					
					if(matrix[ip.a][ip.b+1] != 0) { // if place below is full
						boolean isOwnPiece = false;
						for(IntPair ip2 : f.parts) { // if it's part of the shape is own piece = true
							if(ip.a == ip2.a && (ip.b + 1) == ip2.b) {
								isOwnPiece = true;
							}
						}
					 if(!isOwnPiece) {
						 System.out.println("can't move");
						 if(isPieceInTopRow()) {
								gameOver = true;
							}
						 return false;
					 }	
					}
				}else {
					System.out.println("can't move");
					if(isPieceInTopRow()) {
						gameOver = true;
					}
					return false;
				}	
			}
			return true;
		}
		
		public boolean isPieceInTopRow() {
			
			
			for(IntPair ip : f.parts) {
				if(ip.b == 0) {
					System.out.println("Game Over");
					return true;
				}
			}
			
			return false;
		}
		
		public boolean rotateIfPos(){ 
			//change the falling pieces rotation value
			if(canPieceRotate()) {
				
				//clear piece from matrix 
				for(IntPair ip : f.parts) {
					matrix[ip.a][ip.b] = 0;
				}
				
				
				if((upPressed && downPressed) || (!upPressed && !downPressed)) {
					//do nothing "canel eachother out"
					
				}else if(upPressed || downPressed) {

					if(upPressed) {
						f.rotationValu++;
					}else if(downPressed) {
						f.rotationValu = f.rotationValu + 3;
					}
					
					
					System.out.println("Rotation Value  =  " + f.rotationValu);
					
					//for type get relevant array
					IntPair centerPair = new IntPair(f.parts.get(1).a, f.parts.get(1).b);
					
					System.out.println("getting index   =  " + f.rotationValu % f.lOneArray.size());
					
					//hardcoded for l shape atm
					
					ArrayList<IntPair> nextRotation = null;
					//= f.lOneArray.get(f.rotationValu % f.lOneArray.size());
					
					if(f.objectType == 0) {
						nextRotation = f.lOneArray.get(f.rotationValu % f.lOneArray.size());
					} else if(f.objectType == 1) {
						nextRotation = f.lTwoArray.get(f.rotationValu % f.lTwoArray.size());
					}else if(f.objectType == 2) {
						return true; // boolean return type for the break here
					}else if(f.objectType == 3) {
						nextRotation = f.TArray.get(f.rotationValu % f.TArray.size());
					}else if(f.objectType == 4) {
						nextRotation = f.lineArray.get(f.rotationValu % f.lineArray.size());
					}else if(f.objectType == 5) {
						nextRotation = f.zigArray.get(f.rotationValu % f.zigArray.size());
					}else if(f.objectType == 6) {
						nextRotation = f.zagArray.get(f.rotationValu % f.zagArray.size());
					}
					
					
					for(int i = 0; i < f.parts.size() ; i++) {
						f.parts.set(i, new IntPair(nextRotation.get(i).a + centerPair.a,nextRotation.get(i).b + centerPair.b ));
					}
					
					//f.rotationValu + 1 % position arry
					//check potential postion 
					//if it's empty 
					//return true
					
				}
			}
			
			for(IntPair ip : f.parts) {
				matrix[ip.a][ip.b] = f.colour.getRGB();
			}
			
			upPressed = false;
			downPressed = false;
			return true;
		}
		
		public boolean canPieceRotate() {
			
			int tempRotationValue = 0;
			IntPair centerPair = new IntPair(f.parts.get(1).a, f.parts.get(1).b);// center is always second entry
			
			//if both up and down have been pressed do nothing 
			if((upPressed && downPressed) || (!upPressed && !downPressed)) {
				
			}else if(upPressed) {
				tempRotationValue = f.rotationValu + 1;
			}else if(downPressed) {
				tempRotationValue = f.rotationValu + 3;
				
			}
			
			
			//big if statement for ratation super array
			ArrayList<IntPair> nextRotation = null;
			
			if(f.objectType == 0) {
				nextRotation = f.lOneArray.get(tempRotationValue % f.lOneArray.size());
			} else if(f.objectType == 1) {
				nextRotation = f.lTwoArray.get(tempRotationValue % f.lTwoArray.size());
			}else if( f.objectType == 2) {
				return true;
			}else if(f.objectType == 3) {
				nextRotation = f.TArray.get(tempRotationValue % f.TArray.size());
			}else if(f.objectType == 4) {
				nextRotation = f.lineArray.get(tempRotationValue % f.lineArray.size());
			}else if(f.objectType == 5) {
				nextRotation = f.zigArray.get(tempRotationValue % f.zigArray.size());
			}else if(f.objectType == 6) {
				nextRotation = f.zagArray.get(tempRotationValue % f.zagArray.size());
			}
			
			
			for(IntPair ip : nextRotation) { // for each cube in piece
				
				//lazy lol ik
				try {
					
					if(matrix[ip.a + centerPair.a][ip.b + centerPair.b] != 0) {
						//if not empty
						
							boolean isOwnPiece = false;
							for(IntPair ip2 : f.parts) { // if it's part of the shape is own piece = true
								if(ip.a + centerPair.a == ip2.a && (ip.b + centerPair.b) == ip2.b) {
									isOwnPiece = true;
								}
							}
							
							 if(!isOwnPiece) {
								 System.out.println("can't rotate");
								 return false;
							 }	
							}
					
					
				}catch(Exception E) {
					System.out.println("can't rotate");
					return false;
				}	
			}		
			return true;
			
		}
		
		public void sidewaysMovePiece() {
			
			if((leftPressed && rightPressed) ||(!leftPressed && !rightPressed)) {
			
			}else if(rightPressed) {
				//if all pieces have space on the right 
				
				if(sideSpace(1)) {
					//clear pos on matrix
					for(IntPair ip : f.parts) {		
						//set orignoal matrix pos to 0
						matrix[ip.a][ip.b] = 0;
						//change coords
						ip.a++;
						//fall	
					}
				}
				
			}else if(leftPressed) {
				//if all piece have space on the left
				
				if(sideSpace(-1)) {
					//clear pos on matrix
					for(IntPair ip : f.parts) {		
						//set orignoal matrix pos to 0
						matrix[ip.a][ip.b] = 0;
						//change coords
						ip.a--;
					}
				}
				
			}
			
			for(IntPair ip : f.parts) {
				matrix[ip.a][ip.b] = this.f.colour.getRGB();
				
			}

			leftPressed = false;
			rightPressed = false;
			
		}
		
		// 1 right -1 left
		public boolean sideSpace(int i) {
			boolean space = true;
			
			for(IntPair ip : f.parts) {
				if(ip.a + i != matrix.length && ip.a + i != -1) {
					if(matrix[ip.a + i][ip.b] != 0) {
						boolean isOwnPiece = false;
						for(IntPair ip2 : f.parts) {
							if(ip.a  + i == ip2.a && (ip.b) == ip2.b) {
								isOwnPiece = true;
							}
							
						}
						
					 if(!isOwnPiece) {
						 space = false;
					 }	
					}
				}else {
					space = false;
				}	
			}
			
			return space;
		}
		
		
		public void fallPiece() {
			
			if(this.canPieceMove()) {
				for(IntPair ip : f.parts) {		
					//set orignoal matrix pos to 0
					matrix[ip.a][ip.b] = 0;
					//change coords
					ip.b++;
					//fall	
				}
				for(IntPair ip : f.parts) {
					matrix[ip.a][ip.b] = f.colour.getRGB();
					
				}
			}else {
				
				// if piece can't move you should spawn another
				newPieceNeeded = true;
				
			}
		}
		
		
		public TetrisJPanel() 
		{

			this.f = new FallingObject();
			for(IntPair ip : f.parts) {
				matrix[ip.a][ip.b] = f.colour.getRGB();
				
			}
			
			this.add(scoreLabel);
			scoreLabel.setFont(new Font("Helvetica", Font.PLAIN, 24));
			scoreLabel.setOpaque(true);

		}
		
		public void resetGame(){
			System.out.println("resetting game");
			
			int[][] newMatrix = new int[matrix.length][matrix[0].length];
			
			this.gameOver = false;
			this.newPieceNeeded = true;		
			this.resetPressed = true;
			
			matrix = newMatrix;			
			
			
			System.out.println("==============WITHIN RESET PRINT==================");
			this.printMatrix();
			System.out.println("==================================================");
			
			this.playGame();
			//this.revalidate();
			//this.repaint();
			//this.playGame();
		}
		
		@Override
		public void paint(Graphics g) {
			super.paintComponents(g);
			paintComponents(g);
		}
		
		@Override 
		public void paintComponents(Graphics g) {

			scoreLabel.setText(scoreLabel.getText().split(" ")[0] + " " + scoreLabel.getText().split(" ")[1] + " " + score);

			g.setColor(Color.white);
			g.fillRect (50,50, matrix.length * 20, matrix[0].length * 20);
			
			for(int x = 0; x < matrix.length; x ++) {
				for(int y = 0; y < matrix[1].length; y ++) {

					g.setColor(new Color(matrix[x][y]));
					super.paintComponents(g);
					
					int xpos, ypos;
					xpos = x;
					ypos = y;
					xpos = xpos * 20;
					ypos = ypos * 20;
					xpos = xpos + 50;
					ypos = ypos + 50;
					if(matrix[x][y] != 0) {
						
						//System.out.println("printing at " + xpos +" " + ypos + " y = " + y + " x = " + x );
						g.fillRect ((xpos), (ypos), 20, 20);
					}	
				}
			}			
		}

		

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		//=====================================DEBUGING TOOL=========================
		 public void printMatrix() {
			 for (int[] x : matrix)
			 {
			    for (int y : x)
			    {
			         System.out.print(y + " ");
			    }
			    System.out.println();
			 }
			 
			 System.out.println("-------------------------");
		 }


		 @Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				//System.out.println("something pressed");
				if(arg0.getKeyCode() == KeyEvent.VK_LEFT) {
					//System.out.println("			left");
					leftPressed = true;
				}else if(arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
					//System.out.println("			right");
					rightPressed = true;
				}else if(arg0.getKeyCode() == KeyEvent.VK_UP) {
					//System.out.println("			up");
					upPressed = true;
				}else if(arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					//System.out.println("			down");
					downPressed = true;
				}
				
			}
	}
	 
	
	
}


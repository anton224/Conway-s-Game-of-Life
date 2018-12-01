/* Maman 15 , question 2
* Class which represents a thread of a single cell
* Author : Anton Rubenchik
* Date : 08/06/2018 */
public class CellThread extends Thread{

	private int row,col;
	LifeMatrix lifeMatrix;
	// constructor
    public CellThread(int row,int col,LifeMatrix lifeMatrix)
    {
        this.row = row;
        this.col = col;
        this.lifeMatrix = lifeMatrix;     
    }

    @Override
    public void run()
    {    	    		
    	while(true)
    	{    	
    		lifeMatrix.changeCell(row,col); // call a method which changes a single cell
    	}
    }
	
}


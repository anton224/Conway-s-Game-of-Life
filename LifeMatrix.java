/* Maman 15 , question 2
* LifeMarix class represents the life matrix and the relevant algorithm for calculating the next generations.
* Author : Anton Rubenchik
* Date : 08/06/2018 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.regex.*;
import java.io.*;
import java.util.concurrent.*;


public class LifeMatrix extends JPanel{
	
	private int[][] arr, tempArr; // stores the data about the current and the next generation life
	private boolean isNum = false;
	private Random rand = new Random(); // generate random num
	private JPanel mainPanel = new JPanel();
	private int size,runningThreads,totalThreads;
	private JPanel arrPanel[][];
	private boolean firstTime,run;
		
	public LifeMatrix() // contructor - initializes values in the array which represents the life matrix
	{	
		setSize();
		totalThreads = size * size; // for easier understanding of the code
		firstTime = true;
		run = false;
		arrPanel = new JPanel[size][size];
		arr = new int[size][size];
		tempArr = new int[size][size];
		setLayout(new GridLayout(size,size,5,5));
		drawMatrix(true); // true - generate new values in the array	
		firstTime = false;							
	}
	// return the size of the matrix
	public int getSizeM()
	{
		return size;
	}
	public synchronized void drawMatrix(boolean newValues)
	{
		for(int i = 0;i < size ; i++)
		{
			for(int j = 0;j < size ; j++)
			{				
				if(newValues)
				{
					arr[i][j] = rand.nextInt(2); // enter random life statuses in the matrix
					tempArr[i][j]  = arr[i][j];					
				}
				if(firstTime) // if first time then : put random values in arr, create jpanel and thread for each cell(+ start them) , add controller, and add the jpanel to the main panel
				{					
					arrPanel[i][j] = new JPanel();							
					new CellThread(i,j,this).start();
					arrPanel[i][j].setPreferredSize(new Dimension(50,50));
					add(arrPanel[i][j]);
				}			
				if (arr[i][j] == 1)
					arrPanel[i][j].setBackground(Color.WHITE);
				else
					arrPanel[i][j].setBackground(Color.BLACK);
				
			}
		}
		if(!newValues)
		{
			try{
				TimeUnit.SECONDS.sleep(1); // sleep in order to give the user the opportunity to see the matrix
				}
				catch(InterruptedException e){}
		}
	}
	public synchronized void wake()
	{
		run = !(run);		
		notifyAll();
	}
	public synchronized void goDown()
	{
		try{
            wait();
       		}
        catch(InterruptedException e){}
	}
	public synchronized boolean getRun()
	{
		return run;
	}
	public synchronized void changeStatus()
	{
		run = !run;
	}
	public synchronized void changeCell(int i,int j)
	{	
		if( !( getRun() ) ) // go to sleep if the thread arrives here before the matrix was shown
			{
				try{
		            wait();
		       		}
		        catch(InterruptedException e){}
			}
		
		int neighbors = checkNeighbors(i,j); // calculate the neighbors for the current cell
		if(arr[i][j] == 0)	// if it is a dead cell	
			if(neighbors == 3)
				tempArr[i][j] = 1; 
			else
				tempArr[i][j] = 0;					
		else // if it is a living cell
			if(neighbors == 0 || neighbors == 1 || neighbors > 3)
				tempArr[i][j] = 0;
			else
				tempArr[i][j] = 1;
		runningThreads++;
		if( runningThreads == totalThreads) // all the threads finished updating the temp matrix - this is the last thread
		{
			runningThreads = 0; 
			arr = tempArr.clone(); // the tempArr is filled with the new values - so we transform the value to the original array and then update the matrix itself
			for (int k = 0; k < arr.length; k++)
			{
			    arr[k] = arr[k].clone();
			}
			drawMatrix(false); // false - don't generate new random values in the array		
			notifyAll();			
		}
		else
		{
			try{
	            wait();
	       		}
	        catch(InterruptedException e){}
		}
						
	}
	// get the size from user
	private void setSize()
	{
		boolean isNum = false; 
		String str;		
		do{
			 str = JOptionPane.showInputDialog("Enter the size of the matrix - single number");
			if(str == null || (str != null) && "".equals(str))
				System.exit(0);
			if(!(str.matches("\\d+")))
				JOptionPane.showMessageDialog(null,"You entered wrong input - only number allowed!");
			else
				isNum = true;
		}while(!isNum);			
		size = Integer.parseInt(str);					
	}
	// draws the life matrix
	public void paintComponent(Graphics g){
		super.paintComponent(g);		
	}
	// calculates the next life generation and shows it on the panel - using a temporary array
public void nextGeneration()
{
	int neighbors; // store how many neighbors each cell got
	// loops which moves over the matrix
	for(int i = 0;i < size  ; i++)
	{
		for(int j = 0;j < size ; j++)
		{
			neighbors = checkNeighbors(i,j); // calculate the neighbors for the current cell
				if(arr[i][j] == 0)	// if it is a dead cell	
					if(neighbors == 3)
						tempArr[i][j] = 1; 
					else
						tempArr[i][j] = 0;					
				else // if it is a living cell
					if(neighbors == 0 || neighbors == 1 || neighbors > 3)
						tempArr[i][j] = 0;
					else
						tempArr[i][j] = 1;
		}
	}	
}


// check how many neighbors the [i][j] cell got
private int checkNeighbors(int i,int j)
{
	int counter = 0;
	if(i > 0) // check the upper neighbors
	{
		if(j > 0) // up left
			if(arr[i-1][j-1] == 1)
				counter++; 
			if(arr[i-1][j] == 1) // straight up
				counter++;
		if(j < size - 1) // up right
			if(arr[i-1][j + 1] == 1)
				counter++;		
	}
	if(j > 0) // left neighbor
		if(arr[i][j-1] == 1) 
			counter++;
	if(j < size - 1) // right neighbor
		if(arr[i][j + 1] == 1)
			counter++;
	if(i < size - 1) // check the lower neighbors
	{
		if(j > 0) // down left
			if(arr[i+1][j-1] == 1)
				counter++; 
			if(arr[i+1][j] == 1) // down up
				counter++;
		if(j < size - 1) // down right
			if(arr[i+1][j + 1] == 1)
				counter++;	
    }
	return counter; // return the number of neighbors 
}
}
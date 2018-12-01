/* Maman 15 , question 2
* The program represents John Conway's simulation called life game.
* Class which creates the frame and runs the program
* Author : Anton Rubenchik
* Date : 08/06/2018 */


import javax.swing.*;
// This class is in charge of communicating with the user - user interface.
public class LifeGame {
	public static void main(String[] args)
	{		
		int input; // contains user's answer for the question asked
		MatrixFrame window = new MatrixFrame(); // creates frame						
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(window.getSizeM()*50,window.getSizeM()*55); // set window's size
		window.setVisible(true);		// make is visible
		window.wake();
		window.waitFor();
	}
}

/* Maman 15 , question 2
* Class which represents the frame on which the matrix is added
* Author : Anton Rubenchik
* Date : 08/06/2018 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MatrixFrame extends JFrame{

	private final JButton restartButton;
	private final GridLayout layout;
	private final Container container;
	private LifeMatrix lifeMatrix;
	private JPanel buttonPanel;
	public MatrixFrame()
	{
		super("Matrix");
		layout = new GridLayout(2,1);
		container = getContentPane();
		setLayout(layout);
		buttonPanel = new JPanel();
		restartButton = new JButton("restart");
		buttonPanel.setSize(30, 30);
		buttonPanel.add(restartButton);
		lifeMatrix = new LifeMatrix();  
		add(lifeMatrix);
		add(buttonPanel);
		ButtonHandler handler = new ButtonHandler();
		restartButton.addActionListener(handler);
					
	}
	public int getSizeM()
	{
		return lifeMatrix.getSizeM();
	}
	// wake up all the threads
	public void wake()
	{	
		lifeMatrix.wake();		
	}
	// go to wait and let the threads do their job
	public void waitFor()
	{
		while(true)
		{								
			lifeMatrix.goDown(); // it could be down also an additional controller to avoid contraddiction
		}
	}
private class ButtonHandler implements ActionListener
{
	public void actionPerformed(ActionEvent event)
	{
		lifeMatrix.changeStatus();
		lifeMatrix.drawMatrix(true); // true - generate new random values in the array
		lifeMatrix.wake();
	}
}
}
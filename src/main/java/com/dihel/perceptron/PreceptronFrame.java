package com.dihel.perceptron;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color; 

public class PreceptronFrame extends javax.swing.JFrame
{

	// these are the components we need.
    private JSplitPane splitPane;  // split the window in top and bottom
    private JPanel topPanel;       // container panel for the top
    private JPanel bottomPanel;    // container panel for the bottom
    private JScrollPane scrollPane; // makes the text scrollable
    private JTextArea textArea;     // the text
    private JPanel inputPanel;      // under the text a container for all the input elements
    private JButton button;         // and a "send" button

	private Perceptron perceptron;
	
    public PreceptronFrame(final Perceptron perceptron) {

    	this.perceptron = perceptron;

        splitPane = new JSplitPane();

        PlotPerceptron plotPerceptron = new PlotPerceptron(perceptron);
        plotPerceptron.setVisible(true);
        plotPerceptron.setBackground(Color.WHITE);
        bottomPanel = new JPanel();      // our bottom component

        // in our bottom panel we want the text area and the input components
        scrollPane = new JScrollPane();  // this scrollPane is used to make the text area scrollable
        textArea = new JTextArea();      // this text area will be put inside the scrollPane

        inputPanel = new JPanel();
        button = new JButton("Next");    // and a button at the right, to send the text

        button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean isTrained = perceptron.trainStep();
				splitPane.setVisible(false);
				splitPane.setVisible(true);
				double[] weights = perceptron.getWeights();
				textArea.append("Weight[0]: " + weights[0] + " Weight[1]" + weights[1] + " weights[2] " + weights[2] + "\n");

				if (isTrained) {
					textArea.append("Trained ");
					button.setVisible(false);
				}
			}
        });
        
        setPreferredSize(new Dimension(600, 600));     // let's open the window with a default size of 400x400 pixels
        getContentPane().setLayout(new GridLayout());  // the default GridLayout is like a grid with 1 column and 1 row,
        getContentPane().add(splitPane);               // due to the GridLayout, our splitPane will now fill the whole window

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);  // we want it to split the window verticaly
        splitPane.setDividerLocation(400);                    // the initial position of the divider is 200 (our window is 400 pixels high)
        splitPane.setTopComponent(plotPerceptron);                  // at the top we want our "topPanel"
        splitPane.setBottomComponent(bottomPanel);            // and at the bottom we want our "bottomPanel"

        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS)); // BoxLayout.Y_AXIS will arrange the content vertically

        bottomPanel.add(scrollPane);                // first we add the scrollPane to the bottomPanel, so it is at the top
        scrollPane.setViewportView(textArea);       // the scrollPane should make the textArea scrollable, so we define the viewport
        bottomPanel.add(inputPanel);                // then we add the inputPanel to the bottomPanel, so it under the scrollPane / textArea

        // let's set the maximum size of the inputPanel, so it doesn't get too big when the user resizes the window
        inputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));     // we set the max height to 75 and the max width to (almost) unlimited
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));   // X_Axis will arrange the content horizontally

        inputPanel.add(button);           // and right the "send" button

        pack();   // calling pack() at the end, will ensure that every layout and size we just defined gets applied before the stuff becomes visible
    }

	
}

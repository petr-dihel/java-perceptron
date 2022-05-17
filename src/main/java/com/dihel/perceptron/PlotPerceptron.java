package com.dihel.perceptron;
import java.awt.*;  
import javax.swing.*;  
import java.awt.geom.*;
import java.io.IOException;

import javax.swing.JPanel;

import com.dihel.perceptron.PlotPerceptron.Point;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class PlotPerceptron extends JPanel {

    //initialize coordinates  
    //int[] cord = {65, 20, 40, 80};  
    int marg = 60;  
    
    Perceptron perceptron;
    
    Point scale = new Point(1, 1);
    Point min = new Point(0, 0);
    Point max = new Point(1, 1);
    Graphics2D graph;
    
    public class Point {
    	public double x,y;
    	
    	public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
    }
    
    public PlotPerceptron(Perceptron perceptron) {
    	super();
    	this.perceptron = perceptron;
    }
    

    private void drawPoints() {
    	graph.setPaint(Color.BLACK);  
    	for (TrainSetElement e : perceptron.getTrainSet()) {
    		double[] inputs = e.getInputs();
    		int output = e.getIntOutput();
    		int pre = perceptron.predictSigmum(inputs);
    		graph.setPaint(Color.RED);  
    		if (output > 0) {
    			graph.setPaint(Color.GREEN);  
    		}
    		Point p = new Point(inputs[0], inputs[1]);
    		p = getPoint(p);
    		graph.drawOval((int)p.x, (int)p.y, 5,5);  	
    	}
    	
    	
    	for (TestSetElement e : perceptron.getTestSet()) {
    		double[] inputs = e.getInputs();
    		int pre = perceptron.predictSigmum(inputs);
    		graph.setPaint(Color.BLACK);  
    		if (pre > 0) {
    			//graph.setPaint(Color.GREEN);  
    		}
    		Point p = new Point(inputs[0], inputs[1]);
    		p = getPoint(p);
    		graph.drawOval((int)p.x, (int)p.y, 5,5);  	
    	}
    }
    
    private void pLine() {
    	double[] weights = perceptron.getWeights();
    	
    	double[] normalWeights = new double[3];
    	double sum = Math.abs(weights[0]) + Math.abs(weights[1]) + Math.abs(weights[2]);
    	normalWeights[0] = weights[0]/sum;
    	normalWeights[1] = weights[1]/sum;
    	normalWeights[2] = weights[2]/sum;
    	
        double slope = -(normalWeights[0]/normalWeights[2])/(normalWeights[0]/normalWeights[1]);
        double intercept = -normalWeights[0]/normalWeights[2];

        //y =mx+c, m is slope and c is intercept
        double y2 = -(weights[1]/weights[2]) * 1 - (weights[0]/weights[2]);
        //y2 = height-marg-scale*y2;  
        double y1 = -(weights[1]/weights[2]) * 0 - (weights[0]/weights[2]);
        //y1 = height-marg-scale*y1;  */
        //x = (y-c)/k
    	

    	double x1 = 0;  
        double x2 = 1;
        
        //double y1 = -(normalWeights[0]+normalWeights[1]*x1)/normalWeights[2];
        //double y2 = -(normalWeights[0]+normalWeights[1]*x2)/normalWeights[2];
        
        
        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        if (max.y < p1.y) {
        	max.y = p1.y;
        }
        if (max.y < p2.y) {
        	max.y = p2.y;
        }
        if (min.y > p1.y) {
        	min.y = p1.y;
        }
        if (min.y > p2.y) {
        	min.y = p2.y;
        }
        
        System.out.println("LINE ( " + p1.x + "  " + p1.y + " ); (" + p2.x + "  " + p2.y + " ) ");
        System.out.println("PLINE " + min.y + " max " + max.y);

        double rangeY = Math.ceil(max.y) - Math.floor(min.y);
        //scale = new Point((getWidth() - 2* marg)/max.x, (getHeight() - 2*marg)/rangeY) ;
        scale.x = (getWidth() - 2* marg)/max.x;
        scale.y = (getHeight() - 2*marg)/rangeY;
        
        Point rfP = getPoint(p1);
        Point rSP = getPoint(p2);
        graph.setPaint(Color.RED);  
        graph.setStroke(new BasicStroke(2));   
        graph.draw(new Line2D.Double(rfP.x, rfP.y, rSP.x, rSP.y));  	
        graph.setPaint(Color.BLACK);  
        
    }
    
    protected void paintComponent(Graphics grf) {
    	System.out.println("Zde Test " + min.y);
    	max = new Point(1, 1);
    	min = new Point(0, 0);
    	super.paintComponent(grf);  
        graph = (Graphics2D)grf;
        
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    


        graph.draw(new Line2D.Double(marg, marg, marg, getHeight()-marg));  
      
        pLine();
        drawCordinates();
            
        Point p1 = new Point(0, 0);
        p1 = getPoint(p1);
        Point p2 = new Point(1, 0);
        p2 = getPoint(p2);
		graph.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));  	
    
		drawPoints();
    }  
    
    
    private Point getPoint(Point point) {
    	Point newPoint = new Point(0, 0);
    	newPoint.x = point.x * scale.x + (marg);
    	
    	newPoint.y = (getHeight() - marg) - ((point.y - Math.floor(min.y)) * scale.y);
    	return newPoint;
    }
    
    private void drawCordinates() {
    	double rate = 10;
    	for (int i = 0; i <= (max.x*rate); i++) {
    		Point p = new Point(i/rate, 0);
    		Point rP = getPoint(p);
    		//System.out.println("I "  + i +" X " + rP.x + " Y " + rP.y);
    		graph.drawString(Double.toString(i/rate), (int)rP.x - 5, (int)rP.y + 15);
    	}
    	
    	
    	double rangeY = Math.ceil(max.y) - Math.floor(min.y);
    	double step = rangeY / 10;
    	int yMin = (int)Math.floor(min.y);
    	int yMax = (int)Math.ceil(max.y);
    	System.out.println("yMin "  + yMin);
    	for (double i = yMin; i <= (yMax); i = i + step) {
    		Point p = new Point(0, i);
    		Point rP = getPoint(p);
    		graph.drawString(String.format("%,.2f",i), (int)rP.x + 5, (int)rP.y + 15);
    	}
    }
  

}

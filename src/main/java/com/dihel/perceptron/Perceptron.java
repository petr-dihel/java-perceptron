package com.dihel.perceptron;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;

import java.awt.*;  
import javax.swing.*;  
import java.awt.geom.*;  

public class Perceptron {

	private double[] weights;
	private double[] inputs;
	private InputDescription[] inputDescriptions;
	private double output;
	private double lerningRate = 0.3;
	private String name = "y";
	
	private List<TrainSetElement> trainSet;
	private List<TestSetElement> testSet;
	
	
	public Perceptron() {
		this(2);
	}

	public Perceptron(int numberOfInputs){
		weights = new double[numberOfInputs+1];
		inputs = new double[numberOfInputs+1];
		inputDescriptions = new InputDescription[numberOfInputs];
		for (int i = 0; i < inputDescriptions.length; i++) {
			inputDescriptions[i] = new InputDescription("Input " + i, 0, 1); 
		}
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@XmlElementWrapper(name="weights")
	@XmlElement(name="weight")
	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}

	public double getOutput() {
		return output;
	}

	
	public InputDescription[] getInputDescriptions() {
		return inputDescriptions;
	}

	public void setInputDescriptions(InputDescription[] inputDescriptions) {
		this.inputDescriptions = inputDescriptions;
		if(inputDescriptions.length != inputs.length+1){
			inputs = new double[inputDescriptions.length+1];
		}
	}
	
	public void normalizeInputs() 
	{
		for (int i = 0; i < inputDescriptions.length; i++) {
			inputs[i] = (inputs[i] - inputDescriptions[i].getMinimum())/(inputDescriptions[i].getMaximum() - inputDescriptions[i].getMinimum());  
		}
	}
	
	
	
	public void trainAndPlot(List<TrainSetElement> trainSet, List<TestSetElement> testSet) {
		//todo
		weights[0] = 0.5;
		inputs[0] = 1.0;
		for (TrainSetElement trainSetElement : trainSet) {
			double[] trainInputs = trainSetElement.getInputs();
			for (int i = 0; i < inputDescriptions.length; i++) {
				trainInputs[i] = (trainInputs[i] - inputDescriptions[i].getMinimum())/(inputDescriptions[i].getMaximum() - inputDescriptions[i].getMinimum());  
			}
			trainSetElement.setInputs(trainInputs);
		}
		this.trainSet = trainSet;
		
		for (TestSetElement testSetElement : testSet) {
			double[] testInputs = testSetElement.getInputs();
			for (int i = 0; i < inputDescriptions.length; i++) {
				testInputs[i] = (testInputs[i] - inputDescriptions[i].getMinimum())/(inputDescriptions[i].getMaximum() - inputDescriptions[i].getMinimum());  
			}
			testSetElement.setInputs(testInputs);
		}
		this.testSet = testSet;
		
		new PreceptronFrame(this).setVisible(true);
	}
	
	public boolean trainStep() {
		boolean isTrained = true; 
		for (TrainSetElement trainSetElement : trainSet) {
			double[] trainInputs = trainSetElement.getInputs();

			int output = this.predictSigmum(trainInputs);

			int expected = trainSetElement.getIntOutput();
			System.out.println("Test row " + trainInputs[0] + " " + trainInputs[1]);
				
			System.out.println(" expected " + expected + " output " + output);
			isTrained = isTrained && (output == expected); 


			System.out.println(" weights.length " + weights.length);
			for (int i = 0; i < weights.length; i++) {
				double a = (expected - output) * (inputs[i]) * lerningRate; 					
				System.out.println(" changed by " + a);
				weights[i] = weights[i] + ((double)expected - (double)output) * (inputs[i]) * lerningRate; 					
			}
		}	
		return isTrained;
	}
	
	public void train(List<TrainSetElement> trainSet) {
		
		
		//create an instance of JFrame class  
/*  
		List<Double> x = NumpyUtils.linspace(-3, 3, 100);
		//List<Double> y = x.stream().map(xi -> Math.sin(xi) +　Math.random()).collect(Collectors.toList());
		List<Double> y = x.stream().map(xi -> Math.sin(xi) +　Math.random()).collect(Collectors.toList());

		Plot plt = Plot.create();
		plt.plot().add(x, y, "o").label("sin");
		plt.legend().loc("upper right");
		plt.title("scatter");
		plt.show();*/
        
		// normalize inputs
		weights[0] = 0.5;
		inputs[0] = 1;
		for (TrainSetElement trainSetElement : trainSet) {
			double[] trainInputs = trainSetElement.getInputs();
			for (int i = 0; i < inputDescriptions.length; i++) {
				trainInputs[i] = (trainInputs[i] - inputDescriptions[i].getMinimum())/(inputDescriptions[i].getMaximum() - inputDescriptions[i].getMinimum());  
			}
			trainSetElement.setInputs(trainInputs);
		}
		
		boolean isTrained = false;
		int interations = 0;
		while (!isTrained) {
			isTrained = true; 
			for (TrainSetElement trainSetElement : trainSet) {
				double[] trainInputs = trainSetElement.getInputs();
				//System.out.println("Inputs " + trainInputs[0] + " : " + trainInputs[1]);
				System.out.println("Before Inputs " + inputs[0] + " : " + inputs[1] + " : " + inputs[2]);

				int output = this.predictSigmum(trainInputs);
				System.out.println("After Inputs " + inputs[0] + " : " + inputs[1] + " : " + inputs[2]);
				System.out.println("After Wei " + weights[0] + " : " + weights[1] + " : " + weights[2]);
				
				int expected = trainSetElement.getIntOutput();
				isTrained = isTrained && (output == expected); 

				//System.out.println("Output " + output + " expected " + expected);
				
				for (int i = 0; i < weights.length; i++) {
					//System.out.println("BEFORE weights " + i + " value " + weights[i] );
					//System.out.println(weights[i] + " + " + " ( " + output 
					//		+ " - " +  expected + ") " + " * " + inputs[i] + " * " + lerningRate);
					weights[i] = weights[i] + ((expected- output) * inputs[i] * lerningRate); 
					//System.out.println("AFTER weights " + i + " value " + weights[i] );	
					//System.out.println("");	
				}
			}	
			interations++;
			//System.out.println("Debug " + interations);
			if (interations > 500) {
				System.out.println("Exceeded number of 5000 iterations");
				break; 
			}
			
		}
			
		System.out.println("Iterations " + interations);
		
	}
	
	public int predictSigmum(double[] pIntputs) {
		double sum = 0.0;
		//sum += weights[0];
		for (int i = 1; i < inputs.length; i++) {
			inputs[i] = pIntputs[i - 1];
		}
		
		// first input is theta
		for (int i = 0; i < inputs.length; i++) {
			sum += inputs	[i] * weights[i];
		}
		//System.out.println("Sum : " + sum);
		return (sum >= 0.0 ? 1 : 0);
	}
		
	public double getLerningRate() {
		return lerningRate;
	}

	public void setLerningRate(double lerningRate) {
		this.lerningRate = lerningRate;
	}
	
	public List<TrainSetElement> getTrainSet() {
		return this.trainSet;
	}
	

	public List<TestSetElement> getTestSet() {
		return this.testSet;
	}
	
	
	@Override
	public String toString() {
		return "Perceptron [weights=" + Arrays.toString(weights) + ", inputs="
				+ Arrays.toString(inputs) + ", inputDescriptions="
				+ Arrays.toString(inputDescriptions) + ", output=" + output
				+ ", lerningRate=" + lerningRate + ", name=" + name + "]";
	}

}

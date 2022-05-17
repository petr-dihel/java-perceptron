package com.dihel.perceptron;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.management.openmbean.InvalidOpenTypeException;
import javax.xml.bind.JAXBException;

/**
 * Hello world!
 *
 */
public class App 
{
	
	public static void setRandomWeights(Perceptron perceptron) {
		Random r = new Random();
		double[] weights = perceptron.getWeights();
		for (double weight : weights) {
			weight = r.nextFloat();
		}
		
	}

	public static void Test(Perceptron perceptron, List<TestSetElement> testSet) {
		for (TestSetElement testSetElement : testSet) {
			double[] inputs = testSetElement.getInputs();
			int output = perceptron.predictSigmum(inputs);
			//int expected = testSetElement.
			//System.out.println("Output " + output + " expected : " + expected);
		}
	}
	
    public static void main( String[] args )
    {
		PerceptronTask pt;
		try {
			pt = PerceptronTask.loadFromXML(new File("t3r.xml"));
			System.out.println(pt.toString());
			List<TrainSetElement> trainSet = pt.getTrainSet();
			Perceptron perceptron = pt.getPerceptron();
			//setRandomWeights(perceptron);
			int inputs = trainSet.get(0).getInputs().length;
			if (inputs == 2) {
				perceptron.trainAndPlot(trainSet, pt.getTestSet());	
			} else {
				perceptron.train(trainSet);	
			}
			//Test(perceptron, pt.getTestSet());
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
    }
}

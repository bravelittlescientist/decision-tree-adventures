import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DecisionTreeContainer {
	
	public static void main(String[] args) throws Exception {
		
		ArrayList<ArrayList<Integer>> TrainingSet = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> TestingSet = new ArrayList<ArrayList<Integer>>();
		
		// Known training set size is 80
		int trainingSize = 80;
		int counter = 0;
		
		System.out.println("Reading training/testing data...");
		String line = null;
		BufferedReader train = new BufferedReader(new FileReader("heart_data/SPECT_heart_data"));
		while((line = train.readLine()) != null) {

			StringTokenizer st = new StringTokenizer(line, ",");
			ArrayList<Integer> t = new ArrayList<Integer>();

			while (st.hasMoreElements()) {
				t.add(Integer.decode(st.nextToken()));
			}

			if (counter < trainingSize) {
				TrainingSet.add(t);
			} else {
				TestingSet.add(t);
			}
			counter++;
		}
		
		// Create attribute list
		ArrayList<Integer> attributes = new ArrayList<Integer>();
		for (int i = 1; i < TrainingSet.get(0).size(); i++) {
			attributes.add(new Integer(i));
		}
		
		// Build decision tree
		System.out.println("Building decision tree...");
		DecisionTree DT = new DecisionTree(TrainingSet, 0, attributes);
		
		System.out.println("Classfying Testing Set...");
		double correct = 0.0;
		for (ArrayList<Integer> datapoint : TestingSet) {
			// Compute classification
			Integer result = DT.classifyDatapoint(datapoint);
			
			// If correct, increment correct value
			if (result.intValue() == datapoint.get(0).intValue()) {
				correct += 1.0;
			}
		}
		
		System.out.println("Percentage Correct: " + (100*correct)/TestingSet.size() + "%");

		System.out.println("Done");
		
	}
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class RandomForestRunner {
	
	/** 
	 * Forest Generation
	 */
	
	public static class HeartInput {
		
		private ArrayList<ArrayList<Integer>> training = new ArrayList<ArrayList<Integer>>();
		private ArrayList<ArrayList<Integer>> testing = new ArrayList<ArrayList<Integer>>();
		
		public HeartInput () throws NumberFormatException, IOException {
		
			// Known training set size is 80
			int trainingSize = 80;
			int counter = 0;
		
			System.out.println("Reading training/testing data...");
			String line = null;
			BufferedReader train = 
				//new BufferedReader(new FileReader("heart_data/SPECT_heart_data"));
				new BufferedReader(new FileReader("/home/genia/Datasets/Spect/SPECT_heart_data"));
				
			// Read training and testing data
			while((line = train.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, ",");
				ArrayList<Integer> t = new ArrayList<Integer>();

				while (st.hasMoreElements()) {
					t.add(Integer.decode(st.nextToken()));
				}

				if (counter < trainingSize) {
					this.training.add(t);
				} else {
					this.testing.add(t);
				}
				counter++;
			}
		}
		
		public ArrayList<ArrayList<Integer>> getTrainingData () {
			return this.training;
		}
		
		public ArrayList<ArrayList<Integer>> getTestingData() {
			return testing;
		}

	}
	
	public static ArrayList<Integer> 
	trainingSubsetWithReplacement(ArrayList<ArrayList<Integer>> data) {
		
		ArrayList<Integer> subset = new ArrayList<Integer>();
		
		Random r = new Random();
		for (int i = 0; i < data.size(); i++) {
			subset.add(r.nextInt(data.size()));
		}
		
		return subset;
	}
	
	/** 
	 * Computation and Graph Topology
	 */
	
	public static void main(String[] args) throws Exception {
	
		System.out.println("Reading input data...");
		HeartInput Input = new HeartInput();
		
		ArrayList<ArrayList<Integer>> TrainingSet = Input.getTrainingData();
		ArrayList<ArrayList<Integer>> TestingSet = Input.getTestingData();
		
		ArrayList<RandomForestClassificationTree> Forest = 
			new ArrayList<RandomForestClassificationTree>();
		
		System.out.println("Generating Forest of 1000 trees...");
		
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (int i = 1; i < TrainingSet.get(0).size(); i++) {
			a.add(i);
		}
		
		for (int i = 0; i < 1000; i++)
		{
			// Create training subset with replacement
			ArrayList<ArrayList<Integer>> s = new ArrayList<ArrayList<Integer>>();
			
			for (Integer index : trainingSubsetWithReplacement(TrainingSet)) {
				s.add(TrainingSet.get(index));
			}
			
			Forest.add(new RandomForestClassificationTree(s, 0, a));
		}
		
		System.out.println("Classifying Test Dataset...");
		
		double correct = 0.0;
		
		for (ArrayList<Integer> testData : TestingSet) {
			
			int expected = testData.get(0);
		
			Map<Integer, Integer> poll = new HashMap<Integer, Integer>();

			// Classify with each decision tree
			for (RandomForestClassificationTree T : Forest) {
				int result = T.classifyDatapoint(testData);
				
				if (!poll.containsKey(result)) {
					poll.put(result, 1);
				}
				else
				{
					poll.put(result, poll.get(result) + 1);
				}
			}
			
			if (Collections.max(poll.values()) == poll.get(expected)) {
				correct++;
			}
			
		}

		System.out.println("Done, Percentage Correct: " + 100*correct/TestingSet.size());
	}
}

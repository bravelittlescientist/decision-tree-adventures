import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * ID3 Decision Tree Implementation
 * @author genia
 *
 */
public class DecisionTree {
	
	// Root node of DecisionTree
	private DecisionTreeNode rootNode;
	
	/**
	 * Decision Tree Constructor
	 * 
	 * Build decision tree from data, target attribute, and attributes to consider
	 * 
	 * @param data
	 * @param targetAttributeValue
	 * @param attributeIndices
	 */
	public DecisionTree (ArrayList<ArrayList<Integer>> data, 
			Integer targetAttributeValue,
			ArrayList<Integer> attributeIndices) {
		
		// Build decision tree
		this.rootNode = 
			this.buildDecisionTree(data, targetAttributeValue, attributeIndices);	
	}
	
	/**
	 * printTreeNodes
	 * 
	 * Prints the nodes of the tree in a breadth first manner
	 */
	public void printDecisionTree () {
	
		// Initialize queue
		LinkedList<DecisionTreeNode> queue = new LinkedList<DecisionTreeNode>();
		
		// Add root node to queue
		queue.add(this.rootNode);
		
		// Until empty, print all available nodes
		while (!queue.isEmpty()) {
			
			// Pop front element
			DecisionTreeNode front = queue.remove();
			
			// Print front element
			front.printNode();
			
			// Add children of front, if they exist, to the queue
			queue.addAll(front.getChildren());
		}

	}
	
	/**
	 * Building Decision Tree
	 */
	
	/**
	 * majorityValue
	 * 
	 * Compute majority result of target attribute. 
	 * 
	 * @param datapoints		Set of data for classification
	 * @param targetAttribute	Index of target attribute
	 * @return majority			Majority value of target attribute
	 */
	public int majorityValue (ArrayList<Integer> targetAttributeValues) {
		
		// Initialize majority value and value tracker
		int majority = -1; // FIXME In the future, edit this value.
		
		// Initialize majority value tracker
		Map<Integer, Integer> tracker = new HashMap<Integer, Integer>();
		
		// Count up occurrences of target value
		for (Integer attributeValue : targetAttributeValues) {
			
			// Update count for occurence of attribute
			int count = tracker.containsKey(attributeValue) ? 
					tracker.get(attributeValue).intValue() : 0;
			tracker.put(attributeValue, count + 1);
						
			// Check if this count is greater than current/initial majority
			if (majority == -1 || count + 1 > tracker.get(majority).intValue()) {
				majority = attributeValue.intValue();
			}
		}
		
		// Return majority value in this dataset for target attribute
		return majority;
	}
	
	/**
	 * allElementsSame
	 * 
	 * Tests if all elements in an array are equal
	 * 
	 * @param elements
	 * @return true/false
	 */
	public boolean allElementsSame(ArrayList<Integer> elements) {
		// First element in list is initial value
		Integer first = elements.get(0);
		
		// If any elements are not equal to initial value, return false
		for (Integer elem : elements) {
			if (elem.intValue() != first.intValue()) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * getAttributeValues
	 * 
	 * Get all values of an attribute present in dataset
	 * 
	 * @param data
	 * @param attribute
	 * @return
	 */
	public Set<Integer> getAttributeValues(ArrayList<ArrayList<Integer>> data, Integer attribute) {
		
		// Create set of integers
		Set<Integer> possibleValues = new HashSet<Integer>();
		
		// Add all possible values of target attribute
		for (ArrayList<Integer> datapoint : data) {
			possibleValues.add(datapoint.get(attribute));
		}
		
		// Return set of possible values
		return possibleValues;
	}
	
	/**
	 * chooseSplitAttribute
	 * 
	 * Choose the best attribute upon which to classify
	 * 
	 * @param data
	 * @param targetAttribute
	 * @param attributes
	 * @return
	 */
	public Integer chooseSplitAttribute (ArrayList<ArrayList<Integer>> data,
			Integer targetAttribute,
			ArrayList<Integer> attributes) {
		
		// Initialize best attribute at -1;
		Integer bestSplitAttribute = -1;
		Double maxInformationGain = 0.0;
		
		// Compute gain of all attributes except target attribute
		for (Integer attribute : attributes) {
			
			// Compute gains and compare with existing maximum 
			if (attribute.intValue() != targetAttribute.intValue()) {
				
				// Compute gain from splitting data on this attribute
				Double gain = this.calculateInformationGain(data, attribute, targetAttribute);
				
				// Compare with existing best information gain, updating best split attribute
				// if greater
				if (gain > maxInformationGain) {
					// Update information gain
					maxInformationGain = gain;
					
					// Update best split attribute
					bestSplitAttribute = attribute;
				}
			}
			
		}
		
		return bestSplitAttribute;
		
	}
	
	/**
	 * calculateEntropy
	 * 
	 * Calculates the entropy associated with a given attribute in the dataset
	 * 
	 * @param data
	 * @param targetAttribute
	 * @return
	 */
	public Double calculateEntropy(ArrayList<ArrayList<Integer>> data, Integer targetAttribute) {
		
		// Create map of value frequencies for this attribute
		Map<Integer, Double> valueFrequency = new HashMap<Integer, Double>();
		
		// Initialize entropy at 0
		Double dataEntropy = 0.0;
		
		// Calculate the frequency of values of the target attribute for each data record
		for (ArrayList<Integer> datapoint : data) {
			
			// Get value of target attribute at this datapoint
			Integer targetValue = datapoint.get(targetAttribute);
			
			// If a value for this value exists, increment frequency
			if (valueFrequency.containsKey(targetValue)) {
				valueFrequency.put(targetValue, valueFrequency.get(targetValue) + 1.0);
				
			// Otherwise, create a new entry with a count of 1
			} else {
				valueFrequency.put(targetValue, 1.0);
			}
		}
		
		// Calculate the entropy of the data for the target attribute
		for (Double frequency : valueFrequency.values()) {
			dataEntropy += (-frequency/data.size()) * (Math.log(frequency/data.size()) / Math.log(2));
		}
		
		return dataEntropy;
	}
	
	/**
	 * calculateInformationGain
	 * 
	 * Calculates information gain (decreased entropy) which would result in
	 * splitting the data on the chosen attribute (splitAttribut)
	 *  
	 * @param data
	 * @param splitAttribute
	 * @param targetAttribute
	 * @return
	 */
	public Double calculateInformationGain(ArrayList<ArrayList<Integer>> data,
			Integer splitAttribute, Integer targetAttribute) {
		
		// Initialize value frequency
		Map<Integer, Double> valueFrequency = new HashMap<Integer, Double>();
		
		// Initialize subset entropy
		Double subsetEntropy = 0.0;
		
		// Calculate frequencies values of split attribute
		for (ArrayList<Integer> datapoint : data) {
			
			// Get target value for split attribute from datapoint
			Integer targetValue = datapoint.get(splitAttribute);
			
			// If already existing, increment frequency
			if (valueFrequency.containsKey(targetValue)) {
				
				valueFrequency.put(targetValue, valueFrequency.get(targetValue) + 1.0);
				
			// Otherwise create new entry
			} else {
				valueFrequency.put(targetValue, 1.0);
			}
		
		}

		// Calculate the sum of the entropies for each of the subsets of datapoints,
		// weighted by their probability of occurring in the training data
		for (Integer attributeValue : valueFrequency.keySet()) {
			
			// Calculate probability of this value occurring in the training data
			Double valueProbability = valueFrequency.get(attributeValue) / data.size();
			
			// Create subset of data which only includes records where the split attribute
			// has this attributeValue
			ArrayList<ArrayList<Integer>> subset = 
				this.getDataByAttributeValue(data, splitAttribute, attributeValue);
			
			// Update subset entropy with entropy of this subset relative to the attribute
			// of classification, multiplied by the probability of this value occurring in
			// the training set
			subsetEntropy += valueProbability * this.calculateEntropy(subset, targetAttribute);
			
		}
		
		// Return the difference of the entropy of the whole data set with respect to the 
		// attribute upon which to classify, with the entropy of the split attribute
		return (this.calculateEntropy(data, targetAttribute) - subsetEntropy);
	}
	
	/**
	 * getDataByAttributeValue
	 * 
	 * FIXME Refactor name in the future, not totally clear
	 * Returns subset of data for which a particular attribute has this value
	 * 
	 * @param data
	 * @param attributeId
	 * @param targetValue
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> getDataByAttributeValue (ArrayList<ArrayList<Integer>> data,
			Integer attributeId, Integer targetValue) {
		
		// Initialize list of example values
		ArrayList<ArrayList<Integer>> exampleValues = new ArrayList<ArrayList<Integer>>();
		
		// Add only datapoints for which the value of attributeId is attributeValue
		for (ArrayList<Integer> datapoint : data) {
			if (datapoint.get(attributeId).intValue() == targetValue.intValue()) {
				exampleValues.add(datapoint);
			}
		}
		
		// Return newly created data list
		return exampleValues;
	}
	
	/**
	 * buildDecisionTree
	 * 
	 * Create a decision tree from training dataset.
	 * 
	 * @param data
	 * @param targetAttribute
	 * @return
	 */
	public DecisionTreeNode buildDecisionTree(ArrayList<ArrayList<Integer>> data, 
			int targetAttribute,
			ArrayList<Integer> attributeList) {
		
		// Values of target attribute
		ArrayList<Integer> targetAttributeValues = new ArrayList<Integer>();
		for (ArrayList<Integer> datapoint : data) {
			targetAttributeValues.add(datapoint.get(targetAttribute));
		}
		
		// Compute majority value
		Integer majorityValue = this.majorityValue(targetAttributeValues);
		
		// If there is only one attribute, or no data, return the default value
		if (data.size() == 0 || attributeList.size() <= 1) {
			return new DecisionTreeNode(targetAttribute, majorityValue);
		}
		
		// If all of the target attributes have one value, then return this value
		// as classification
		if (this.allElementsSame(targetAttributeValues)) {
			return new DecisionTreeNode(targetAttribute, majorityValue);
		}
		
		// Construction of decision tree
		else {
		
			// Select best attribute upon which to classify
			Integer best = this.chooseSplitAttribute(data, targetAttribute, attributeList);
			
			// If computation of best attribute fails, return a node which provides majority value
			if (best == -1) {
				return new DecisionTreeNode(targetAttribute, majorityValue); 
			}
			
			// Create list of attributes not including this one
			ArrayList<Integer> otherAttributes = new ArrayList<Integer>();
			for (Integer attribute : attributeList) {
				if (attribute.intValue() != best.intValue()) {
					otherAttributes.add(attribute);
				}
			}
			
			// Create a tree node for this attribute
			DecisionTreeNode bestAttributeNode = new DecisionTreeNode(best);
			
			// Create a subtree for each available value of best attribute
			Set<Integer> attributeValues = this.getAttributeValues(data, best);
			for (Integer dataValue : attributeValues) {
				
				// Only consider datapoints for which the best attribute has this datavalue
				ArrayList<ArrayList<Integer>> subset = 
					this.getDataByAttributeValue(data, best, dataValue);
				
				// Create subtree for each value
				DecisionTreeNode subtree = this.buildDecisionTree(subset, 
						targetAttribute, 
						otherAttributes);
				
				// Add new subtree to our best attribute tree node
				bestAttributeNode.addChildNode(dataValue, subtree);
			}
			
			return bestAttributeNode;
			
		}
	}
	
	/**
	 * Data Classification
	 * 
	 * Methods for classifying a given datapoint with the tree of this Data Vertex
	 */
	
	/**
	 * classifyDatapoint
	 * 
	 * Classify a datapoint with the decision tree
	 * 
	 * @param datapoint
	 */
	public Integer classifyDatapoint (ArrayList<Integer> datapoint) {
		
		// Start at current node
		DecisionTreeNode current = this.rootNode;
		
		// While current node != null
		while (current != null) {
			
			// If the current node is a leaf, we make our decision based on its value
			if (current.isLeaf()) {
				return current.getValue();
			}
			
			// Otherwise, update current to be decision value
			else {
				Integer targetValue = datapoint.get(current.getTreeId());
				current = current.getChildByDecisionValue(targetValue);
			}
		}
		
		return -1;
	}
}

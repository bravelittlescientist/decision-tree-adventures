import java.util.Collection;
import java.util.HashMap;

/**
 * DecisionTreeNode
 * @author genia
 *
 * A node for a decision tree 
 */
public class DecisionTreeNode {
	
	private Integer attributeValue;
	private Integer attributeId;
	private HashMap<Integer, DecisionTreeNode> nodeTree;
	
	/**
	 * DecisionTreeNode
	 * 
	 * Default constructor, creates empty tree
	 * 
	 * @param id
	 */
	public DecisionTreeNode(Integer id) {
		this.attributeId = id;
		this.nodeTree = new HashMap<Integer, DecisionTreeNode>();
		this.attributeValue = -1;
	}
	
	/**
	 * DecisionTreeNode - Leaf
	 * 
	 * Constructor for a tree node with an id and value, but no children
	 * 
	 * @param id
	 * @param value
	 */
	public DecisionTreeNode(Integer id, Integer value) {
		this.attributeId = id;
		this.attributeValue = value;
		this.nodeTree = new HashMap<Integer, DecisionTreeNode>();
	}
	
	/**
	 * getTreeId
	 * 
	 * @return ID of this node
	 */
	public Integer getTreeId () {
		return this.attributeId;
	}
	
	/**
	 * addChildNode
	 * 
	 * Add a child node to this tree
	 * 
	 * @param child
	 */
	public void addChildNode (Integer decision, DecisionTreeNode child) {
		
		if (child != null) {
			this.nodeTree.put(decision, child);
		}
	}
	
	/**
	 * isLeaf
	 * 
	 * Check if node has any children
	 * 
	 * @return true/false
	 */
	public boolean isLeaf() {
		return this.nodeTree.size() == 0 ? true : false;
	}
	
	/**
	 * getValue
	 * 
	 * Return value of this node
	 * 
	 * @return value
	 */
	public Integer getValue() {
		return this.attributeValue;
	}

	/**
	 * getChildren
	 * 
	 * Returns children of this node.
	 * 
	 * @return
	 */
	public Collection<DecisionTreeNode> getChildren() {
		return this.nodeTree.values();
	}
	
	/**
	 * getChildByDecisionValue
	 * 
	 * Given a value of the node's attribute, choose the appropriate path
	 * down the decision tree.
	 * 
	 * @param decision
	 * @return
	 */
	public DecisionTreeNode getChildByDecisionValue (Integer decision) {
		return this.nodeTree.get(decision);
	}
	
	/**
	 * printNode
	 * 
	 * Very basic print function for a node.
	 */
	public void printNode () {
		// Initialize info string with 
		String basicInfo = "Node: " + this.attributeId;
		
		// If it has a value, print that value too
		if (this.attributeValue != -1) {
			basicInfo += ", Value: " + this.attributeValue;
		}
		
		// If no children, this is a leaf
		if (this.nodeTree.size() == 0) {
			basicInfo += ", Leaf";
		}
		
		// Otherwise, print nodeIds of children
		else {
			basicInfo += ", Children:";
			for (DecisionTreeNode child : this.nodeTree.values()) {
				basicInfo += " " + child.getTreeId();
			}
		}
		
		// Print out node
		System.out.println(basicInfo); 
	}
}

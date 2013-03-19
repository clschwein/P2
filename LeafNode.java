/**
 * This class represents a leaf node in the tree that contains a String
 * that represents the DNA sequence (can be null for empty leaf nodes).
 *
 */
public class LeafNode extends DNATreeNode {
	
	/**
	 * String variable that holds the DNA sequence.
	 */
	private String sequence;
	
	/**
	 * Constructor that initializes the given sequence as the sequence
	 * of this node.
	 * 
	 * @param seq - the DNA sequence
	 * @param level - the level of the node
	 */
	public LeafNode(String seq, int level) {
		
		sequence = seq;
		
		setLevel(level);
	}
	
	/**
	 * Returns a String value of the sequence of this leaf node.
	 * 
	 * @return - the DNA sequence
	 */
	public String getSequence() {
		return sequence;
	}
}

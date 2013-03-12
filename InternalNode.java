/**
 * This class represents an internal node in the tree. This node does not store
 * a sequence value, but points to other InternalNode or LeafNode objects.
 * It holds references to five different nodes (one for each letter ACGT and one
 * to signify the end of the sequence, AKA an exact match thus far).
 *
 */
public class InternalNode implements DNATreeNode {
	/**
	 * Five references to the nodes that are held within this InternalNode.
	 * They can be either InternalNodes or LeafNodes.
	 */
	private DNATreeNode a, c, g, t, end;
	
	/**
	 * Constructor that initializes all nodes as LeafNode objects with no
	 * sequence stored.
	 */
	public InternalNode() {
		a = new LeafNode(null);
		c = new LeafNode(null);
		g = new LeafNode(null);
		t = new LeafNode(null);
		end = new LeafNode(null);
	}
	
	/**
	 * Adds a DNATreeNode object to the specified position.
	 * @param node - the DNATreeNode to be added as a child node
	 * @param pos - the position to add it at (a, c, g, t, or e for 'end')
	 */
	public void addNode(DNATreeNode node, char pos) {
		if (pos == 'a') {
			a = node;
		} else if (pos == 'c') {
			c = node;
		} else if (pos == 'g') {
			g = node;
		} else if (pos == 't') {
			t = node;
		} else if (pos == 'e') {
			end = node;
		}
	}
	
	/**
	 * Returns the node at the specified position.
	 * @param pos - character that determines what position to find the node at.
	 * Valid values are a, c, g, t, and e (for end)
	 * @return the DNATreeNode stored at the specified location
	 */
	public DNATreeNode getNode(char pos) {
		if (pos == 'a') {
			return a;
		} else if (pos == 'c') {
			return c;
		} else if (pos == 'g') {
			return g;
		} else if (pos == 't') {
			return t;
		} else if (pos == 'e') {
			return end;
		}
		return null;
	}
}

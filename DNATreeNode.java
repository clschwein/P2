/**
 * This class represents a flyweight node in the tree. This node does not store
 * a sequence value, nor does it point to other InternalNode or LeafNode objects.
 * Instead, it is used to save space when referring to an empty node.
 *
 */
public abstract class DNATreeNode {
	/**
	 * Represents the level of the node.  Used for print string formating.
	 */
	private int level;
	
	/**
	 * Returns the level of the node.
	 * @return - the level of the node
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Sets the level of the node to a new value.
	 * @param l - the new level for the node
	 */
	public void setLevel(int l)
	{
		level = l;
	}
}
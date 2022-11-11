package rs.ac.uns.ftn.informatika.bibliography.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;


/**
 * Abstract class that holds manipulation logic for rich tree object
 * 
 * 
 */
@SuppressWarnings("serial")
public class TreeNodeDTO <TreeNodeObjectType>  extends DefaultTreeNode implements Serializable, TreeNode{

	protected boolean checkbox_state = false;
	public String [] tree_stateType = { "empty", "full", "partial"};
	protected String tree_state = "empty";
	
	protected TreeNodeObjectType element;
	protected String type ="";
	protected boolean expanded = false;
	protected boolean selected = false;
	
	private TreeNodeDTO <TreeNodeObjectType> parent = null;
	private List<TreeNodeDTO <TreeNodeObjectType>> children = new ArrayList<TreeNodeDTO <TreeNodeObjectType>>();
	
	
	public TreeNodeDTO(TreeNodeObjectType element)
	{
		this.element=element;
		type = element.getClass().getSimpleName();
	}
	
	
	/**
	 * @return the element
	 */
	public TreeNodeObjectType getData() {
		return element;
	}

	public void setData(Object element) {
		this.element = (TreeNodeObjectType) element;
	}
	/**
	 * @return the element class type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	public void setTree_state(String treeState) {
		tree_state = treeState;
	}

	public String getTree_state() {
		return tree_state;
	}

	public String[] getTree_stateType() {
		return tree_stateType;
	}

	public void setTree_stateType(String[] treeStateType) {
		tree_stateType = treeStateType;
	}

	public boolean isCheckbox_state() {
		return checkbox_state;
	}

	public void setCheckbox_state(boolean checkbox_state) {
		this.checkbox_state = checkbox_state;
	}
	
	public String toString()
	{
		return this.element.toString();
	}
	
	public boolean equals(Object o)
	{
		return this.element.equals(o);
	}

	/**
	 * @return the expanded
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * @param expanded the expanded to set
	 */
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	
	public boolean addChild(TreeNodeDTO <TreeNodeObjectType> child){
		return children.add(child);
	}

	@Override
	public List<TreeNodeDTO <TreeNodeObjectType>> getChildren()
	{
		return children;
	}

	@Override
	public int getChildCount() {
		 return children.size();
	}

	@Override
	public void setParent(TreeNode treeNode) {
		this.parent = (TreeNodeDTO<TreeNodeObjectType>) treeNode;
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}

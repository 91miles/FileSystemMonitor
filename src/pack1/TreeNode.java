package pack1;

import java.io.File;
import java.util.ArrayList;

public class TreeNode {

  public File file;
  public ArrayList<TreeNode> children;
  public int checkSum;
  public TreeNode(File x) { 
    file = x; 
    checkSum = 0;
    children = new ArrayList<TreeNode>();
  }

  public void addChild(TreeNode node) {
    children.add(node);
  }

  public void updateCheckSum(){
    updateCheckSum(this);
  }

  public void updateCheckSum(TreeNode node) {
    if (node ==null){
      return;
    }
    checkSum += 17 * node.file.hashCode(); 

    for (int i=0; i<node.children.size(); i++){
      TreeNode child = node.children.get(i);
      updateCheckSum(child);
    }
  }

}

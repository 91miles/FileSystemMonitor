package pack1;
import java.io.File;

public class Monitor {

  private TreeNode oldRoot;
  private TreeNode newRoot;

  public Monitor() {
    this.oldRoot = null;
    this.newRoot = null;
  }

  public void init(String pathToFolder) {
    File folder = new File(pathToFolder);
    TreeNode root = new TreeNode(folder);
    walk(root, folder);
    oldRoot = root;
  }

  public void walk(TreeNode root, File folder){
    // System.out.println(folder.getName());
    if (!folder.exists()){
      return;
    }
    File[] listOfFiles = folder.listFiles();
    if (listOfFiles.length ==0 || listOfFiles ==null) {
      return;
    }
    for (int i = 0; i < listOfFiles.length; i++) {
      File file = listOfFiles[i];
      TreeNode node = new TreeNode(file);

      root.addChild(node);
      if (file.isDirectory()){
        walk(node, file);
      }
    }

  }

  public void updateNewTree (String folderPath) {
    File folder = new File(folderPath);
    TreeNode root = new TreeNode(folder);
    walk(root, folder);
    newRoot = root;
  }

  public void updateOldTree (String folderPath) {
    File folder = new File(folderPath);
    TreeNode root = new TreeNode(folder);
    walk(root, folder);
    oldRoot = root;
  }

  public boolean modified () {
    //    newRoot.checkSum = 0;
    //    oldRoot.checkSum = 0;
    //    newRoot.updateCheckSum();
    //    oldRoot.updateCheckSum();
    //    if (newRoot.checkSum == oldRoot.checkSum){
    //      return false;
    //    }else {
    //      return true;
    //    }
    return !compareTree(oldRoot, newRoot);
  }

  // if modified, return false
  public boolean compareTree(TreeNode n1, TreeNode n2) {

    if (n1.file.isDirectory() && n2.file.isDirectory()){
      return compareDirectory(n1, n2);
    }
    else if (n1.file.isFile() && n2.file.isFile()){
      return compareFile(n1, n2);
    }
    return false;
 
  }

  public boolean compareFile(TreeNode n1, TreeNode n2) {
    if (n1.file.getAbsolutePath().equals(n2.file.getAbsolutePath())
        && n1.file.lastModified() == n2.file.lastModified()) {
      return true;
    }else {
      return false;
    }
  }
  
  public boolean compareDirectory(TreeNode n1, TreeNode n2) {
    if( !compareFile(n1, n2)){
      return false;
    }
    if(n1.children.size() < n2.children.size()){
      return false;
    }else if (n1.children.size() > n2.children.size()){
      return false;
    }
    int flag=0;
    for (int i=0; i<n1.children.size(); i++) {
      if (n1.children.get(i).file.isFile() && n2.children.get(i).file.isFile() ) {
        if (compareFile(n1.children.get(i), n2.children.get(i))){
          continue; 
        }else {
          return false;
        }
      }else if (n1.children.get(i).file.isDirectory() && n2.children.get(i).file.isDirectory()){
        if(compareTree(n1.children.get(i), n2.children.get(i))) {
          
        }else{
          flag++;
        }
      }else{
        return false;
      }
    }

    if (flag>0){
      return false;
    }else {
      return true;
    }
    
  }

  public static void main(String[] args) throws InterruptedException {
    String path = System.getProperty("user.home")+"/Desktop/test/";
    System.out.println(System.getProperty("user.home")+"/Desktop/test/");
    Monitor monitor = new Monitor();
    monitor.init(path);
    while (true) {
      System.out.println("Detecting...");
      Thread.sleep(10000);
      monitor.updateNewTree(path);
      if (monitor.modified()) {
        System.out.println("The folder has been modified. ");
      }else {
        System.out.println("Nothing changed. ");
      }
      monitor.updateOldTree(path);
    }

  }
}

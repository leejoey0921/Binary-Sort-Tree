import java.util.ArrayList;

// Binary Search Tree implementation
public class BST {

  protected boolean NOBSTified = false;
  protected boolean OBSTified = false;
  protected int[][] roots, costs;
  protected ArrayList<Node> inorderNodes = new ArrayList<>();
  protected ArrayList<Integer> weights = new ArrayList<>();
  protected Node root;


  // initializer
  public BST() {
   this.root = null;
  }


  // return number of nodes in tree
  public int size() {
    return recurseCount(this.root);
  }


  // insert a new node
  public void insert(String key) {
    Node newNode = new Node(key);

    if (this.root == null) { this.root = newNode; return; }

    Node temp = this.root;
    Node parent;

    while (true) {
      if (key.compareTo(temp.key) > 0) { // insert key > node --> traverse down right
        parent = temp;
        temp = temp.right;
        if (temp == null) {
          parent.right = newNode;
          newNode.parent = parent;
          return;
        }
      }
      else if (key.compareTo(temp.key) < 0) { // insert key < node --> traverse down left
        parent = temp;
        temp = temp.left;
        if (temp == null) {
          parent.left = newNode;
          newNode.parent = parent;
          return;
        }
      }
      else { // insert key == node --> increase node frequency
        temp.frequency++; return;
      }
    }
  }


  // search for a node
  public boolean find(String key) {
    Node temp = this.root;

    while (temp != null) {
      temp.accessCount++;

      if (key.compareTo(temp.key) < 0) {  // insert key > node --> traverse down right
        temp = temp.left;
      }
      else if (key.compareTo(temp.key) > 0) {  // insert key < node --> traverse down left
        temp = temp.right;
      }
      else {  // insert key == node --> found
        return true;
      }
    }
    return false;  // reached end of tree --> not found
  }


  public int sumFreq() {
    return recurseFreq(this.root);
  }


  public int sumProbes() {
    return recurseProbes(this.root);
  }


  public int sumWeightedPath() {
    return recurseSumPath(this.root, 1);
  }


  public void resetCounters() {
    recurseReset(this.root);
  }


  // Set NOBSTified to true.
  public void nobst() {
    this.NOBSTified = true;
    convertNobst();
  }


  // Set OBSTified to true.
  public void obst() {
    this.OBSTified = true;
    convertObst();
  }


  // print all nodes in the tree
  public void print() {
    recursePrint(leftMost(this.root));
  }


  // recursively count all nodes in the tree
  private int recurseCount(Node node) {
    if (node == null) {
      return 0;
    } else {
      return 1 + recurseCount(node.left) + recurseCount(node.right);
    }
  }


  // recursively calculate sum of all node frequencies in a subtree
  private int recurseFreq(Node node) {
    if (node == null) {
      return 0;
    } else {
      return node.frequency + recurseFreq(node.left) + recurseFreq(node.right);
    }
  }


  // recursively calculate sum of all node access counts in a subtree
  private int recurseProbes(Node node) {
    if (node == null) {
      return 0;
    } else {
      return node.accessCount + recurseProbes(node.left) + recurseProbes(node.right);
    }
  }


  // recursively calculate sum of all weighted path lengths in a subtree
  private int recurseSumPath(Node node, int depth) {
    if (node == null) {
      return 0;
    } else {
      return depth * node.frequency + recurseSumPath(node.right, depth+1) + recurseSumPath(node.left, depth+1);
    }
  }


  // recursively traverse and reset all nodes in a subtree
  private void recurseReset(Node node) {
    if (node != null) {
      node.accessCount = 0;
      node.frequency = 0;
      recurseReset(node.left);
      recurseReset(node.right);
    }
  }


  // return the leftmost node in a subtree
  private Node leftMost(Node node) {
    if (node == null) {
      return null;
    }
    Node temp = node;
    while(temp.left != null) {
      temp = temp.left;
    }
    return temp;
  }


  // print inorder, assuming given node is leftmost in the tree
  private void recursePrint(Node node) {
    if (node == null) {
      return;
    }
    Node temp = node;
    while(temp != null) {
      System.out.printf("[%s:%d:%d]%n", temp.key, temp.frequency, temp.accessCount);
      temp = inorderSuccessor(temp);
    }
  }


  // set ArrayLists inorderNodes and weights
  private void setInorder(Node node) {
    if (node == null) { return; }
    Node temp = leftMost(node);
    while(temp != null) {
      inorderNodes.add(temp);
      weights.add(temp.frequency);
      temp = inorderSuccessor(temp);
    }
  }


  // return the inorder successor of a given node
  private Node inorderSuccessor(Node node) {
    if (node == null) {
      return null;
    }
    else if (node.right != null) { // has right child --> leftmost in right subtree
      return leftMost(node.right);
    }
    else { // doesn't have right child --> first right parent
      Node parent = node.parent;
      while (parent != null && node == parent.right) {
        node = parent;
        parent = parent.parent;
      }
      return parent;
    }
  }


  // convert tree to a NOBST
  private void convertNobst() {
    setInorder(root);
    int cap = inorderNodes.size()-1;
    int r = rootNobst(0, cap);
    root = inorderNodes.get(r);
    inorderNodes.get(r).parent = null;

    setNobst(0, cap, r);
  }


  // convert tree to an OBST
  private void convertObst() {
    setInorder(root);

    int n = inorderNodes.size();
    roots = new int[n+2][n+1];
    costs = new int[n+2][n+1];
    int[][] sums = new int[n+2][n+1];

    for (int low = n+1; low >= 1; low--) {
      for (int high = low-1; high <= n; high++) {
        if (low > high) {
          costs[low][high] = 0;
          roots[low][high] = 0;
          sums[low][high] = 0;
        }
        else if (low == high) {
          costs[low][high] = weights.get(low-1);
          roots[low][high] = low;
          sums[low][high] = weights.get(low-1);
        }
        else {
          sums[low][high] = sums[low][high-1] + sums[high][high];

          int min = Integer.MAX_VALUE;

          for (int r = roots[low][high-1]; r <= roots[low+1][high]; r++) {
            int temp = costs[low][r-1] + costs[r+1][high];
            if (temp < min) {
              min = temp;
              roots[low][high] = r;
            }
          }
          costs[low][high] = min + sums[low][high];
        }
      }
    }
    root = setObst(1, n);
    root.parent = null;
  }


  // set nodes as a OBST in a given index range
  private Node setObst(int low, int high) {
    if(low > high) { return null; }

    int r = roots[low][high] - 1;
    Node node = inorderNodes.get(r);

    if(node == null) { return null; }

    node.left = setObst(low, r);
    if (node.left != null) {
      node.left.parent = node;
    }

    node.right = setObst(r+2, high);
    if (node.right != null) { node.right.parent = node; }

    return node;
  }


  // set nodes as a NOBST in a given index range
  private void setNobst(int low, int high, int parent) {
    if(low == high) {
      inorderNodes.get(parent).right = null;
      inorderNodes.get(parent).left = null;
      return;
    }

    if (low <= parent-1) {
      int leftRoot = rootNobst(low, parent - 1);
      inorderNodes.get(leftRoot).parent = inorderNodes.get(parent);
      inorderNodes.get(parent).left = inorderNodes.get(leftRoot);
      setNobst(low, parent-1, leftRoot);
    } else {
      inorderNodes.get(parent).left = null;
    }
    if (high >= parent+1) {
      int rightRoot = rootNobst(parent + 1, high);
      inorderNodes.get(parent).right = inorderNodes.get(rightRoot);
      inorderNodes.get(rightRoot).parent = inorderNodes.get(parent);
      setNobst(parent+1, high, rightRoot);
    } else {
      inorderNodes.get(parent).right = null;
    }
  }

  // return the index of the root in a NOBST for a given range
  private int rootNobst(int low, int high) {
    if (low == high) {
      return low;
    }

    int r = (low+high)/2;
    int diff;

    int leftSum = 0; int rightSum = 0;

    for (int i = low; i < r; i++) {
      leftSum += weights.get(i);
    }
    for (int i = high; i > r; i--) {
      rightSum += weights.get(i);
    }
    while (true) {
      diff = leftSum - rightSum;
      if (leftSum < rightSum) {
        leftSum += weights.get(r);
        r++;
        rightSum -= weights.get(r);
        if (leftSum >= rightSum) {
          if (Math.abs(diff) <= leftSum - rightSum) {
            return r - 1;
          } else {
            return r;
          }
        }
      } else if (leftSum > rightSum) {
        rightSum += weights.get(r);
        r--;
        leftSum -= weights.get(r);
        if (leftSum <= rightSum) {
          if (Math.abs(diff) < rightSum - leftSum) {
            return r + 1;
          } else {
            return r;
          }
        }
      } else { // leftSum == rightSum
        return r;
      }
    }
  }
}



class Node {
  String key;
  Node left, right, parent;  // Node pointers
  protected int accessCount;  //  number of times probed
  protected int frequency;  // frequency of the value in input
  protected int height;  // height of tree rooted at node


  public Node(String key) {
    this.key = key;
    this.left = null;
    this.right = null;
    this.parent = null;
    this.accessCount = 0;
    this.frequency = 1;
    this.height = 1;
  }
}



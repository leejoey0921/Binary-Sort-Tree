// AVL Binary Search Tree

public class AVL extends BST {

  public AVL() { super(); }


  // override BST's insert method
  public void insert(String key) {
    Node newNode = new Node(key);

    if (this.root == null) {
      this.root = newNode; // with default height of 1
      return;
    }

    Node temp = this.root;
    Node parent;

    // insert node
    while (true) {
      if (key.compareTo(temp.key) > 0) {
        parent = temp;
        temp = temp.right;
        if (temp == null) {
          parent.right = newNode;
          newNode.parent = parent;
          break;
        }
      }
      else if (key.compareTo(temp.key) < 0) {
        parent = temp;
        temp = temp.left;
        if (temp == null) {
          parent.left = newNode;
          newNode.parent = parent;
          break;
        }
      }
      else {
        temp.frequency++;
        return;
      }
    }

    // set height
    temp = parent;
    while (temp != null) {
      setHeight(temp);
      temp = temp.parent;
    }

    temp = parent.parent;
    while (temp != null) {
      setHeight(temp);

      int bf = getBF(temp);  // balance factor

      if (bf > 1) {
        if (key.compareTo(temp.left.key) < 0) {
          rotateRight(temp);
        } else {
          rotateLeft(temp.left);
          rotateRight(temp);
        }
      }
      else if (bf < -1) {
        if (key.compareTo(temp.right.key) > 0) {
          rotateLeft(temp);
        } else {
          rotateRight(temp.right);
          rotateLeft(temp);
        }
      }
      temp = temp.parent;
    }
  }


  // make a right rotation
  private void rotateRight(Node node) {
    Node tmp = node.left;

    if (tmp.right != null) {
      node.left = tmp.right;
      node.left.parent = node;
    } else {
      node.left = null;
    }

    if (this.root == node) {
      this.root = tmp;
    }

    tmp.right = node;
    tmp.parent = node.parent;
    if (tmp.parent != null) {
      if (tmp.parent.right == node) {
        tmp.parent.right = tmp;
      } else if (tmp.parent.left == node) {
        tmp.parent.left = tmp;
      }
    }
    node.parent = tmp;

    setHeight(node);
    setHeight(tmp);
  }


  // make a left rotation
  private void rotateLeft(Node node) {
    Node tmp = node.right;

    if (tmp.left != null) {
      node.right = tmp.left;
      node.right.parent = node;
    } else {
      node.right = null;
    }

    if (this.root == node) {
      this.root = tmp;
    }

    tmp.left = node;
    tmp.parent = node.parent;
    if (tmp.parent != null) {
      if (tmp.parent.right == node) {
        tmp.parent.right = tmp;
      } else if (tmp.parent.left == node) {
        tmp.parent.left = tmp;
      }
    }
    node.parent = tmp;

    setHeight(node);
    setHeight(tmp);
  }


  // return the height of a node
  private int height(Node node) {
    if (node == null) {
      return 0;
    } else {
      return node.height;
    }
  }


  // get the balance factor of a node
  private int getBF(Node node) {
    if (node == null) {
      return 0;
    }
    return height(node.left) - height(node.right);
  }


  private int max(int s1, int s2) {
    return Math.max(s1, s2);
  }


  // set the height of a node
  private void setHeight(Node node) {
    node.height = max(height(node.right), height(node.left)) + 1;
  }
}





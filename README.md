# Binary-Sort-Tree
A **Java** implementation of BST, NOBST, OBST, AVL

## 1. BST.java
### 1-1. BST (Binary Sort Tree)
A Basic Binary Sort Tree.  
I assumed that all keys are words(strings), and the node weight is the frequency of the word in the input sequence.  

- `void insert(String key)`   
Inserts a node with the given string as the key. If the value is already in the tree, the node's frequency is incremented by 1.  

- `boolean find(String key)`   
Searches the BST for a node with the given key. Increments the access count of all nodes in the search path by 1. Returns `true` if the node is found, and returns `false` otherwise.  

- `int sumFreq()`  
Returns the sum of the frequency values of all nodes in the tree.  

- `int sumProbes()`  
Returns the sum of the access count values of all nodes in the tree.  

- `int sumWeightedPath()`  
Returns the sum of the weighted path lengths of all nodes in the tree.  

- `int size()`  
Returns the number of nodes in the tree.  

- `void obst()`  
Converts the tree to a OBST.  

- `void nobst()`  
Converts the tree to a NOBST.  

- `boolean OBSTified`  
`true` if the tree has been converted to an OBST. `false` if otherwise.  

- `boolean NOBSTified`  
`true` if the tree has been converted to an NOBST. `false` if otherwise.  

### 1-2. NOBST (Nearly Optimal Binary Sort Tree)  
A Nearly Optimal Binary Sort Tree.  
The root of a NOBST is chosen so that the weight sum difference of its left and right subtrees is minimal.  
The roots of its subtrees is chosen in the same way recursively.  
If there is a tie, the right subtree is made heavier.

### 1-3. OBST (Optimal Binary Sort Tree)
An Optimal Binary Sort Tree.  
The tree is constructed so that the sum of all weighted path lengths is minimal.
<br></br>

## 2. AVL.java
An implementation of an AVL binary sort tree.  
The `AVL` class inherits the `BST` class, and overrides only the `insert()` method. 
- `void insert(String key)`  
Inserts a node with the given string as the key. If the value is already in the tree, the node's frequency is incremented by 1.  
Makes rotations so that the magnitude of the balance factor of all nodes is always less than 2.

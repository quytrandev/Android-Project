package com.quytran.battletext;


/*Code về Prefix Tree (Trie) thuộc quyền sở hữu của msukkari
 được tham khảo tại https://github.com/msukkari/Autocomplete
*/

import java.util.*;

/*
 *  Trie class.  Each node is associated with a prefix of some key
 *  stored in the trie.   (Any string is a prefix of itself.)
 */

public class Trie
{
    private TrieNode root;

    // Empty trie has just a root node.  All the children are null.

    public Trie()
    {
        root = new TrieNode();
    }

    public TrieNode getRoot(){
        return root;
    }


    /*
     * Insert key into the trie.  First, find the longest
     * prefix of a key that is already in the trie (use getPrefixNode() below).
     * Then, add TrieNode(s) such that the key is inserted
     * according to the specification in PDF.
     */
    public void insert(String key)
    {
        //  ADD YOUR CODE BELOW HERE

        // We want to start from the top
        TrieNode cur = root;

        /*
         * For each character in the key, create it's subsequent child in the trie, and
         * then change the pointer to point to that child
         */
        for(int i = 0; i < key.length(); i++){
            cur.createChild(key.charAt(i));
            cur = cur.children[(int)key.charAt(i)];
        }

        /*
         * cur will now point to the last node that represents the key, so we must set its
         * endOfKey field to be true. Check that cur is not null to prevent NullPointer error
         */
        if(cur != null)
            cur.endOfKey = true;
        //  ADD YOUR ABOVE HERE
    }

    // insert each key in the list (keys)

    //load danh sách các key vào Trie (prefix tree)
    //ở đây ta sẽ load các từ (words) vào Trie
    public void loadKeys(ArrayList<String> keys)
    {
        int size=keys.size();
        for (int i = 0; i <size ; i++)
        {
            String key = keys.get(i);
            if(key.length()<2){

            }
            else{
                insert(key);
            }
        }

        return;
    }

    /*
     * Given an input key, return the TrieNode corresponding the longest prefix that is found.
     * If no prefix is found, return the root.
     * In the example in the PDF, running getPrefixNode("any") should return the
     * dashed node under "n", since "an" is the longest prefix of "any" in the trie.
     */
    private TrieNode getPrefixNode(String key)
    {
        //   ADD YOUR CODE BELOW HERE

        // maxNode will point to the deepest node that is a prefix of key
        TrieNode maxNode = root; // initialize to root so we can loop through the entire string


        /*
         * This loop will find the deepest node that is a prefix of the key
         *
         * We will loop through each character in the string. If maxNode contains a child
         * at the ASCII value of the character, we make maxNode point to this child. Else if
         * this condition is not met, we break the loop and return maxNode (which should point
         * to the deepest node which makes a prefix of the key).
         *
         */
        for(int i = 0; i < key.length(); i++){
            if(maxNode.children[(int)key.charAt(i)] != null)
                maxNode = maxNode.children[(int)key.charAt(i)];
            else
                return maxNode;
        }

        return maxNode;
        //
        //   ADD YOUR CODE ABOVE HERE

    }

    /*
     * Similar to getPrefixNode() but now return the prefix as a String, rather than as a TrieNode.
     */

    public String getPrefix(String key)
    {
        return getPrefixNode(key).toString();
    }


    /*
     *  Return true if key is contained in the trie (i.e. it was added by insert), false otherwise.
     *  Hint:  any string is a prefix of itself, so you can use getPrefixNode().
     */
    public boolean contains(String key)
    {
        //   ADD YOUR CODE BELOW HERE

        /*
         * In order for the trie to contain the key, the last node of the prefix must be the end of a key
         */

        // the length of the longest prefix of the key must be the same length of the key for the trie to contain it
        if(getPrefix(key).length() != key.length())
            return false;

        return getPrefixNode(key).endOfKey;
        //   ADD YOUR CODE ABOVE HERE
    }

    /*
     *  Return a list of all keys in the trie that have the given prefix.
     */

    //search các từ bắt đầu bằng giá trị 'prefix' được truyền vào và trả ra một Array list
    // VD: prefix = "th" => stringList =[than, their, the, think,...]
    public ArrayList<String> getAllPrefixMatches( String prefix )
    {
        //  ADD YOUR CODE BELOW
        ArrayList<String> stringList = new ArrayList<String>();

        // we want to start at the last node that represents the prefix passed in
        TrieNode cur = getPrefixNode(prefix);
        String tmp = cur.toString();

        /*
         * the longest prefix of the string being passed in must be equal in length to the string
         *
         *  ex. given a list ["bookkeeper", "bookkeeping"]
         *  if the the prefix passed into this method is "bookks", there should be no autocorrect. Therefore,
         *  we can notice that getPrefix("bookks") = "bookk" != "bookks"
         *
         *  the easiest way to check this, without using the String.equals() method is to check if these two
         *  Strings are equal in length. If they are not equal in length, we return the empty list.
         *
         */
        if(tmp.length() != prefix.length())
            return stringList;

        // if the cur node represents a key, add it since it will not be checked in the recursive method
        if(cur.endOfKey){
            stringList.add(tmp);
        }
        // fill the list using the helper method found below
        stringList.addAll(recursivePrefix(cur));


        //  ADD YOUR CODE ABOVE HERE

        return stringList;
    }

    /* recursively call each node's children, use getUpperString to get all the keys
     *
     * This method contains a for loop which will loop through each of the node's children, if the
     * children is valid (not null) AND is also the end of a key, we add the String onto the list.
     */
    public ArrayList<String> recursivePrefix(TrieNode cur){
        ArrayList<String> list = new ArrayList<String>();

        if(cur == null)
            return list;

        for(TrieNode node : cur.children){
            list.addAll(recursivePrefix(node)); // recursively add all of key of the child's children keys

            if(node != null && node.endOfKey)
                list.add(node.getUpperString(node)); // add the child's keys if they are the end of a key
        }

        return list; // finally, return the list
    }


    /*
     *  A node in a Trie (prefix) tree.
     *  It contains an array of children: one for each possible character.
     *  The ith child of a node corresponds to character (char)i
     *  which is the UNICODE (and ASCII) value of i.
     *  Similarly the index of character c is (int)c.
     *  So children[97] = children[ (int) 'a']  would contain the child for 'a'
     *  since (char)97 == 'a'   and  (int)'a' == 97.
     *
     * References:
     * -For all unicode charactors, see http://unicode.org/charts
     *  in particular, the ascii characters are listed at http://unicode.org/charts/PDF/U0000.pdf
     * -For ascii table, see http://www.asciitable.com/
     * -For basic idea of converting (casting) from one type to another, see
     *  any intro to Java book (index "primitive type conversions"), or google
     *  that phrase.   We will cover casting of reference types when get to the
     *  Object Oriented Design part of this course.
     */

    private class TrieNode
    {
        /*
         *   Highest allowable character index is NUMCHILDREN-1
         *   (assuming one-byte ASCII i.e. "extended ASCII")
         *
         *   NUMCHILDREN is constant (static and final)
         *   To access it, write "TrieNode.NUMCHILDREN"
         */

        public static final int NUMCHILDREN = 256;

        private TrieNode   parent;
        private TrieNode[] children;
        private int        depth;            // 0 for root, 1 for root's children, 2 for their children, etc..
        private char       charInParent;    // Character associated with edge between this node and its parent.
        // See comment above for relationship between an index in 0 to 255 and a char value.
        private boolean endOfKey;   // Set to true if prefix associated with this node is also a key.

        // Constructor for new, empty node with NUMCHILDREN children.  All the children are null.

        public TrieNode()
        {
            children = new TrieNode[NUMCHILDREN];
            endOfKey = false;
            depth = 0;
            charInParent = (char)0;
        }


        /*
         *  Add a child to current node.  The child is associated with the character specified by
         *  the method parameter.  Make sure you set all fields in the child node.
         *
         *  To implement this method, see the comment above the inner class TrieNode declaration.
         */
        public TrieNode createChild(char  c)
        {
            TrieNode child       = new TrieNode();

            // ADD YOUR CODE BELOW HERE

            // get ASCII value for the character
            int index = (int)c;

            // check if the node contains this character
            if(children[index] == null){
                this.children[index] = child; // add child into children array


                // init child fields
                child.charInParent = c; // set passed in character as charInParent
                child.depth = this.depth + 1; // the childs depth is one more than the current depth
                child.parent = this; // this node is the child's parent

                // must be made true in insert method
                child.endOfKey = false;

            }

            // ADD YOUR CODE ABOVE HERE

            return child;
        }

        // Get the child node associated with a given character, i.e. that character is "on"
        // the edge from this node to the child.  The child could be null.

        public TrieNode getChild(char c)
        {
            return children[ c ];
        }

        // Test whether the path from the root to this node is a key in the trie.
        // Return true if it is, false if it is prefix but not a key.

        public boolean isEndOfKey()
        {
            return endOfKey;
        }

        // Set to true for the node associated with the last character of an input word

        public void setEndOfKey(boolean endOfKey)
        {
            this.endOfKey = endOfKey;
        }

        /*
         *  Return the prefix (as a String) associated with this node.  This prefix
         *  is defined by descending from the root to this node.  However, you will
         *  find it is easier to implement by ascending from the node to the root,
         *  composing the prefix string from its last character to its first.
         *
         *  This overrides the default toString() method.
         */

        public String toString()
        {
            // ADD YOUR CODE BELOW HERE

            // call helper method (see below)
            return this.getUpperString(this);
            // ADD YOUR CODE ABOVE HERE
        }

        // Recursive method for getting the String made from a node
        public String getUpperString(TrieNode input){

            // base case: if passed root return an empty string
            if(input.parent == null)
                return "";

            // recursively get the string made by the parent + char from this node
            return getUpperString(input.parent) + input.charInParent;
        }
    }


}
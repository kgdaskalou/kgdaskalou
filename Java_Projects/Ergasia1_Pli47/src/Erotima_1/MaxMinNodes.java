
package Erotima_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MaxMinNodes extends Thread {
    //Number of threads for parallel max min modes degrees
    private static final Integer THREADCOUNT=4;
    //File to be accessed
    private static final String FILE_NAME = "amazon.txt";
    //Path file of file to be accessed
    private static final String PATH_FILE = "./src/Erotima_1/files/";
    //ArrayList to save all min and max node degrees by individual threads
    private static ArrayList<Integer> myNodesMaxMin=new ArrayList<>();
    //Constructors Lowest and Highest index of each accessed ArrayList
    private int lo, hi;
    //Constructors ArrayList where all Node Degrees of the graph will eventually be saved
    private ArrayList<Integer> myArr;
    //Constructor
    public MaxMinNodes(ArrayList<Integer> myArr, int lo, int hi) {
        this.lo = lo;
        this.hi = hi;
        this.myArr = myArr;
    }
    //Runnable for each thread which is executed while the thread is active
    @Override
    public void run() {
        //Simple Algorithm to find min and max degree of nodes in my graph
        int min=myArr.get(lo);
        int max=myArr.get(lo);
        for (int i = lo+1; i < hi; i++) {
            if (min>myArr.get(i)) min=myArr.get(i);
            if (max<myArr.get(i)) max=myArr.get(i);
        }
        //Adds both values in Arraylist that contains all of threads min and max of degrees
        myNodesMaxMin.add(min);
        myNodesMaxMin.add(max);
    }
    //Main
    public static void main(String[] args) throws InterruptedException{
        //Load graph nodes as adjacency list in a hash map where keys are the node to be examined
        //and values is an arraylist with all the nodes the key is connected with
        HashMap<Integer,ArrayList<Integer>> myGraph = loadDataFromFile();
        System.out.println("Loaded " + myGraph.size() + " lines"+"\n");
        ArrayList<Integer> myHashKeysInArrayList=new ArrayList<>();
        ArrayList<Integer> myHashValuesSizeInArrayList=new ArrayList<>();
        //Loads all hash keys (nodes) in an arraylist
        myHashKeysInArrayList=loadMyHashMapKeysToArrayList(myGraph);
        //Loads all the arraylist sizes of the values of the hash map that represent the degree
        //of each node of the graph
        myHashValuesSizeInArrayList=loadMyHashMapValuesSizeToArrayList(myGraph);
        //START THREAD TIMER
        long startTime = System.nanoTime();
        //Run my threads in order to find in parallel the max and min degrees of each subarraylist
        maxAndMin(myHashValuesSizeInArrayList,THREADCOUNT);
        //STOP THREAD TIMER
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in Java Threads in millis: "
                + elapsedTime/1000000);
        //Simple algorithm to find min and max degree that was saved by my threads in my arraylist
        int myMax=myNodesMaxMin.get(0);
        int myMin=myNodesMaxMin.get(0);
        for (int i=1;i<myNodesMaxMin.size();i++){
            if (myMin>myNodesMaxMin.get(i)) myMin=myNodesMaxMin.get(i);
            if (myMax<myNodesMaxMin.get(i)) myMax=myNodesMaxMin.get(i);
         }
        System.out.println("max:"+myMax+"\n");
        System.out.println("min:"+myMin+"\n");
    }
    //Thread
    public static void maxAndMin(ArrayList<Integer> arr,Integer threadCount) throws InterruptedException {
        int len = arr.size();
        //Create and start THREADCOUNT threads (number of threads that was defined in final values)
        MaxMinNodes[] ts = new MaxMinNodes[threadCount];
        for (int i = 0; i < threadCount; i++) {
            ts[i] = new MaxMinNodes(arr, (((i)* len) / threadCount), (((i + 1) * len) / threadCount));
            ts[i].start();
        }
        //Wait for the threads to finish and sum their results.
        for (int i = 0; i < threadCount; i++) {
            ts[i].join();
        }
    }
    //Loads data from my target file that contains the graph to be examined
    static HashMap<Integer, ArrayList<Integer>> loadDataFromFile() {
        System.out.println("Loading " + FILE_NAME);
        HashMap<Integer,ArrayList<Integer>> hashGraph=new HashMap<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(PATH_FILE+FILE_NAME));
            String inputLine;
            Integer firstNode;
            Integer secondNode;
            //Reads file line by line until no lines are available in the target file
            while ((inputLine = in.readLine()) != null) {
                firstNode=Integer.valueOf(inputLine.substring(0,inputLine.indexOf("\t")));
                secondNode=Integer.valueOf(inputLine.substring(inputLine.indexOf("\t")+1));
                ArrayList<Integer> arr1=new ArrayList<Integer>();
                ArrayList<Integer> arr2=new ArrayList<Integer>();
                //In every line the first node (that represents a key in my hash) has an arraylist assigned (represents every node that
                //is connected in that node-key). We load them in the hash and vice versa ( eg 1 "\t" 2 means key=1->{2} and key=2->{1})
                //If there is a new node it is added to the keys arraylist of Nodes
                arr1= loadIntoMyHashMapTheArrayList(firstNode,secondNode,hashGraph,arr1);
                arr2= loadIntoMyHashMapTheArrayList(secondNode,firstNode,hashGraph,arr2);
                //We put the arraylist that was produced above in the hashmap. If the arraylist has a new node, put replaces the arr with
                //the new that has the newly added nodes
                hashGraph.put(firstNode,arr1);
                hashGraph.put(secondNode,arr2);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashGraph;
    }
    //simple algorithm to load arraylist of values in key-nodes of the hashmap with new nodes
    static ArrayList<Integer> loadIntoMyHashMapTheArrayList(Integer oneNode, Integer twoNode, HashMap<Integer,ArrayList<Integer>> myHash, ArrayList<Integer> myArr){
        //If my node exists in the keys of the hash i get the arraylist that is the value of the key-node and contains the values arraylist
        //with the connected nodes and i add the new connected node. If it does not exist in the keys i produse the first node that is connected with the key node.
        if (myHash.get(oneNode)!=null) {
            myArr=myHash.get(oneNode);
            myArr.add(twoNode);
        } else {
            myArr.add(twoNode);
        }
        return myArr;
    }
    //Simple algorithm to load hash keys in an arraylist (it contains all the nodes of the graph)
    static ArrayList<Integer> loadMyHashMapKeysToArrayList(HashMap<Integer,ArrayList<Integer>> myHash){
        ArrayList<Integer> myArr=new ArrayList<>();
        for (Integer i:myHash.keySet()) {
            myArr.add(i);
        }
        return myArr;
    }
    //Simple algorithm to load all arraylist sizes in my hashmap values (represent degree of each node in my graph) into an arraylist
    static ArrayList<Integer> loadMyHashMapValuesSizeToArrayList(HashMap<Integer,ArrayList<Integer>> myHash){
        ArrayList<Integer> myArr=new ArrayList<>();
        for (ArrayList<Integer> arr:myHash.values()) {
            myArr.add(arr.size());
        }
        return myArr;
    }
}

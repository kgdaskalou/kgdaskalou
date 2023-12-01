package Erotima_2.withMerge;

import java.util.Arrays;
import java.util.Random;

public class SortMyArr extends Thread{
    //Number of threads for parallel sorting
    public static final Integer NUMBER_OF_THREADS=8;
    //My generated arrays size
    private static final Integer SIZE_OF_ARRAY=1048576;
    //My array to be generated and examined
    private int[] myArr;
    //My subarrays lowest and highest index
    private int lo,hi;
    //Constructor
    public SortMyArr(int[] myArr,int lo,int hi) {
        this.myArr=myArr;
        this.lo=lo;
        this.hi=hi;
    }
    //Runnable for each thread which is executed while the thread is active (using native sorting method of Array)
    public void run() {
        Arrays.sort(myArr,lo,hi);
    }
    //Main
    public static void main(String[] args) throws InterruptedException{
        //Generate array
        int[] nonsortedArr=GenerateArray(SIZE_OF_ARRAY);
        /**Used to print generated array
         for (int i:nonsortedArr) {
         System.out.print(i+"/");
         }*/
        //START THREAD TIMER
        long startTime = System.nanoTime();
        //Produce with my threads sorted subarrays
        int[] partiallysortedArr=sortMyThread(nonsortedArr,NUMBER_OF_THREADS);
        /**Used to print array after being joined with sorted subarrays
         for (int i:partiallysortedArr) {
         System.out.print(i+"_");
         }*/
        //Multithreading merging algorithm (MergeSortMultiThreaded) for sorted subarrays
        MergeSortMultiThreaded mergeMyStuff=new MergeSortMultiThreaded();
        mergeMyStuff.recursiveMerging(partiallysortedArr, 0, partiallysortedArr.length - 1,NUMBER_OF_THREADS);
        MergeSortMultiThreaded.shutMeDown();
        /**STOP THREAD TIMER*/
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in Java Threads in millis: "
                + elapsedTime/1000000);
        /**Used to print final sorted array
         for (int i:partiallysortedArr) {
         System.out.print(i+"/");
         }*/
    }
    //Thread
    public static int[] sortMyThread(int[] arr, Integer threadCount) throws InterruptedException {
        int len = arr.length;
        // Create and start threadCount threads.
        SortMyArr[] threads = new SortMyArr[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new SortMyArr(arr, (((i)* len) / threadCount), (((i + 1) * len) / threadCount));
            threads[i].start();
        }
        // Wait for the threads to finish and sum their results.
        for (Thread thread:threads) {
            thread.join();
        }
        return arr;
    }
    //Simple algorithm to create random values array-fixed final size
    static int[] GenerateArray(Integer size){
        int[] myArr=new int[size];
        Random rd=new Random();
        for (int i=0;i<size;i++){
            myArr[i]=rd.nextInt(0,size);
        }
        return myArr;
    }
}
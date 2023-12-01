package Erotima_2.noMerge;

import java.util.Arrays;
import java.util.Random;

public class SortMyArr extends Thread{
    //Number of threads for parallel sorting
    private static final Integer NUMBER_OF_THREADS=8;
    //Used in a recursive merging algorithm as a test base case (based on mergesort algorithm)
    private static final Integer split=NUMBER_OF_THREADS/2;
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
         * for (int i:nonsortedArr) {
            System.out.print(i+"/");
        }*/
        //START THREAD TIMER
        long startTime = System.nanoTime();
        //Produce with my threads sorted subarrays
        int[] partiallysortedArr=sortMyThread(nonsortedArr,NUMBER_OF_THREADS);
        //STOP THREAD TIMER
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in Java Threads in millis: "
                + elapsedTime/1000000);
        /**
         * Used to print array after being joined with sorted subarrays
        for (int i:partiallysortedArr) {
            System.out.print(i+"_");
        }*/
        //Simple merging algorithm for sorted subarrays
        recursiveMerging(partiallysortedArr,0,partiallysortedArr.length-1,NUMBER_OF_THREADS);
        /**Used to print final sorted array
        for (int i:partiallysortedArr) {
            System.out.print(i+"/");
        }*/
    }
    //Thread
    public static int[] sortMyThread(int[] arr, Integer threadCount) throws InterruptedException {
        int len = arr.length;
        //Create and start threadCount threads.
        SortMyArr[] threads = new SortMyArr[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new SortMyArr(arr, ((i*len)/threadCount), (((i+1)*len)/threadCount));
            threads[i].start();
        }
        //Wait for the threads to finish and sum their results.
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
    //splits array, sorts subarrays and merges subarrays into sorted array
    //(based on the mergesort algorithm of book: DEITTEL Java 10th edition)
    private static void recursiveMerging(int[] data, int low, int high,int thread)
    {
        //test base case; until leafs created in recursive are equal with number of subarrays created by threads
        if (split>0){
            if ((thread-1)/split>0) {
                thread--;
                int middle1 = (low + high) / 2; // calculate middle of array
                int middle2 = middle1 + 1; // calculate next element over

                //Outputs split step
                /**System.out.printf("split:   %s%n",
                 subarrayString(data, low, high));
                 System.out.printf("         %s%n",
                 subarrayString(data, low, middle1));
                 System.out.printf("         %s%n%n",
                 subarrayString(data, middle2, high));*/

                //split array in half; sort each half (recursive calls)*/
                recursiveMerging(data, low, middle1,thread); // first half of array
                recursiveMerging(data, middle2, high,thread); // second half of array

                // merge two sorted arrays after split calls return
                mergeSortedSubArrays (data, low, middle1, middle2, high);
            }// end if
        }
    } // end method recursiveMerging
    //Merge sorted subarrays
    private static void mergeSortedSubArrays(int[] data, int left, int middle1,int middle2, int right)
    {
        int leftIndex = left; // index into left subarray
        int rightIndex = middle2; // index into right subarray
        int combinedIndex = left; // index into temporary working array
        int[] combined = new int[data.length]; // working array

        //output two subarrays before merging
        /**System.out.printf("merge:   %s%n",
                subarrayString(data, left, middle1));
        System.out.printf("         %s%n",
                subarrayString(data, middle2, right));*/

        // merge arrays until reaching end of either
        while (leftIndex <= middle1 && rightIndex <= right)
        {
            // place smaller of two current elements into result
            // and move to next space in arrays
            if (data[leftIndex] <= data[rightIndex])
                combined[combinedIndex++] = data[leftIndex++];
            else
                combined[combinedIndex++] = data[rightIndex++];
        }

        // if left array is empty
        if (leftIndex == middle2)
            // copy in rest of right array
            while (rightIndex <= right)
                combined[combinedIndex++] = data[rightIndex++];
        else // right array is empty
            // copy in rest of left array
            while (leftIndex <= middle1)
                combined[combinedIndex++] = data[leftIndex++];

        // copy values back into original array
        for (int i = left; i <= right; i++)
            data[i] = combined[i];

        // output merged array
        /**System.out.printf("         %s%n%n",
                subarrayString(data, left, right));*/
    } // end method merge
    //Used to output subarrays if selection of code in line 145 to 146 is used
    private static String subarrayString(int[] data, int low, int high)
    {
        StringBuilder temporary = new StringBuilder();

        // output spaces for alignment
        for (int i = 0; i < low; i++)
            temporary.append("   ");

        // output elements left in array
        for (int i = low; i <= high; i++)
            temporary.append(" " + data[i]);

        return temporary.toString();
    }
}
package Erotima_2.withMerge;
import java.util.concurrent.*;
public class MergeSortMultiThreaded {
    //Using cachedthreadpool for future threads that are created in recursive multithreading merging
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    //Used in a recursive merging algorithm as a test base case (based on mergesort algorithm)
    private static int split= SortMyArr.NUMBER_OF_THREADS/2;
    //splits array, sorts subarrays and merges subarrays into sorted array
    //(based on the mergesort algorithm of book: DEITTEL Java 10th edition)
    // Runnable class representing a sorting task for a subarray
    static class SortTask implements Runnable {
        private int[] arr;
        private int low, high;
        private int thread;
        //Constructor
        SortTask(int[] arr, int low, int high,int thread) {
            this.arr = arr;
            this.low = low;
            this.high = high;
            this.thread=thread;
        }
        //Runnable for each thread which is executed while the thread is active (using merging method recursiveMerging)
        @Override
        public void run() {
            // Merge the subarray using mergesort based algorithm with threadpool
            recursiveMerging(arr, low, high,thread);
        }
    }
    public static void recursiveMerging(int[] data, int low, int high, int thread) {
        //test base case; until leafs created in recursive are equal with number of subarrays created by threads
        if (split>0){
            if ((thread-1)/split>0) {
                thread--;
                int middle1 = (low + high) / 2; // calculate middle of array
                int middle2 = middle1 + 1; // calculate next element over

                // output split step
                /**System.out.printf("split:   %s%n",
                 subarrayString(data, low, high));
                 System.out.printf("         %s%n",
                 subarrayString(data, low, middle1));
                 System.out.printf("         %s%n%n",
                 subarrayString(data, middle2, high));*/

                // Submit left and right subarray sorting tasks to the thread pool
                Future<?> leftSubArray = threadPool.submit(new SortTask(data, low, middle1,thread));
                Future<?> rightSubArray = threadPool.submit(new SortTask(data, middle2, high,thread));
                try {
                    // Wait for my tasks to complete
                    leftSubArray.get();
                    rightSubArray.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                // merge two sorted arrays after split calls return
                merge(data, low, middle1,middle2, high);
            }// end if
        }
    }// end method recursiveMerging
    //Merge sorted subarrays
    private static void merge(int[] data, int left, int middle1, int middle2, int right) {
        int leftIndex = left; // index into left subarray
        int rightIndex = middle2; // index into right subarray
        int combinedIndex = left; // index into temporary working array
        int[] combined = new int[data.length]; // working array

        // output two subarrays before merging
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
    }
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
    public static void shutMeDown(){
        threadPool.shutdown();
    }
}
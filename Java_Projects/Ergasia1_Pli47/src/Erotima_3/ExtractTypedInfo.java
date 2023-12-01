package Erotima_3;

import java.io.*;
import java.util.ArrayList;

public class ExtractTypedInfo extends Thread{
    //Path of target files
    private static final String PATH_FILE = "./src/Erotima_3/files";
    //Path of output files
    private static final String PATH_FILE_OUTPUT = "./src/Erotima_3/output";
    //Target phrase
    private static final String TARGET_PHRASE="http://www.gutenberg.org";
    private static final File folder = new File(PATH_FILE);
    //Number of used threads
    private static final Integer THREADS=8;
    //ArrayList with the title of the books in the files
    private static ArrayList<String> arrayListWithBooks=new ArrayList<>();
    //Arraylist with the number of lines from every book
    private static ArrayList<Integer> arrayListWithBookSize=new ArrayList<>();
    //ArrayList that holds all the lines from the books
    private static ArrayList<String> arrayListOfLines=new ArrayList<>();
    //Number of books in the file folder
    private static Integer numberOfBooks;
    //Array[][] that holds all lines by book
    private static String[][] arrayWithLinesbyBook;
    //Array that holds the lines, that contained the target phrase , by book
    private static String[][] result;
    //Target phrase
    private static String wordToFind;
    //An integer that is unique for every individual book
    private static Integer book;
    //Lower , higher index in book lines
    private  Integer loIndex,hiIndex;
    //Constructor
    public ExtractTypedInfo(String[][] arrayWithLinesbyBook,String wordToFind,Integer loIndex,Integer hiIndex) {
        this.arrayWithLinesbyBook=arrayWithLinesbyBook;
        this.wordToFind=wordToFind;
        this.loIndex=loIndex;
        this.hiIndex=hiIndex;
    }
    //Runnable for each thread which is executed while the thread is active in order to find target phrase in parts (threads) of the book
    public void run() {
        for (int i=loIndex;i<hiIndex;i++){
            if (arrayWithLinesbyBook[book][i].contains(wordToFind)) {
                /**Outputs found lines from every book and every thread with the target phrase
                System.out.println(arrayWithLinesbyBook[book][i]+" "+arrayListWithBooks.get(book)+" at line number: "+(i+1));*/
                String s=arrayWithLinesbyBook[book][i]+" "+arrayListWithBooks.get(book)+" at line number: "+(i+1);
                if (arrayWithLinesbyBook[book][i]!=null) result[book][i]=s;
            }
        }
    }
    //Main
    public static void main(String[] args) throws InterruptedException{
        //LIST FILES OF FOLDER AND LOAD IN ARRAYLIST
        listFilesForFolder(folder);
        //Loads all lines from all files in folder in an arraylist
        loadLinesFromFilesinArrayList();
        /**Outputs sizes of books
        for (Integer i:arrayListWithBookSize) {
            System.out.println("size of book number : "+(arrayListWithBookSize.indexOf(i)+1)+" is "+arrayListWithBookSize.get(arrayListWithBookSize.indexOf(i)) );
        }*/
        //Holds the number of all books in the folder
        numberOfBooks=arrayListWithBookSize.size();
        arrayWithLinesbyBook=new String[numberOfBooks][];
        //Create result[][] array to hold the results by book
        result=new String[numberOfBooks][];
        for(int i=0;i<result.length;i++){
            result[i]=new String[arrayListWithBookSize.get(i)];
        }
        //creates array to hold lines by book
        createArray();
        //fills array[][] with the lines that are contained in every book (loaded from arrayListOfLines), by book
        fillArrayWithBooks();
        //Clears arrayListOfLines to free up memory
        clearArraylists();
        /**Checks start and end of its books line saved in order to know if we successfully loaded them.
        checkMyBooksStartAndEnd();*/
        //first book on list-folder
        book=0;
        //START THREAD TIMER
        long startTime = System.nanoTime();
        //Finds target phrase in every books lines added in array[][]
        for(int i=0;i<arrayWithLinesbyBook.length;i++){
            findMyWord(arrayWithLinesbyBook,TARGET_PHRASE,THREADS);
            book++;
        }
        //STOP THREAD TIMER
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Total execution time in Java Threads in millis: "
                + elapsedTime/1000000);
        /**Outputs the target phrases, in what book and what line they where found
        for (int i=0;i< result.length;i++){
            for(int j=0;j<result[i].length;j++){
                if (result[i][j]!=null) System.out.println(result[i][j]);
            }
        }*/
        //Creates the output file and folder
        createFile();
        //Writes results on the output file
        writeToFile();

    }
    public static void findMyWord(String[][] arr,String word,Integer threadCount) throws InterruptedException {
        ExtractTypedInfo[] threads = new ExtractTypedInfo[threadCount];
        int len = arrayWithLinesbyBook[book].length;
        //Create and start threadcount threads.
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new ExtractTypedInfo(arr,word,((i* len) / threadCount), (((i + 1) * len) / threadCount));
            threads[i].start();
            System.out.println("Thread N. "+i+" started, checking book "+arrayListWithBooks.get(book)+" from lines "+((i* len) / threadCount)+" to "+(((i + 1) * len) / threadCount)+".");
        }
        // Wait for the threads to finish and sum their results.
        for (Thread thread:threads) {
            thread.join();
        }
    }

    //LIST AND LOAD ALL MY FILES FROM MY FOLDER INTO AN ARRAYLIST
    public static void listFilesForFolder(final File folder) {
        System.out.println("The files in my folder are:");
        int i=1;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(i+". "+fileEntry.getName());
                i++;
                arrayListWithBooks.add(fileEntry.getName());
            }
        }
    }
    //Loads all lines from all books consecutively in an arraylist
    static void loadLinesFromFilesinArrayList() {
        for (String filename:arrayListWithBooks){
            System.out.println("Loading in an ArrayList lines from file: " + filename);
            try {
                BufferedReader in = new BufferedReader(new FileReader(PATH_FILE+"/"+filename));
                String inputLine;
                int i=0;
                while ((inputLine = in.readLine()) != null) {
                    arrayListOfLines.add(inputLine);
                    i++;
                }
                arrayListWithBookSize.add(i);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //Creates the array[][] that will hold each book lines by book
    static void createArray(){
        for(int i=0;i<arrayListWithBookSize.size();i++){
            arrayWithLinesbyBook[i]=new String[arrayListWithBookSize.get(i)];
        }
        System.out.println("Array to hold the books has been created.");
    }
    //Fills the array[][] with the lines from each book
    static void fillArrayWithBooks(){
        int sumOfLines=0;
        for(int i=0;i<arrayWithLinesbyBook.length;i++){
            for (int j=0;j<arrayWithLinesbyBook[i].length;j++){
                arrayWithLinesbyBook[i][j]=arrayListOfLines.get(sumOfLines);
                sumOfLines=sumOfLines+1;
            }
        }
    }
    //Clears arraylist that holded the lines in order to minimize ram usage
    static void clearArraylists(){
        arrayListOfLines.clear();
        System.out.println("\n* The ArrayList with name: arrayListOfLines, was succesfully cleared in order to free up memory space.\n");
    }
    /**Checks the first line and last line of each book in my array[][]
    static void checkMyBooksStartAndEnd(){
        for (int i=0;i<arrayWithLinesbyBook.length;i++){
            System.out.println(arrayWithLinesbyBook[i][0]);
            System.out.println(arrayWithLinesbyBook[i][arrayWithLinesbyBook[i].length-1]);
        }
    }*/
    //Creates the file in the specified output folder that will hold the output file
    public static void createFile() {
        try {
            File myObj = new File(PATH_FILE_OUTPUT+"/"+"output.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                myObj.delete();
                System.out.println("File already exists. It has been deleted");
                new File(PATH_FILE_OUTPUT+"/"+"output.txt");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    //Writes the target phrase and all the line it was found in as well as the book, and the line it was found consecutively
    public static void writeToFile() {
        try {
            FileWriter myWriter = new FileWriter(PATH_FILE_OUTPUT+"/"+"output.txt");
            for (int i=0;i<result.length;i++) {
                for (int j=0;j<result[i].length;j++) {
                    if (result[i][j]!=null) myWriter.write(result[i][j]+"\n");
                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}


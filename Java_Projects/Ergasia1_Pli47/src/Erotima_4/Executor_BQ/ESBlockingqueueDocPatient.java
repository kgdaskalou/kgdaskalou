package Erotima_4.Executor_BQ;


import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class ESBlockingqueueDocPatient {
    //Doctors queue size
    private final static Integer MY_QUEUE_SIZE=10;
    //Max number of patients to try to enter in the doctor lobby
    private static final int MAX_PATIENTS = 20;
    //Thread that represents the Doctor that examines
    private static class Doctor extends Thread {
        //Private final parameters of Doctor
        private final LobbyQueue queue;
        private final String name;
        //Constructor
        public Doctor(LobbyQueue queue, String name) {
            this.queue = queue;
            this.name = name;
        }
        //Runnable of Doctor Thread
        public void run() {
            while (true) {
                try {
                    //Mentions when Dr House starts to examine patient
                    System.out.println("Dr."+name + " started examining " +queue.get() + " at the examine room.");
                    //Thread Dr sleeps (examines patient) for 5 seconds
                    Doctor.sleep(5000);
                    //End of examination
                    System.out.println("Dr."+name + " has finished examining " +queue.getname() + ".");
                    //Outputs how many patients are in the lobby
                    System.out.println("There are "+queue.getsize()+" waiting in the examine room");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //Thread that represents the Patient
    public static class Patient extends Thread {
        //Private parameters
        private final LobbyQueue queue;
        private final String name;
        //Constructor
        public Patient(LobbyQueue queue, String name) {
            this.queue = queue;
            this.name = name;
        }
        //Runnable of Patient Thread
        public void run() {
            for (int i = 0; i < MAX_PATIENTS; i++) {
                Random random=new Random();
                try {
                    //Random time from 0 seconds to 3 seconds for every patient to arrive in the lobby of the Doctor
                    Patient.sleep(random.nextInt(3000));
                    //If lobby has empty spots the patient enters
                    if (queue.getsize()<10) {
                        queue.put(name + " number: " + i);
                    } else System.out.println(name +" number: " + i+ " has left the Lobby."); //else he leaves the Doctors office
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Blockingqueue (Doctors Lobby)
    public static class LobbyQueue {
        //Private parameters
        private final int bufferSize;
        private final BlockingQueue<String> buffer;
        private String s;
        //Constructor
        public LobbyQueue(int bufferSize) {
            if (bufferSize <= 0) {
                throw new IllegalArgumentException("Size is illegal.");
            }
            this.bufferSize = bufferSize;
            this.buffer = new LinkedBlockingQueue<>(bufferSize);
        }
        //LobbyQueue: puts patient in Lobby
        public void put(String s) {
            try {
                buffer.put(s);
                this.s=s;
                System.out.println("At the Doctor's Lobby patient " + s + " "+ "has entered.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //LobbyQueue: gets patient from Lobby in Examine room
        public String get() {
            try {
                return buffer.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }
        //Returns size of patients in Lobby
        public int getsize() {
            return buffer.size();
        }
        //Returns name of Patient
        public String getname(){
            return s;
        }
    }
    //Main
    public static void main(String[] args) {
        //new Lobby
        LobbyQueue q = new LobbyQueue(MY_QUEUE_SIZE);
        //New Executor Service
        ExecutorService es = Executors.newCachedThreadPool();
        //Executes patient thread
        es.execute(new Patient(q, "Patient"));
        //Executes doctor thread
        es.execute(new Doctor(q, "House"));
        //Shutsdown executor service
        es.shutdown();
    }
}
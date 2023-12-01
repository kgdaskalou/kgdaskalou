package Erotima_4.SemaphoreDrPat;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class SemaphoreDocPatient {
    //Doctors queue size
    private static final int BUFFER_SIZE = 10;
    //Max number of patients to try to enter in the doctor lobby
    private static final int MAX_PATIENTS = 20;
    //Arraylist to be used as lobby
    private final ArrayList<Integer> buffer = new ArrayList<>();
    //Semaphore used to count empty seats
    private final Semaphore lobbyEnter = new Semaphore(BUFFER_SIZE);
    //Semaphore used to mark Doctor access
    private final Semaphore doctorEnter = new Semaphore(0);
    //Random to be used for random access time of patients
    private final Random random = new Random();
    //Runnable that represents the Patient that goes to the Doctors office
    private class Patient implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < MAX_PATIENTS; i++) {
                //random time that patients arrive at lobby
                try {
                    Thread.sleep(random.nextInt(4000));
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                //if patient can aqcuire a semaphore-open seat patient enters
                if (lobbyEnter.tryAcquire()){
                    Integer name=i;
                    buffer.add(name);
                    System.out.println("Patient n. "+name+" has entered the lobby. The lobby has "+(BUFFER_SIZE-lobbyEnter.availablePermits())+" patients waiting.");
                } else {
                    System.out.println("Patient n. "+i+" has left the Doctors office.");//else leaves
                }
                doctorEnter.release();//Doctor is permitted to accept patients
            }
        }
    }
    //Runnable that represents the Doctor that examines
    private class Doctor implements Runnable {
        //parameters
        private String name;
        //Constructor
        public Doctor(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            doctorEnter.acquireUninterruptibly(); //Blocked until a release happens (doctorEnter.release()) - Starts to examine if there is a patient in the lobby
            while (buffer.size()!=0) {
                //Starts doctor examinations
                try {
                    System.out.println(name + " started examining patient  n. " + buffer.get(0));
                    //releases a semaphore-seat in the lobby
                    lobbyEnter.release();
                    //duration of examination
                    Thread.sleep(5000);
                    if (buffer.size()>0) doctorEnter.release();//The doctor when finishes examination receives next patient if there is any
                    System.out.println(name + " has examined patient  n. " + buffer.remove(0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //Main
    public static void main(String[] args) {
        //New class
        SemaphoreDocPatient obj = new SemaphoreDocPatient();
        //New Patient
        Patient myPatient = obj.new Patient();
        Thread patientThread = new Thread(myPatient);
        //New Doctor
        Doctor myDoctor = obj.new Doctor("Dr. House");
        Thread doctorThread = new Thread(myDoctor);
        //Starting the threads
        patientThread.start();
        doctorThread.start();
    }
}

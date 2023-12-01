import java.util.Random;

public class Student extends Human implements IGrade{
    private final String email;
    private final String department;
    private float myAverage;
    //CONSTRUCTOR
    public Student(String amka, String fullName, String email,String department){
        super(amka,fullName);
        this.email=email;
        this.department=department;
        //ΥΠΟΛΟΓΙΣΜΟΣ ΜΕΣΟΥ ΟΡΟΥ ΚΑΤΑ ΤΗ ΔΗΜΙΟΥΡΓΙΑ ΤΟΥ ΑΝΤΙΚΕΙΜΕΝΟΥ
        calculateAverage();
        showAverage();
    }
    //ΕΠΙΣΤΡΕΦΕΙ ΤΟΝ ΜΕΣΟ ΟΡΟ
    @Override
    public float showAverage() {
        return myAverage;
    }
    //ΥΠΟΛΟΓΙΣΜΟΣ ΜΕΣΟΥ ΟΡΟΥ-ΤΥΧΑΙΟΣ ΜΕΤΑΞΥ 0 ΚΑΙ 10
    private float calculateAverage(){
        Random r=new Random();
        myAverage=r.nextFloat(0,10);
        return myAverage;
    }
}

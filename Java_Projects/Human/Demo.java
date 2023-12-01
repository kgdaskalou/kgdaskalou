
import java.util.ArrayList;
import java.util.List;
public class Demo {
    static List<IGrade> gradedStudents = new ArrayList<>();
    static List<IGrade> studentsWhoPass = new ArrayList<>();
    static List<IGrade> studentsWhoFail = new ArrayList<>();

    public static void main(String[] args) {
        // Προσθέστε κώδικα εδώ
        //ΔΗΜΙΟΥΡΓΙΑ ΦΟΙΤΗΤΩΝ ΜΕ ΤΗ ΒΟΗΘΕΙΑ ΤΗΣ ADDLIST
        addToList(new Student("12","K KK","12@eap.gr","dep1"));
        addToList(new Student("34","L LL","34@eap.gr","dep2"));
        addToList(new Student("56","M MM","56@eap.gr","dep3"));
        addToList(new Student("78","N NN","78@eap.gr","dep4"));
        addToList(new Student("910","O OO","910@eap.gr","dep5"));
        //ΦΙΛΤΡΑΡΙΣΜΑ ΤΩΝ ΒΑΘΜΟΛΟΓΙΩΝ
        filterList(gradedStudents);
        //ΕΚΤΥΠΩΣΗ ΒΑΘΜΩΝ ΦΟΙΤΗΤΩΝ
        System.out.println("Students grades:");
            printMyList(gradedStudents);
        System.out.println("\n Students who pass grades:");
        //ΕΚΤΥΠΩΣΗ ΒΑΘΜΩΝ ΦΟΙΤΗΤΩΝ ΠΟΥ ΠΕΡΑΣΑΝ
            printMyList(studentsWhoPass);
        //ΕΚΤΥΠΩΣΗ ΒΑΘΜΩΝ ΦΟΙΤΗΤΩΝ ΠΟΥ ΔΕΝ ΠΕΡΑΣΑΝ
        System.out.println("\nStudents who failed grades:");
            printMyList(studentsWhoFail);
    }

    static void addToList(IGrade graded) {
        // Προσθέστε κώδικα εδώ
        gradedStudents.add(graded);
    }

    static void filterList(List<IGrade> gradedStudents) {
        // Προσθέστε κώδικα εδώ }
        float x;
        for (IGrade grade:gradedStudents) {
            if (grade.showAverage()>=5.0) {
                studentsWhoPass.add(grade);
            } else if(grade.showAverage()<5.0){
                studentsWhoFail.add(grade);
            }
        }
    }
    //ΣΤΑΤΙΚΗ ΜΕΘΟΔΟΣ ΕΚΤΥΠΩΣΗΣ ΛΙΣΤΑΣ
    static void printMyList(List<IGrade> myList){
        for (IGrade grade:myList) {
            System.out.printf("%.2f",grade.showAverage());
            if (myList.indexOf(grade)!=myList.size()-1) {
                System.out.print("|");
            }
        }
    }
}
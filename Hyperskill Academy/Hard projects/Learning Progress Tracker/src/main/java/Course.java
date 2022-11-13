import java.util.LinkedList;

abstract class Course{
    LinkedList<Student> students;
    String name = null;
    int enrolledStudents = 0;
    int activity = 0;
    int averageScore = 0;

    public Course(String name, LinkedList<Student> students){
        this.name = name;
        this.students = students;
    }

    public abstract double getEnrolledStudents();
    public abstract double getAverageScore();
    public abstract int getActivity();
}
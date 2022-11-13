import java.util.LinkedList;

class dbCourse extends Course{

    public dbCourse(String name, LinkedList<Student> students){
        super(name, students);
    }

    @Override
    public double getEnrolledStudents() {
        return 0;
    }

    @Override
    public double getAverageScore() {
        return 0;
    }

    @Override
    public int getActivity() {
        return 0;
    }
}
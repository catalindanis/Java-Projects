import java.util.LinkedList;

class javaCourse extends Course{

    public javaCourse(String name, LinkedList<Student> students){
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
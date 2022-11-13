import java.util.LinkedList;

class dsaCourse extends Course{

    public dsaCourse(String name, LinkedList<Student> students){
        super(name,students);
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
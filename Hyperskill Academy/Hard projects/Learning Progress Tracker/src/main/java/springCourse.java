import java.util.LinkedList;

class springCourse extends Course{

    public springCourse(String name, LinkedList<Student> students){
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
        for(Student student : students)
            activity += student.getSpringPoints();
        return activity;
    }
}
class Student{
    private String id;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private int javaPoints ,dsaPoints,dbPoints,springPoints;

    public Student(String id,String firstName,String lastName,String email){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.javaPoints = 0;
        this.dsaPoints = 0;
        this.dbPoints = 0;
        this.springPoints = 0;
    }

    public int getJavaPoints() {
        return javaPoints;
    }

    public int getDsaPoints() {
        return dsaPoints;
    }

    public int getDbPoints() {
        return dbPoints;
    }

    public int getSpringPoints() {
        return springPoints;
    }

    public String getId(){
        return this.id;
    }

    public String getEmail() {
        return email;
    }

    public void updatePoints(int javaPoints, int dsaPoints, int dbPoints, int springPoints){
        this.javaPoints += javaPoints;
        this.dsaPoints += dsaPoints;
        this.dbPoints += dbPoints;
        this.springPoints += springPoints;
    }
}
package bgu.spl.net;

import java.util.ArrayList;


public class User {
    private String userName;
    private String passWord;
    private boolean isAdmin;
    private boolean isLogOn;
    private ArrayList<Short> studentCourses;

    public User(String userName, String passWord, boolean isAdmin) {
        this.userName = userName;
        this.passWord = passWord;
        this.isAdmin=isAdmin;
        isLogOn=false;
        studentCourses = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getPassWord() {
        return passWord;
    }

    public boolean isLogin() {
        return isLogOn;
    }

    public void setLogOn(boolean logOn) {
        isLogOn = logOn;
    }

    public ArrayList<Short> getStudentCourses() {
        return studentCourses;
    }

    public void regCourse(short courseNum){
        studentCourses.add(courseNum);
    }

    public void unregCourse(short courseNum){
        studentCourses.remove(new Short(courseNum));
    }

    public String toString(){
        String output = "Student: "+userName+"\n"+
                "Courses: "+studentCourses.toString().replaceAll(" ","");
        return output;
    }
}


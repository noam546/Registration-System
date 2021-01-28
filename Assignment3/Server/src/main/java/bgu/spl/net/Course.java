package bgu.spl.net;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class Course {

    private short num;
    private String name;
    private ArrayList<Short> kdamCourses;
    private int maxCapacity;
    private int registerStudentsSize;
    private ArrayList<String> registeredStudents;

    public Course(short _num,String _name, ArrayList<Short> _kdam,int _maxCapacity){
        num = _num;
        name=_name;
        kdamCourses=_kdam;
        maxCapacity=_maxCapacity;
        registerStudentsSize = 0;
        registeredStudents = new ArrayList<>();
    }

    public ArrayList<Short> getKdamCourses(){
        return kdamCourses;
    }

    public boolean isCourseAvailable(){
        return maxCapacity > registerStudentsSize;
    }
    public String toString(){
        registeredStudents.sort(Comparator.naturalOrder());
        int availableSeats=maxCapacity-registerStudentsSize;
        String output = "Course: ("+num+") "+name+"\n" +
                "Seats Available: "+availableSeats+"/"+maxCapacity+"\n" +
                "Students Registered: "+registeredStudents.toString().replaceAll(" ","");
                ;
        return output;
    }


    public void registerStudent(String student){
        if(registerStudentsSize < maxCapacity && !registeredStudents.contains(student)) {
            registeredStudents.add(student);
            registerStudentsSize++;
        }
    }
    public void unregisterStudent(String student){
        if(registeredStudents.contains(student)) {
            registeredStudents.remove(student);
            registerStudentsSize--;
        }
    }
public void sortKdamCourses(HashMap<Integer,Short> coursesIndices,int numOfCourses){
        ArrayList<Short> temp=new ArrayList<>();
        for(int i=0;i<=numOfCourses;i=i+1){
            Short currCourseNum=coursesIndices.get((Integer)i);
            if(kdamCourses.contains(currCourseNum)){
                temp.add(currCourseNum);
            }
        }
        kdamCourses=temp;
    }
}

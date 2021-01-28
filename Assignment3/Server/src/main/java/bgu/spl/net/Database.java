package bgu.spl.net;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	private ConcurrentHashMap<Short, Course> courses;
	private ConcurrentHashMap<String, User> users;
	private boolean isInitiallized = false;


	//to prevent user from creating new Database
	private Database() {
		courses = new ConcurrentHashMap<>();
		users = new ConcurrentHashMap<>();
	}

	private static class DatabaseSingletonHolder {
		private static Database instance = new Database();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		if (!DatabaseSingletonHolder.instance.isInitiallized) {
			try {
				synchronized (Database.class) {
					if (!DatabaseSingletonHolder.instance.isInitiallized) {
						DatabaseSingletonHolder.instance.initialize("./Courses.txt");
						DatabaseSingletonHolder.instance.isInitiallized = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return DatabaseSingletonHolder.instance;
	}

	/**
	 * loades the courses from the file path specified
	 * into the Database, returns true if successful.
	 */
	boolean initialize(String coursesFilePath) throws FileNotFoundException {
		File coursesFile = new File(coursesFilePath);
		Scanner coursesFileScanner = new Scanner(coursesFile);
		String currLine = "";
		Short currCourseNum;
		String currCourseName;
		String kdamCoursesList;
		int currMaxCapacity;
		String temp;
		HashMap<Integer,Short> coursesIndices=new HashMap<>();
		int currind=0;
		while (coursesFileScanner.hasNext()) {
			ArrayList<Short> currKdamCourses = new ArrayList<>();
			currLine = coursesFileScanner.nextLine();
			currCourseNum = Short.parseShort(currLine.substring(0, currLine.indexOf('|')));
			temp = currLine.substring(currLine.indexOf('|') + 1);
			currCourseName = temp.substring(0, temp.indexOf('|'));
			temp = temp.substring(temp.indexOf('|') + 1);
			kdamCoursesList = temp.substring(1, temp.indexOf(']') + 1);

			while (kdamCoursesList.indexOf(',') != -1) {
				currKdamCourses.add(Short.parseShort(kdamCoursesList.substring(0, kdamCoursesList.indexOf(','))));
				kdamCoursesList = kdamCoursesList.substring(kdamCoursesList.indexOf(',') + 1);
			}
			if (kdamCoursesList.substring(0, kdamCoursesList.indexOf(']')).length() > 0) {
				currKdamCourses.add(Short.parseShort(kdamCoursesList.substring(0, kdamCoursesList.indexOf(']'))));
			}

			temp = temp.substring(temp.indexOf('|') + 1);

			currMaxCapacity = Integer.parseInt(temp);

			Course course = new Course(currCourseNum, currCourseName, currKdamCourses, currMaxCapacity);
			courses.putIfAbsent(currCourseNum, course);
coursesIndices.put(currind,currCourseNum);
			currind=currind+1;
		}
//sort the kdamcourses list for all courses
		for(Course c : courses.values()){
			c.sortKdamCourses(coursesIndices,currind);
		}
		return true;
	}

	public Course getCourse(short courseNum) {
		if (!courses.containsKey(courseNum)) {
			return null;
		}
		return courses.get(courseNum);
	}

	public synchronized boolean isUserRegistered(String userName) {
		if (users.containsKey(userName)) {
			return true;
		}
		return false;
	}

	public synchronized void RegisterUser(String userName, String password, boolean isAdmin) {
		User newUser = new User(userName, password, isAdmin);
		users.putIfAbsent(userName, newUser);
	}

	public String getUserPassword(String userName) {
		if (users.containsKey(userName)) {
			return users.get(userName).getPassWord();
		}
		//if the user didn't found on the database, return null
		return null;
	}

	public boolean isLogin(String username) {
		return users.containsKey(username) && users.get(username).isLogin();
	}

	public void setLogin(String username, boolean isLogin) {
		if (users.containsKey(username))
			users.get(username).setLogOn(isLogin);
	}

	public boolean userHasAllKdamCourses(String userName, Short courseNum) {
		ArrayList<Short> studentCourses = users.get(userName).getStudentCourses();
		for (Short nextCourse : courses.get(courseNum).getKdamCourses()) {
			if (!studentCourses.contains(nextCourse))
				return false;
		}
		return true;
	}

	public boolean isAdmin(String userName) {
		return users.containsKey(userName) && users.get(userName).isAdmin();
	}

	public boolean courseExists(Short courseNum) {
		return courses.containsKey(courseNum);
	}

	public void regStudentToCourse(String userName, Short courseNum) {
		if (courseExists(courseNum) && userHasAllKdamCourses(userName, courseNum) && isCourseAvailable(courseNum)) {
			users.get(userName).regCourse(courseNum);
			courses.get(courseNum).registerStudent(userName);
		}
	}

	public ArrayList<Short> getKdamCourses(Short coursenum) {
		if (courseExists(coursenum))
			return courses.get(coursenum).getKdamCourses();
		return null;
	}

	public User getUser(String userName) {
		if (isUserRegistered(userName))
			return users.get(userName);
		return null;
	}

	public boolean isCourseAvailable(Short couseNum) {
		if (courseExists(couseNum)) {
			Course cr = courses.get(couseNum);
			return cr.isCourseAvailable();
		}
		return false;
	}

	public boolean isStudentRegisterToCourse(String userName, Short courseNum) {
		if (isUserRegistered(userName) && courseExists(courseNum)) {
			return users.get(userName).getStudentCourses().contains(courseNum);
		}
		return false;
	}

	public void unregisterStudentToCourse(String username, Short courseName) {
		if (isUserRegistered(username) && courseExists(courseName)) {
			users.get(username).unregCourse(courseName);
			courses.get(courseName).unregisterStudent(username);
		}
	}

	public void addCourse(Short courseNum, Course course) {
		courses.putIfAbsent(courseNum, course);
	}

}



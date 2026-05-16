/**
 * Student.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * outlines student behaviors and attributes
*/

import java.util.*; //ArrayLists

/*
 * outlines student behaviors and attributes
*/
public class Student {
	//class variables
	private static int coursesPerS;
	//instance variables
	private int idNum;
	private String name;
	private String email;
	private Time formTime; //not used presently for any function
	private ArrayList<Course> courseRequest;
	private Course[] studentSchedule;
	private String sToString; //stores toString
	
	/*
	 * student constructor initializes instance variables/attributes for student objects; it creates courseRequest ArrayList and
	 * sets the size for the studentSchedule array
	*/
	public Student(int id, String n, String e, Time t) {
		//System.out.println("constructor called");
		idNum = id;
		name = n;
		email = e;
		formTime = t;
		courseRequest = new ArrayList<Course>();
		studentSchedule = new Course[coursesPerS];
		//System.out.println("l43 works");
	}
	//methods
	/*
	 * setCoursesPerStudent is a class method setter which defines the number of courses a student takes
	*/
	public static void setCoursesPerStudent(int n){
		coursesPerS=n;
	}
	/*
	 * getID is a getter which fetches the student id attribute
	*/	
	public int getID(){
		return idNum;
	}
	/*
	 * getName is a getter which fetches the student name attribute
	*/
	public String getName(){
		return name;
	}
	/*
	 * getEmail is a getter which fetches the student email attribute
	*/
	public String getEmail(){
		return email;
	}
	/*
	 * getFormTime is a getter which fetches the time in which the form was submitted
	*/
	public Time getFormTime(){
		return formTime;
	}
	/*
	 * getCourse is a method which fetches the course associated with a ranking; it has ranking as a parameter
	*/
	public Course getCourse(int ranking){
		return courseRequest.get(ranking-1); //ArrayList starts at index 0
	}
	/*
	 * addCourseReq is a method which adds a course to the courseRequest ArrayList; it has course as a parameter
	*/	
	public void addCourseReq(Course c){ //add requested course to courseRequest ArrayList
		Course addCourse = c;
		courseRequest.add(addCourse);
	}
	/*
	 * checkConflict checks if there is a conflict at a certain time in the student's schedule; 
	 * it has time as a parameter;
	 * it returns true if there is conflict and returns false if there is not conflict
	*/
	public boolean checkConflict(int t){ 
		if(studentSchedule[t]!=null){
			return true;
			
		} else {
			return false;	
		}			
	}
	/*
	 * updateSchedule will update the student schedule with a course if there is an available slot
	 * in the student's schedule; it has time and course as parameters
	*/
	public void updateSchedule(int t, Course c){
		if(studentSchedule[t]==null){
			studentSchedule[t]=c;
		}				
	}
	/*
	 * updateScheduleDelete will delete the course associated with a time slot in a student's schedule;
	 * time is a parameter
	*/
	public void updateScheduleDelete(int t){ //it will delete course at that timeSlot
		studentSchedule[t]=null;	
	}
	/*
	 * calculateConflictInd will count the number of conflicts a student has;
	 * if a student has made requests, it will check matches between courseRequest ArrayList
	 * and studentSchedule array; if there is no match for a given course in courseRequest,
	 * conflictCounter--which stores number of conflicts--will be incremented
	*/
	public int calculateConflictInd(){
		int conflictCounter = 0;
		boolean placed;
		for(int r=0;r<courseRequest.size();r++){ //r for request
			if(courseRequest.get(r)!=null){
				placed=false;			
				for(int s=0;s<studentSchedule.length;s++){ //s for schedule
					if(studentSchedule[s]!=null){
						if((courseRequest.get(r).getID()==studentSchedule[s].getID())){
							placed=true;	
						}		
					}
				}
				if(placed==false){
					conflictCounter++;
				}
			}		
		}
		return conflictCounter;	
	}
	/*
	 * getSchedule returns the student's schedule in the form of an array of courses
	*/
	public Course[] getSchedule(){
		return studentSchedule;
	}
	/*
	 * madeRequest checks if a student has made a course request or not; it will return a boolean value
	*/
	public boolean madeRequest(){
		for(int i=0;i<courseRequest.size();i++){
			if(courseRequest.get(i)!=null){
				return true;
			}	
		}
		return false;	
	}
	/*
	 * getRanking finds the ranking of a course in a student's schedule; it takes in a course parameter
	 * and returns an int, conveying ranking; if course is not found, method returns -1
	*/
	public int getRanking(Course c){
		for(int i=0;i<courseRequest.size();i++){
			if(c==courseRequest.get(i)){
				return i+1;
			}	
		}
		return -1;	
	}
	/*
	 * toString turns the info for a given student (name, id, email, form time, course requests, student schedule) into String form
	*/
	public String toString(){
		sToString = "";
		sToString = "id number: " + idNum;
		sToString = sToString + "\nname: " + name;
		sToString = sToString + "\nemail: " + email;
		sToString = sToString + "\nform time: " + formTime.toString();
		sToString = sToString + "\nREQUESTS";
		for (int i=0; i<courseRequest.size();i++){
			sToString = sToString + "\nchoice " + (i+1) + ":" + courseRequest.get(i); 
		}
		sToString = sToString + "\nSCHEDULE";
		for (int i=0; i<studentSchedule.length;i++){
			sToString = sToString + "\ntime " + (i+1) + ":" + studentSchedule[i];
			
		}		
		return sToString;
	}			
}

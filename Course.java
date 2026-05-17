/**
 * Course.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * outlines course behaviors and attributes
*/

import java.util.*; //ArrayLists
/*
 *outlines scourse behaviors and attributes 
*/
public class Course {
	//class variables
	private static int numSections=0; //not necessarily running
	//instance variables
	private String courseTeacher;
	private String courseName;
	private int courseID;
	private int popRating;
	private int studentDemand; //number of students that select the course (regardless of choice #)
	private int priorityRating;
	private int rosterSize;
	private ArrayList<Student> roster = new ArrayList<Student>();
	private int sectionID;
	/*
	 * course constructor initializes attributes/instance variables and sets popularity rating, student demand, and roster size
	 * to 0
	*/
	public Course(String t, String n, int id){
		numSections++;
		courseTeacher = t;
		courseName = n;
		courseID = id;
		popRating = 0;
		studentDemand = 0;
		rosterSize=0;
		sectionID=numSections;
	}
	//methods
	/*
	 * getID is a getter which fetches the course id attribute
	*/
	public int getID(){
		return courseID;
	}
	/*
	 * getSectionID is a getter which fetches the section id attribute
	*/
	public int getSectionID(){
		return sectionID;
	}
	/*
	 * getName is a getter which fetches the course name attribute
	*/
	public String getName(){
		return courseName;
	}
	/*
	 * getTeacher is a getter which fetches the teacher attribute in the form of a String
	*/
	public String getTeacher(){
		return courseTeacher;
	}
	/*
	 * updatePopRating updates a course's popularity rating by a parameter increment; the parameter, n,
	 * signals the course's ranking on a student's courseRequests
	*/
	public void updatePopRating(int n){
		popRating = popRating + (1*n);		
	}
	/*
	 * updateDemand updates student demand by incrementing studentDemand by 1
	*/
	public void updateDemand(){
		studentDemand++;			
	}
	/*
	 * updatePriorityRating updates a course's priority rating by summing student demand and popularity rating
	*/
	public void updatePriorityRating(){
		priorityRating = studentDemand+popRating;
	}
	/*
	 * getPR is a getter that fetches the priority rating of a course
	*/
	public int getPR(){
		return priorityRating;
	}
	/*
	 * updateRoster takes in parameter student and adds the student to the roster
	*/
	public void updateRoster(Student s){
		roster.add(s);
		rosterSize=roster.size();
	}
	/*
	 * updateRoster with no parameter updates roster size
	*/
	public void updateRoster(){
		rosterSize=roster.size();
	}
	/*
	 * getRosterSize is a getter that fetches the roster size of a course
	*/	
	public int getRosterSize(){
		updateRoster();
		return roster.size();
	}
	/*
	 * rosterRemove w/ no arguments removes last student in roster;
	 *  overloaded method
	*/
	public void rosterRemove(){//overloaded
		roster.remove(roster.size()-1);
		rosterSize=roster.size();
	}
	/*
	 * rosterRemove w/ student argument searches the roster for a student and removes
	 * student if found;
	 * overloaded method
	*/
	public void rosterRemove(Student s){
		//search for student object;if found, remove
		boolean found = false;
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				roster.remove(i);
				found = true;
				//System.out.println("Successful removal of " + s.getID() + "from " + courseName);
			}	
		}	
		rosterSize=roster.size();
	}
	/*
	 * rosterRemove w/ int argumentremoves a student at a given roster position/index in roster; 
	 * rosterPos is a parameter;
	 * overloaded method
	*/
	public void rosterRemove(int rosterPos){
		roster.remove(rosterPos);
		rosterSize=roster.size();
	}
	/*
	 * this rosterRemove w/ 2 arguments assists searchDelete method in Schedule class; it removes
	 * students who were somehow placed in a class; the second parameter has no meaning and
	 * is only used to differentiate this rosterRemove w/ a student argument from the other one--it
	 * allowed me to put a print statement to help debug;
	 * overloaded method
	*/
	public void rosterRemove(Student s, int useless){
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				//System.out.println(s.getID() + " was somehow found in/not deleted from " + courseName);
				roster.remove(i);
			}	
		}
		rosterSize=roster.size();
	}
	/*
	 * setDemand sets the demand of a course to a given int parameter
	*/
	public void setDemand(int d){
		studentDemand = d;
	}
	/*
	 * getDemand is a getter that fetches the student demand for a course
	*/
	public int getDemand(){
		return studentDemand; 
	}
	/*
	 * setDPR sets the priority rating of a course to a given int parameter
	*/
	public void setPR(int pr){
		priorityRating=pr;
	}
	/*
	 * getStudent is a getter which fetches the student at a certain roster position; rosterPos is the parameter
	*/		
	public Student getStudent(int rosterPos){
		return roster.get(rosterPos);
	}
	/*
	 * rosterFind ascertains whether a student is in the course's roster; it returns a boolean and takes in a student
	 * argument
	*/
	public boolean rosterFind(Student s){
		//search for student object;if found, remove
		boolean found = false;
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				return true;
			}	
			
		}
		return false;
	}
	
	/*
	 * getRoster is a getter which fetches the roster (in the form of an ArrayList) of a course
	*/
	public ArrayList<Student> getRoster(){
		return roster;
	}
	/*
	 * printRoster prints out the roster of a course with student names
	*/
	public void printRoster(){
		for(int i=0;i<roster.size();i++){
			System.out.println("\nStudent #" + (i+1) + roster.get(i).getName());	
		}	
	}
	/*
	 * printRosterSimple prints out the roster of a course with student ids
	*/
	public void printRosterSimple(){
		System.out.println(courseName);
		for(int i=0;i<roster.size();i++){
			System.out.println(roster.get(i).getID());	
		}	
	}						
	/*
	 * toString turns the info about a course (teacher, name, id, popularity rating, student demand, priority rating) into String form
	*/	
	public String toString(){
		
		String cToString = "\n";
		cToString = cToString + "teacher: " + courseTeacher + "\n";
		cToString = cToString + "name: " + courseName + "\n";
		cToString = cToString + "id: " + courseID + "\n";
		cToString = cToString + "popularity rating: " + popRating + "\n";
		cToString = cToString + "student demand: " + studentDemand + "\n";
		cToString = cToString + "priority rating: " + priorityRating + "\n";
		return cToString;
	}				
}

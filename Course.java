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

	//instance variables
	private String courseTeacher;
	private String courseName;
	private int courseID;
	private int popRating;
	private int studentDemand; //number of students that select the course (regardless of choice #)
	private int priorityRating;
	private int rosterSize;
	private ArrayList<Student> roster = new ArrayList<Student>();
	/*
	 * course constructor initializes attributes/instance variables and sets popularity rating, student demand, and roster size
	 * to 0
	*/
	public Course(String t, String n, int id){
		courseTeacher = t;
		courseName = n;
		courseID = id;
		popRating = 0;
		studentDemand = 0;
		rosterSize=0;
	}
	//methods
	/*
	 * getID is a getter which fetches the course id attribute
	*/
	public int getID(){
		return courseID;
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
	}/*
	 * getRosterSize is a getter that fetches the roster size of a course
	*/	
	public int getRosterSize(){
		updateRoster();
		return roster.size();
	}
	public void rosterRemove(int rosterPos){ //overloaded
		roster.remove(rosterPos);
		rosterSize=roster.size();
	}
	//following setters and getters are used to copy courses
	public void setDemand(int d){
		studentDemand = d;
	}
	public int getDemand(){
		return studentDemand; 
	}
	public void setPR(int pr){
		priorityRating=pr;
		
	}				
	public Student getStudent(int rosterPos){
		return roster.get(rosterPos);
		
	}
	public void rosterRemove(){//overloaded
		roster.remove(roster.size()-1);
		rosterSize=roster.size();
		
	}
	public void rosterRemove(Student s){
		//search for student object;if found, remove
		boolean found = false;
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				roster.remove(i);
				found = true;
			}	
			
		}
		if(found==false){
			//System.out.println("ERROR");
			
			
		}	
		rosterSize=roster.size();
	}
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
	public void rosterRemove(Student s, int useless){
		//search for student object;if found, remove
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				//System.out.println(s.toString());
				System.out.println(s.getID() + " was somehow found in/not deleted from " + courseName);
				roster.remove(i);
			}	
			
		}
		rosterSize=roster.size();
	}	
	public ArrayList<Student> getRoster(){
		return roster;
		
	}						
		
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
	public void printRoster(){
		for(int i=0;i<roster.size();i++){
			System.out.println("\nStudent #" + (i+1) + roster.get(i).getName());	
		}	
	}
	public void printRosterSimple(){
		System.out.println(courseName);
		for(int i=0;i<roster.size();i++){
			System.out.println(roster.get(i).getID());	
		}	
	}				
}

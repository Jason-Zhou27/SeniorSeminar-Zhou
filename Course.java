import java.util.*;

public class Course {

	//variables
	private String courseTeacher;
	private String courseName;
	private int courseID;
	private int popRating = 0;
	private int studentDemand = 0; //number of students that select the course (regardless of choice #)
	private int priorityRating;
	private int rosterSize=0;
	private ArrayList<Student> roster = new ArrayList<Student>();
	//constructors
	public Course(String t, String n, int id){
		courseTeacher = t;
		courseName = n;
		courseID = id;
	}
	public Course(String t, int id){
		courseTeacher = t;
		courseID=id;
	}
	//DNE constructor	
	public Course(String n){
		courseName = n;
	}
	//methods
	public int getID(){
		return courseID;
	}
	public String getName(){
		return courseName;
	}
	public String getTeacher(){
		return courseTeacher;
	}
	public void updatePopRating(int n){
		popRating = popRating + (1*n);		
	}
	public void updateDemand(){
		studentDemand++;			
	}
	public void updatePriorityRating(){
		priorityRating = studentDemand + popRating/3; //aims to give equal weight b/w studentDemand & popRating			
	}
	public int getPR(){
		return priorityRating;
	}
	public void updateRoster(Student s){
		
		roster.add(s);
		rosterSize=roster.size();
	}
	public void updateRoster(){
		rosterSize=roster.size();
		
	}	
	public int getRosterSize(){
		return rosterSize;
	}
	public void rosterRemove(int rosterPos){ //overloaded
		roster.remove(rosterPos);
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
		
	}
	public void rosterRemove(Student s){
		//search for student object;if found, remove
		boolean found = false;
		for(int i=0;i<roster.size();i++){
			if(roster.get(i)==s){
				roster.remove(i);
				found=true;
				
			}	
			
		}
		if(found==true){
			System.out.println("removed");
			
			
		}
		else {
			System.out.println("notfound & roster size is " + roster.size() + " and student " + s.getName() + " is trying to get removed from" + courseName);
				
		}			
		
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


}

public class Course {

	//variables
	private String courseTeacher;
	private String courseName;
	private int courseID;
	private int popRating = 0;
	private int studentDemand = 0; //number of students that select the course (regardless of choice #)
	private int priorityRating;
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

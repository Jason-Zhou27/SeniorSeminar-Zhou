import java.util.*;

public class Student {

	//variables
	private int idNum;
	private String name;
	private String email;
	private Time formTime;
	
	private static int coursesPerS;
	private ArrayList<Course> courseRequest;
	private Course[] studentSchedule;
	private String sToString;
	//constructors

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
	
	//getters
	public static void setCoursesPerStudent(int n){
		coursesPerS=n;
	}	
	public int getID(){
		return idNum;
	}
	public String getName(){
		return name;
	}
	public String getEmail(){
		return email;
	}
	public Time getFormTime(){
		return formTime;
	}
	public Course getCourse(int ranking){
		return courseRequest.get(ranking-1); //ArrayList starts at index 0
	}	
	public void addCourseReq(Course c){ //add requested course to courseRequest ArrayList
		Course addCourse = c;
		courseRequest.add(addCourse);
	}
	public boolean checkConflict(int t){ //returns true if there is conflict and returns false if there is not conflict
		if(studentSchedule[t]!=null){
			return true;
			
		} else {
			return false;	
		}			
	}
	public void updateSchedule(int t, Course c){ //it will update schedule and will return boolean for confirmation
		if(studentSchedule[t]==null){ //maybe scrutinize this line right here
			studentSchedule[t]=c;
		}				
	}
	public void updateScheduleDelete(int t){ //it will delete course at that timeSlot
		studentSchedule[t]=null;	
	}
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
	public Course[] getSchedule(){
		return studentSchedule;
	}					
				
	
	//setters
	//toString
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
	public int getNumRequests(){
		int counter =0;
		int size = courseRequest.size();
		for(int i=0;i<size;i++){
			if(courseRequest.get(0).getName().equals("NA")){
				courseRequest.remove(0);
			}
			else {
				counter++;
				
			}		
		}
		return counter;		
	}		
}

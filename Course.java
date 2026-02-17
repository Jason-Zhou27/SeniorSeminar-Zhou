public class Course {

	//variables
	private Teacher courseTeacher;
	private String courseName;
	private int courseID;
	//constructors
	public Course(Teacher t, String n, int id){
		courseTeacher = t;
		courseName = n;
		courseID = id;
	}
	public Course(Teacher t, int id){
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
	public String toString(){
		
		String cToString = "\nTHIS DOES NOT WORK";
		//cToString = cToString + "teacher: " + courseTeacher.getName() + "\n";
		cToString = cToString + "name: " + courseName + "\n";
		cToString = cToString + "id: " + courseID + "\n";
		return cToString;
		
		
	}		


}

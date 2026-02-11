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
	//DNE constructor
	public Course(String n){
		courseName = n;
	}	
	//methods
	public int getID(){
		return ID;
	}	


}

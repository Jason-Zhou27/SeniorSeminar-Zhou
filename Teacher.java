public class Teacher {


	//variables
	private int idNum;
	private String name;
	private Course course1;
	private Course course2;
	//constructors
	public Teacher(int id, String n, Course c1, Course c2) {
		idNum = id;
		name = n;
		course1 = c1;
		course2 = c2;
	}	
	//methods
	
	//getters
	public int getID(){
		return idNum;
	}
	public String getName(){
		return name;
	}
	public Course getC1(){
		return course1;
	}
	public Course getC2(){
		return course2;
	}
	//toString
	public String toString(){
		sToString = "";
		sToString = "id number: " + idNum;
		sToString + "\nname: " + name;
		sToString + "\n1st course: " + course1;
		sToString + "\n2nd course: " + course2;
		return sToString;
	}



}

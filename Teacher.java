public class Teacher {


	//variables
	private int idNum;
	private String name;
	private Course course1;
	private Course course2;
	//constructors
	/*
	public Teacher(int id, String n, Course c1, Course c2) {
		idNum = id;
		name = n;
		course1 = c1;
		course2 = c2;
	}
	*/
	public Teacher(int id, String n) {
		idNum = id;
		name = n;
	}
	public Teacher(String n) {
		name = n;
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
	/*
	public String toString(){
		String tToString = "THIS WORKS";
		tToString = "\nid number: " + idNum;
		tToString = tToString + "\nname: " + name;
		tToString = tToString + "\n1st course: " + course1;
		tToString = tToString + "\n2nd course: " + course2;
		return tToString;
	}
	*/



}

public class Student {

	//variables
	private int idNum;
	private String name;
	private String email;
	private Time formTime;
	private Course course1;
	private Course course2;
	private Course course3;
	private Course course4;
	private Course course5;
	private String sToString;
	//constructors
	public Student(int id, String n, String e, Time t, Course c1, Course c2, Course c3, Course c4, Course c5) {
		idNum = id;
		name = n;
		email = e;
		formTime = t;
		course1 = c1;
		course2 = c2;
		course3 = c3;
		course4 = c4;
		course5 = c5;
	}	
	//methods
	
	//getters
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
		return email;
	}
	public Course getC1(){
		return course1;
	}
	public Course getC2(){
		return course2;
	}
	public Course getC3(){
		return course3;
	}
	public Course getC4(){
		return course4;
	}
	public Course getC5(){
		return course5;
	}			
		
	
	//setters
	//toString
	public String toString(){
		sToString = "";
		sToString = "id number: " + idNum;
		sToString = sToString + "\nname: " + name;
		sToString = sToString + "\nemail: " + email;
		sToString = sToString + "\nform time: " + formTime;
		sToString = sToString + "\n1st choice: " + course1;
		sToString = sToString + "\n2nd choice: " + course2;
		sToString = sToString + "\n3rd choice: " + course3;
		sToString = sToString + "\n4th choice: " + course4;
		sToString = sToString + "\n5th choice: " + course5;
		return sToString;
	}	


}

/**
 * Schedule.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * runner class that runs the program
*/
/*
 * runs the program by constructing Schedule object and calling object methods
*/
public class Main {
	public static void main(String[] args){
		Schedule s1 = new Schedule(5,5,5,16,2); //parameters for given senior seminar
		s1.placeCourses();
		s1.fillGaps();
		s1.printSeniorSeminar();
		s1.printOverview();
		//s1.printFree();
		//s1.printAllRosters();
		s1.menu();
		
	}
}
	

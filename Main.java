import java.util.*;
//tester class
public class Main {
	public static void main(String[] args){
		Schedule s1 = new Schedule(5,5);
		s1.printStudents();
		s1.findPop();
		s1.findDemand();
		s1.assignPriority();
		s1.sortCourses();
		s1.loadRoster();
		s1.printCourses();
		
	
	}
}
	

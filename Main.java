import java.util.*;
//tester class
public class Main {
	public static void main(String[] args){
		Schedule s1 = new Schedule(5,5,5,16);
		s1.placeCourses();
		s1.fillGaps();
		//s1.printStudents();
		//s1.printCourses();
		//System.out.println(s1.getStudent(5).toString());
		s1.printSeniorSeminar();
		s1.printOverview();

		
	
	}
}
	

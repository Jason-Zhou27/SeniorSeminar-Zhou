/**
 * Schedule.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * This class is the runner class that runs the program
*/
import java.util.*; //Scanner
/*
 * Main class runs the program by constructing Schedule object and calling object methods
*/
public class Main {
	/*
	 * main method creates instantiation of schedule and calls object methods of schedule;
	 * this instantiation is either defaulted to the given parameters to the problem or
	 * custom parameters
	*/ 
	public static void main(String[] args){
		Scanner setup = new Scanner(System.in);
		System.out.println("\n\n|| WELCOME TO SENIOR SEMINAR ||\n-a program by Jason Zhou");
		System.out.println("\nChoose either...\n1 - default parameters (given by Mr. Twyford)\n2 - custom parameters");
		System.out.print("Enter selection (numeral): ");
		String response = setup.nextLine();
		if(response.equals("1")){
			Schedule s1 = new Schedule(5,5,16,2); //parameters for given senior seminar
			s1.placeCourses();
			s1.fillGaps();
			s1.menu();
		}
		else if(response.equals("2")){
			System.out.println("\nnote: # of times should match # of course requests in data file");
			System.out.print("Enter # of times: ");
			int numTimes = Integer.parseInt(setup.nextLine());
			System.out.print("Enter # of classrooms: ");
			int numClassrooms = Integer.parseInt(setup.nextLine());
			System.out.print("Enter classroom capacity: ");
			int capacity= Integer.parseInt(setup.nextLine());
			System.out.print("Enter max sections per course: ");
			int maxSections = Integer.parseInt(setup.nextLine());
			Schedule s1 = new Schedule(numTimes,numClassrooms,capacity,maxSections);
			s1.placeCourses();
			s1.fillGaps();
			s1.menu();	
		}	
		
	}
}
	

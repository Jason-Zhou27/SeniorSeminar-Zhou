import java.util.*;
import java.io.*;

public class Schedule{
	//variables
	private String fileNameStudent = "studentInfo.txt"; //Name of txt file with Student info
	private String fileNameCourse = "courseInfo.txt"; //Name of txt file with Student info
	private ArrayList<Student> studentList = new ArrayList<Student>(); //ArrayList to store student objects
	private ArrayList<Course> courseList = new ArrayList<Course>(); //ArrayList to store course objects
	private Course[][] seniorS;
	
	private int numTimes;
	private int coursesPerS; //number of courses per student
	private int numClassrooms;
	
	//constructors
	public Schedule(int nT, int nC){
		readFileCourse();
		readFileStudent();
		numTimes = nT;
		coursesPerS = numTimes;
		
		numClassrooms = nC;
		seniorS = new Course[numTimes][numClassrooms];
	}	
	//methods
	public void readFileStudent() {
		try {
			Scanner scanStudent = new Scanner(new File(fileNameStudent));
			scanStudent.nextLine(); //skips over first line (column names)
			while (scanStudent.hasNext()){
				String lineStudent = scanStudent.nextLine();
				//extract info from line
				String[] elementsStudent = lineStudent.split(",");
				
				//email and name info
				String emailStudent = elementsStudent[3];
				String emailPrefix = elementsStudent[4];
				String nameStudent = elementsStudent[5];
				//course info
				int course1Index = Integer.parseInt(elementsStudent[6]);
				int course2Index = Integer.parseInt(elementsStudent[7]);
				int course3Index = Integer.parseInt(elementsStudent[8]);
				int course4Index = Integer.parseInt(elementsStudent[9]);
				int course5Index = Integer.parseInt(elementsStudent[10]);
				
				Course course1 = getCourse(course1Index);
				Course course2 = getCourse(course2Index);
				Course course3 = getCourse(course3Index);
				Course course4 = getCourse(course4Index);
				Course course5 = getCourse(course5Index);
				//student id
				int studentId = studentList.size()+1;
				
				//time info
				String timeStamp = elementsStudent[0];
				String timeStampD = elementsStudent[1];
				if (timeStamp.equals("")){
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100), course1, course2, course3, course4, course5));
					
				} else {	
					String[] timeStampDE = timeStampD.split("/");
					int month = Integer.parseInt(timeStampDE[0]);
					int day = Integer.parseInt(timeStampDE[1]);
					int year = Integer.parseInt(timeStampDE[2]);
					
					String timeStampT = elementsStudent[2];
					String[] timeStampTE = timeStampT.split(":");
					int hour = Integer.parseInt(timeStampTE[0]);
					int minute = Integer.parseInt(timeStampTE[1]);
					int second = Integer.parseInt(timeStampTE[2]);
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(month, day, year, hour, minute, second), course1, course2, course3, course4, course5));
					//studentList.add(new Student(studentId, nameStudent, emailStudent, course1, course2, course3, course4, course5));
				}	
				//studentList.add(new Student(studentId, nameStudent, emailStudent, course1, course2, course3, course4, course5));
				
			}	
		} catch (FileNotFoundException e){
				System.out.println("File Not Found!");
			}			
	}
	public void readFileCourse() {
		try {
			Scanner scanCourse = new Scanner(new File(fileNameCourse));
			scanCourse.nextLine(); //skips over first line (column names)
			while (scanCourse.hasNext()){
				String lineCourse = scanCourse.nextLine();
				//extract info from line
				String[] elementsCourse = lineCourse.split(",");
				//course name
				String nameCourse = elementsCourse[0];
				int idCourse = Integer.parseInt(elementsCourse[1]);
				String nameTeacher = elementsCourse[2];
				courseList.add(new Course(nameTeacher, nameCourse, idCourse));
				}	
			} catch (FileNotFoundException e){
				System.out.println("File Not Found!");
			}			
	}
	public void placeCourses(){ //takes charge in placing courses
		for(int i=0;i<courseList.size();i++){
			
			
		
		
		}
	}
	public int[][] findOptimalPlace(Course c){ //assists placeCourses method by finding optimal position in 2d course array & returning it
		
		
	}		
	public Course getCourse(int idCourse){
		//traverse course arraylist
		if(idCourse==0){ //for students who do not fill out form
			return new Course("NA");
		}	
		for(int i=0; i<courseList.size(); i++){
			int currentID = courseList.get(i).getID();
			if(currentID==idCourse){
				return courseList.get(i);		
			}	
		}
		return new Course("Does Not Exist");	
	}
	public void printStudents(){
		for (int i=0;i<studentList.size();i++){
			System.out.println(studentList.get(i).toString());		
		}	
		
	}
	public void printCourses(){
		for (int i=0;i<courseList.size();i++){
			System.out.println(courseList.get(i).toString());		
		}	
	}
	
	public void findPop(){
		for(int i=0; i<studentList.size();i++){
			studentList.get(i).getC1().updatePopRating(5);
			studentList.get(i).getC2().updatePopRating(4);
			studentList.get(i).getC3().updatePopRating(3);
			studentList.get(i).getC4().updatePopRating(2);
			studentList.get(i).getC5().updatePopRating(1);
		}	
	}
	
	public void findDemand(){
		for(int i=0; i<studentList.size();i++){
			studentList.get(i).getC1().updateDemand();
			studentList.get(i).getC2().updateDemand();
			studentList.get(i).getC3().updateDemand();
			studentList.get(i).getC4().updateDemand();
			studentList.get(i).getC5().updateDemand();
		}	
	}
	
	public void assignPriority(){ //priority rating for courses are used to determine order for placement
		for(int i=0; i<courseList.size();i++){
			courseList.get(i).updatePriorityRating();
			
		}		
	}
	
	public void sortCourses(){ //sorts courses so those with highest priority are at the beginning of the arrayList courseList
		do {	
			for(int i=0;i<courseList.size()-1;i++){
				if(courseList.get(i).getPR()<courseList.get(i+1).getPR()){
					Course temp = courseList.get(i+1); 
					courseList.remove(i+1);
					courseList.add(i,temp);
				}
			}
		} while(sortCCheck()==false);
		
	}
	public boolean sortCCheck(){
		boolean sorted = true;
		for(int i=0;i<courseList.size()-1;i++){
			if(courseList.get(i).getPR()<courseList.get(i+1).getPR()){
				sorted=false;
			}	
		}
		return sorted;
	}
	public void loadRoster(){ //uses updateRoster method from Course class to add students to each of their courses' rosters
		for(int i=0; i<studentList.size();i++){
			studentList.get(i).getC1().updateRoster(studentList.get(i));
			studentList.get(i).getC2().updateRoster(studentList.get(i));
			studentList.get(i).getC3().updateRoster(studentList.get(i));
			studentList.get(i).getC4().updateRoster(studentList.get(i));
			studentList.get(i).getC5().updateRoster(studentList.get(i));	
		}	
	}		
		
		
				
			
			
}	
		
				
	
			


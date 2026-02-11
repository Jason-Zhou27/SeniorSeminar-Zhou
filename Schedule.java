import java.util.*;
import java.io.*;

public class Schedule{
	//variables
	private String fileNameStudent = "studentInfo.txt"; //Name of txt file with Student info
	private String fileNameCourse = "courseInfo.txt"; //Name of txt file with Student info
	private ArrayList<Student> studentList = new ArrayList<Student>(); //ArrayList to store student objects
	private ArrayList<Course> courseList = new ArrayList<Course>(); //ArrayList to store course objects
	
	//constructors
	public Schedule(){
		readFileCourse();
		readFileStudent();
		
		
		
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
				//time info
				String timeStamp = elementsStudent[0];
				String timeStampD = elementsStudent[1];
				String[] timeStampDE = timeStampD.split("/");
				int month = Integer.parseInt(timeStampDE[0]);
				int day = Integer.parseInt(timeStampDE[1]);
				int year = Integer.parseInt(timeStampDE[2]);
				
				String timeStampT = elementsStudent[2];
				String[] timeStampTE = timeStampT.split(":");
				int hour = Integer.parseInt(timeStampTE[0]);
				int minute = Integer.parseInt(timeStampTE[1]);
				int second = Integer.parseInt(timeStampTE[2]);
				
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
				int studentID = studentList.size()+1;
				
				studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(month, day, year, hour, minute, second), course1, course2, course3, course4, course5));
				
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
				String idCourse = Integer.parseInt(elementsCourse[1]);
				String nameTeacher = elementsCourse[2];
				courseList.add(new Course(new Teacher(nameTeacher), idCourse));;
				}	
			} catch (FileNotFoundException e){
				System.out.println("File Not Found!");
			}			
	}
	public Course getCourse(int idCourse){
		//traverse course arraylist
		for(int i=0; i<courseList.size(); i++){
			int currentID = courseList.get(i).getID();
			if(currentID==idCourse){
				return courseList.get(i);		
			}	
		}
		return new Course("Does Not Exist");	
	}
	
			
}

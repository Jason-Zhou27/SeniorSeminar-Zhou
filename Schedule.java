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
	private int maxStudents;
	private int maxSpots;
	
	private int conflicts = 0;
	
	//constructors
	public Schedule(int nT, int nCPS, int nC, int mS){
		numTimes = nT;
		coursesPerS = nCPS;
		maxStudents = mS;
		numClassrooms = nC;
		maxSpots=nT*nC;
		Student.setCoursesPerStudent(coursesPerS);
		readFileCourse();
		readFileStudent();
		
		
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

				/*	
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
				*/
				//student id
				int studentId = studentList.size()+1;
				
				//time info
				String timeStamp = elementsStudent[0];
				String timeStampD = elementsStudent[1];
				if (timeStamp.equals("")){
					//studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100), course1, course2, course3, course4, course5));
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100)));
					System.out.println("Constructor WAS CALLED");
					
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
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(month, day, year, hour, minute, second)));
					System.out.println("Constructor WAS CALLED");
					//studentList.add(new Student(studentId, nameStudent, emailStudent, course1, course2, course3, course4, course5));
				}	
				//studentList.add(new Student(studentId, nameStudent, emailStudent, course1, course2, course3, course4, course5));
				//System.out.println("I'm working1 and the value of coursesPerS is " + coursesPerS);
				
				//int numUse=5;
				for(int i=0; i<coursesPerS;i++){ //load courses for students
					//System.out.println("I'm working2");
					int courseIndex = Integer.parseInt(elementsStudent[6+i]);
					Course courseAdd = getCourse(courseIndex);
					//System.out.println("I'm working3");
					studentList.get(studentList.size()-1).addCourseReq(courseAdd);
				}
				
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
		findPop();
		findDemand();
		assignPriority();
		sortCourses();
		loadRoster();
		duplicateCourses();
		System.out.println("Course List Size is: " + courseList.size());
		printCourses();
		for(int i=0;i<maxSpots;i++){
			
			int[] cPlacement = findOptimalPlace(courseList.get(i));
			int timeBlock = cPlacement[0]+1; //time in 2d array
			int classroom = cPlacement[1]+1; //classroom in 2d array
			seniorS[timeBlock-1][classroom-1]=courseList.get(i);
			if(timeBlock!=-1 &&classroom!=-1){
				for(int k=0;k<courseList.get(i).getRosterSize();k++){
					if(courseList.get(i).getStudent(k).updateSchedule(timeBlock-1, courseList.get(i))==false){
						courseList.get(i).rosterRemove(k);
						//conflicts++; I changed my method of calculating conflicts
						courseList.get(i).updateRoster();
					}
				}
				if(courseList.get(i).getRosterSize()>maxStudents){
					//removeStudents as of right now, I will just truncate; but later, for optimization, the removal of students should be more strategic
					int numRemove = courseList.get(i).getRosterSize()-maxStudents;
					for(int k=0;k<numRemove;k++){
						courseList.get(i).rosterRemove();
						
					}
					courseList.get(i).updateRoster();
					
				}
				removeDuplicateStudents(courseList.get(i).getRoster(), courseList.get(i).getID(), i);	
			}
		}
	}
	
	public int[] findOptimalPlace(Course c){ //assists placeCourses method by finding optimal position in 2d course array & returning it
		int optRow = -2;
		int optCol = -2;
		int counter;
		int fewestConflicts=Integer.MAX_VALUE;
		int rowMax = numTimes;
		int colMax = numClassrooms;
		for(int row=0;row<rowMax;row++){
			for(int col=0;col<colMax;col++){
				if(seniorS[row][col]==null){
					counter=0;
					int numRoster = c.getRosterSize();
	
					for(int i=0;i<numRoster;i++){
						if(c.getStudent(i).checkConflict(row)==true){
							counter++;
						}
					}
					if(counter<fewestConflicts){
						fewestConflicts=counter;
						optRow=row;
						optCol=col;
					}		
					
				}	
			}
		}
		int[] infoOptimalRC = {optRow, optCol};
		return infoOptimalRC;
			
		
		
	}
	public void duplicateCourses(){
		Course c;
		Course cCopy;
		double quotient=0.0;
		int multiple =0; //placeholder value
		for(int i=0;i<courseList.size();i=i+multiple){
			c = courseList.get(i);
			System.out.println("Course size: " + c.getRosterSize() + "\nmaxStudents:" + maxStudents);
			if(c.getRosterSize()>maxStudents){
				quotient = (double)c.getRosterSize()/maxStudents;
				multiple = (int)(quotient+0.5);
				System.out.println(multiple);
				
				for(int k=0; k<multiple-1; k++){
					courseList.add(i+1, new Course(c.getTeacher(), c.getName(), c.getID()));
					cCopy = courseList.get(i+1);
					cCopy.setDemand(c.getDemand());
					cCopy.setPR(c.getPR());
					for(int s=0; s<c.getRosterSize(); s++){
						cCopy.updateRoster(c.getStudent(s));
					}
				}	
				
			}	
			
			
		}	
	}
	public void removeDuplicateStudents(ArrayList<Student> r, int idCourse, int pos){ //this method removes the students placed in a course from the other section
		ArrayList<Student> placed = r;
		//the logic here is that the same courses diff sections are adjacent to each other on the courseList
		//and I only have to check the courses after and take off already placed students from those
		for(int i=pos+1; i<courseList.size();i++){
			if(courseList.get(i).getID()==idCourse){
				for(int k=0; k<placed.size(); k++){
					courseList.get(i).rosterRemove(placed.get(k));
					
					
				}
				courseList.get(i).updateRoster();	
				
				
			}	
		}	
		
		
		
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
			/*
			studentList.get(i).getC1().updatePopRating(5);
			studentList.get(i).getC2().updatePopRating(4);
			studentList.get(i).getC3().updatePopRating(3);
			studentList.get(i).getC4().updatePopRating(2);
			studentList.get(i).getC5().updatePopRating(1);
			*/
			for(int k=0;k<coursesPerS;k++){
				studentList.get(i).getCourse(k+1).updatePopRating(coursesPerS-k);
			}
		}	
	}
	
	public void findDemand(){
		for(int i=0; i<studentList.size();i++){
			/*
			studentList.get(i).getC1().updateDemand();
			studentList.get(i).getC2().updateDemand();
			studentList.get(i).getC3().updateDemand();
			studentList.get(i).getC4().updateDemand();
			studentList.get(i).getC5().updateDemand();
			*/
			for(int k=0;k<coursesPerS;k++){
				studentList.get(i).getCourse(k+1).updateDemand();
			}
			
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
			/*
			studentList.get(i).getC1().updateRoster(studentList.get(i));
			studentList.get(i).getC2().updateRoster(studentList.get(i));
			studentList.get(i).getC3().updateRoster(studentList.get(i));
			studentList.get(i).getC4().updateRoster(studentList.get(i));
			studentList.get(i).getC5().updateRoster(studentList.get(i));
			*/	
			for(int k=0;k<coursesPerS;k++){
				studentList.get(i).getCourse(k+1).updateRoster(studentList.get(i));
			}
		}	
	}
	public void printSeniorSeminar(){
		
		int maxRow=numTimes;
		int maxCol=numClassrooms;
		for(int r=0;r<maxRow;r++){
			for(int c=0;c<maxCol;c++){
				if(seniorS[r][c]!=null){
					System.out.print(" " + seniorS[r][c].getID() + " ");
				}
				else {
					System.out.print(" " + -1 + " ");
				}		
			}
			System.out.println();	
		}
		
		System.out.print("\n\n\nNumber of Conflicts: " + conflicts);	
	
	}
	public int calculateOverallConflicts(){
		int totalConflicts =0;
		for(int i=0;i<studentList.size();i++){
			totalConflicts+=studentList.get(i).calculateConflictInd();
				
		}
		return totalConflicts;
		
		
		
		
		
	}				
		
		
				
			
			
}	
		
				
	
			


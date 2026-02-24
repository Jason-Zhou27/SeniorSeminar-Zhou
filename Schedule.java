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
	private double conflictPerS = 0.0;
	private int numGaps;
	
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
	/*The method readFileStudent uses a Scanner to read a txt file; it splits each line of a Student's info
	 * into individual pieces/elements stored in an array. This method then extracts individual elements 
	 * from the array and stores them into variables--which are used in the Student constructor to create
	 * Student objects. This method does not load course requests in the constructor but rather after the objects
	 * are created w/ Student object methods. It accesses the recently created Student object and loads all 
	 * the course requests available into the Student's ArrayList of course requests. This method employs
	 * a try catch structure to catch any File error
	*/
	public void readFileStudent() {
		try {
			Scanner scanStudent = new Scanner(new File(fileNameStudent));//Scanner object for student
			scanStudent.nextLine(); //skips over first line (column names)
			while (scanStudent.hasNext()){
				String lineStudent = scanStudent.nextLine();
				//extract info from line
				String[] elementsStudent = lineStudent.split(",");
				
				//email and name info
				String emailStudent = elementsStudent[3];
				String emailPrefix = elementsStudent[4];
				String nameStudent = elementsStudent[5];
				//following lines are commented out as they referenced magic numbers (courses 1-5)
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
				if (timeStamp.equals("")){ //if there is no time recorded, create student object w/ time object of greatest time (100 for all time measurements)
					//studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100), course1, course2, course3, course4, course5));
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100)));
					//System.out.println("Constructor WAS CALLED");
					
				} else {	//if there is a time recorded, create student object w/ time object provided by form info
					String[] timeStampDE = timeStampD.split("/"); //time stamp date elements
					int month = Integer.parseInt(timeStampDE[0]);
					int day = Integer.parseInt(timeStampDE[1]);
					int year = Integer.parseInt(timeStampDE[2]);
					
					String timeStampT = elementsStudent[2];
					String[] timeStampTE = timeStampT.split(":"); //time stamp time elements (more specific than just date)
					int hour = Integer.parseInt(timeStampTE[0]);
					int minute = Integer.parseInt(timeStampTE[1]);
					int second = Integer.parseInt(timeStampTE[2]);
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(month, day, year, hour, minute, second)));
					//System.out.println("Constructor WAS CALLED");
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
					studentList.get(studentList.size()-1).addCourseReq(courseAdd); //add course requests for recently created student object
				}
				
			}	
		} catch (FileNotFoundException e){ //catch File error
				System.out.println("File Not Found!");
			}			
	}
	/*The method readFileCourse uses a Scanner to read a txt file; it splits each line of a Course's info
	 * into individual pieces/elements stored in an array. This method then extracts individual elements 
	 * from the array and stores them into variables--which are used in the Course constructor to create
	 * Course objects. This method employs a try catch structure to catch any File error.
	*/
	public void readFileCourse() {
		try {
			Scanner scanCourse = new Scanner(new File(fileNameCourse)); //Scanner object for course
			scanCourse.nextLine(); //skips over first line (column names)
			while (scanCourse.hasNext()){
				String lineCourse = scanCourse.nextLine();
				//extract info from line
				String[] elementsCourse = lineCourse.split(",");
				//course name
				String nameCourse = elementsCourse[0];
				//course id
				int idCourse = Integer.parseInt(elementsCourse[1]);
				//course teacher name
				String nameTeacher = elementsCourse[2];
				courseList.add(new Course(nameTeacher, nameCourse, idCourse)); //create course object with info from created variables
				}	
			} catch (FileNotFoundException e){ //catch File error
				System.out.println("File Not Found!");
			}			
	}
	public void placeCourses(){ //takes charge in placing courses
		//fillBlankRequests();
		findPop();
		findDemand();
		assignPriority();
		sortCourses();
		fillBlankRequests();
		loadRoster();
		duplicateCourses();
		//System.out.println("Course List Size is: " + courseList.size());
		//printCourses();
		for(int i=0;i<maxSpots;i++){
			
			int[] cPlacement = findOptimalPlace(courseList.get(i));
			int timeBlock = cPlacement[0]+1; //time in 2d array
			int classroom = cPlacement[1]+1; //classroom in 2d array
			seniorS[timeBlock-1][classroom-1]=courseList.get(i);
			//FOR EACH CLASS: I first remove people from the rosters of classes they cannot attend; then, I make sure each class has 16; then, I remove duplicates
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
						courseList.get(i).updateRoster();
						courseList.get(i).getStudent(courseList.get(i).getRosterSize()-1).updateScheduleDelete(timeBlock-1);
						courseList.get(i).rosterRemove();
						
						
					}
					courseList.get(i).updateRoster();
					
				}
				//System.out.println("remove attempted");	
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
				if(seniorS[row][col]==null && checkTeacherAvailability(row, c.getTeacher())==true){
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
	public boolean checkTeacherAvailability(int r, String teacher){
		int timeBlock = r+1;
		boolean free = true;
		for(int c=0; c<numClassrooms; c++){
			if(seniorS[timeBlock-1][c]!=null && seniorS[timeBlock-1][c].getTeacher().equals(teacher)){ //prefixed to prevent null error
				free=false;
				
			}	
		}
		return free;	
		
		
		
	}
	public void fillBlankRequests(){
		System.out.println("fillBlankRequests is being called");
		for(int i=0;i<studentList.size();i++){
			Student s = studentList.get(i);
			if (s.getNumRequests()<coursesPerS){
				int numFilled = coursesPerS-s.getNumRequests();
				for(int k=0;k<numFilled;k++){
					s.addCourseReq(courseList.get(courseList.size()-1-k)); //don't make these people who have not filled out forms compete with those who did
					System.out.println("I'm GETTING ADDED to fill in the BLANK");
				}	
			}		
		}		
		
	}		
	public void fillGaps(){
		boolean filled;
		Course[] schedule;
		numGaps=0;
		for(int i=0; i<studentList.size();i++){
			Student s = studentList.get(i);
			schedule = s.getSchedule();
			for(int k=0;k<schedule.length;k++){
				if(schedule[k]==null){
					System.out.println("there's a spot!");
					filled = false;
					int timeBlock = k+1;
					for(int c=0;c<numClassrooms && filled==false;c++){ //c for column
						if(seniorS[timeBlock-1][c].getRosterSize()<maxStudents){
							seniorS[timeBlock-1][c].updateRoster(s);
							s.updateSchedule(timeBlock-1, seniorS[timeBlock-1][c]);
							filled=true;
							
						}	
						
					}
					if(filled==false){
						numGaps=numGaps+1;
						System.out.println("not filled!");
					} else {
						System.out.println("filled!");
						
					}		
					
					
				}	
			}
		}	
	}	
	public void duplicateCourses(){
		Course c;
		Course cCopy;
		double quotient=0.0;
		int multiple =0; //placeholder value
		for(int i=0;i<courseList.size();i=i+multiple){
			c = courseList.get(i);
			//System.out.println("Course size: " + c.getRosterSize() + "\nmaxStudents:" + maxStudents);
			if(c.getRosterSize()>maxStudents || courseList.size()<maxSpots){
				quotient = (double)c.getRosterSize()/maxStudents;
				multiple = (int)(quotient+0.5); //round to nearest int
				if(multiple>1 || courseList.size()<maxSpots){
					multiple=2;
				}	
					
						
				
				//System.out.println(multiple);
				
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
		for(int i=0; i<courseList.size();i++){
			if(courseList.get(i).getID()==idCourse && i!=pos){
				for(int k=0; k<placed.size(); k++){
					System.out.println("remove line 231 works");
					courseList.get(i).rosterRemove(placed.get(k));
					courseList.get(i).updateRoster();
					
					
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
		return new Course("DNE");	
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
	
	}
	public void calculateOverallConflicts(){
		conflicts = 0;
		for(int i=0;i<studentList.size();i++){
			conflicts+=studentList.get(i).calculateConflictInd();
				
		}
			
	}
	public void calculateConflictPerStudent(){
		conflictPerS = (double)conflicts/studentList.size();
		
		
	}
	public void printOverview(){
		printAllRosters();
		calculateOverallConflicts();
		calculateConflictPerStudent();
		System.out.println("Number of Conflicts: " + conflicts);
		System.out.println("Conflicts Per Student: " + conflictPerS);
		System.out.println("Number of Gaps: " + numGaps);
		printFree();
		printCheckDuplicate();
		
		
		
	}
	public void printFree(){ //more of a debugging tool as of 2/23
		int trackerTotal = 0;
		for(int i=0;i<numTimes;i++){
			int tracker =0;
			for(int k=0;k<numClassrooms;k++){
				tracker+=(maxStudents-seniorS[i][k].getRosterSize());
				
			}
		System.out.println("For time " + (i+1) + " there are " + tracker + "spots free");
		trackerTotal+=tracker;	
		}
		System.out.println("There are " + trackerTotal + " spots free total, and there are supposed to be " + (numClassrooms*maxStudents-studentList.size())*numTimes);
	}
	public void printCheckDuplicate(){ //debugging
		boolean isDuplicate = false;
		for(int i=0;i<courseList.size();i++){
			for(int k=0; k<courseList.size();k++){
				if(i!=k && courseList.get(i).getID()==courseList.get(k).getID()){
					for(int j=0;j<courseList.get(i).getRosterSize();j++){
						for(int l=0;l<courseList.get(k).getRosterSize();l++){
							if(courseList.get(i).getStudent(j)==courseList.get(k).getStudent(k)){
								isDuplicate = true;
							}	
						}			
					}			
				}				
			}		
		}
		System.out.println("isDuplicate?: " + isDuplicate);
				
			
			
			
			
			
	}
	public void printAllRosters(){
		for(int i=0;i<maxSpots;i++){
			System.out.print("\nCourse: " + courseList.get(i).getName() + "\n\nRoster:");
			courseList.get(i).printRoster();
		}
			
		
		
		
			
		
		
		
		
	}		
						
		
		
				
			
			
}	
		
				
	
			


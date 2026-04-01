//import libraries
import java.util.*;
import java.io.*;

public class Schedule{
	//variables
	
	//files, arrays, arrayLists
	private String fileNameStudent = "studentInfo.txt"; //Name of txt file with Student info
	private String fileNameCourse = "courseInfo.txt"; //Name of txt file with Student info
	private ArrayList<Student> studentList = new ArrayList<Student>(); //ArrayList to store student objects
	private ArrayList<Course> courseList = new ArrayList<Course>(); //ArrayList to store course objects
	private Course[][] seniorS;
	
	//important parameters
	private int numTimes;
	private int coursesPerS; //number of courses per student
	private int numClassrooms;
	private int maxStudents;
	private int maxSpots;
	private int maxSections;
	
	//effectiveness values
	private int conflicts = 0;
	private double conflictPerS = 0.0;
	private int numGaps;
	
	//constructors
	public Schedule(int nT, int nCPS, int nC, int mS, int mSx){
		numTimes = nT;
		coursesPerS = nCPS;
		maxStudents = mS;
		numClassrooms = nC;
		maxSpots=nT*nC;
		maxSections = mSx;
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
				//student id
				int studentId = studentList.size()+1;
				
				//time info
				String timeStamp = elementsStudent[0];
				String timeStampD = elementsStudent[1];
				if (timeStamp.equals("")){ //if there is no time recorded, create student object w/ time object of greatest time (100 for all time measurements)
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100)));
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
				}	
				for(int i=0; i<coursesPerS;i++){ //load courses for students
					int courseIndex = Integer.parseInt(elementsStudent[6+i]);
					Course courseAdd = getCourse(courseIndex);
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
		findPop();
		findDemand();
		assignPriority();
		sortCourses();
		loadRoster();
		duplicateCourses();
		//Twyford's reference schedule on the whiteboad 3/13; it's difficult to integrate into my program
		//because my algorithm works in a very different manner--if I were to implement this algorithm, it would take revamping 
		//of my program infrastructure
		//Nevertheless, without Twyford's reference schedule, I still figure out that most if not all can get at least 3 courses
		//int[][] seniorSID = {{1,9,14,5,115},{2,6,10,12,116},{15,3,11,4,107},{16,18,13,101,109},{7,8,17,102,106}};  
		
		//for how many courses I can place...
		for(int i=0;i<maxSpots;i++){
			
			int[] cPlacement = findOptimalPlace(courseList.get(i));
			int timeBlock = cPlacement[0]+1; //time in 2d array
			int classroom = cPlacement[1]+1; //classroom in 2d array
			while(timeBlock==-1 && classroom==-1){
				courseList.remove(i);
				cPlacement = findOptimalPlace(courseList.get(i));
				timeBlock = cPlacement[0]+1; //time in 2d array
				classroom = cPlacement[1]+1; //classroom in 2d array
				
			}	
			seniorS[timeBlock-1][classroom-1]=courseList.get(i);
			studentHandling(i, timeBlock, classroom);
		}
		searchDelete(); //deletes students from the rosters of courses they do not take
	}
	public void studentHandling(int i, int timeBlock, int classroom){
		//for each class
			Course c = courseList.get(i);
			for(int k=0;k<courseList.get(i).getRosterSize();k++){
				//try to update their schedule w/ the course
				c.getStudent(k).updateSchedule(timeBlock-1, courseList.get(i));
				Course[] s = courseList.get(i).getStudent(k).getSchedule();
				boolean matched = false;
				for(int j=0; j<s.length;j++){
					if(s[j]==c){
						matched = true;
					}	
				}
				if(matched==false){
					//c.printRosterSimple();
					//System.out.println(courseList.get(i).getStudent(k).getName() + " is removed from " + courseList.get(i).getName() + " because of conflict");
					c.rosterRemove(c.getStudent(k));
					//c.printRosterSimple();
				}		
			}
			int numRemove = c.getRosterSize()-maxStudents; //finds how many students to remove
			for(int k=0;k<numRemove;k++){ //for loop goes through # of iterations it takes to get students to proper capacity
				Student studentRemove = studentRemove(c);
				//System.out.println(studentRemove.getName() + " is removed from " + courseList.get(i).getName() + " because of overflow");
				studentRemove.updateScheduleDelete(timeBlock-1); //deletes course from removed student's schedule
				//courseList.get(i).printRosterSimple();
				c.rosterRemove(studentRemove); //removes student from roster
				//courseList.get(i).printRosterSimple();
			}
			removeDuplicateStudents(c.getRoster(), c.getID(), i, timeBlock); //removes student from the same courses (just diff sections)
	}	
	public Student studentRemove(Course c){
		ArrayList<Student> r = c.getRoster();
		int highest=r.get(0).getRanking(c);
		Student toRemove = r.get(0);
		int studentRanking;
		for(int i=0;i<r.size();i++){
			studentRanking = r.get(i).getRanking(c);
			if(studentRanking>highest){
				highest = studentRanking;
				toRemove = r.get(i);	
			}	
		}
		return toRemove;	
	}	
	 //deletes students from the rosters of courses they do not take
	public void searchDelete(){
		for(int i=0; i<studentList.size();i++){
			Course[] s = studentList.get(i).getSchedule();
			for(int k=0; k<maxSpots;k++){
				boolean matched = false;
				for(int j=0; j<s.length;j++){
					if(s[j]==courseList.get(k)){
						//System.out.println("It's A MATCH");
						matched = true;
					}	
				}
				if(matched==false){
					courseList.get(k).rosterRemove(studentList.get(i),i);
				}		
			}	
		}	
	}	
	public int[] findOptimalPlace(Course c){ //assists placeCourses method by finding optimal position in 2d course array & returning it
		int optRow = -2; //not magic numbers * there is not a -1 row/column, which is why -2 is used (-2 eventually has 1 added to it in another method for intuitive understanding)
		int optCol = -2; //not magic numbers * there is not a -1 row/colum, which is why -2 is used (-2 eventually has 1 added to it in another method for intuitive understanding)
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
	public void fillGaps(){
		boolean filled;
		Course[] schedule;
		Student s;
		numGaps=0;
		//for each student
		for(int i=0; i<studentList.size();i++){
			s = studentList.get(i);
			schedule = s.getSchedule();
			//for each block in their schedule
			for(int k=0;k<schedule.length;k++){
				//if their schedule has an empty spot
				if(schedule[k]==null){
					//set a boolean filled to convey that the block is not filled
					filled = false;
					int timeBlock = k+1;
					//search the courses at that time and see if there is availability
					for(int c=0;c<numClassrooms;c++){ //c for column
						if(seniorS[timeBlock-1][c].getRosterSize()<maxStudents && filled==false){
							seniorS[timeBlock-1][c].updateRoster(s); //updates the course's roster
							s.updateSchedule(timeBlock-1, seniorS[timeBlock-1][c]); //updates the student's schedule
							//System.out.println(s.getID() + ": " + "filled"); //debugging
							filled=true;
						}	
					}
					if(filled==false){
						numGaps=numGaps+1;
					}
				}	
			}
		}	
	}	
	public void duplicateCourses(){
		Course c;
		Course cCopy;
		int fit = 0;
		int multiple =0; //placeholder value
		for(int i=0;i<courseList.size();i=i+multiple){
			c = courseList.get(i);
			if(c.getRosterSize()>maxStudents || courseList.size()<maxSpots*maxSections){
				fit = c.getRosterSize()/maxStudents;
				int overflow = c.getRosterSize()-fit*maxStudents;
				//comparing to "what could have been" to see if duplication is worth it
				int compared;
				if(courseList.size()<maxSpots){
					compared = courseList.size()-1;	
				}
				else {
					compared = maxSpots-1;
					
				}		
				if(overflow>courseList.get(compared).getRosterSize()){
					multiple=fit+1;
				}
				else {
					multiple=fit;
					
				}		
				if(multiple>1 || courseList.size()<maxSpots*maxSections){
					multiple=2;
				}	
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
	public void removeDuplicateStudents(ArrayList<Student> r, int idCourse, int pos, int timeBlock){ //this method removes the students placed in a course from the other section
		ArrayList<Student> placed = r;
		for(int i=0; i<courseList.size();i++){
			if(courseList.get(i).getID()==idCourse && i!=pos){
				for(int k=0; k<placed.size(); k++){	
					courseList.get(i).rosterRemove(placed.get(k));
					courseList.get(i).updateRoster();
				}
			}	
		}	
	}	
	public Course getCourse(int idCourse){
		//traverse course arraylist
		if(idCourse==0){ //for students who do not fill out form
			return null;
		}	
		for(int i=0; i<courseList.size(); i++){
			int currentID = courseList.get(i).getID();
			if(currentID==idCourse){
				return courseList.get(i);		
			}	
		}
		return null;	
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
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updatePopRating(coursesPerS-k);
				}
			}
		}	
	}
	public void findDemand(){
		for(int i=0; i<studentList.size();i++){
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updateDemand();
				}
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
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updateRoster(studentList.get(i));
				}
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
		//not all students made requests
		int counter=0;
		for(int i=0; i<studentList.size();i++){
			if(studentList.get(i).madeRequest()==true){
				counter++;
			}
		}	
		conflictPerS = (double)conflicts/counter;
	}
	public void printOverview(){
		//printAllRosters();
		calculateOverallConflicts();
		calculateConflictPerStudent();
		System.out.println("Number of Conflicts: " + conflicts);
		System.out.println("Conflicts Per Student: " + conflictPerS);
		System.out.println("Number of Gaps: " + numGaps);
		//printFree();
	}
	public void printFree(){ //more of a debugging tool as of 2/23; still might be needed so I will keep it here
		int trackerTotal = 0;
		for(int i=0;i<numTimes;i++){
			int tracker =0;
			for(int k=0;k<numClassrooms;k++){
				tracker+=(maxStudents-seniorS[i][k].getRosterSize());
				
			}
		System.out.println("For time " + (i+1) + " there are " + tracker + " spots free");
		trackerTotal+=tracker;	
		}
		System.out.println("There are " + trackerTotal + " spots free total, and there are supposed to be " + (numClassrooms*maxStudents-studentList.size())*numTimes);
	}
	
	public void printAllRosters(){
		for(int i=0;i<maxSpots;i++){
			System.out.print("\nCourse: " + courseList.get(i).getID() + "\n\nRoster:");
			courseList.get(i).printRosterSimple();
		}
	}		
	public Student getStudent(int i){
		return studentList.get(i);
		
	}
	
}	

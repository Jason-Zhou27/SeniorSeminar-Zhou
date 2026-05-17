/**
 * Schedule.java
 * @author Jason Zhou
 * @since (date) 02/09/2026
 * This class outlines schedule class which handles main functions of the program
*/

//import libraries
import java.util.*; //Scanner + ArrayLists
import java.io.*; //File
/*
 * Schedule class handles main functions (loads data, organizes, optimization)
*/ 
public class Schedule{
	//variables
	
	//files, arrays, arrayLists
	private String fileNameStudent; //Name of txt file with Student info
	private String fileNameCourse; //Name of txt file with Student info
	private ArrayList<Student> studentList = new ArrayList<Student>(); //ArrayList to store student objects
	private ArrayList<Course> courseList = new ArrayList<Course>(); //ArrayList to store section objects
	private ArrayList<Course> courseListOriginal = new ArrayList<Course>(); //ArrayList to store course objects (no duplicates)
	private Course[][] seniorS;//2d array of courses with rows being times and columns being classrooms
	
	//important conditions
	private int numTimes; //number of times/time blocks
	private int coursesPerS; //number of courses per student
	private int numClassrooms; //number of classrooms available
	private int maxStudents; //max students per seminar/class
	private int maxSpots; // number of seminars/classes that will run throughout the schedule (can include repeats)
	private int maxSections; //max number of repeats of same seminar/class
	
	//effectiveness values
	private int conflicts; //tracks total conflicts
	private double conflictPerS; //tracks average conflicts per student
	private int numGaps; //tracks empty spots in schedules
	
	//constructor
	/*
	 * Schedule constructor initializes the conditions for a specific schedule using parameters;
	 * parameters include number of times, max students, number of classrooms, and max sections;
	 * It initializes file name variables. It sets conflicts at 0 and conflicts per student at 0.0.
	*/
	public Schedule(int nT, int nC, int mS, int mSx){ //parameter names are abreviations of what they represent
		fileNameStudent = "studentInfo.txt";
		fileNameCourse = "courseInfo.txt";
		numTimes = nT;
		coursesPerS = nT;
		maxStudents = mS;
		numClassrooms = nC;
		maxSpots=nT*nC;
		maxSections = mSx;
		Student.setCoursesPerStudent(coursesPerS);
		readFileCourse();
		readFileStudent();
		seniorS = new Course[numTimes][numClassrooms];
		conflicts = 0;
		conflictPerS = 0.0;
	}	
	//methods
	/*readFileStudent reads the student txt file with a Scanner and a try catch structure. For each line available, 
	 * data will be separated into individual elements stored in variables; these variables are later used in the
	 * Student constructor. Course requests are inserted not in the Student constructor call but instead through
	 * Student methods.
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
				//time info --> it is important to note that the program does not currently use time in any capacity
				String timeStamp = elementsStudent[0];
				String timeStampD = elementsStudent[1];
				if (timeStamp.equals("")){ //if there is no time recorded, create student object w/ time object of greatest time (100 for all time measurements)
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(100,100,100,100,100,100)));
				} else {	//if there is a time recorded, create student object w/ time object provided by form info
					String[] timeStampDE = timeStampD.split("/"); //time stamp date elements (month, day, year)
					int month = Integer.parseInt(timeStampDE[0]);
					int day = Integer.parseInt(timeStampDE[1]);
					int year = Integer.parseInt(timeStampDE[2]);
					String timeStampT = elementsStudent[2];
					String[] timeStampTE = timeStampT.split(":"); //time stamp time elements (hour, minute, second)
					int hour = Integer.parseInt(timeStampTE[0]);
					int minute = Integer.parseInt(timeStampTE[1]);
					int second = Integer.parseInt(timeStampTE[2]);
					studentList.add(new Student(studentId, nameStudent, emailStudent, new Time(month, day, year, hour, minute, second)));
				}	
				for(int i=0; i<coursesPerS;i++){ //load courses for students
					int courseIndex = Integer.parseInt(elementsStudent[6+i]);
					Course courseAdd = getCourse(courseIndex);
					studentList.get(studentList.size()-1).addCourseReq(courseAdd); //add course requests for recently created student object with Student method addCourseReq()
				}
			}	
		} catch (FileNotFoundException e){ //catch File error
				System.out.println("File Not Found!");
			}			
	}
	/*readFileCourse reads the course txt file with a Scanner and a try catch structure. For each line available, 
	 * data will be separated into individual elements stored in variables; these variables are later used in the
	 * Course constructor.
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
			for(int i=0;i<courseList.size();i++){
				courseListOriginal.add(courseList.get(i));
			}	
		} catch (FileNotFoundException e){ //catch File error
			System.out.println("File Not Found!");
		}			
	}
	/*
	 * placeCourses places courses in the 2d senior seminar array; it places courses based on the optimal spot--determiend in findOptimalPlace method
	*/ 
	public void placeCourses(){
		findPop(); //evaluates course popularity with weight (choice #); lower choice --> greater weight
		findDemand(); //overall number of requests
		assignPriority(); //creates priority rating from popularity and demand
		sortCourses(); //sorts courses lin courseList so courses earlier in the list have higher priority ratings
		loadRoster(); //load the roster for each course
		duplicateCourses(); //duplicate high priority courses until max course number is reached
		for(int i=0;i<maxSpots && i<courseListOriginal.size()*maxSections && i<courseList.size();i++){
			
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
		searchDelete(); //deletes students from the rosters of courses they do not take; ideally, should not be implemented but it is b/c somehow students are not getting removed from course rosters
	}
	/*
	 * studentHandling manages much of the student side of organizing a schedule; it will update a student's schedule (or attempt to), remove students from courses if capacity is exceeded, and oversee removing duplicate students in different sections.
	 * index (i) of course, time block, and classroom are the parameters
	*/
	public void studentHandling(int i, int timeBlock, int classroom){
			Course c = courseList.get(i);
			for(int k=0;k<courseList.get(i).getRosterSize();k++){ //go through each student in course's roster
				//try to update the student's schedule w/ the course
				if(c.getStudent(k).updateSchedule(timeBlock-1, courseList.get(i))==false){
					c.rosterRemove(c.getStudent(k));
				}
			}
			int numRemove = c.getRosterSize()-maxStudents; //finds how many students to remove
			for(int k=0;k<numRemove;k++){ //for loop goes through # of iterations it takes to get students to proper capacity
				Student studentRemove = studentRemove(c);
				studentRemove.updateScheduleDelete(timeBlock-1); //deletes course from removed student's schedule
				c.rosterRemove(studentRemove); //removes student from roster
			}
			removeDuplicateStudents(c, c.getID(), i, timeBlock); //removes student from the same courses (just diff sections)
	}
	/*
	 * studentRemove uses students' ranks of a course to decide who is removed (over capacity); student return type; Course parameter;
	 * it uses same algorithm as finding min value;
	 * overloaded
	*/	
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
	/*
	 * studentRemove uses students' ranks of a course to decide who is x-best candidate to be removed; student return type; 
	 * Course and int parameter;
	 * it uses a bubble sort algorithm;
	 * overloaded
	*/	
	public Student studentRemove(Course c, int x){
		ArrayList<Student> r = c.getRoster();
		ArrayList<Student> removeList = new ArrayList<Student>(); //have to create new ArrayList b/c I don't want to modify og
		for(int i=0;i<r.size();i++){
			removeList.add(r.get(i));
		}	
		do {	
			for(int i=0;i<removeList.size()-1;i++){
				if(removeList.get(i).getRanking(c)<removeList.get(i+1).getRanking(c)){
					Student temp = removeList.get(i+1); 
					removeList.remove(i+1);
					removeList.add(i,temp);
				}
			}
		} while(sortStudentRemoveCheck(removeList, c)==false);
		return removeList.get(x);	
	}
	/*
	 * sortStudentRemoveCheck supplements studentRemove (w/ 2 parameters) to check if the removeList is sorted by ranking; 
	 * takes in arraylist parameter and a course parameter;
	 * returns boolean true for sorted and false for not sorted
	*/
	public boolean sortStudentRemoveCheck(ArrayList<Student> removeList, Course c){
		boolean sorted = true;
		for(int i=0;i<removeList.size()-1;i++){
			if(removeList.get(i).getRanking(c)<removeList.get(i+1).getRanking(c)){
				sorted=false;
			}	
		}
		return sorted;
	}	
	/*
	 * searchDelete removes students from any courses they do not take; this method is a substitute to finding the
	 * error which causes students to be not removed from rosters
	*/
	public void searchDelete(){
		for(int i=0; i<studentList.size();i++){
			Course[] s = studentList.get(i).getSchedule();
			for(int k=0; k<maxSpots && k<courseListOriginal.size()*maxSections && k<courseList.size();k++){
				boolean matched = false;
				for(int j=0; j<s.length;j++){
					if(s[j]==courseList.get(k)){
						matched = true;
					}	
				}
				if(matched==false){
					courseList.get(k).rosterRemove(studentList.get(i),i);
				}		
			}	
		}	
	}
	/*
	 * findOptimalPlace takes in a course parameter to find the optimal place in the 2d
	 * senior seminar array that would result in the fewest conflicts; it employs a counter
	 * and variables to store the optimal place (optRow and optCol)
	*/	
	public int[] findOptimalPlace(Course c){ //assists placeCourses method by finding optimal position in 2d course array & returning it
		//not magic numbers * there is not a -1 row/column, which is why -2 is used (-2 has 1 added to it in the method that calls findOptimalPlace)
		int optRow = -2; 
		int optCol = -2; 
		int counter;
		int fewestConflicts=Integer.MAX_VALUE; //Integer class
		int rowMax = numTimes;
		int colMax = numClassrooms;
		for(int row=0;row<rowMax;row++){
			for(int col=0;col<colMax;col++){
				if(seniorS[row][col]==null && checkTeacherAvailability(row, c.getTeacher())==true){ //must ensure that the teacher is not teaching at the same time
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
	/*
	 * checkTeacherAvailability ensures that the teacher is not already teaching in a certain time block;
	 * it searches the columns of the 2d senior seminar array as a row represents a time block and columns represent classrooms;
	 * boolean return type--true means that the teacher is available and false means that the teacher is not available
	*/
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
	/*
	 * fillGaps ensures that all students have full schedules; some have not been loaded into courses because
	 * they did not fill out thier ranked choices or because choices were not compatible with their schedules
	*/
	public void fillGaps(){
		boolean filled;
		Course[] schedule;
		Student s;
		numGaps=0;//tracks numGaps
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
						if(seniorS[timeBlock-1][c]!=null&&seniorS[timeBlock-1][c].getRosterSize()<maxStudents && filled==false){
							seniorS[timeBlock-1][c].updateRoster(s); //updates the course's roster
							if(s.updateSchedule(timeBlock-1, seniorS[timeBlock-1][c])==false){
								seniorS[timeBlock-1][c].rosterRemove(s);
								filled=false;
							} else {	
								filled=true;
							}
						}	
					}
					if(filled==false){
						reduceGaps(s, schedule, timeBlock-1);
					}
				}	
			}
		}	
	}
	/*
	 * duplicateCourses duplicates courses (starting with most popular) to ensure that all available class spots are filled
	*/	
	public void duplicateCourses(){
		Course c;
		Course cCopy; //duplicated or copied course
		int fit = 0;
		int multiple =0; //placeholder value
		for(int i=0;i<courseList.size();i=i+multiple){
			c = courseList.get(i);
			if(c.getRosterSize()>maxStudents || courseList.size()<maxSpots*maxSections){
				fit = c.getRosterSize()/maxStudents;
				int overflow = c.getRosterSize()-fit*maxStudents;
				//comparing to "what could have been" to see if duplication is worth it
				int compared; //index of course in courseList that will be compared
				if(courseList.size()<maxSpots){
					compared = courseList.size()-1;	//if number of sections is less than total available sections, compare with least priority course
				}
				else {
					compared = maxSpots-1; //if number of sections is equal to or exceeds total available sections, compare with the least priority course that is set to run
				}		
				if(overflow>courseList.get(compared).getRosterSize()){
					multiple=fit+overflow/courseList.get(compared).getRosterSize();
				}
				else {
					multiple=fit;
				}		
				if(multiple>1 || courseList.size()<maxSpots*maxSections){
					multiple=maxSections; //can't duplicate more than maxSections
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
	/*
	 * removeDuplicateStudents removes the students placed in a course from the other section; it has course placed, id of course, position/index
	 * of course in the courseList, and time block as parameters
	*/
	public void removeDuplicateStudents(Course placed, int idCourse, int pos, int timeBlock){ //pos represents position in courseList ArrayList where student was placed
		for(int k=0; k<placed.getRoster().size(); k++){	
			for(int i=0; i<courseList.size();i++){
				if(courseList.get(i).getID()==idCourse && i!=pos){
					courseList.get(i).rosterRemove(placed.getRoster().get(k));
				}	
			}
		}	
	}
	/*
	 * getCourse returns the Course object for a particular course id; it serves as to associate course objects with students rather than course ids with students
	*/	
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
	/*
	 * printStudents prints out the toStrings of each student
	*/
	public void printStudents(){
		for (int i=0;i<studentList.size();i++){
			System.out.println(studentList.get(i).toString());		
		}	
		
	}
	/*
	 * printCourses prints out the toStrings of each course
	*/
	public void printCourses(){
		for (int i=0;i<courseList.size();i++){
			System.out.println(courseList.get(i).toString());		
		}	
	}
	/*
	 * findPop updates the popularity rating for each course
	 * by cycling through each student's ranked choices (and taking into account the rating)
	*/
	public void findPop(){
		for(int i=0; i<studentList.size();i++){
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updatePopRating(coursesPerS-k);
				}
			}
		}	
	}
	/*
	 * findDemand updates the demand for each course
	 * by cycling through each student's choices
	*/
	public void findDemand(){
		for(int i=0; i<studentList.size();i++){
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updateDemand();
				}
			}
		}	
	}
	/*
	 * assignPriority creates a priority rating for each course based on 
	 * demand and popularity; prioirty rating is used to determine order of
	 * placement/order in the courseList ArrayList
	*/
	public void assignPriority(){
		for(int i=0; i<courseList.size();i++){
			courseList.get(i).updatePriorityRating();
		}		
	}
	/*
	 * sortCourses sorts courses so those with highest priority are at the beginning of the arrayList courseList; it 
	 * employs a bubble sort algorithm
	*/
	public void sortCourses(){
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
	/*
	 * wortCCheck supplements sortCourses to check if the courseList is sorted by priority; returns true for sorted and false for not sorted
	*/
	public boolean sortCCheck(){
		boolean sorted = true;
		for(int i=0;i<courseList.size()-1;i++){
			if(courseList.get(i).getPR()<courseList.get(i+1).getPR()){
				sorted=false;
			}	
		}
		return sorted;
	}
	/*
	 * loadRoster uses updateRoster method from Course class to add students to each of their courses' rosters
	*/
	public void loadRoster(){
		for(int i=0; i<studentList.size();i++){
			for(int k=0;k<coursesPerS;k++){
				if(studentList.get(i).getCourse(k+1)!=null){
					studentList.get(i).getCourse(k+1).updateRoster(studentList.get(i));
				}
			}
		}	
	}
	/*
	 * printSeniorSeminar prints the 2d array of courses for rows being time blocks and columns being classrooms
	*/
	public void printSeniorSeminar(){
		int maxDigits; //used for spacing
		int largestID; //used for spacing
		largestID=courseListOriginal.size();
		maxDigits=0;
		while(largestID>0){
			largestID/=10;
			maxDigits++;
		}		
		int maxRow=numTimes;
		int maxCol=numClassrooms;
		System.out.print("\n");
		System.out.println("2d array of running course ids; rows = time blocks and columns = classrooms");
		System.out.print("    ");
		for(int c=0; c<maxCol;c++){	
			int idFindDigits = c+1;
			int digits = 0;
			while(idFindDigits>0){
				idFindDigits/=10;
				digits++;
			}
			for(int i=0; i<maxDigits-digits-1;i++){ //-1 b/c c is a character
				System.out.print(" ");
			}
			System.out.print(" c" + (c+1) + " ");
		}	
		System.out.print("\n");
		for(int r=0;r<maxRow;r++){
			System.out.print("t" + (r+1) + ": ");
			for(int c=0;c<maxCol;c++){
				if(seniorS[r][c]!=null){
					int idFindDigits = seniorS[r][c].getID();
					int digits = 0;
					while(idFindDigits>0){
						idFindDigits/=10;
						digits++;
					}
					for(int i=0; i<maxDigits-digits;i++){
						System.out.print(" ");
					}	
					System.out.print(" " + seniorS[r][c].getID() + " ");
				}
				else {
					for(int i=0; i<maxDigits-2;i++){ //2 comes from -1 having 2 characters
						System.out.print(" ");
					}
					System.out.print(" " + -1 + " ");
				}		
			}
			System.out.println();	
		}
		
		largestID=courseList.size();
		maxDigits=0;
		while(largestID>0){
			largestID/=10;
			maxDigits++;
		}
		System.out.print("\n");
		System.out.println("2d array of running section ids; rows = time blocks and columns = classrooms");
		System.out.print("     ");
		for(int c=0; c<maxCol;c++){
			System.out.print("c" + (c+1) + "  ");	
		}
		System.out.print("\n");
		for(int r=0;r<maxRow;r++){
			System.out.print("t" + (r+1) + ": ");
			for(int c=0;c<maxCol;c++){
				if(seniorS[r][c]!=null){	
					System.out.print(" " + seniorS[r][c].getSectionID() + " ");
				}
				else {
					System.out.print("  " + -1 + " ");
				}		
			}
			System.out.println();	
		}	
		System.out.print("\n");
	}
	/*
	 * calculateOverallConflcits sums the conflicts for each student (# of courses from requests that do not appear in schedule)
	*/
	public void calculateOverallConflicts(){
		conflicts = 0;
		for(int i=0;i<studentList.size();i++){
			conflicts+=studentList.get(i).calculateConflictInd();	
		}	
	}
	/*
	 * calculateConflictPerStudent calculates the average conflicts per student (that made requests)
	*/
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
	/* printOverview calls the methods which calculate conflict and conflict per student; it prints out important
	 * info that signals effectiveness of the program (number of conflicts, avg conflicts, gaps)
	*/
	public void printOverview(){
		System.out.print("\n");
		calculateOverallConflicts();
		countGaps();
		calculateConflictPerStudent();
		System.out.println("Number of Conflicts: " + conflicts);
		System.out.println("Conflicts Per Student: " + conflictPerS);
		System.out.println("Number of Gaps: " + numGaps);
		System.out.print("\n");
	}
	/*
	 * RETIRED!!! printFree was a debugging tool that signaled how many total available spots were in the schedule (where students could be placed)
	*/
	public void printFree(){
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
	/*
	 * reduceGaps method finds students with gaps and reduces those gaps by either swapping when the student takes a certain course (preferred method)
	 * or switching the student's course with another student's course (last ditch b/c results in more conflicts); it takes Student, Course[], and int as parameters
	*/
	public void reduceGaps(Student stud, Course[] schedule, int timeIndex){
		int classroom=-1;
		Course sectionNew = null; //sectionNew is the new section a student will attempt to join
		Course sectionOld = null; //sectionOld is the old section a student will attempt to join
		boolean filled = false; //dictates whether student's schedule is filled
		for(int c=0;c<numClassrooms;c++){ //c for column
			if(seniorS[timeIndex][c].getRosterSize()<maxStudents && filled==false){
				if(stud.updateSchedule(timeIndex, seniorS[timeIndex][c])==false){ //there is space but the student can't take course b/c taking another section
					seniorS[timeIndex][c].rosterRemove(stud); //temp remove student from new roster he/she tries to join
					classroom=c; //initialize the classroom of the course he/she tries to join (index)
					sectionNew = seniorS[timeIndex][c]; //store sectoin the student tries to join in sectionNew
				}
			}	
		}
		int timeIndexVacantNew = -1; //guaranteed to become a valid number b/c of the context of this method
		for(int i=0;i<schedule.length;i++){
			if(schedule[i]!=null && schedule[i].getID()==sectionNew.getID()){
				sectionOld=schedule[i]; //store sectino student tries to leave in sectionOld
				schedule[i]=null; //now emptied schedule there
				sectionOld.rosterRemove(stud); //now roster does not have record there
				timeIndexVacantNew = i; //new vacancy to be filled
			}
		}
		stud.updateSchedule(timeIndex, seniorS[timeIndex][classroom]); //updates student schedule w student joining the sectionNew
		sectionNew.updateRoster(stud); //updates course roster w student joining the sectionNew; essentially undos line 568 but still helps with reasoning
		//go through the classrooms in the new vacant spot and try to place a student there
		for(int c=0;c<numClassrooms;c++){
			if(seniorS[timeIndexVacantNew][c].getRosterSize()<maxStudents && filled==false){
				seniorS[timeIndexVacantNew][c].updateRoster(stud); //updates the course's roster
				if(stud.updateSchedule(timeIndexVacantNew, seniorS[timeIndexVacantNew][c])==false){
					seniorS[timeIndexVacantNew][c].rosterRemove(stud);
					filled = false;
				} else {	
					filled = true;
				}
			}	
		}
		if(filled==false){ //last ditch effort; swap with someone else
			boolean swapped = false;
			int x=0; //incrementor that decides the x-best candidate for swapping
			while(swapped==false){
				for(int c=0; c<numClassrooms && swapped==false; c++){ //keep going until someone is swapped
					Student swapCandidate = studentRemove(seniorS[timeIndexVacantNew][c],x); //get a swap candidate by finding someone with the x-most priority for the course
					boolean swapCandidateAble = true;
					Course[] swapCandidateSchedule = swapCandidate.getSchedule();
					//circulate through the swap candidate's schedule, and if the swap candidate cannot take on the course left empty, abandon swap  candidate
					for(int i=0;i<swapCandidateSchedule.length;i++){
						if(swapCandidateSchedule[i].getID()==sectionOld.getID()){
							swapCandidateAble=false;
						}
					}
					if(swapCandidateAble==true){
						//now check if the student can take on the swap candidate's course at that new vacant time 
						boolean studAble = true;
						//circulate through the conflicted individual's schedule, and if the student cannot take on the swap candidate's empty course, abandon swap
						for(int i=0;i<schedule.length;i++){
							if(schedule[i]!=null && schedule[i].getID()==seniorS[timeIndexVacantNew][c].getID()){
								studAble=false;
							}
						}
						if(studAble==true){
							//if student can swap and swap candidate can swap, adjust their rosters/schedules accordingly to make the swap
							seniorS[timeIndexVacantNew][c].rosterRemove(swapCandidate);
							seniorS[timeIndexVacantNew][c].updateRoster(stud);
							sectionOld.rosterRemove(stud);
							sectionOld.updateRoster(swapCandidate);
							
							swapCandidate.updateScheduleDelete(timeIndexVacantNew);
							stud.updateScheduleDelete(timeIndexVacantNew);
							stud.updateSchedule(timeIndexVacantNew, seniorS[timeIndexVacantNew][c]);
							swapCandidate.updateSchedule(timeIndexVacantNew, sectionOld);
							swapped=true;
						}		
					}
				}
				x++;
			}	
		}					
	}
	/*
	 * countGaps counts total number of gaps in the schedules
	*/
	public void countGaps(){
		numGaps=0;
		for(int i=0;i<studentList.size();i++){
			Course[] schedule = studentList.get(i).getSchedule();
			for(int j=0;j<schedule.length;j++){
				if(schedule[j]==null){
					numGaps++;
				}
			}
		}
	}	
	/*
	 * printAllRosters prints the rosters for all the courses that run
	*/
	public void printAllRosters(){
		for(int i=0;i<maxSpots;i++){
			System.out.print("\nCourse: " + courseList.get(i).getID() + "\n\nRoster:");
			courseList.get(i).printRosterSimple();
		}
	}
	/*
	 * getStudent fetches the student given the student's name; name is the parameter;
	 * overloaded
	*/
	public Student getStudent(String name){
		for(int i=0;i<studentList.size();i++){
			if(studentList.get(i).getName().equals(name)){
				return studentList.get(i);
			}	
			
		}
		return null;
	}
	/*
	 * getStudent fetches the student given the student's id; id is the parameter;
	 * overloaded
	*/
	public Student getStudent(int id){
		for(int i=0;i<studentList.size();i++){
			if(studentList.get(i).getID()==id){
				return studentList.get(i);
			}	
			
		}
		return null;	
	}
	/*
	 * getSection fetches the section given the section's id;
	*/
	public Course getSection(int id){
		for(int i=0;i<courseList.size();i++){
			if(courseList.get(i).getSectionID()==id){
				return courseList.get(i);
			}	
			
		}
		return null;	
	}
	/*
	 * printRunLogistics prints timeblock and classroom for a given section, and it returns a boolean
	 * signaling if the section runs; section is the parameter
	*/
	public boolean printRunLogistics(Course section){
		int timeBlock=-1;
		int classroom=-1;
		for(int t = 0; t<numTimes;t++){
			for(int c=0; c<numClassrooms;c++){
				if(seniorS[t][c]==section){	
					timeBlock = t+1;
					classroom = c+1;
				}
			}	
		}	
		if(timeBlock!=-1 && classroom!=-1){
			System.out.print("\ntime block: " + timeBlock + "\nclassroom: " + classroom + "\n");
			return true;
		}
		else {
			System.out.print("section is not running\n");
			return false;
		}		
	}	
	/*
	 * findStudent prints out info for given student
	*/
	public void findStudent(){
		Scanner s = new Scanner(System.in);
		String response = "";
		System.out.print("1 - id \n2 - name \n\nenter selection: ");
		response=s.nextLine();
		if(response.equals("1")){
			boolean cont = true;
			int id;
			int x=0; //incrementor
			while(cont==true){
				if(x!=0){
					id = Integer.parseInt(response);
					System.out.println(getStudent(id).toString());
					System.out.println("Number of conflicts: " + getStudent(id).calculateConflictInd());
				}
				System.out.print("Enter student id: ");
				response = s.nextLine();
				if(response.equals("q")){
					cont=false;
				}
				x++;	
			}
		}
		if(response.equals("2")){
			boolean cont = true;
			String name;
			int x=0; //incrementor
			while(cont==true){
				if(x!=0){
					name = response;
					Student stud = getStudent(name);
					while(stud==null){
						System.out.println("invalid name");
						System.out.print("Enter student name: ");
						response = s.nextLine();
						stud = getStudent(name);
						if(response.equals("q")){
							break;
						}
					}
					if(stud!=null){
						System.out.println(stud.toString());
						System.out.println("Number of conflicts: " + stud.calculateConflictInd());
					} else{
						break;
					}	
				}
				System.out.print("Enter student name: ");
				response = s.nextLine();
				if(response.equals("q")){
					cont=false;
				}	
				x++;	
			}
		}
	}
	/*
	 * findSection prints out info for given section
	*/
	public void findSection(){
		Scanner s = new Scanner(System.in);
		boolean cont = true;
		int id;
		int x=0; //incrementor
		String response = "";
		while(cont==true){
			if(x!=0){
				id = Integer.parseInt(response);
				System.out.println(getSection(id).toString());
				boolean running = printRunLogistics(getSection(id));
				if(running==true){
					getSection(id).printRoster();
				}
			}
			System.out.print("\nEnter section id (1-" + (courseList.size()) + "):");
			response = s.nextLine();
			if(response.equals("q")){
				cont=false;
			}
			x++;	
		}
	}
	/*
	 * coursesAndSections prints out courses and their corresponding sections
	*/
	public void coursesAndSections(){
		Scanner s = new Scanner(System.in);
		for(int i=0;i<courseListOriginal.size();i++){
			System.out.println("\ncourse name" + courseListOriginal.get(i).getName() + "\ncourse id: " + courseListOriginal.get(i).getID() + "\nsection ids: ");
			for(int j=0;j<maxSpots;j++){
				if(courseList.get(j).getID()==courseListOriginal.get(i).getID()){
					System.out.println("\t" + courseList.get(j).getSectionID());
				}	
			}
		}
		System.out.print("q - exit\nType q: ");
		String response = s.nextLine();
		if(response.equals("q")){
			
		}	
	}
	/*
	 * printAdditionalStats aims to help user understanding of the true effectiveness of the program;
	 * it quanitifies additional values, so the user can get a holistic view of effectiveness;
	 * it finds the # who got each request (and also makes it a percentage of who actually made requests)
	*/
	public void printAdditionalStats(){
		Scanner s = new Scanner(System.in);
		int accumulator;
		int totalMadeRequests=0; //number of people who made requests
		for(int i=0; i<studentList.size();i++){
			if(studentList.get(i).madeRequest()==true){
				totalMadeRequests++;
			}
		}
		for(int i=1;i<=numTimes;i++){
			accumulator = 0;
			for(int j=0;j<studentList.size();j++){
				Course request = studentList.get(j).getCourse(i);
				Course[] schedule = studentList.get(j).getSchedule();
				for(int k=0;k<schedule.length;k++){
					if((schedule[k]!=null && request!=null) && schedule[k].getID()==request.getID()){
						accumulator++;
					}	
				}	
			}
			int percentage = (int)((double)accumulator/totalMadeRequests*100);
			System.out.println("# of people who got choice " + i + ": " + accumulator + " (" + percentage + "%)");	
		}
		System.out.print("q - exit\nType q: ");
		String response = s.nextLine();
		if(response.equals("q")){
			
		}
	}	
	/*
	 * menu method uses Scanner to enable the user to navigate the program
	*/
	public void menu(){
		Scanner menu = new Scanner(System.in);
		boolean cont = true;
		int x=0; //incrementor
		String response = "";
		while(cont==true){
			
			if(x!=0){
				int choice = Integer.parseInt(response);
				if(choice==1){
					findStudent();
				}
				if(choice==2){
					findSection();
				}		
				if(choice==3){
					coursesAndSections();
				}
				if(choice==4){
					printAdditionalStats();
				}			
			}
			System.out.println("\n\n\n\n\n\n\n\n\n | SENIOR SEMINAR |");
			printSeniorSeminar();
			printOverview();
			System.out.println("\n\nMENU: \n 1 - search student \n 2 - search section\n 3 - courses & their sections\n 4 - additional stats\n q - quit at any time\n\n");
			System.out.print("Enter selection (numeral/q): ");
			response = menu.nextLine();
			if(response.equals("q")){
				cont=false;
			}
			x++;
		}	
	}
		
}	

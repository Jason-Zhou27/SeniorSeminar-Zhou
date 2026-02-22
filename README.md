# SeniorSeminar
A student project for Managing Scheduling and Optimizing a Solution that reduces conflicts

## Develop a Java Based Solution to Minimize Scheduling Conflicts

The attached CSV/Spreadsheet file is the result of sending out a google form and having students enter their top 5 ranked choices, using the mini table in the spreadsheet that contains the sessions, IDs and speakers.

**Feel free to clean up the spreadsheets, and even create new ones if needed, HOWEVER - you many not alter student preferences in the fiel or spreadsheet.

- 5 Time Slots 
- 5 Classrooms (16 students max/ class)
- Sessions Cannot Run more than Twice
- Notice that some sessions are taught by same person
- Student May Not Repeat a Session
- Schedule a Student for 5 Session

**Goal is to design and code an algorithm that generates a schedule for the students, given structure above, with the fewest number of conflicts

- Generate a schedule for the sessions and speakers, 
- Create lists for each student so they know what sessions to attend, rooms and times.

Journal Notes:

02.18.2026 (02.19.2026 Class Work Day Make-up)

I added multiple new methods in Schedule; among which inlcude findPop, findDemand, and assignPriority--all methods to track how popular a course is, 
how much student demand there is (unweighted), and the priority that should be given to the course when sorting the course array. 
I adjusted other classes accordingly to accomodate this new functionality. Furthermore, I implemented a bubble sort algorithm to sort the courses 
courseList by comparing adjacent courses' priority ratings. I begun to build the 2d senior seminar array which will have rows of time and columns of classrooms. 
Furthermore, I have begun to include important numbers from the program into arguments of the Schedule class constructor. I have improved toString/printCourses/printStudents
to be more comprehensive in detail.


02.21.2026

I worked on placing the courses into the 2d array of times and classrooms. I tried to reduce conflicts as much as possible by coding the findOptimal 
method which tracks which position (in the 2d array) would pose least amount of conflicts to the students within the course's roster. I spent considerable time 
removing any sort of magic numbers that had originated from the known fact that there would be five courses for the students to pick and attend.
One notable thing I had to do to remove magic numbers was loading the course requests through methods rather than the constructor. This process 
took considerable time as I had to modify other methods within the Schedule class, but overall, I think I definitely reduced clutter in my program.
Nevertheless, there is still a lot of fluff in my program as I commented those inefficiencies/magic number lines out with multi-line comments. You 
may also notice that I have considerable print lines commented out--that fact can be attributed to my numerous attempts at debugging index out of bounds
and other errors. To supplement my progress in the Schedule class, I adjusted/inserted new functionality into my other classes, notably Student and 
Course. For Course, I notably added an overloaded updateRoster method to keep track of roster size and add Student objects to the ArrayList roster.
For Course, I notably added checkConflict and updateSchedule. In the schedule class, I created a method to print a visual representation of the 2d
senior seminar array using course ids. Since some of the spots were not filled, I used a conditional to ensure that the program would not throw an error;
as of right now, -1 serves as a placeholder for the spots that are not filled. Spots are currently empty because I have not yet added functionality which
would allow a seminar to run twice; I hope to do that in a future work session. Today's work session was very comprehensive and spanned 4-5 hours;
despite my work session being quite long, I spent considerable time thinking about the approach, mapping out the process, and contemplating about sources
of error.


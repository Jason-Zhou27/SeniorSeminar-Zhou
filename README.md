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




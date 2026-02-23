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


2.22.2026

Today, I worked through the intricacies of the project; I created multiple sections of coureses that have excessive demand, maxed out courses
at a certain capacity, created methods to ensure that no student is in two sections of the same course, and filled in gaps in course requests and schedules. 

Furthermore, I've programmed functionality to calculate the # of conflicts, # of conflicts per student, and # of gaps in the students' schedules (unscheduled blocks). 
In order to create the functionality above, I had to add many methods in Schedule and supplementary ones in Course and Student. Also, a key aspect of today was debugging: 
I have spent at least two hours thinking about the sources of error and how I can overcome those. Some sources of error that are worth mentioning are forgetting
to update student schedules despite updating rosters and failure to update roster size in many places. I recently come across the issue in which 
the files would not be built correctly (reflecting a past version of the file), but I was able to fix that issue by deleting the class files and 
building the class files again through geany. So far in the project, I think I made considerable progress on many fronts; as of right now, I am able
to get total conflicts to 103, which averages to ~1.39 per student out of 5 time blocks--which is the lowest I have gotten to. I have also got the 
number of gaps (unscheduled blocks) to 20 for all students; I think that feat is still solid, but ideally, this number should be 0. I now realize
why this project is so difficult: I must ensure that every spot is filled for every student. This notion may seem easy, but it poses considerable conflict
to my optimization. I will need to devise a strategy to approach this issue, but I think it will require a sacrifice in number of conflicts. The approach
I have in my mind as of right now is to adjust priority of courses based on the updated rosters with those who forgot to request courses. I also plan 
to change the threshold for a course to be duplicated to be based off a detailed calculation rather than proximity to the nearest integer. As I move forward,
I want to ensure that I am eliminating any source (no matter how tiny) of magic numbers: I want to ensure that no matter how I adjust values, I will not
encounter an unexpected outcome/error (like out of bounds). I also want to make sure that when I duplicate courses, I am strategic on where I put the 
duplicated copy--maybe it is not wise to place the duplicated course adjacent to the original one; we'll see. In my future work sessions, I anticipate 
that I will not make visual progress in terms of lines of code, but I will make progress in thought. I will do a lot of mapping of ideas and strategy to ensure that
my strategy moving forward is effective.


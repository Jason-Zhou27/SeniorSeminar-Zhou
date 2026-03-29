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

2.23.2026

Today, I realized that I violated one of the rules of the project (no session is repeated more than twice) in duplicateCourses() method, so I changed the conditional
to account for the fact. Other than that, I did some significant debugging. There is a disrepancy between the number of free spots that should exist at the end (30)
and the actual number of free spots that exist (2). I want to find the source of such error. So far, I have analyzed the potential outcome that the roster was not getting
updated properly (which does not seem to be the case). I want to print out the rosters right now to see if I can find the bug that way. I think that by fixing this error,
I can greatly reduce the number of gaps.

2.24.2026

I updated the documentation/comments for readFileStudent and readFileCourse methods in Schedule class.

2.27.2026

I developed a new approach for optimization; instead of following the current order of placing courses w/
same courses placed adjacently & higher demand courses put in first, I aim to place the coureses in every order
possible--then picking the most optimal one for the user. I am still working to fix the error which results
in the disrepancy of gaps; so far, I have gotten the printRoster() method to work, and I plan to use that
to find the source of the error.

3.12.2026

Today, I changed the printRoster method to print IDs, so I can import each roster into a spreadsheet. I looked through the spreadsheet, and some people
are taking >5 classes, which signals that an issue truly exists. The problem is that this issue can occur in multiple places--so right now, I am looking through the whole program
and continuing to try to debug the error. I expect the origin of this issue is in removeDuplicateStudents.
Please see the spreadsheet to see my work: https://docs.google.com/spreadsheets/d/16BMeEpPLZVsgxSx0Q-8vri2covNVRRhxb3X6e6Uh3dM/edit?usp=sharing

3.13.2026

Today, I documented the reference schedule that Mr. Twyford put on the board. I created a 2d array of senior seminar ids which is initialized to this reference schedule. However,
because of the infrastructure of my program , it is exceptionally difficult to integrate the "place course then place students" algorithm into my program; nevertheless, I have pretty much
achieved the objective of this activity earlier: I have determined that nearly all/all can get at least 3 of their requested schedules. I am still encountering the issue of gaps and
making some progress in debugging. I think to fix this issue, I must devote a long stretch of time to debugging--simply debugging in small bits is ineffective and inefficient. 
Over Spring Break, I hope to finish the project.

3.27.2026

As I work to debug the issue, I will make some notes here to help myself:
ok, so for me to debug, I want to limit as many sources of error as possible; thus, right now, I will take out fillBlankRequests (it's kind of redundant
anyways as it has the same function as fillGaps)
for students to be placed in more courses than they should...
issue with fillGaps? no
I've noticed that those with >5 courses are not placed into the same courses --> thus, I don't think the error comes from duplicating courses,
copying rosters, or removing duplicate students...so for right now, I will eliminate those from scrutiny

I've so far deduced that fillGaps is not the issue by looking at the code--but a puzzling issue comes up. How can a student get courses he/she
never even picked?--that has to do somehow with fillGaps, right? b/c no other method deals with inserting courses that a student has not requested
BUT WAIT--what if fillGaps is acting correctly and putting these students in new courses BECAUSE THE STUDENT'S SCHEDULE WASN'T UPDATED PROPERLY TO BEGIN WITH!
a student can be placed into let's say 5 course rosters, but if his schedule is only updated with 3 courses, fillGaps will add him to 2 additional courses.

ok, so how can a student be placed into rosters but not have his/her schedule updated correctly?
it must be the placeStudents, right? I will check that.

the question I'm trying to tackle right now is when do I update the students' schedules when I place the courses? do I even do that in the placeCourses section?
so I actually do that

sidenote: I adjusted the getCourse method to return null as a course instead of some placeholder course--no change I think NEVERMIND--I lowered conflicts per student to 1.5
	same amount of # free total
	
alright, so I think my intuition was wrong--I think these students have their schedules updated. how do I know? well, I looked at case study student 6.
Student 6 had a full schedule when I ran the toString on him, and the courses in his schedule were all those that he requested. The extra 2 courses
in whose rosters he appeared in were courses he DID NOT request. What does this phenomenon tell us? fillGaps or maybe another method somehow adds students with full
schedules to other full courses

	nevermind, this statement is wrong; I incorrectly referenced the spreadsheet
	
I think my intuition was correct: for case study student 6, his schedule shows that he was placed into most (4) of his choices' rosters, but his schedule didn't update correctly
(in fact, he only got 2 in his schedule); therefore, fillGaps added 3 more courses

please see my spreadsheet linked above to see more work.

I have a new question: could those courses whose rosters he had been added to even fit in his schedule? 
ok so he wanted 18 and 14 (both at time block 1) but got neither added to his schedule--he could have gotten removed from 1 of them, and when it was removed,
the "null" that was typed in may not have been the same null that had been originated

also I have been thinking and I realized that my program takes into account ranked choice indirectly to satisfy the collective good (to address individual good
I will need to look more at the removal of students when there are excess)

created debugging tools (prints)

3.28.2026 

I think the best way to resolve this issue is to look at case by case basis. I will analyze student 6 to see where the issue originates from.
So I tried to debug by analyzing student 6...but I realized that I can just see if this issue exists by resolving it with a searchDelete() method.
This searchDelete() method will look through all the rosters for each student and if the student does not have that course in his/her schedule, he/she will be removed.
Rather than finding the source of error (which has taken me days upon days) and fixing it there, I can just create a new method that will resolve the issue.
Guess what? it works!
Here's the thing though: eliminating students from rosters of courses they do not take may not be most optimized; I might still look into the underlying issue;
neverthless, I've done some effective eliminating of the issue.

I did quite a bit of housekeeping today; I eliminated old debugging tools that were not needed/old lines. 

3.29.2026

Today, I made sure that students got choices based on their ranks. I initially had used only ranks to set the common good (what courses to choose). However,
running this change allowed me to see a new issue: the last course could sometimes not be placed because the teacher was not available. Therefore, I adjusted
the placeCourses method to change that last course if needed. I made sure also to clean up placeCourses() method by separating it into placeCourses() and
studentHandling(). I changed the calculation of conflicts per student to be more accurate (not counting those who did not even submit course requests). I made
the program more judicious in judging whether a course should be duplicated by looking at opportunity cost of duplicating that course. I made a debugging tool
with print statements to see where students are not getting properly deleted from. By looking at the results of this debugging tool, I'm guessing that I can reduce the
conflicts by more than 10 if I fix this issue.

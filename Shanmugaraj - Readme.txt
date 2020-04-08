List of completed items below:
- designed complete framework using Java TestNG
- included retrieving session ID from Web UI through automation
- customised HTML report
- common methods to handle all 4 http requests
- data driven using excel
- configured to run using "mvn test" command, hence Jenkins ready


Test included in automation:
- 1 end to end scenario with post, get to validate after post, then put to complete upload, get to validate again to validate completed status(includes getting session ID and logging out of web UI as pre-requisite)


Defects found while exploring:
- when file uploaded with 0KB, status in get call gives "pending" and not completed
- After file of size, say 40 is completely uploaded, when put call is again called with 30 as bytescompleted, file status changes to pending
- Delete call returns 405 (method not allowed) error received

List of items not included:
- Report to combine all test cases and give one file
- design and alignment of HTML file
- Test case for:
1. to test put, get, post and delete call negative test cases
2. file sharing scenario and its related http calls.

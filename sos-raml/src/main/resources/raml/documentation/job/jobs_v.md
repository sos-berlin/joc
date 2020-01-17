Each job can have a compact or detailed response.<br/>
It depends on the parameter ``compact``.<br/>
* Required fields are
    * surveyDate
    * path
    * name
    * state
    * stateText
    * ordersSummary (only for order jobs)
    * numOfRunningTasks
    * numOfQueuedTasks
* The compact view have further optional fields
    * locks
    * configurationStatus
    * error
* The detailed view has the following required fields in addition to the compact view
    * allTasks
    * allSteps
* The detailed view has the following optional fields in addition to the compact view
    * runningTasks
    * taskQueue
    * params
    * nextStartTime
    * delayUntil (only for standalone jobs)
* A job object as child of a job chain node  in the response have only the following required fields
    * path
    * state
* and one optional field
    * configurationStatus

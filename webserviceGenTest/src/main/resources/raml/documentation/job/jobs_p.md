Each job can have a compact or detailed response.<br/>
It depends on the parameter ``compact``.<br/>
* Required fields are
    * surveyDate
    * path
    * name
    * isOrderJob
    * usedInJobChains
    * estimatedDuration
    * maxTasks (default:1)
    * hasDescription (default:false)
* The compact view has the following optional fields
    * title
* The detailed view has the following required fields in addition to the compact view
    * configurationDate
* The detailed view has the following optional fields in addition to the compact view
    * processClass
    * params
    * locks
    * jobChains (only for order jobs)
* A job object as child of a job chain node in the response have only the following required fields
    * path
* and doesn't have any optional fields

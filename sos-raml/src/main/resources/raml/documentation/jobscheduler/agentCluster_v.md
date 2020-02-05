Each agent cluster can have a compact or detailed response.<br/>
It depends on the parameter ``compact``.<br/>
* Required fields are
    * surveyDate
    * path
    * name
    * state
    * maxProcesses
    * numOfAgents
    * _type
* The compact view doesn't have further optional fields
* The detailed view has in addition the following required field to the compact view
    * agents
* The detailed view has the optional field
    * processes

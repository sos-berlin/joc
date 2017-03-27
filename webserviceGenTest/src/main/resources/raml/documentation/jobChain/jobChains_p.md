Each job chain can have a compact or detailed response.<br/>
It depends on the parameter ``compact``.<br/>
* Required fields are
    * surveyDate
    * path
    * name
    * numOfNodes
* The compact view has the following optional fields
    * title
    * maxOrders
    * distributed
    * processClass
    * fileWatchingProcessClass
* The detailed view has the following required fields in addition to the compact view
    * nodes
    * endNodes
    * configurationDate
* The detailed view has the following optional fields in addition to the compact view
    * fileOrderSources

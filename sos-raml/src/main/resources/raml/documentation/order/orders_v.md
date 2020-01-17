Each order can have a compact or detailed response.<br/>
It depends on the parameter ``compact``.
* Fix required fields are
    * surveyDate
    * path
    * id
    * jobChain
    * state (name of the node)
    * job
    * processingState (e.g. suspended, waiting_for_agent, ...)
    * _type (permanent, ad_hoc, file_order)
* Additional variable required fields depends on the orders' processingState<br/>
  (*pending*, *running*, *waitingForResource*, *setback*, *suspended*, *blacklist*) where<br/>
  *waitingForResource* means orders which have the processingState (*job_not_in_period*, *node_delay*, *waiting_for_lock*,
  *waiting_for_process*, *waiting_for_agent*, *job_chain_stopped*, *node_stopped*,
  *job_stopped*, *waiting_for_task*)
    * nextStartTime (for *pending*, *job_not_in_period*, *node_delay* orders)
    * historyId (for all except *pending* orders)
    * startedAt (for all except *pending* orders)
    * taskId (for *running* orders)
    * setback (for *setback* orders)
    * lock (for *waiting_for_lock* orders)
    * processClass (for *waiting_for_process*, *waiting_for_agent* orders)
* The compact view has the following optional fields
    * title
    * processedBy (for *running*, *blacklist* orders)
    * configurationStatus
* The detailed view has the following optional fields in addition to the compact view
    * stateText
    * endState (name of the end node)
    * priority
    * params

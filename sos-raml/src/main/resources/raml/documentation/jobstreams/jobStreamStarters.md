<tr><td>``jobstreamStarters``</td><td>required, Object</td>
	
<td>List of job stream starters that should be started</td><td>[{"name":"Test",
"params":[{"name":"param1","value":"new value of param1"},{"name":"param2","value":"new value of param2"}]}]</td><td></td></tr>

<tr>
<tr><td>``starterName``</td><td>optional (one of starterName and jobStreamStarterId is required), String</td>
<td>The starter that have this name wil be started.</td><td>myName</td><td></td></tr>
</tr>
<tr>
<tr><td>``jobStreamStarterId``</td><td>optional (one of starterName and jobStreamStarterId is required), String</td>
<td>The starter that have this id wil be started. Note: When both name and id are specified the starter that matches both values will be started.</td><td>4711</td><td></td></tr>
</tr>
<tr>
<tr><td>``params``</td><td>optional, Object</td>
<td>A list of name value pairs.</td><td>[{"name":"param1","value":"new value of param1"},{"name":"param2","value":"new value of param2"}]</td><td>
</td></tr>
</tr>

 

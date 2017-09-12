<tr><td>``calendars``</td><td>optional, array</td>
<td>Filtered response by a collection of calendars specified by its calendar path and an optional category.
If "category" is undefined then all categories of the specified calendar are included in the response.
If "category" is empty then calendar without a category is responded.
Some other parameters such as ``folders`` and ``regex`` are ignored if this parameter is defined.</td>
<td> [{
  <div style="padding-left:10px;">"path": "/sos/myCalendar",</div>
  <div style="padding-left:10px;">"category": ""</div>
  }]</td>
<td></td>
</tr>

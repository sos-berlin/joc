<tr><td>``calendar``</td><td>required, object</td>
<td>Calendar object
<ul>
  <li>If a calendar is new then the ``id`` field is undefined and a new calendar will be stored.
    <ul>
      <li>If the ``path`` field is equal to an already existing calendar then a corresponding error returns.</li>
    </ul>
  </li>
  <li>If an existing calendar is edited then the ``id`` from the <a href="../calendar.html">./calendar</a> api must sent in the calendar object
    <ul>
      <li>If the ``path`` field was edited too the calendar will be moved to the new path</li>
    </ul>
  </li>
</ul>
</td>
<td></td><td></td></tr>
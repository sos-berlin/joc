<tr><td>``orders``</td><td>optional, array</td>
<td>Filtered response by a collection of orders specified by its job chain path and an optional order id and state (name of the job chain node).<br/>
If "orderId" and "state" is undefined then all orders of the specified job chain are included in the response.<br/>
If "orderId" is specified then parameters such as ``folders``, ``types``, ``excludeJobs`` and ``regex`` are ignored.</td>
<td> [{
  <div style="padding-left:10px;">"jobChain":"/sos/reporting/Inventory",</div>
  <div style="padding-left:10px;">"orderId":"Inventory"</div>
  <div style="padding-left:10px;">"state":"exec"</div>
  }]</td>
<td></td>
</tr>

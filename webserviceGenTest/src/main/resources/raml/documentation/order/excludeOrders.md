<tr><td>``excludeOrders``</td><td>optional, array</td>
<td>Response contains all orders except the orders which are specified in this collection.<br/>
An order is specified by its job chain path and an optional order id.<br/>
If "orderId" is undefined then all orders of the specified job chain are excluded in the response.<br/>
This parameter will be ignored if ``orders`` parameter is set.</td>
<td> [{
  <div style="padding-left:10px;">"jobChain":"/sos/reporting/Inventory",</div>
  <div style="padding-left:10px;">"orderId":"Inventory"</div>
  }]</td>
<td></td>
</tr>

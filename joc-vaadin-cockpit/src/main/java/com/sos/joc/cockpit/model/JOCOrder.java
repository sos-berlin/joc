package com.sos.joc.cockpit.model;

import com.sos.scheduler.model.SchedulerObjectFactoryOptions;
import com.sos.scheduler.model.answers.Order;

public class JOCOrder extends JOCObjects implements IJOCObjects<Order> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8840077244310699165L;
	private Order order = null;
	private final String EXTENSION = ".order";
	private final String ICON = "explorer_item_order.gif";

	public JOCOrder(final Order order, final SchedulerObjectFactoryOptions options) {
		super(options);
		this.order = order;
	}
	
	@Override
	public String toString() {
		return order.getName();
	}

	@Override
	public String getName() {
		return order.getId();
	}

	@Override
	public String getPath() {
		return order.getPath();
	}

	@Override
	public Order getAnswerObject() {
		return order;
	}

	@Override
	public String getItemId() {
		return super.getItemId(getPath(), EXTENSION);
	}

	@Override
	public String getIcon() {
		return ICON;
	}

}

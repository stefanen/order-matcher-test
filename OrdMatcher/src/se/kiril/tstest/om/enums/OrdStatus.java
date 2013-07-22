package se.kiril.tstest.om.enums;

public enum OrdStatus {
	/**
	 * Outstanding order with no executions
	 */
	NEW,
	/**
	 * Outstanding order with executions and remaining quantity
	 */
	PARTIALLY_FILLED,
	/**
	 * Order completely filled, no remaining quantity
	 */
	FILLED,
	/**
	 * Order not, or partially, filled; no further executions forthcoming for
	 * the trading day
	 */
	DONE_FOR_DAY,
	/**
	 * Canceled order with or without executions
	 */
	CANCELED, @Deprecated
	REPLACED,
	/**
	 * Order with an Order Cancel Request pending, used to confirm receipt of an
	 * Order Cancel Request. DOES NOT INDICATE THAT THE ORDER HAS BEEN CANCELED.
	 */
	PENDING_CANCEL,
	/**
	 * A trade is guaranteed for the order, usually at a stated price or better,
	 * but has not yet occurred. For example, a specialist on an exchange may
	 * stop an order while searching for a better price.
	 */
	STOPPED,
	/**
	 * Order has been rejected by sell-side (broker, exchange, ECN). NOTE: An
	 * order can be rejected subsequent to order acknowledgment, i.e. an order
	 * can pass from New to Rejected status.
	 */
	REJECTED,
	/**
	 * The order is not eligible for trading. This usually happens as a result
	 * of a verbal or otherwise out of band request to suspend the order, or
	 * because the order was submitted, or modified via a Cancel/Replace
	 * Request, with ExecInst=Suspended.
	 */
	SUSPENDED,
	/**
	 * Order has been received by sell-sides (broker, exchange, ECN) system but
	 * not yet accepted for execution. An execution message with this status
	 * will only be sent in response to a Status Request message.
	 */
	PENDING_NEW,
	/**
	 * Order has been completed for the day (either filled or done for day).
	 * Commission or currency settlement details have been calculated and
	 * reported in this execution message
	 */
	CALCULATED,
	/**
	 * Outstanding order with executions and remaining quantity
	 */
	EXPIRED,
	/**
	 * Order has been received and is being evaluated for pricing. It is
	 * anticipated that this status will only be used with the sDisclosed
	 * BidType List Order Trading model.
	 */
	ACCEPTED_FOR_BIDDING,
	/**
	 * Order with an Order Cancel/Replace Request pending, used to confirm
	 * receipt of an Order Cancel/Replace Request. DOES NOT INDICATE THAT THE
	 * ORDER HAS BEEN REPLACED.
	 */
	PENDING_REPLACE,
}

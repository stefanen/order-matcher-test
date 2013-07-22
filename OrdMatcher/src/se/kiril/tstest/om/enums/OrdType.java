package se.kiril.tstest.om.enums;

public enum OrdType {
	/**
	 * An order to buy a security at or below a stated price, or to sell a
	 * security at or above a stated price.
	 */
	LIMIT,
	/**
	 * Indicates an order to buy or sell a stated amount of a security at the
	 * most advantageous price obtainable after the order is represented in the
	 * Trading Crowd.
	 */
	MARKET,
	/**
	 * A stop order to buy which becomes a market order when the security trades
	 * at - or above - the stop price after the order is represented in the
	 * Trading Crowd. A stop order to sell which becomes a market order when the
	 * security trades at - or below - the stop price after the order is
	 * represented in the Trading Crowd.
	 */
	STOP_LOSS,
	/**
	 * A stop order to buy which becomes a limit order at the limit price when
	 * the security trades at - or above - the stop price after the order is
	 * represented in the Trading Crowd. A stop order to sell which becomes a
	 * limit order at the limit price when the security trades at - or below-
	 * the stop price after the order is represented in the Trading Crowd.
	 */
	STOP_LIMIT, PEGGED, ALL_OR_NOTHING, FILL_OR_KILL
}

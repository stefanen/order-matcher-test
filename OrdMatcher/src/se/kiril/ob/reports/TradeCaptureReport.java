package se.kiril.ob.reports;

import java.util.Date;

import se.kiril.ob.enums.ExecType;

public class TradeCaptureReport {
	private String tradeReportId; // TCR identifier
	private ExecType execType; // execution type being reported
	// private OrdStatus ordStatus;
	private boolean previouslReported;

	// Instrument
	private String symbol;

	private int lastQty;
	private double lastPx; // price of last fill

	private Date tradeDate;
	private int transactTime;

	private int noSides;
	private char side;

	private String orderId;

}

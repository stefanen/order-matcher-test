package se.kiril.tstest.om.bdx;

import se.kiril.tstest.om.reports.ExecutionReport;

//TODO
public class BdxExecReport {
	public BdxExecReport(ExecutionReport report) {
		System.out.println(report.getExecId() + " " + report.getSymbol() + " "
				+ report.getOrdQty());
	}
}

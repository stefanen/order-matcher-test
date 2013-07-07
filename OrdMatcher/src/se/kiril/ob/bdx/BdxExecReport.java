package se.kiril.ob.bdx;

import se.kiril.ob.reports.ExecutionReport;
//TODO
public class BdxExecReport {
	public BdxExecReport(ExecutionReport report){
		System.out.println(report.getExecId()+" "+report.getSymbol()+" "+ report.getOrdQty());
	}
}

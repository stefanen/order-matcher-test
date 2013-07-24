package se.kiril.tstest.fc;

import java.awt.List;
import java.io.IOException;

import quickfix.SessionNotFound;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.Message;
import quickfix.fix44.NewOrderSingle;
import se.kiril.tstest.fc.commons.FixOrder;
import se.kiril.tstest.fc.fileinput.ParseFile;
import se.kiril.tstest.fc.fileinput.ParseMessage;
import se.kiril.tstest.fc.fix.session.FcConnectionFix;

public class FixClient {

	private static FcConnectionFix fixConnection = new FcConnectionFix();

	public static void main(String[] args) throws InterruptedException,
			IOException, SessionNotFound {
		String tradesFile = "trades.in";
		List fLines = new List();
		ParseFile pf = new ParseFile(tradesFile);
		fLines = pf.getParsedFile();

		fixConnection
				.initiatorConnectAndLogon("ClientFixInitiatorSettings.cfg");

		for (int i = 0; i < fLines.getItemCount(); i++) {
			ParseMessage pm = new ParseMessage(fLines.getItem(i));
			FixOrder fOrd = new FixOrder(pm.getSymbol(), pm.getSide(),
					pm.getOrdType(), pm.getPrice(), pm.getQty(), pm.getUser());
			fixConnection.initiatorSendMsg(createFixMsg(fOrd));
			// Thread.sleep(2000);
		}
		Thread.sleep(2000); // Waiting for any remaining Exec reports
		fixConnection.initiatorDisconnect();
		System.out.println("Done inserting trades.");
	}

	private static Message createFixMsg(FixOrder ord) {
		ClOrdID orderId = new ClOrdID(ord.getOrdSeqNr().toString());
		Side side = new Side(ord.getSide());
		TransactTime transactionTime = new TransactTime();
		OrdType orderType = new OrdType(OrdType.LIMIT);

		NewOrderSingle newOrderSingle = new NewOrderSingle(orderId, side,
				transactionTime, orderType);

		newOrderSingle.set(new HandlInst(
				HandlInst.AUTOMATED_EXECUTION_ORDER_PRIVATE));
		newOrderSingle.set(new OrderQty(ord.getQty()));
		newOrderSingle.set(new Symbol(ord.getSymbol()));
		newOrderSingle.set(new Price(ord.getLimitPx()));
		newOrderSingle.set(new Account(ord.getUser()));
		return (Message) newOrderSingle;
	}
}

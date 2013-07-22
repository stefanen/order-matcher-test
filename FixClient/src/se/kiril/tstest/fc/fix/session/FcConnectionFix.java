package se.kiril.tstest.fc.fix.session;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.ExecutionReport;
import se.kiril.tstest.fs.fix.connection.ConnectionFix;

public class FcConnectionFix extends ConnectionFix {

	public void onMessage(ExecutionReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// System.out.println(sessionID + ": "+ message);
	}
}

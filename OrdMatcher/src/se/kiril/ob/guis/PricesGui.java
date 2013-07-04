package se.kiril.ob.guis;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class PricesGui {
	private String[] colNames = { "SYMBOL", "BID", "ASK" };
	private JTable table = new JTable();
	private DecimalFormat df = new DecimalFormat("0.00");

	private Object[][] data = { { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" }, { "-", "-", "-" }, { "-", "-", "-" },
			{ "-", "-", "-" } };

	public PricesGui() {
		JFrame frame = new JFrame("Market prices");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		frame.setPreferredSize(new Dimension(500, 500));
		frame.setResizable(false);

		table = new JTable(data, colNames);

		frame.add(new JScrollPane(table));

		frame.pack();
		frame.setVisible(true);
	}

	public void setTxt(HashMap prices) {
		HashMap<String, Double[]> tPr = new HashMap<String, Double[]>();
		tPr = prices;
		int i = 0;

		Map<Integer, String[]> map = new HashMap<Integer, String[]>();

		for (Map.Entry<String, Double[]> e : tPr.entrySet()) {
			String[] props = { "", "", "" };

			props[0] = e.getKey();
			if (e.getValue()[0] == 0.0) {
				props[1] = "-";
			} else {
				props[1] = df.format(e.getValue()[0]).toString();
			}
			if (e.getValue()[1] == 0.0) {
				props[2] = "-";
			} else {
				props[2] = df.format(e.getValue()[1]).toString();
			}
			map.put(i, props);
			i++;
		}
		for (int k = 0; k < map.size(); k++) {
			data[k] = map.get(k);
		}
		table.repaint();
	}
}

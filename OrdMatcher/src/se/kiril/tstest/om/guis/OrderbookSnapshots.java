package se.kiril.tstest.om.guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Savepoint;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import se.kiril.tstest.om.SaveObStateEventIf;
import se.kiril.tstest.om.orderbook.Limit;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.orderbook.OrderBook;
import se.kiril.tstest.om.symbols.Symbol;
import se.kiril.tstest.om.symbols.SymbolSide;

public class OrderbookSnapshots implements ActionListener{
	
	private SaveObStateEventIf saveObStateEvent;
	private static volatile int eventNr = 1;
	
	private static Map<Long, OrderBook> snapshotsMap = new HashMap<Long, OrderBook>();
	
	private static DefaultListModel<Long> listModel = new DefaultListModel<Long>(); 
	private static JList<Long> snapshotsList = new JList<Long>(listModel);
	
	private static Long currentObSnapshotIndex = null;
	private static JLabel contentsLabel = new JLabel();
	private static DefaultMutableTreeNode top = null;
	
	private static JTree tree;
	private static DefaultMutableTreeNode category = null;
    private static DefaultMutableTreeNode symbol = null;
    private static DefaultMutableTreeNode symSideB = null;
    private static DefaultMutableTreeNode symSideS = null;
    private static DefaultMutableTreeNode limitsC = new DefaultMutableTreeNode("Limits");
    private static DefaultMutableTreeNode marketsC = new DefaultMutableTreeNode("Markets");
    private static DefaultMutableTreeNode lim = null;
    private static DefaultMutableTreeNode mar = null;
    private static DefaultMutableTreeNode ord = null;
    
    JButton saveStateButton= new JButton();
    
    private static JPanel contentsPane = null;
	
	
	public OrderbookSnapshots(){
		JFrame frame = new JFrame("Market prices");
		
		JPanel listPane = new JPanel();
		listPane.add(new JLabel("OB snapshots"));
		listPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		listPane.setPreferredSize(new Dimension(140, 590));
		listPane.setBackground(new Color(220, 220, 220));

		
		saveStateButton.setPreferredSize(new Dimension(95, 70));
		saveStateButton.setText("Save OB");
		saveStateButton.addActionListener(this);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout(FlowLayout.LEFT)); //
		frame.setPreferredSize(new Dimension(700, 600));
		frame.setResizable(false);

		
		
//		label.setLabelFor(snapshotsList);
		snapshotsList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		snapshotsList.setLayoutOrientation(JList.VERTICAL);
		snapshotsList.setPreferredSize(new Dimension(90, 400));
		snapshotsList.setVisibleRowCount(-1);
		snapshotsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane listScroller = new JScrollPane(snapshotsList);
		listScroller.setPreferredSize(new Dimension(100, 540));
		
		
//		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		snapshotsList.addListSelectionListener(listSelectionListener);
		

//		top = new DefaultMutableTreeNode("Orderbook");
//	    createNodes(top);
//	    tree = new JTree(top);
//	    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//	    tree.setLayout(new FlowLayout(FlowLayout.LEFT));
//	    tree.setPreferredSize(new Dimension(410, 540));
	    
//		contentsLabel.setText("OB");
		contentsPane = new JPanel();
		contentsPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentsPane.setPreferredSize(new Dimension(440, 590));
		contentsPane.setBackground(new Color(220, 220, 220));
		
		contentsPane.add(Box.createRigidArea(new Dimension(0,5)));
//		contentsPane.add(tree);
		contentsPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		
		frame.add(saveStateButton);
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.add(listPane);
		frame.add(contentsPane);
		
		
//		frame.add(snapshotsList);
//		snapshotsList.setVisible(true);
//		frame.add(new JScrollPane(table));

		frame.pack();
		frame.setVisible(true);
		
	}
	
	private static DefaultMutableTreeNode createNodesStructure(DefaultMutableTreeNode pTop, OrderBook pOb){
		
		OrderBook ob = pOb;
		DefaultMutableTreeNode top = pTop; // root of the tree

	    System.out.println("creating nodes...");
	    
	    Map<String, Symbol> tSymbols = ob.getSymbolsMap();
	    for (Map.Entry<String, Symbol> s : tSymbols.entrySet()) {
	    	DefaultMutableTreeNode symbol = new DefaultMutableTreeNode(s.getKey());
	    	top.add(symbol);
	    	
	    	//Buy side
	    	DefaultMutableTreeNode tSymbolSideBuy = new DefaultMutableTreeNode("BUY");
	    	symbol.add(tSymbolSideBuy);
	    	DefaultMutableTreeNode limitsCatB = new DefaultMutableTreeNode("LimitOrders");
	    	tSymbolSideBuy.add(limitsCatB);
	    	if (s.getValue().getSideB().getLimitsMap() != null && s.getValue().getSideB().getLimitsMap().size() > 0){
	    		for (Map.Entry<Double, Limit> l : s.getValue().getSideB().getLimitsMap().entrySet()) {
	    			DefaultMutableTreeNode tLimit = new DefaultMutableTreeNode(l.getKey());
	    			limitsCatB.add(tLimit);
	    			if (l.getValue().getOrders() != null && l.getValue().getOrders().size() > 0){
	    				for (Order o : l.getValue().getOrders()){
	    					DefaultMutableTreeNode tOrder = new DefaultMutableTreeNode(o);
	    					tLimit.add(tOrder);
	    				}
	    			}
	    		}
	    	}
	    	DefaultMutableTreeNode marketsCatB = new DefaultMutableTreeNode("MarketOrders");
	    	tSymbolSideBuy.add(marketsCatB);
	    	if (s.getValue().getSideB().getMarketOrds() != null && s.getValue().getSideB().getMarketOrds().size() > 0){
	    		for (Order o : s.getValue().getSideB().getMarketOrds()){
	    			DefaultMutableTreeNode tOrder = new DefaultMutableTreeNode(o);
	    			marketsCatB.add(tOrder);
	    		}
	    	}
	    	//Sell side
	    	DefaultMutableTreeNode tSymbolSideSell = new DefaultMutableTreeNode("SELL");
	    	symbol.add(tSymbolSideSell);
	    	DefaultMutableTreeNode limitsCatS = new DefaultMutableTreeNode("LimitOrders");
	    	tSymbolSideSell.add(limitsCatS);
	    	if (s.getValue().getSideS().getLimitsMap() != null && s.getValue().getSideS().getLimitsMap().size() > 0){
	    		for (Map.Entry<Double, Limit> l : s.getValue().getSideS().getLimitsMap().entrySet()){
	    			DefaultMutableTreeNode tLimit = new DefaultMutableTreeNode(l.getKey());
	    			limitsCatS.add(tLimit);
	    			if (l.getValue().getOrders() != null && l.getValue().getOrders().size() > 0){
	    				for (Order o : l.getValue().getOrders()){
	    					DefaultMutableTreeNode tOrder = new DefaultMutableTreeNode(o);
	    					tLimit.add(tOrder);
	    				}
	    			}
	    		}
	    	}
	    	DefaultMutableTreeNode marketsCatS = new DefaultMutableTreeNode("MarketOrds");
	    	tSymbolSideSell.add(marketsCatS);
	    	if (s.getValue().getSideS().getMarketOrds() != null && s.getValue().getSideS().getMarketOrds().size() > 0){
	    		for (Order o : s.getValue().getSideS().getMarketOrds()){
	    			DefaultMutableTreeNode tOrder = new DefaultMutableTreeNode(o);
	    			marketsCatS.add(tOrder);
	    		}
	    	}
	    }
		return top;
	}
	
	
	private static void createNodes(DefaultMutableTreeNode top) {
       
		System.out.println("Creating nodes...");

        category = new DefaultMutableTreeNode("Symbols");
        top.add(category);

        if (currentObSnapshotIndex != null && snapshotsMap != null && snapshotsMap.get(currentObSnapshotIndex).getSymbolsMap() != null){
        	Map<String, Symbol> tSymbols = snapshotsMap.get(currentObSnapshotIndex).getSymbolsMap();
            for (Map.Entry<String, Symbol> e : tSymbols.entrySet()) {
            	symbol = new DefaultMutableTreeNode(e.getValue().getSymbolName());
                category.add(symbol);
                //
                SymbolSide symB = e.getValue().getSideB();
                SymbolSide symS = e.getValue().getSideS();
                if (symB != null){
                	symSideB = new DefaultMutableTreeNode(symB.getSide());
                	symbol.add(symSideB);
                	
                	symSideB.add(limitsC);
                	//
                	TreeMap<Double, Limit> limits = symB.getLimitsMap();
                	for (Map.Entry<Double, Limit> limit : limits.entrySet()) {
                		lim = new DefaultMutableTreeNode(limit.getValue().getPrice());
                		limitsC.add(lim);
                		
                		 LinkedList<Order> orders = limit.getValue().getOrders();
                		 for (Order o : orders) {
                			 ord = new DefaultMutableTreeNode(o);
                			 lim.add(ord);
                		 }
                		
                	}
                	//
                	symSideB.add(marketsC);
                	List<Order> markets = symB.getMarketOrds();
                	if (markets.size() > 0) {
                		for (int i = 0; i< markets.size(); i++){
                			mar = new DefaultMutableTreeNode(markets.get(i));
                			marketsC.add(mar);
                		}
                		
                	}
                }
                if (symS != null){
                	symSideS = new DefaultMutableTreeNode(symS.getSide());
                	symbol.add(symSideS);

                	symSideS.add(limitsC);
                	//
                	TreeMap<Double, Limit> limits = symS.getLimitsMap();
                	for (Map.Entry<Double, Limit> limit : limits.entrySet()) {
                		lim = new DefaultMutableTreeNode(limit.getValue());
                		limitsC.add(lim);

                		LinkedList<Order> orders = limit.getValue().getOrders();
                		for (Order o : orders) {
                			ord = new DefaultMutableTreeNode(o);
                			lim.add(ord);
                		}
                	}
                	//
                	symSideS.add(marketsC);
                	List<Order> markets = symS.getMarketOrds();
                	if (markets.size() > 0) {
                		for (int i = 0; i< markets.size(); i++){
                			mar = new DefaultMutableTreeNode(markets.get(i));
                			marketsC.add(mar);
                		}
                	}
                }
            }
        }
    }
	
	



	public void addSnapshotToList(Long snapshotName, OrderBook ob){
		snapshotsMap.put(snapshotName, ob);
		listModel.addElement(snapshotName);

		contentsLabel.setText(snapshotName.toString());
		currentObSnapshotIndex = snapshotName;
		updateContentsPane();
		
	}
	private static void updateContentsPane(){
		if (currentObSnapshotIndex != null){
			try {
				contentsPane.removeAll();
				
			}catch(Exception e){
			}
			contentsLabel.setText(currentObSnapshotIndex.toString());
			contentsPane.add(contentsLabel);
			OrderBook displayedOb = snapshotsMap.get(currentObSnapshotIndex);
			top = new DefaultMutableTreeNode("Orderbook");
//		    createNodes(top);
			createNodesStructure(top, displayedOb);
		    tree = new JTree(top);
		    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		    tree.setLayout(new FlowLayout(FlowLayout.LEFT));
		    tree.setPreferredSize(new Dimension(410, 540));
		    contentsPane.add(tree);
		}
	}
	private static ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent listSelectionEvent) {
			boolean adjust = listSelectionEvent.getValueIsAdjusting();
			if (!adjust) {
				JList list = (JList) listSelectionEvent.getSource();
				Object selectedValue = list.getSelectedValue();
				contentsLabel.setText(selectedValue.toString());
				//Slightly unsafe
				currentObSnapshotIndex = Long.parseLong(selectedValue.toString());
				
				//Update contents pane
				updateContentsPane();
			}
		}
	};


	@Override
	public void actionPerformed(ActionEvent evt) {
		Object src = evt.getSource();
	    if (src == saveStateButton) {
	    	saveObStateEvent.triggerSaveStateEvent(new Event(this, eventNr, src));
	    	eventNr++;
	    } 		
	}
	
	public void setEventListener(SaveObStateEventIf lst){
		this.saveObStateEvent = lst;
	}
}

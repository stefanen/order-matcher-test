package se.kiril.tstest.om.symbols;

import se.kiril.tstest.om.enums.OrdType;
import se.kiril.tstest.om.enums.Side;
import se.kiril.tstest.om.orderbook.Order;
import se.kiril.tstest.om.reports.ExecutionReport;

public class Symbol {

	private final String symbolName;
	protected SymbolSide symB = new SymbolSide(Side.BID);
	protected SymbolSide symS = new SymbolSide(Side.ASK);

	
	public Symbol(String pSymName) {
		this.symbolName = pSymName;
	}
	//debug only
	public SymbolSide getSideB(){
		return symB;
	}	
	//debug only
	public SymbolSide getSideS(){
		return symS;
	}	
	
	public void execOrd(Order pOrd) {
		Order initOrd = pOrd;
		if (initOrd.getSide().equals(Side.BID)) { // initiator is a bid, looking
													// for asks
			if (initOrd.getLimitPx() > symS.bestLimit) {
				// TODO Fix this
				if (initOrd != null && initOrd.getLeavesQty() > 0
						&& symS.execLimOrd(initOrd) != null) {
					initOrd = symS.execLimOrd(initOrd);
				}
			}
		} else { // initiator is an ASK, looking for bids
			if (initOrd.getLimitPx() < symB.bestLimit) {
				if (initOrd != null && initOrd.getLeavesQty() > 0
						&& symS.execLimOrd(initOrd) != null) {
					initOrd = symB.execLimOrd(initOrd);
				}
			}
		}
	}

	public void removeOrd(Order ord) {

	}

	public ExecutionReport addOrd(Order ord) {
		if (ord.getSide().equals(Side.BID)) {
			if (ord.getOrdType().equals(OrdType.MARKET)) {
				return symB.addMarketOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.LIMIT)) {
				return symB.addLimitOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.STOP_LOSS)) {
				return symB.addStopLossOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.STOP_LIMIT)) {
				return symB.addStopLimitOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.ALL_OR_NOTHING)) {
				return symB.addAllOrNothingOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.FILL_OR_KILL)) {
				return symB.addFillOrKillOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.PEGGED)) {
				return symB.addPeggedOrd(ord);
			} else {
				return null;
			}
		} else if (ord.getSide().equals(Side.ASK)) {
			if (ord.getOrdType().equals(OrdType.MARKET)) {
				return symS.addMarketOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.LIMIT)) {
				return symS.addLimitOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.STOP_LOSS)) {
				return symS.addStopLossOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.STOP_LIMIT)) {
				return symS.addStopLimitOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.ALL_OR_NOTHING)) {
				return symS.addAllOrNothingOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.FILL_OR_KILL)) {
				return symS.addFillOrKillOrd(ord);
			} else if (ord.getOrdType().equals(OrdType.PEGGED)) {
				return symS.addPeggedOrd(ord);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public String getSymbolName() {
		return symbolName;
	}

	public int getTotaNumberOfLimits() {
		return symB.getNumberOfLimits() + symS.getNumberOfLimits();
	}
	// ==============

	// public void addOrd_old(Order pOrd){
	// addOrdToLimit(pOrd);
	// //TODO This is not very efficient, need to change it.
	// purgeEmptyLimits();
	// //setBestBidAsk(pOrd);
	// }
	// public void removeOrder(Order ord){
	// removeOrdFromLimit(ord);
	// }
	// public int getTotaNolLimits(){
	// return limitBids.size() + limitAsks.size();
	// }
	//
	// private void createLimitAndAddOrd(Order ord){
	// Limit lim = new Limit(ord.getLimit());
	// lim.addOrderToLimit(ord);
	// if (ord.getSide() == 'B'){
	// limitBids.put(lim.getPrice(), lim);
	//
	// }else{
	// limitAsks.put(lim.getPrice(), lim);
	// }
	// }
	// private void removeOrdFromLimit(Order ord){
	// if (ord.getSide()== 'B'){ // bids side
	// limitBids.get(ord.getLimit()).removeOrderFromLimit(ord);
	// if(limitBids.get(ord.getLimit()).getSize() <= 0){
	// limitBids.remove(ord.getLimit());
	// }
	// bestBid = getHighestBid(ord);
	// }else{ // asks side
	// limitAsks.get(ord.getLimit()).removeOrderFromLimit(ord);
	// if(limitAsks.get(ord.getLimit()).getSize() <= 0){
	// limitAsks.remove(ord.getLimit());
	// }
	// bestAsk = getLowestAsk(ord);
	// }
	// }
	//
	// private void addOrdToLimit(Order ord){
	// //=====MARKET ORDER=====
	// if(ord.getOrdType()=='M'){
	// if (ord.getSide()=='B'){ //bids
	// marketBids.add(ord);
	// //EXECUTE
	// executeOrder(ord);
	// }else{ //asks
	// marketAsks.add(ord);
	// //EXECUTE
	// executeOrder(ord);
	// }
	// //=====LIMIT ORDER=====
	// }else if (ord.getOrdType()=='L'){
	// if (ord.getSide()=='B'){ // bids
	// if(limitBids.containsKey(ord.getLimit())){ // limit already exists
	// limitBids.get(ord.getLimit()).addOrderToLimit(ord);
	// // EXECUTE ORDER HERE
	// executeOrder(ord);
	// }else{
	// createLimitAndAddOrd(ord);
	// // EXECUTE ORDER HERE
	// executeOrder(ord);
	// }
	// bestBid = getHighestBid(ord);
	// }else{ // asks
	// if (limitAsks.containsKey(ord.getLimit())){
	// limitAsks.get(ord.getLimit()).addOrderToLimit(ord);
	// // EXECUTE ORDER HERE
	// executeOrder(ord);
	// }else{
	// createLimitAndAddOrd(ord);
	// // EXECUTE ORDER HERE
	// executeOrder(ord);
	// }
	// bestAsk = getLowestAsk(ord);
	// }
	// //TODO
	// //=====PEG ORDER=====
	// }else if (ord.getOrdType()=='P'){
	//
	// }
	// }
	// private void setBestBidAsk(Order ord){
	// if (ord.getSide() == 'B' && ord.getOrdType()=='L'){
	// bestBid = getHighestBid(ord);
	// }else if (ord.getOrdType()== 'L'){
	// bestAsk = getLowestAsk(ord);
	// }
	// }
	// public void purgeEmptyLimits(){
	// //TODO This could be multithreaded
	// clearEmptyLimitAsks();
	// clearEmptyLimitBids();
	// clearEmptyMarketAsks();
	// clearEmptyMarketBids();
	// }
	// private void clearEmptyMarketAsks(){
	// if (!marketAsks.isEmpty()){
	// for (Iterator<Order> it = marketAsks.iterator(); it.hasNext();){
	// if(it.next().getQty() <= 0){
	// it.remove();
	// }
	// }
	// }
	// }
	// private void clearEmptyMarketBids(){
	// if (!marketBids.isEmpty()){
	// for (Iterator<Order> it = marketBids.iterator(); it.hasNext();){
	// if(it.next().getQty() <= 0){
	// it.remove();
	// }
	// }
	// }
	// }
	// private void clearEmptyLimitAsks(){
	// if (!limitAsks.isEmpty()){
	// Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
	// for (Map.Entry<Double, Limit> e : limitAsks.entrySet()){
	// tempMap.put(e.getKey(), e.getValue());
	// if (e.getValue().getSize() <= 0){
	// tempMap.remove(e.getKey());
	// }
	// }
	// limitAsks = tempMap;
	// if(limitAsks.size() == 0){
	// bestAsk = 0.0;
	// }
	// }
	// }
	// private void clearEmptyLimitBids(){
	// if (!limitBids.isEmpty()){
	// Map<Double, Limit> tempMap = new TreeMap<Double, Limit>();
	// for (Map.Entry<Double, Limit> e : limitBids.entrySet()){
	// tempMap.put(e.getKey(), e.getValue());
	// if (e.getValue().getSize() <= 0){
	// tempMap.remove(e.getKey());
	// }
	// }
	// limitBids = tempMap;
	// if(limitBids.size() == 0){
	// bestBid = 0;
	// }
	// }
	// }
	// private void executeOrder(Order ord){
	// if (ord.getOrdType()=='L'){
	// }else if(ord.getOrdType()=='M'){
	// executeMarketOrder(ord);
	// }
	// }
	//
	//
	// private void executeMarketOrder(Order ord){
	// if (ord.getSide()=='B'){ // bid
	// int tradedQty = 0;
	// int remainingVol = ord.getQty();
	// for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
	// if (remainingVol > 0){
	// int tVol = 0;
	// tVol = askLimit.getValue().popFromInsideOfLimit(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// ord.setQty(ord.getQty()-tradedQty);
	// //bidLimits.get(ord.getLimit()).removeFromSize(tradedQty);
	// }else{ // ask
	// int tradedQty = 0;
	// int remainingVol = ord.getQty();
	// List<Double> listBidLimits = new LinkedList<Double>();
	// for (Map.Entry<Double, Limit> bLimit : limitBids.entrySet()){
	// listBidLimits.add(bLimit.getKey());
	// }
	// for (int i=listBidLimits.size()-1; i>=0; i--){
	// //listBidLimits.get(i);
	// if (remainingVol > 0){
	// int tVol = 0;
	// tVol =
	// limitBids.get(listBidLimits.get(i)).popFromInsideOfLimit(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// ord.setQty(ord.getQty()-tradedQty);
	// //askLimits.get(ord.getLimit()).removeFromSize(tradedQty);
	// }
	// }
	//
	// private Double getHighestBid(Order ord){
	// Double highestBid = ord.getLimit();
	// for (Map.Entry<Double, Limit> bidLimit : limitBids.entrySet()){
	// if(highestBid!=null && highestBid < bidLimit.getKey()){
	// highestBid = bidLimit.getKey();
	// }
	// }
	// if (highestBid!= null){
	// return highestBid;
	// }else{
	// return 0.0;
	// }
	// }
	// private Double getLowestAsk(Order ord){
	// Double lowestAsk = ord.getLimit();
	// for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
	// if(lowestAsk!=null && lowestAsk > askLimit.getKey()){
	// lowestAsk = askLimit.getKey();
	// }
	// }
	// if (lowestAsk!= null){
	// return lowestAsk;
	// }else{
	// return 0.0;
	// }
	//
	// }
	//
	// private void executeLimitOrder(Order ord){
	// if (ord.getSide() == 'B'){
	// int tradedQty = 0;
	// int remainingVol = ord.getQty();
	// //Going through market orders first
	// for (Order order : marketAsks){
	// if (remainingVol > 0){
	// int tVol = 0;
	// tVol = order.trade(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// //Going through limits
	// for (Map.Entry<Double, Limit> askLimit : limitAsks.entrySet()){
	// if (remainingVol > 0 && askLimit.getValue().getPrice() < ord.getLimit()){
	// int tVol = 0;
	// tVol = askLimit.getValue().popFromInsideOfLimit(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// ord.setQty(ord.getQty()-tradedQty);
	// limitBids.get(ord.getLimit()).removeFromSize(tradedQty);
	// }else{
	// int tradedQty = 0;
	// int remainingVol = ord.getQty();
	// //Going through market orders first
	// //TODO not correct ??
	// for (Order order : marketBids){
	// if (remainingVol > 0){
	// int tVol = 0;
	// tVol = order.trade(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// //Going through limits
	// List<Double> listBidLimits = new LinkedList<Double>();
	// for (Map.Entry<Double, Limit> bLimit : limitBids.entrySet()){
	// listBidLimits.add(bLimit.getKey());
	// }
	// for (int i=listBidLimits.size()-1; i>=0; i--){
	// //listBidLimits.get(i);
	// if (remainingVol > 0 && limitBids.get(listBidLimits.get(i)).getPrice() >
	// ord.getLimit()){
	// int tVol = 0;
	// tVol =
	// limitBids.get(listBidLimits.get(i)).popFromInsideOfLimit(remainingVol);
	// tradedQty += tVol;
	// remainingVol -= tVol;
	// }else{
	// break;
	// }
	// }
	// //clearEmptyBidLimits(); /not very efficient
	// ord.setQty(ord.getQty()-tradedQty);
	// limitAsks.get(ord.getLimit()).removeFromSize(tradedQty);
	// }
	// }
	//
	//
	//
	// public Double[] getBestBidBestAsk(){
	// Double[] prices = new Double[2];
	// prices[0] = bestBid;
	// prices[1] = bestAsk;
	// return prices;
	// }

}

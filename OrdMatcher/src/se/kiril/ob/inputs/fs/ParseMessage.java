package se.kiril.ob.inputs.fs;

// message format:
// "symbol side type price quantity user"
public class ParseMessage {
    private char operation;
    private String symbol;
    private char side;
    private char ordType;
    private double price;
    private int qty;
    private String user;
    public ParseMessage(String lineToParse){
        String[] lineElements = lineToParse.split(" ");
        operation = lineElements[0].charAt(0);
        symbol = lineElements[1];
        side = lineElements[2].charAt(0);
        ordType = lineElements[3].charAt(0);
        price = Double.parseDouble(lineElements[4]);
        qty = Integer.parseInt(lineElements[5]);
        user = lineElements[6];
    }
    public char getOperation(){
        return operation;
    }
    public String getSymbol() {
        return symbol;
    }
    public char getSide() {
        return side;
    }
    public char getOrdType() {
        return ordType;
    }
    public double getPrice() {
        return price;
    }
    public int getQty() {
        return qty;
    }
    public String getUser() {
        return user;
    }
}
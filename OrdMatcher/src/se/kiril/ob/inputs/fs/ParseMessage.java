package se.kiril.ob.inputs.fs;

// message format:
// "symbol side price quantity user"
public class ParseMessage {
    private char operation;
    private String symbol;
    private char side;
    private double price;
    private int qty;
    private String user;
    public ParseMessage(String lineToParse){
        String[] lineElements = lineToParse.split(" ");
        operation = lineElements[0].charAt(0);
        symbol = lineElements[1];
        side = lineElements[2].charAt(0);
        price = Double.parseDouble(lineElements[3]);
        qty = Integer.parseInt(lineElements[4]);
        user = lineElements[5];
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
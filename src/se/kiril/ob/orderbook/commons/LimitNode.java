package se.kiril.ob.orderbook.commons;

/**
 * Created with IntelliJ IDEA.
 * User: kiril
 * Date: 6/19/13
 * Time: 11:37 PM
 * To change this template use File | Settings | File Templates.
 */

import se.kiril.ob.orderbook.Limit;

public class LimitNode {
    public LimitNode leftLimitNode, rightLimitNode;
    public Limit limit;

    public LimitNode(Limit lim){
        this.leftLimitNode = null;
        this.rightLimitNode = null;
        this.limit = lim;
    }
}

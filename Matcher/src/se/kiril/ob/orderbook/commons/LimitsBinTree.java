package se.kiril.ob.orderbook.commons;

/**
 * Created with IntelliJ IDEA.
 * User: kiril
 * Date: 6/19/13
 * Time: 11:57 PM
 * To change this template use File | Settings | File Templates.
 */

import java.util.List;

import se.kiril.ob.orderbook.Limit;


public class LimitsBinTree {
    LimitNode rootNode;
    public LimitsBinTree(){
        rootNode = null;
    }

    public void addLimit(Limit lim){
        LimitNode limNode = new LimitNode(lim);
        rootNode = insertLimitNode(rootNode, limNode);
    }
    public Limit search(double pPrice){
        LimitNode tmp = locate(rootNode, pPrice);
        if (tmp == null){
            return null;
        }else{
            return tmp.limit;
        }
    }
    public Limit getLimit(double pPrice){
        LimitNode ln = locate(rootNode, pPrice);
        if (ln != null){
            return ln.limit;
        }else {
            return null;
        }

    }
    public void populateBinTree(List<Limit> pList){
        rootNode = null;
        for (int i = 0; i< pList.size(); i++){
            //LimitNode tempNode = null;
            addLimit((Limit) pList.get(i));
        }
    }




    protected LimitNode insertLimitNode(LimitNode rootNode, LimitNode newLimitNode){
        if (rootNode == null){
            rootNode = newLimitNode;
        }else if(newLimitNode.limit.getPrice() < rootNode.limit.getPrice()){
            rootNode.leftLimitNode = insertLimitNode(rootNode.leftLimitNode, newLimitNode);
        }else{
            rootNode.rightLimitNode = insertLimitNode(rootNode.rightLimitNode, newLimitNode);
        }
        return rootNode;
    }


    protected void inorder(LimitNode rooNode){
        if (rootNode != null){
            inorder(rooNode.leftLimitNode);
            inorder(rooNode.rightLimitNode);
        }
    }
    protected LimitNode locate(LimitNode rootNode, double limPrice){
        if (rootNode == null){
            return null;
        }else{
            if(limPrice == rootNode.limit.getPrice()){
                return locate(rootNode.leftLimitNode, limPrice);
            }else{
                return locate(rootNode.rightLimitNode, limPrice);
            }
        }
    }

}
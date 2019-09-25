package com.example.startactivity.Shopping;

public class ShoppingItem {

    private int shoppingID;
    private int groupID;
    private String item_name;

    public ShoppingItem(int shoppingID, int groupID, String item_name) {
        this.shoppingID = shoppingID;
        this.groupID = groupID;
        this.item_name = item_name;
    }

    public ShoppingItem() {
    }

    public int getShoppingID() {
        return shoppingID;
    }

    public void setShoppingID(int shoppingID) {
        this.shoppingID = shoppingID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}

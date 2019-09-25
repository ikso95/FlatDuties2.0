package com.example.startactivity.Shopping;

public class ShoppingListItem {

    private int shoppingID;
    private String shopping_name;
    private int groupID;

    public ShoppingListItem(String shopping_name, int groupID) {
        this.shopping_name = shopping_name;
        this.groupID = groupID;
    }

    public ShoppingListItem(String shopping_name) {
        this.shopping_name = shopping_name;
    }

    public ShoppingListItem() {
    }

    public int getShoppingID() {
        return shoppingID;
    }

    public void setShoppingID(int shoppingID) {
        this.shoppingID = shoppingID;
    }

    public String getShopping_name() {
        return shopping_name;
    }

    public void setShopping_name(String shopping_name) {
        this.shopping_name = shopping_name;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }
}

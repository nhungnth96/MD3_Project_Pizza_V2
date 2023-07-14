package ra.model.cart;

import ra.config.Color;
import ra.config.Validation;
import ra.model.food.Food;
import ra.model.food.PizzaCrust;
import ra.model.food.PizzaExtrasCheese;
import ra.model.food.PizzaSize;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int itemId;
    private Food itemFood;
    private int itemQuantity;
    private PizzaCrust pizzaCrust = PizzaCrust.MEDIUM;
    private PizzaSize pizzaSize = PizzaSize.MEDIUM;
    private PizzaExtrasCheese pizzaExtrasCheese = PizzaExtrasCheese.NONE;
    private double pizzaPrice;
    public CartItem() {

    }

    public CartItem(int itemId, Food itemFood, int itemQuantity) {
        this.itemId = itemId;
        this.itemFood = itemFood;
        this.itemQuantity = itemQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Food getItemFood() {
        return itemFood;
    }

    public void setItemFood(Food itemFood) {
        this.itemFood = itemFood;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public PizzaCrust getPizzaCrust() {
        return pizzaCrust;
    }

    public void setPizzaCrust(PizzaCrust pizzaCrust) {
        this.pizzaCrust = pizzaCrust;
    }

    public PizzaSize getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(PizzaSize pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public PizzaExtrasCheese getPizzaExtrasCheese() {
        return pizzaExtrasCheese;
    }

    public void setPizzaExtrasCheese(PizzaExtrasCheese pizzaExtrasCheese) {
        this.pizzaExtrasCheese = pizzaExtrasCheese;
    }

    public double getPizzaPrice() {
        return pizzaPrice;
    }

    public void setPizzaPrice(double pizzaPrice) {
        this.pizzaPrice = pizzaPrice;
    }

    @Override
    public String toString() {
        if(itemFood.getFoodCategory().getCategoryName().equals("Pizza")){
            String pizzaInfo ="Crust: " + pizzaCrust + " ─ Size: " + pizzaSize + " ─ Extras: " + pizzaExtrasCheese;
            int messageLength = pizzaInfo.length();
            int availableWidth = 67;
            int remainingWidth = availableWidth - messageLength;
            int leftWidth = remainingWidth / 2;
            int rightWidth = remainingWidth - leftWidth;
            String leftSeparator = "";
            String rightSeparator = "";
            for (int i = 0; i < leftWidth; i++) {
                leftSeparator += " ";
            }
            for (int i = 0; i < rightWidth; i++) {
                rightSeparator += " ";
            }
            String infoStr = "║" + leftSeparator + pizzaInfo + rightSeparator + "║";
            return String.format("║ %-3s │ %-10s │ %-25s │ %-8s│ %-8s ║", itemId, itemFood.getFoodCategory().getCategoryName(), itemFood.getFoodName(), itemQuantity, Validation.formatPrice(itemFood.getFoodPrice()))+"\n"+infoStr;
        } else {
           return String.format("║ %-3s │ %-10s │ %-25s │ %-8s│ %-8s ║", itemId,itemFood.getFoodCategory().getCategoryName(), itemFood.getFoodName(), itemQuantity, Validation.formatPrice(itemFood.getFoodPrice()));
        }
    }


}

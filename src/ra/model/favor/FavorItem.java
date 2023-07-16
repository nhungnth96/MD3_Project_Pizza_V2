package ra.model.favor;

import ra.config.Validation;
import ra.model.food.Food;
import ra.model.food.PizzaCrust;
import ra.model.food.PizzaExtrasCheese;
import ra.model.food.PizzaSize;

import java.io.Serializable;

public class FavorItem implements Serializable {
    private int favorId;
    private Food favorFood;
    private int itemQuantity;
    private PizzaCrust pizzaCrust = PizzaCrust.MEDIUM;
    private PizzaSize pizzaSize = PizzaSize.MEDIUM;
    private PizzaExtrasCheese pizzaExtrasCheese = PizzaExtrasCheese.NONE;
    private double pizzaPrice;
    public FavorItem() {
    }

    public FavorItem(int favorId, Food favorFood, int itemQuantity) {
        this.favorId = favorId;
        this.favorFood = favorFood;
        this.itemQuantity = itemQuantity;
    }

    public int getFavorId() {
        return favorId;
    }

    public void setFavorId(int favorId) {
        this.favorId = favorId;
    }

    public Food getFavorFood() {
        return favorFood;
    }

    public void setFavorFood(Food favorFood) {
        this.favorFood = favorFood;
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
        if(favorFood.getFoodCategory().getCategoryName().equals("Pizza")){
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
            return String.format("║ %-3s │ %-10s │ %-25s │ %-8s│ %-8s ║", favorId, favorFood.getFoodCategory().getCategoryName(), favorFood.getFoodName(), itemQuantity, Validation.formatPrice(favorFood.getFoodPrice()))+"\n"+infoStr;
        } else {
            return String.format("║ %-3s │ %-10s │ %-25s │ %-8s│ %-8s ║", favorId,favorFood.getFoodCategory().getCategoryName(), favorFood.getFoodName(), itemQuantity, Validation.formatPrice(favorFood.getFoodPrice()));
        }
    }

}

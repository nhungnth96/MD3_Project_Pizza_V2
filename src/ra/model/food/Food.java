package ra.model.food;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import ra.config.Alert;
import ra.config.InputMethods;
import ra.config.Validation;

import java.io.Serializable;
import java.util.List;

public class Food implements Serializable  {
    private int foodId;
    private String foodName;
    private String foodIngredient;
    private Category foodCategory;
    private double foodPrice;
    private int foodStock;
    private boolean foodStatus = true;
    public Food() {
    }

    public Food(int foodId, String foodName, String foodIngredient, Category foodCategory, double foodPrice, int foodStock, boolean foodStatus) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodIngredient = foodIngredient;
        this.foodCategory = foodCategory;
        this.foodPrice = foodPrice;
        this.foodStock = foodStock;
        this.foodStatus = foodStatus;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodIngredient() {
        return foodIngredient;
    }

    public void setFoodIngredient(String foodIngredient) {
        this.foodIngredient = foodIngredient;
    }

    public Category getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(Category foodCategory) {
        this.foodCategory = foodCategory;
    }

    public double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public int getFoodStock() {
        return foodStock;
    }

    public void setFoodStock(int foodStock) {
        this.foodStock = foodStock;
    }

    public boolean isFoodStatus() {
        return foodStatus;
    }

    public void setFoodStatus(boolean foodStatus) {
        this.foodStatus = foodStatus;
    }



    @Override
    public String toString() {

        return

                String.format("║─────────────────────────────────────────────────────────║"+"\n"+"║ %-3s │ %-25s │ %-10s │ %-8s ║", foodId, foodName, foodIngredient, Validation.formatPrice(foodPrice));
    }

    public String displayForAdmin() {
//        return  "---------------------------"+ "\n" +
//                "ID: " + foodId + " | Category: " + foodCategory.getCategoryName() +  " | Status: " + (foodStatus ? "On stock" : "Out of stock") + "\n" +
//                        "Name: " + foodName +  " | Ingredient: " + foodIngredient + "\n" +
//
//                "Price: " +  Validation.formatPrice(foodPrice) +
//                        " | Stock: " + foodStock
//                        ;
        return String.format("║────────────────────────────────────────────────────────────────────────────────────────────────║"+"\n"+
               "║ %-3s│ %-10s │ %-25s │ %-10s │ %-8s│ %-8s│ %-15s ║", foodId,foodCategory.getCategoryName(), foodName, foodIngredient, Validation.formatPrice(foodPrice), foodStock, (foodStatus ? "On stock" : "Out of stock")) ;

    }


    public void inputData(List<Category> categories) {
        System.out.println("Enter food name: ");
        this.foodName = InputMethods.getString();
        System.out.println("Enter food price: ");
        this.foodPrice = InputMethods.getPositiveDouble();
        System.out.println("Enter food ingredient: ");
        this.foodIngredient = InputMethods.getString();
        System.out.println("Enter food stock: ");
        this.foodStock = InputMethods.getStock();
        System.out.println("Choose food category: ");
        System.out.println("╓──────────────────╖");
        System.out.printf("║ %-3s │ %-10s ║%n","ID","Name" );
        for (Category category : categories) {
            System.out.println(category);
        }
        System.out.println("╙──────────────────╜");
        System.out.println("Enter category ID : ");
        while (true) {
            int categoryId = InputMethods.getInteger();
            boolean flag = true;
            for (Category category : categories) {
                if (categoryId == category.getCategoryId()) {
                    flag = false;
                    this.foodCategory = category;
                    break;
                }
            }
            if (flag) {
                System.err.println(Alert.NOT_FOUND);
            } else {
                break;
            }
        }
    }



}

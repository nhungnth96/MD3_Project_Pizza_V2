package ra.manager;

import ra.config.Alert;
import ra.config.Color;
import ra.config.InputMethods;
import ra.config.Validation;
import ra.controller.CategoryController;
import ra.controller.FoodController;
import ra.model.food.Category;
import ra.model.food.Food;
import ra.model.user.RoleName;

import java.util.ArrayList;
import java.util.List;

public class FoodManager {
    private FoodController foodController;
    private CategoryController categoryController;
    private List<Food> foodList;
    private List<Category> categoryList;

    public FoodManager() {
    }

    public FoodManager(FoodController foodController, CategoryController categoryController) {
        this.foodController = foodController;
        this.categoryController = categoryController;
        this.foodList = foodController.getAll();
        this.categoryList = categoryController.getAll();
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"────────────FOOD MANAGER─────────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. View food                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Search food               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Create food               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. Edit food                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        5. Delete food               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        6. Change food status        "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        7. Fill food stock           "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    viewFoodMenu();
                    break;
                case 2:
                    searchFoodMenu();
                    break;
                case 3:
                    createFood();
                    break;
                case 4:
                    editFood();
                    break;
                case 5:
                    deleteFood();
                    break;
                case 6:
                    changeFoodStatus();
                    break;
                case 7:
                    fillFoodStock();
                    break;
                case 0:
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (choice == 0) {
                break;
            }
        }

    }

    public static void viewFoodMenu() {
        FoodController foodController = new FoodController();
        List<Food> foodList = foodController.getAll();
        if (foodList.size() == 0) {
            System.err.println(Alert.EMPTY_LIST);
            return;
        }
        while (true) {
            System.out.println(Color.P + "╓" + Color.P + "────────────PIZZA MENU───────────────" + Color.P + "╖" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        1. View all                  " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        2. View by category          " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        0. Back                      " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "╙" + Color.P + "─────────────────────────────────────" + Color.P + "╜" + Color.RS);
            System.out.print("Enter choice: ");
            int choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    viewAllFood(foodList);
                    break;
                case 2:
                    viewFoodByCategory(foodList);
                    break;
                case 0:
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (choice == 0) {
                break;
            }
        }

    }

    private static void viewAllFood(List<Food> foodList) {
        if (Main.currentLogin != null && Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            System.out.println("╓────────────────────────────────────────────────────────────────────────────────────────────────╖");
            System.out.printf("║ %-3s│ %-10s │ %-25s │ %-10s │ %-8s│ %-8s│ %-15s ║%n", "ID", "Category", "Name", "Ingredient", "Price", "Stock","Status");

            for (Food food : foodList) {
                System.out.println(food.displayForAdmin());
            }
            System.out.println("╙────────────────────────────────────────────────────────────────────────────────────────────────╜");


        } else {
            System.out.println("╓─────────────────────────────────────────────────────────╖");
            System.out.printf("║ %-3s │ %-25s │ %-10s │ %-8s ║%n", "ID", "Name", "Ingredient", "Price");
            for (Food food : foodList) {
                if (food.isFoodStatus()) {
                    System.out.println(food);
                }
            }
            System.out.println("╙─────────────────────────────────────────────────────────╜");
        }
    }

    private static void viewFoodByCategory(List<Food> foodList) {
        CategoryController categoryController = new CategoryController();
        List<Category> categories = categoryController.getAll();
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getCategoryName());
        }
        System.out.println("Enter choice: ");
        while (true) {
            int catChoice = InputMethods.getInteger();
            if (catChoice >= 1 && catChoice <= categories.size()) {
                String catName = categories.get(catChoice - 1).getCategoryName();
                if (Main.currentLogin != null && Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
                    System.out.println("╓────────────────────────────────────────────────────────────────────────────────────────────────╖");
                    System.out.printf("║ %-3s│ %-10s │ %-25s │ %-10s │ %-8s│ %-8s│ %-15s ║%n", "ID", "Category", "Name", "Ingredient", "Price", "Stock","Status");
                    for (Food food : foodList) {
                        if (food.getFoodCategory().getCategoryName().equals(catName)) {
                            System.out.println(food.displayForAdmin());
                        }
                    }
                    System.out.println("╙────────────────────────────────────────────────────────────────────────────────────────────────╜");

                } else {
                    System.out.println("╓─────────────────────────────────────────────────────────╖");
                    System.out.printf("║ %-3s │ %-25s │ %-10s │ %-8s ║%n", "ID", "Name", "Ingredient", "Price");
                    for (Food food : foodList) {
                        if (food.getFoodCategory().getCategoryName().equals(catName)&&food.isFoodStatus()) {
                            System.out.println(food);
                        }
                    }
                    System.out.println("╙─────────────────────────────────────────────────────────╜");

                }
                break;
            } else {
                System.err.println(InputMethods.FORMAT_ERROR);
            }
        }
    }


    public static void searchFoodMenu() {
        FoodController foodController = new FoodController();
        List<Food> foodList = foodController.getAll();
        while (true) {
            System.out.println(Color.P + "╓" + Color.P + "────────────SEARCH PIZZA─────────────" + Color.P + "╖" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        1. Search by Name            " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        2. Search by Ingredient      " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "║" + Color.RS + "        0. Back                      " + Color.P + "║" + Color.RS);
            System.out.println(Color.P + "╙" + Color.P + "─────────────────────────────────────" + Color.P + "╜" + Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    searchFoodByName(foodList);
                    break;
                case 2:
                    searchFoodByIngredient(foodList);
                    break;
                case 0:
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }

            if (choice >= 0 && choice <= 2) {
                break;
            }
        }
    }

    private static void searchFoodByName(List<Food> foodList) {
        System.out.println("Enter keyword: ");
        String name = InputMethods.getString().toLowerCase();
        List<Food> result = new ArrayList<>();
        for (Food food : foodList) {
            if (food.getFoodName().toLowerCase().contains(name)) {
                result.add(food);
            }
        }
        if (result.isEmpty()) {
            System.err.println(Alert.NOT_FOUND);
        } else {
            if (Main.currentLogin != null && Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
                System.out.println("╓────────────────────────────────────────────────────────────────────────────────────────────────╖");
                System.out.printf("║ %-3s│ %-10s │ %-25s │ %-10s │ %-8s│ %-8s│ %-15s ║%n", "ID", "Category", "Name", "Ingredient", "Price", "Stock","Status");
                for (Food food : result) {
                    System.out.println(food.displayForAdmin());
                }
                System.out.println("╙────────────────────────────────────────────────────────────────────────────────────────────────╜");

            } else {
                System.out.println("╓─────────────────────────────────────────────────────────╖");
                System.out.printf("║ %-3s │ %-25s │ %-10s │ %-8s ║%n", "ID", "Name", "Ingredient", "Price");
                for (Food food : result) {
                    if (food.isFoodStatus()) {
                        System.out.println(food);
                    }
                }
                System.out.println("╙─────────────────────────────────────────────────────────╜");
            }

        }
    }

    private static void searchFoodByIngredient(List<Food> foodList) {
        System.out.println("Enter keyword: ");
        String ingredient = InputMethods.getString().toLowerCase();
        List<Food> result = new ArrayList<>();
        for (Food food : foodList) {
            if (food.getFoodIngredient().toLowerCase().contains(ingredient)) {
                result.add(food);
            }
        }
        if (result.isEmpty()) {
            System.err.println(Alert.NOT_FOUND);
        } else {
                if (Main.currentLogin != null && Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
                    System.out.println("╓────────────────────────────────────────────────────────────────────────────────────────────────╖");
                    System.out.printf("║ %-3s│ %-10s │ %-25s │ %-10s │ %-8s│ %-8s│ %-15s ║%n", "ID", "Category", "Name", "Ingredient", "Price", "Stock","Status");
                    for (Food food : result) {
                        System.out.println(food.displayForAdmin());
                    }
                    System.out.println("╙────────────────────────────────────────────────────────────────────────────────────────────────╜");

                } else {
                    System.out.println("╓─────────────────────────────────────────────────────────╖");
                    System.out.printf("║ %-3s │ %-25s │ %-10s │ %-8s ║%n", "ID", "Name", "Ingredient", "Price");
                        for (Food food : result) {
                            if (food.isFoodStatus()) {
                            System.out.println(food);
                        }
                    }
                    System.out.println("╙─────────────────────────────────────────────────────────╜");
                }
            }

        }



    private static void viewFoodByIngredient(List<Food> foodList) {
        while (true) {
            System.out.println("1. Seafood ");
            System.out.println("2. Chicken ");
            System.out.println("3. Beef ");
            System.out.println("4. Pork ");
            System.out.println("5. Vegetable ");
            System.out.println("6. Back");
            System.out.println("Enter choice: ");
            byte choiceDes = InputMethods.getByte();
            String des = "";
            switch (choiceDes) {
                case 1:
                    des = "Seafood";
                    break;
                case 2:
                    des = "Chicken";
                    break;
                case 3:
                    des = "Beef";
                    break;
                case 4:
                    des = "Pork";
                    break;
                case 5:
                    des = "Vegetable";
                    break;
                case 6:
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (!des.isEmpty()) {

                if (Main.currentLogin != null && Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
                    for (Food food : foodList) {
                        if (food.getFoodIngredient().contains(des)) {
                            System.out.println(food.displayForAdmin());
                        }
                    }
                } else {
                    for (Food food : foodList) {
                        if (food.getFoodIngredient().contains(des)) {
                            if (food.isFoodStatus()) {
                                System.out.println(food);
                            }
                        }
                    }
                }
            }
            if (choiceDes >= 1 && choiceDes <= 6) {
                break;
            }
        }
    }

    public void createFood() {
        System.out.println("Enter quantity want to add: ");
        int foodQuantity = InputMethods.getInteger();
        for (int i = 1; i <= foodQuantity; i++) {
            Food food = new Food();
            food.setFoodId(foodController.getNewId());
            System.out.println("Food ID: " + food.getFoodId());
            food.inputData(categoryList);
            foodController.save(food);
            System.out.println(Alert.SUCCESSFUL);
        }
    }

    public void editFood() {
        System.out.println("Enter food ID: ");
        Integer editFoodId = InputMethods.getInteger();
        Food editFood = foodController.findById(editFoodId);
        if (editFood == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
//        System.out.println(editFood.displayForAdmin());
        System.out.println("Category: "+editFood.getFoodCategory().getCategoryName()+ " | Name: "+editFood.getFoodName()+" | Ingredient: "+editFood.getFoodIngredient()+ " | Price: "+Validation.formatPrice(editFood.getFoodPrice())+ " | Stock: "+editFood.getFoodStock()+ " | Status: "+(editFood.isFoodStatus()?"On Stock":"Out of Stock"));
        editFood.inputData(categoryList);
        foodController.save(editFood);
        System.out.println(Alert.SUCCESSFUL);
    }

    public void deleteFood() {
        System.out.println("Enter food ID: ");
        int deleteFoodId = InputMethods.getInteger();
        Food deleteFood = foodController.findById(deleteFoodId);
        if (deleteFood == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        foodController.delete(deleteFoodId);
        System.out.println(Alert.SUCCESSFUL);
    }

    public void changeFoodStatus() {
        System.out.println("Enter food ID: ");
        int changeStatusFoodId = InputMethods.getInteger();
        Food changeStatusFood = foodController.findById(changeStatusFoodId);
        if (changeStatusFood == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        changeStatusFood.setFoodStatus(!changeStatusFood.isFoodStatus());
        foodController.save(changeStatusFood);
        System.out.println(Alert.SUCCESSFUL);
    }

    public void fillFoodStock() {
//        System.out.println("Enter food ID: ");
//        int fillFoodStockId = InputMethods.getInteger();
//        Food fillFoodStock = foodController.findById(fillFoodStockId);
//        if (fillFoodStock == null) {
//            System.err.println(Alert.NOT_FOUND);
//            return;
//        }
//        System.out.println("Current stock: " + fillFoodStock.getFoodStock());
//        System.out.println("Enter quantity want to fill: ");
//        int fillFoodQuantity = InputMethods.getInteger();
//        fillFoodStock.setFoodStock(fillFoodStock.getFoodStock() + fillFoodQuantity);
//        foodController.save(fillFoodStock);
//        System.out.println(Alert.SUCCESSFUL);
        for (Food food : foodList) {
            if (food.getFoodStock() != 100) {
                food.setFoodStock(100);
                foodController.save(food);
            }
        }
        System.out.println(Alert.SUCCESSFUL);
    }
}

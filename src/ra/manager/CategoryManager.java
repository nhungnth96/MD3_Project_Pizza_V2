package ra.manager;

import ra.config.Alert;
import ra.config.Color;
import ra.config.InputMethods;
import ra.controller.CategoryController;
import ra.model.food.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private CategoryController categoryController;
    private List<Category> categoryList;

    public CategoryManager(CategoryController categoryController) {
        this.categoryController = categoryController;
        this.categoryList = categoryController.getAll();
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"───────────CATEGORY MANAGER──────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. View category             "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Create category           "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Edit category             "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. Delete category           "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        5. Search category           "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    viewCategory();
                    break;
                case 2:
                    createCategory();
                    break;
                case 3:
                    editCategory();
                    break;
                case 4:
                    deleteCategory();
                    break;
                case 5:
                    searchCategory();
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

    public void viewCategory() {
        if (categoryList.size() == 0) {
            System.out.println(Alert.EMPTY_LIST);
        }
        System.out.println("╓──────────────────╖");
        System.out.printf("║ %-3s │ %-10s ║%n","ID","Name" );
        for (Category category : categoryList) {
            System.out.println(category);
        }
        System.out.println("╙──────────────────╜");

    }

    public void createCategory() {
        System.out.println("Enter quantity want to add: ");
        int quantity = InputMethods.getInteger();
        for (int i = 1; i <= quantity; i++) {
            Category category = new Category();
            category.setCategoryId(categoryController.getNewId());
            System.out.println("Category ID: " + category.getCategoryId());
            System.out.println("Enter category name: ");
            category.setCategoryName(InputMethods.getString());
            categoryController.save(category);
            System.out.println(Alert.SUCCESSFUL);
        }
    }

    public void editCategory() {
        System.out.println("Enter category ID: ");
        Integer editId = InputMethods.getInteger();
        Category editCategory = categoryController.findById(editId);
        if (editCategory == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        System.out.println("Category name: "+editCategory.getCategoryName());
        System.out.println("Enter category name: ");
        editCategory.setCategoryName(InputMethods.getString());
        categoryController.save(editCategory);
        System.out.println(Alert.SUCCESSFUL);
    }

    public void deleteCategory() {
        System.out.println("Enter category ID: ");
        int deleteId = InputMethods.getInteger();
        Category deleteCategory = categoryController.findById(deleteId);
        if (deleteCategory == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        categoryController.delete(deleteId);
        System.out.println(Alert.SUCCESSFUL);
    }

    public void searchCategory() {
        System.out.println("Enter keyword: ");
        String cat = InputMethods.getString();
        List<Category> result = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getCategoryName().toLowerCase().contains(cat)) {
                result.add(category);
            }
        }
        if (result.isEmpty()) {
            System.err.println(Alert.NOT_FOUND);
        } else {
            System.out.println("╓──────────────────╖");
            System.out.printf("║ %-3s │ %-10s ║%n","ID","Name" );
            for (Category category : result) {
                System.out.println(category);
            }
            System.out.println("╙──────────────────╜");

        }
    }
}

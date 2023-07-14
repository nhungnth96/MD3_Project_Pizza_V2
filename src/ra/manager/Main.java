package ra.manager;


import ra.config.Alert;
import ra.config.Color;
import ra.config.InputMethods;
import ra.config.Validation;
import ra.controller.CategoryController;
import ra.controller.FoodController;
import ra.controller.UserController;
import ra.model.user.RoleName;
import ra.model.user.User;

import java.util.Arrays;
import java.util.HashSet;

public class Main {
    // currentLogin chạy toàn cục
    public static User currentLogin;

    private static UserController userController = new UserController();
    //    private static CartController cartController = new CartController(currentLogin);
    private static CategoryController categoryController = new CategoryController();
    private static FoodController foodController = new FoodController();

    // ===============================HOME PAGE===============================
    public static void homePage() {
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"──────WELCOME TO DOMINO'S PIZZA──────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. Login                     "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Register                  "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. View menu                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Exit                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    guestPage();
                    break;
                case 0:
                    System.out.println(Color.P+"Thank you!!!"+Color.RS);
                    System.exit(0);
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
        }
    }

    // ===============================SIGN IN=============================== DONE
    public static void login() {
        System.out.println(Color.P+"─"+Color.P+"────────────────LOGIN────────────────"+Color.P+"─"+Color.RS);
        System.out.print(Color.P+" "+Color.RS+"Enter username: ");
        String username;
        while (true) {
            username = InputMethods.getString();
            if (!Validation.validateSpaces(username)) {
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (Validation.validateUserName(username)) {
                break;
            }
            System.err.println(Alert.USERNAME_ERROR);
        }
        System.out.print(Color.P+" "+Color.RS+"Enter password: ");

        String password;
        while (true) {
            password = InputMethods.getString();
            if (!Validation.validateSpaces(password)) {
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (Validation.validatePassword(password)) {

                break;
            }
            System.err.println(Alert.PASSWORD_ERROR);

        }
        User userLogin = userController.checkExistedAccount(username, password);
        if (userLogin == null) {
            System.err.println("The account doesn't exist!");
        } else {
            if (userLogin.getRoles().contains(RoleName.ADMIN)) {
                currentLogin = userLogin;
                adminPage();
            } else {
                if (userLogin.getRoles().contains(RoleName.USER) && userLogin.isStatus()) {
                    currentLogin = userLogin;
                    userPage();
                } else {
                    System.err.println("Your account has been locked!");
                    homePage();

                }
            }
        }
    }

    // ===============================SIGN UP=============================== DONE
    public static void register() {
        User user = new User();
        System.out.println(Color.P+"─"+Color.P+"──────────────REGISTER───────────────"+Color.P+"─"+Color.RS);
        user.setId(userController.getNewId());
        System.out.print(Color.P+" "+Color.RS+"Enter name: ");
        user.setName(InputMethods.getString());
        System.out.print(Color.P+" "+Color.RS+"Enter username: ");
        String username;
        while (true) {
            username = InputMethods.getString();
            if (!Validation.validateSpaces(username)) {
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (userController.checkExistedUsername(username)) {
                System.err.println(Alert.EXISTED_ERROR);
                continue;
            }
            if (Validation.validateUserName(username)) {
                user.setUsername(username);
                break;
            }
            System.err.println(Alert.USERNAME_ERROR);

        }
        System.out.print(Color.P+" "+Color.RS+"Enter password: ");
        String password;
        while (true) {
            password = InputMethods.getString();
            if (!Validation.validateSpaces(password)) {
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (Validation.validatePassword(password)) {
                user.setPassword(password);
                break;
            }
            System.err.println(Alert.PASSWORD_ERROR);
        }
        System.out.print(Color.P+" "+Color.RS+"Enter email: ");
        String email;
        while (true) {
            email = InputMethods.getString();
            if (!Validation.validateSpaces(email)) {
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (userController.checkExistedEmail(email)) {
                System.err.println(Alert.EXISTED_ERROR);
                continue;
            }
            if (Validation.validateEmail(email)) {
                user.setEmail(email);
                break;
            }
            System.err.println(Alert.EMAIL_ERROR);
        }
        user.setRoles(new HashSet<>(Arrays.asList(RoleName.USER)));
        userController.save(user);
        System.out.println(Alert.SUCCESSFUL);
        System.out.println(Color.P+"Please login"+Color.RS);
    }

    // ===============================================GUEST PAGE===============================================
    public static void guestPage() {
        System.out.println("\u001B[33mYou must be login to buy!\u001B[0m");
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"──────WELCOME TO DOMINO'S PIZZA──────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. View menu                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Search food               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    FoodManager.viewFoodMenu();
                    break;
                case 2:
                    FoodManager.searchFoodMenu();
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
    // ===============================================USER PAGE===============================================
    public static void userPage() {
        while (true) {
            String welcomeMessage =  "WELCOME " + currentLogin.getName().toUpperCase();
            int messageLength = welcomeMessage.length();
            int availableWidth = 37;

            int remainingWidth = availableWidth - messageLength;
            int leftWidth = remainingWidth / 2;
            int rightWidth = remainingWidth - leftWidth;

            String leftSeparator = "";
            String rightSeparator = "";

            for (int i = 0; i < leftWidth; i++) {
                leftSeparator += "─";
            }

            for (int i = 0; i < rightWidth; i++) {
                rightSeparator += "─";
            }

            System.out.println(Color.P + "╓" + leftSeparator + welcomeMessage + rightSeparator + "╖" + Color.RS);
//            System.out.println(Color.P+"╓"+Color.P+"────────────WELCOME "+currentLogin.getName().toUpperCase()+"────────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. View menu                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Search food               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Add to cart               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. My Cart                   "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        5. Add to favor              "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        6. My Favor                  "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        7. My Order                  "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        8. My Profile                "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Log out                   "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    FoodManager.viewFoodMenu();
                    break;
                case 2:
                    FoodManager.searchFoodMenu();
                    break;
                case 3:
                    CartManager.addItemToCartV2();
                    break;

                case 4:
                    new CartManager();
                    break;
                case 5:
                    FavorManager.addItemToFavorList();
                case 6:
                    new FavorManager();

                    break;
                case 7:
                    new OrderManagerV2();
                    break;
                case 8:
                    new ProfileManager(userController);
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (choice == 0) {
                break;
            }
        }

    }

    // ===============================================ADMIN PAGE===============================================
    public static void adminPage() {
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"────────────WELCOME "+currentLogin.getName().toUpperCase()+"────────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. User Manager              "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Category Manager          "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Food Manager              "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. Order Manager             "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Log out                   "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    new UserManager(userController);
                    break;
                case 2:
                    new CategoryManager(categoryController);
                    break;
                case 3:
                    new FoodManager(foodController, categoryController);
                    break;
                case 4:
                    new OrderManagerV2();
                    break;
                case 0:
                    logout();
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (choice == 0) {
                break;
            }
        }
    }

    // ===============================================LOG OUT===============================================
    public static void logout() {
        currentLogin = null;
        homePage();
    }

    public static void main(String[] args) {
        homePage();

    }

}





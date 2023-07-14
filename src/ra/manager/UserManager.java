package ra.manager;

import ra.config.Alert;
import ra.config.Color;
import ra.config.InputMethods;
import ra.config.Validation;
import ra.controller.UserController;
import ra.model.user.RoleName;
import ra.model.user.User;

import java.util.*;

public class UserManager {
    private UserController userController;
    public UserManager(UserController userController) {
        this.userController = userController;
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"────────────USER MANAGER─────────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. Show all account          "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Search account            "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Change status             "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. Change role               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        5. Create account            "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    showAllAccount();
                    break;
                case 2:
                    searchAccount();
                    break;
                case 3:
                    changeStatus();
                    break;
                case 4:
                    changeRole();
                    break;
                case 5:
                    createAccount();
                    break;
                case 0:
                    Main.adminPage();
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
            if (choice == 0) {
                break;
            }
        }
    }

    public void showAllAccount() {
        if (userController.getAll().isEmpty()) {
            System.out.println(Alert.EMPTY_LIST);
            return;
        }
        System.out.println("╓───────────────────────────────────────────────────────────────────────────────╖");
        System.out.printf("║ %-3s │ %-25s │ %-15s│ %-15s │ %-8s ║%n", "ID", "Name", "Username", "Role","Status");
        for (User user : userController.getAll()) {
            System.out.println("║───────────────────────────────────────────────────────────────────────────────║");
            System.out.println(user);
        }
        System.out.println("╙───────────────────────────────────────────────────────────────────────────────╜");
    }
    public void changeStatus() {
        System.out.println("Enter user ID: ");
        int userId = InputMethods.getInteger();
        userController.changeStatus(userId);
    }
    public void searchAccount(){
        System.out.println("Enter keyword: ");
        String username = InputMethods.getString().toLowerCase();
        List<User> result = new ArrayList<>();
        for (User user : userController.getAll()) {
            if (user.getUsername().toLowerCase().contains(username)) {
                result.add(user);
            }
        }
        if (result.isEmpty()) {
            System.err.println(Alert.NOT_FOUND);
        } else {
            for (User user:result){
                System.out.println(user);
            }
        }
    }
    public void changeRole() {
        System.out.println("Enter user ID: ");
        int userId = InputMethods.getInteger();
        userController.changeRole(userId);
    }
    public void createAccount() {
        User user = new User();
        user.setId(userController.getNewId());
        System.out.println("Enter name: ");
       user.setName(InputMethods.getString());
        System.out.println("Enter username: ");
        String username;
        while (true) {
            username = InputMethods.getString();
            if(!Validation.validateSpaces(username)){
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
        System.out.println("Enter password: ");
        String password;
        while (true) {
            password = InputMethods.getString();
            if(!Validation.validateSpaces(password)){
                System.err.println(Alert.SPACE_ERROR);
                continue;
            }
            if (Validation.validatePassword(password)) {
                user.setPassword(password);
                break;
            }
            System.err.println(Alert.PASSWORD_ERROR);
        }
        System.out.println("Enter email: ");
        String email;
        while (true) {
            email = InputMethods.getString();
            if(!Validation.validateSpaces(email)){
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
//        System.out.println("Enter Roles: (etc: user,admin,...)");
//        String roles = InputMethods.getString().toLowerCase();
//        String[] stringRoles = roles.split(",");
//        List<String> listRoles = Arrays.asList(stringRoles);;
//        for (String role : stringRoles) {
//            switch (role) {
//                case "admin":
//                    user.getRoles().add(RoleName.ADMIN);
//                case "user":
//                    user.getRoles().add(RoleName.USER);
//                default:
//                    user.getRoles().add(RoleName.USER);
//            }
//        }
        user.setRoles(new HashSet<>(Arrays.asList(RoleName.USER)));
        userController.save(user);
        System.out.println(Alert.SUCCESSFUL);

    }

}




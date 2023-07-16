package ra.manager;

import ra.config.Alert;
import ra.config.Color;
import ra.config.InputMethods;
import ra.config.Validation;
import ra.controller.*;
import ra.model.cart.CartItem;
import ra.model.favor.FavorItem;
import ra.model.food.Food;
import ra.model.food.PizzaCrust;
import ra.model.food.PizzaExtrasCheese;
import ra.model.food.PizzaSize;
import ra.model.order.Order;
import ra.model.order.PaymentMethod;
import ra.model.order.ShippingMethod;
import ra.model.user.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavorManager {
    private static FavorController favorController ;
    private FoodController foodController;
    private OrderController orderController;
    public FavorManager() {
        favorController = new FavorController(Main.currentLogin);
        foodController = new FoodController();
        orderController = new OrderController();
        while (true) {
            System.out.println(Color.P+"╓"+Color.P+"──────────────MY FAVOR───────────────"+Color.P+"╖"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        1. View list                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        2. Change item quantity      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        3. Delete item               "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        4. Clear list                "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        5. Check out                 "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
            System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
            System.out.print("Enter choice: ");
            byte choice = InputMethods.getByte();
            switch (choice) {
                case 1:
                    showFavorList();
                    break;
                case 2:
                    changeItemQuantity();
                    break;
                case 3:
                    deleteItemInFavorList();
                    break;
                case 4:
                    clearFavorList();
                    break;
                case 5:
                    checkOutOne(foodController);
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
    public void showFavorList() {
        User currentLogin = Main.currentLogin;
        List<FavorItem> itemList = currentLogin.getFavor();
        if(itemList.isEmpty()){
            System.out.println("\u001B[33mEmpty item list\u001B[0m");
            return;
        }
        System.out.println("╓───────────────────────────────────────────────────────────────────╖");
        System.out.printf("║ %-3s │ %-10s │ %-25s │ %-8s│ %-8s ║%n", "ID","Category", "Name", "Quantity", "Price");
        for (FavorItem item:itemList){
            item.setFavorFood(foodController.findById(item.getFavorFood().getFoodId()));
            System.out.println("║───────────────────────────────────────────────────────────────────║");
            System.out.println(item);
        }
        System.out.println("╙───────────────────────────────────────────────────────────────────╜");
    }
    public static void addItemToFavorList() {
        favorController = new FavorController(Main.currentLogin);
        FoodController foodController = new FoodController();
        System.out.println("Enter food ID: ");
        int footId = InputMethods.getInteger();
        Food food = foodController.findById(footId);
        if(food ==null){
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        FavorItem item = new FavorItem();
        item.setFavorId(favorController.getNewId());
        item.setFavorFood(food);
        item.setItemQuantity(1);
        if(item.getFavorFood().getFoodCategory().getCategoryName().equals("Pizza")){
            System.out.println("Choose pizza crust: ");
            for (int i = 0; i < PizzaCrust.values().length; i++) {
                System.out.println((i + 1) + ". " + PizzaCrust.values()[i]);
            }
            while(true){
                int crustChoice = InputMethods.getInteger();
                if (crustChoice >= 1 && crustChoice <= PizzaCrust.values().length) {
                    item.setPizzaCrust(PizzaCrust.values()[crustChoice-1]);
                    break;
                }
                System.err.println(InputMethods.FORMAT_ERROR);
            }
            System.out.println("Choose pizza size: ");
            for (int i = 0; i < PizzaSize.values().length; i++) {
                System.out.println((i + 1) + ". " + PizzaSize.values()[i] + " --- Price: " + Validation.formatPrice(item.getFavorFood().getFoodPrice() + PizzaSize.values()[i].getPrice()));
            }
            while(true){
                int sizeChoice = InputMethods.getInteger();
                if (sizeChoice >= 1 && sizeChoice <= PizzaSize.values().length) {
                    item.setPizzaSize(PizzaSize.values()[sizeChoice-1]);
                    item.setPizzaPrice(item.getFavorFood().getFoodPrice()+item.getPizzaSize().getPrice());
                    break;
                }
                System.err.println(InputMethods.FORMAT_ERROR);
            }
            System.out.println("Choose pizza extras cheese: ");
            for (int i = 0; i < PizzaExtrasCheese.values().length; i++) {
                System.out.println((i + 1) + ". " + PizzaExtrasCheese.values()[i] + " --- Price: " + Validation.formatPrice(item.getPizzaPrice() + PizzaExtrasCheese.values()[i].getPrice()));

            }
            while(true){
                int extrasCheeseChoice = InputMethods.getInteger();
                if (extrasCheeseChoice >= 1 && extrasCheeseChoice <= PizzaExtrasCheese.values().length) {
                    item.setPizzaExtrasCheese(PizzaExtrasCheese.values()[extrasCheeseChoice-1]);
                    item.setPizzaPrice(item.getPizzaPrice()+item.getPizzaExtrasCheese().getPrice());
                    break;
                }
                System.err.println(InputMethods.FORMAT_ERROR);
            }
        }
        favorController.save(item);
        System.out.println(Alert.SUCCESSFUL);
    }
    public void changeItemQuantity(){
        System.out.println("Enter item ID: ");
        int itemId = InputMethods.getInteger();
        FavorItem item = favorController.findById(itemId);
        if(item==null){
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        System.out.println("Quantity:" + item.getItemQuantity());
        System.out.println("Enter new quantity: ");
        item.setItemQuantity(InputMethods.getInteger());
        favorController.save(item);

    }
    public void deleteItemInFavorList() {
        System.out.println("Enter item ID: ");
        int deleteId = InputMethods.getInteger();
        if(favorController.findById(deleteId)==null){
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        favorController.delete(deleteId);
        System.out.println(Alert.SUCCESSFUL);
    }
    public void clearFavorList() {
        favorController.clearAll();
        System.out.println(Alert.SUCCESSFUL);
    }
    public void checkOut(FoodController foodController){
        favorController = new FavorController(Main.currentLogin);
        this.foodController = foodController;
        User currentLogin = Main.currentLogin;
        List<FavorItem> itemList = currentLogin.getFavor();
        if(itemList.isEmpty()){
            System.out.println("\u001B[33mEmpty item list\u001B[0m");
            return;
        }
        // kiểm tra số lượng trong kho
        for(FavorItem item: itemList){
            Food food = foodController.findById(item.getFavorFood().getFoodId());
            if(item.getItemQuantity()> food.getFoodStock()){
                System.out.println("The "+ food.getFoodName()+" is only have "+ food.getFoodStock()+" in stock, please reduce");
                return;
            }
            if(food.getFoodStock()==0) {
                System.err.println("The "+ food.getFoodName() + " is out of stock");
                return;
            }
        }
        Order newOrder = new Order();
        OrderController orderController = new OrderController() ;
        newOrder.setId(orderController.getNewId());
        // copy sp trong giỏ hàng sang hóa đơn
        List<CartItem> favorToCart = new ArrayList<>();
        for(FavorItem favorItem: currentLogin.getFavor()){
            CartItem cartItem = new CartItem();
            cartItem.setItemId(favorItem.getFavorId());
            cartItem.setItemFood(favorItem.getFavorFood());
            cartItem.setItemQuantity(favorItem.getItemQuantity());
            favorToCart.add(cartItem);
        }

        newOrder.setOrderDetail(favorToCart);
        // cập nhật tổng tiền
        double total = 0;
        for (FavorItem item: itemList) {
            if (item.getFavorFood().getFoodCategory().getCategoryName().equals("Pizza")) {
                total += (item.getItemQuantity() * item.getPizzaPrice());
            } else {
                total += (item.getItemQuantity() * item.getFavorFood().getFoodPrice());
            }
        }
        newOrder.setTotal(total);
        newOrder.setUserId(currentLogin.getId());
        System.out.println("Choose payment method: ");
        for (int i = 0; i < PaymentMethod.values().length; i++) {
            System.out.println((i + 1) + ". " + PaymentMethod.values()[i]);
            // 1. Cash
            // 2. Wallet
        }
        while(true){
            int payChoice = InputMethods.getInteger();
            if (payChoice >= 1 && payChoice <= PaymentMethod.values().length) {
                if(payChoice==2){
                    if(currentLogin.getWallet()< newOrder.getTotal()){
                        System.err.println("Your wallet is not enough money to pay, load more money or choose another method ");
                        return;
                    }
                    currentLogin.setWallet(currentLogin.getWallet()- newOrder.getTotal());
                }
                newOrder.setPaymentMethod(PaymentMethod.values()[payChoice-1]);
                break;
            }
            System.err.println(InputMethods.FORMAT_ERROR);
        }
        System.out.println("Choose shipping method: ");
        for (int i = 0; i < ShippingMethod.values().length; i++) {
            System.out.println((i + 1) + ". " + ShippingMethod.values()[i]);
            // 1. Delivery
            // 2. Takeaway
        }
        while(true){
            int shipChoice = InputMethods.getInteger();
            if (shipChoice >= 1 && shipChoice <= PaymentMethod.values().length){
                newOrder.setShippingMethod(ShippingMethod.values()[shipChoice-1]);
                if(shipChoice==1){
                    newOrder.setTotal(total+newOrder.getShippingMethod().getPrice());
                    if(newOrder.getPaymentMethod().equals(PaymentMethod.WALLET)){
                        if(currentLogin.getWallet()< newOrder.getTotal()){
                            System.err.println("Your wallet is not enough money to pay, load more money or choose another method ");
                            return;
                        }
                    }
                    currentLogin.setWallet(currentLogin.getWallet()- newOrder.getTotal());
                    System.out.println("Enter receiver name: ");
                    newOrder.setReceiver(InputMethods.getString());
                    System.out.println("Enter phone number: ");
                    String tel;
                    while (true) {
                        tel = InputMethods.getString();
                        if (Validation.validateTel(tel)) {
                            newOrder.setPhoneNumber(tel);
                            break;
                        }
                        System.err.println(Alert.TEL_ERROR);
                    }
                    System.out.println("Enter address: ");
                    newOrder.setAddress(InputMethods.getString());
                    break;
                }
                if (currentLogin.getName().equals("")){
                    System.out.println("Enter receiver: ");
                    newOrder.setReceiver(InputMethods.getString());
                    break;
                }
                newOrder.setReceiver(currentLogin.getName());
                if (currentLogin.getTel().equals("")){
                    System.out.println("Enter phone number: ");
                    String tel;
                    while (true) {
                        tel = InputMethods.getString();
                        if (Validation.validateTel(tel)) {
                            break;
                        }
                        System.err.println(Alert.TEL_ERROR);
                    }
                    newOrder.setPhoneNumber(tel);
                    break;
                }
                newOrder.setPhoneNumber(currentLogin.getTel());
                if (currentLogin.getAddress().equals("")){
                    System.out.println("Enter address: ");
                    newOrder.setAddress(InputMethods.getString());
                    break;
                }
                newOrder.setAddress(currentLogin.getAddress());
                break;
            }
            System.err.println(InputMethods.FORMAT_ERROR);
        }
        orderController.save(newOrder);
        System.out.println("\u001B[43mThank you!!!\u001B[0m");
        // giảm số lượng trong stock
        for(FavorItem item: itemList){
            FoodController foodController1 = new FoodController();
            Food food = foodController1.findById(item.getFavorFood().getFoodId());
            food.setFoodStock(food.getFoodStock()- item.getItemQuantity());
            foodController1.save(food);
        }
    }
    public void checkOutOne(FoodController foodController){
        favorController = new FavorController(Main.currentLogin);
        this.foodController = foodController;
        User currentLogin = Main.currentLogin;
        UserController userController = new UserController();
        List<FavorItem> itemList = currentLogin.getFavor();
        List<FavorItem> chooseItemList = new ArrayList<>();
        System.out.println("Enter item ID: (you can choose more than one like 1,3)");
        String itemIdStr = InputMethods.getString();
        String[] itemIdStrArr = itemIdStr.split(",");
        List<String> itemIdList = Arrays.asList(itemIdStrArr);
        for (String id: itemIdList){
            FavorItem item = favorController.findById(Integer.valueOf(id));
            if(item==null){
                System.err.println(Alert.NOT_FOUND);
                return;
            }
            chooseItemList.add(item);
        }
        if(chooseItemList.isEmpty()){
            System.out.println(Alert.EMPTY_LIST);
            return;
        }
        // kiểm tra số lượng trong kho
        for(FavorItem item: chooseItemList){
            Food food = foodController.findById(item.getFavorFood().getFoodId());
            if(item.getItemQuantity()> food.getFoodStock()){
                System.out.println("The "+ food.getFoodName()+" is only have "+ food.getFoodStock()+" in stock, please reduce");
                return;
            }
            if(food.getFoodStock()==0) {
                System.err.println("The "+ food.getFoodName() + " is out of stock");
                return;
            }
        }
        Order newOrder = new Order();
        OrderController orderController = new OrderController() ;
        CartController cartController = new CartController(Main.currentLogin);
        newOrder.setId(orderController.getNewId());
        // copy sp trong giỏ hàng sang hóa đơn
        List<CartItem> favorToCart = new ArrayList<>();
        for(FavorItem favorItem: chooseItemList){
            CartItem cartItem = new CartItem();
            cartItem.setItemId(favorItem.getFavorId());
            cartItem.setItemFood(favorItem.getFavorFood());
            cartItem.setItemQuantity(favorItem.getItemQuantity());
            favorToCart.add(cartItem);
            cartController.resetId(favorToCart);
        }
        newOrder.setOrderDetail(favorToCart);
        // cập nhật tổng tiền
        double total = 0;
        for (CartItem item: favorToCart) {
            if (item.getItemFood().getFoodCategory().getCategoryName().equals("Pizza")) {
                total += (item.getItemQuantity() * item.getPizzaPrice());
            } else {
                total += (item.getItemQuantity() * item.getItemFood().getFoodPrice());
            }
        }
        newOrder.setTotal(total);
        System.out.println("Total: " + Validation.formatPrice(total));
        newOrder.setUserId(currentLogin.getId());
        System.out.println("Choose payment method: ");
        for (int i = 0; i < PaymentMethod.values().length; i++) {
            System.out.println((i + 1) + ". " + PaymentMethod.values()[i]);
            // 1. Cash
            // 2. Wallet
        }
        while (true) {
            int payChoice = InputMethods.getInteger();
            if (payChoice >= 1 && payChoice <= PaymentMethod.values().length) {
                if (payChoice == 2) {
                    if (currentLogin.getWallet() < newOrder.getTotal()) {
                        System.err.println("Your wallet is not enough money to pay, load more money or pay by cash ");
                        return;
                    }
                    currentLogin.setWallet(currentLogin.getWallet() - newOrder.getTotal());
                }
                newOrder.setPaymentMethod(PaymentMethod.values()[payChoice - 1]);
                break;
            }
            System.err.println(InputMethods.FORMAT_ERROR);
        }
        System.out.println("Choose shipping method: ");
        for (int i = 0; i < ShippingMethod.values().length; i++) {
            System.out.println((i + 1) + ". " + ShippingMethod.values()[i] + " --- Total: " + Validation.formatPrice(newOrder.getTotal() + ShippingMethod.values()[i].getPrice()));

            // 1. Delivery
            // 2. Takeaway
        }
        while (true) {
            int shipChoice = InputMethods.getInteger();
            if (shipChoice >= 1 && shipChoice <= PaymentMethod.values().length) {
                newOrder.setShippingMethod(ShippingMethod.values()[shipChoice - 1]);
                if (shipChoice == 1) {
                    newOrder.setTotal(total + newOrder.getShippingMethod().getPrice());
                    if (newOrder.getPaymentMethod().equals(PaymentMethod.WALLET)) {
                        if (currentLogin.getWallet() < newOrder.getTotal()) {
                            System.err.println("Your wallet is not enough money to pay, load more money or choose another method ");
                            return;
                        }
                        currentLogin.setWallet(currentLogin.getWallet() - newOrder.getTotal());
                        userController.save(currentLogin);

                    }

                }
                System.out.println("Enter receiver name: ");
                newOrder.setReceiver(InputMethods.getString());
                System.out.println("Enter phone number: ");
                String tel;
                while (true) {
                    tel = InputMethods.getString();
                    if (Validation.validateTel(tel)) {
                        newOrder.setPhoneNumber(tel);
                        break;
                    }
                    System.err.println(Alert.TEL_ERROR);
                }
                System.out.println("Enter address: ");
                newOrder.setAddress(InputMethods.getString());
                break;
            }

            System.err.println(InputMethods.FORMAT_ERROR);
        }
        orderController.save(newOrder);

        System.out.println("\u001B[1;35mThank you for choosing us! Your order will be confirm soon!\u001B[0m");
        // giảm số lượng trong stock
        for(CartItem item: favorToCart){
            FoodController foodController1 = new FoodController();
            Food food = foodController1.findById(item.getItemFood().getFoodId());
            food.setFoodStock(food.getFoodStock()- item.getItemQuantity());
            foodController1.save(food);
        }
        for (String id: itemIdList){
            FavorItem item = favorController.findById(Integer.valueOf(id));
            if(item==null){
                System.err.println(Alert.NOT_FOUND);
                return;
            }
            itemList.remove(item);
            favorController.resetId(itemList);
        }
        currentLogin.setFavor(itemList);
        userController.save(currentLogin);
    }
}


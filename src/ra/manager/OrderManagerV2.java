package ra.manager;

import ra.config.*;
import ra.controller.FoodController;
import ra.controller.OrderController;
import ra.controller.UserController;
import ra.model.cart.CartItem;
import ra.model.feedback.Feedback;
import ra.model.food.Food;
import ra.model.order.Order;
import ra.model.order.PaymentMethod;
import ra.model.user.RoleName;
import ra.model.user.User;

import java.util.ArrayList;
import java.util.List;


public class OrderManagerV2 {
    private OrderController orderController;

    public OrderManagerV2() {
        orderController = new OrderController();
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            while (true) {
                System.out.println(Color.P+"╓"+Color.P+"────────────ORDER MANAGER────────────"+Color.P+"╖"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        1. View all order            "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        2. View pending order        "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        3. View confirmed order      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        4. View delivered order      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        5. View canceled order       "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        6. Change order status       "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        7. View feedback             "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
                System.out.print("Enter choice: ");
                byte choice = InputMethods.getByte();
                switch (choice) {
                    case 1:
                        viewAllOrder();
                        break;
                    case 2: // pending
                        viewOrderByCode((byte) 2);
                        break;
                    case 3: // confirm
                        viewOrderByCode((byte) 3);
                        break;
                    case 4: // delivered
                        viewInvoice();
                        break;
                    case 5: // cancel
                        viewOrderByCode((byte) 6);
                        break;
                    case 6:
                        changeOrderStatus();
                        break;
                    case 7:
                        viewFeedback();
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

        } else {
            while (true) {
                System.out.println(Color.P+"╓"+Color.P+"──────────────MY ORDER───────────────"+Color.P+"╖"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        1. View all order            "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        2. View pending order        "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        3. View confirmed order      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        4. View delivered order      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        5. View canceled order       "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        6. Cancel order              "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        7. Give a feedback           "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"║"+Color.RS+"        0. Back                      "+Color.P+"║"+Color.RS);
                System.out.println(Color.P+"╙"+Color.P+"─────────────────────────────────────"+Color.P+"╜"+Color.RS);
                System.out.print("Enter choice: ");
                byte choice = InputMethods.getByte();
                switch (choice) {
                    case 1:
                        viewAllOrder();
                        break;
                    case 2: // pending
                        viewOrderByCode((byte) 2);
                        break;
                    case 3: // accepted
                        viewOrderByCode((byte) 3);
                        break;
                    case 4: // delivered
                        viewInvoice();
                        break;
                    case 5: // cancel
                        viewOrderByCode((byte) 6);
                        break;
                    case 6:
                        cancelOrder();
                        break;
                    case 7:
                        giveAFeedback();
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
    }

    public void viewFeedback(){
        List<Order> orderList = orderController.getAll();
        List<Order> filter = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus() == 5&&!order.getFeedbackList().isEmpty()) {
                filter.add(order);
            }
        }
        if (filter.isEmpty()) {
            System.out.println(Alert.NO_FEEDBACK);
            return;
        }
        for (Order order : filter) {
            UserController userController = new UserController();
            User orderUser = userController.findById(order.getUserId());
            List<String> itemName = new ArrayList<>();
           for (CartItem item:order.getOrderDetail()){
               itemName.add(item.getItemFood().getFoodName());
           }
            System.out.println("---------------------------------------" + "\n" +
                    "Order ID: " + order.getId() +  " | Customer: " + orderUser.getName() + "\n"+
                    "Ordered: " + itemName.toString().replace("[","").replace("]","")+ "\n"+
                    "Feedback: " + order.getFeedbackList().toString().replace("[","").replace("]",""));

        }

    }
    public void viewAllOrder() {
        List<Order> orderList;
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {

            orderList = orderController.getAll();
        } else {
            orderList = orderController.findOrderByUserId();
        }
        if (orderList.isEmpty()) {
            System.err.println(Alert.EMPTY_LIST);
            return;
        }
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            for (Order order : orderList) {
                UserController userController = new UserController();
                User orderUser = userController.findById(order.getUserId());
                displayOrderForAdmin(order, orderUser);
            }
        } else {
            for (Order order : orderList) {
                System.out.println(order);
            }

        }
    }

    public void viewOrderByCode(byte code) {
        List<Order> orderList;
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            orderList = orderController.getAll();
        } else {
            orderList = orderController.findOrderByUserId();
        }
        List<Order> filter = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus() == code) {
                filter.add(order);
            }
        }
        if (filter.isEmpty()) {
            System.err.println(Alert.EMPTY_LIST);
            return;
        }
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            for (Order order : filter) {
                UserController userController = new UserController();
                User orderUser = userController.findById(order.getUserId());
                displayOrderForAdmin(order, orderUser);
            }
        } else {
            for (Order order : filter) {
                System.out.println(order);
            }
        }
    }

    public void viewInvoice() {
        List<Order> orderList;
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            orderList = orderController.getAll();
        } else {
            orderList = orderController.findOrderByUserId();
        }
        List<Order> filter = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus() == 5) {
                filter.add(order);
            }
        }
        if (filter.isEmpty()) {
            System.err.println(Alert.EMPTY_LIST);
            return;
        }

        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            for (Order order : filter) {
                UserController userController = new UserController();
                User orderUser = userController.findById(order.getUserId());
                displayOrderForAdmin(order, orderUser);
            }
        } else {
            for (Order order : filter) {
                displayInvoice(order);
            }
        }
    }
    public void cancelOrder() {
        System.out.println("Enter order ID: ");
        int cancelOrderId = InputMethods.getInteger();
        Order cancelOrder = orderController.findById(cancelOrderId);
        if (cancelOrder == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        switch (cancelOrder.getStatus()){
            case (byte)2:
                cancelByCustomer(cancelOrder);
                break;
            case (byte)3:
                System.err.println("The order is preparing, cancel is deny!");
                break;
            case (byte)4:
                System.err.println("The order is delivering, cancel is deny!");
                break;
            case (byte)5:
                System.err.println("The order is delivered");
                break;
            case (byte)6:
                System.err.println("The order has been canceled");
                break;
            default:
                System.err.println(InputMethods.FORMAT_ERROR);
        }
    }
    private void cancelByCustomer(Order order) {
        System.out.println("Are you sure want to cancel ?");
        System.out.println("1.Yes");
        System.out.println("2.No");
        System.out.println("Enter your choice: ");
        int choice = InputMethods.getInteger();
        if (choice == 1) {
            order.setStatus((byte) 6);
            for (CartItem item : order.getOrderDetail()) {
                FoodController foodController = new FoodController();
                Food food = foodController.findById(item.getItemFood().getFoodId());
                food.setFoodStock(food.getFoodStock() + item.getItemQuantity());
                foodController.save(food);

            }
            UserController userController = new UserController();
            Main.currentLogin.setWallet(Main.currentLogin.getWallet()+ order.getTotal());
            userController.save(Main.currentLogin);
            orderController.save(order);

            System.out.println(Alert.SUCCESSFUL);
        }
    }

    public void changeOrderStatus() {
        UserController userController = new UserController();
        System.out.println("Enter order ID: ");
        int changeOrderStatusId = InputMethods.getInteger();
        Order changeOrderStatus = orderController.findByIdForAdmin(changeOrderStatusId);
        if (changeOrderStatus == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        switch (changeOrderStatus.getStatus()){
            case (byte)2:
                confirm(changeOrderStatus);
                break;
            case (byte)3:
                System.err.println("The order is preparing, cancel is deny!");
                break;
            case (byte)4:
                System.err.println("The order is delivering, cancel is deny!");
                break;
            case (byte)5:
                System.err.println("The order is delivered to customer");
                break;
            case (byte) 6:
                System.err.println("The order has been canceled");
                break;
            default:
                System.err.println(InputMethods.FORMAT_ERROR);
        }
    }
    private void confirm(Order changeOrderStatus) {
        System.out.println("Enter choice: ");
        System.out.println("1. Confirm");
        System.out.println("2. Cancel");
        System.out.println("0. Back");
        int choice = InputMethods.getInteger();
            switch (choice) {
                case 1:
                    preparingAndDelivering(changeOrderStatus);
                    break;
                case 2:
                    cancelByAdmin(changeOrderStatus);
                    break;
                case 0:
                    break;
                default:
                    System.err.println(InputMethods.FORMAT_ERROR);
            }
    }
    private void preparingAndDelivering(Order changeOrderStatus) {
        changeOrderStatus.setStatus((byte) 3); // prepare
        orderController.save(changeOrderStatus);
        Thread prepareThread = new Thread(() -> {
            try {
                Thread.sleep(Order.PREPARE_TIME);
                changeOrderStatus.setStatus((byte) 4); // delivering
                orderController.save(changeOrderStatus);
                Thread deliveryThread = new Thread(() -> {
                    try {
                        Thread.sleep(Order.DELIVERY_TIME);
                        changeOrderStatus.setStatus((byte) 5); // delivered
                        System.out.println("\u001B[4;36mOrder ID "+changeOrderStatus.getId()+ " is DELIVERED\u001B[0m");
                        orderController.save(changeOrderStatus);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                deliveryThread.start();
                System.out.println("\u001B[4;36mOrder ID "+changeOrderStatus.getId()+" is DELIVERING\u001B[0m");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        prepareThread.start();
        System.out.println("\u001B[4;36mOrder ID "+changeOrderStatus.getId()+" is PREPARING\u001B[0m");
    }
    private void cancelByAdmin(Order changeOrderStatus) {
        changeOrderStatus.setStatus((byte) 6);
        orderController.save(changeOrderStatus);
        FoodController foodController = new FoodController();
        for (CartItem item : changeOrderStatus.getOrderDetail()) {
            Food food = foodController.findById(item.getItemFood().getFoodId());
            food.setFoodStock(food.getFoodStock() + item.getItemQuantity());
            foodController.save(food);
        }
        UserController userController = new UserController();
        int buyUserId = changeOrderStatus.getUserId();
        User buyUser = userController.findById(buyUserId);
        if(changeOrderStatus.getPaymentMethod().equals(PaymentMethod.WALLET)) {
            buyUser.setWallet(buyUser.getWallet() + changeOrderStatus.getTotal());
            userController.save(buyUser);
        }
        System.out.println(Alert.SUCCESSFUL);

    }

    public void giveAFeedback() {
        List<Order> orderList;
        if (Main.currentLogin.getRoles().contains(RoleName.ADMIN)) {
            orderList = orderController.getAll();
        } else {
            orderList = orderController.findOrderByUserId();
        }
        List<Order> filter = new ArrayList<>();
        for (Order order : orderList) {
            if (order.getStatus() == 5) {
                filter.add(order);
            }
        }
        if (filter.isEmpty()) {
            System.out.println(Alert.EMPTY_LIST);
            return;
        }
        System.out.println("Enter order ID: ");
        int feedbackOrderId = InputMethods.getInteger();
        Order feedbackOrder = find(feedbackOrderId, filter);
        if (feedbackOrder == null) {
            System.err.println(Alert.NOT_FOUND);
            return;
        }
        System.out.println("Enter feedback: ");
        Feedback feedback = new Feedback();
        feedback.setFeedbackContent(InputMethods.getString());
        feedbackOrder.getFeedbackList().add(feedback);
        orderController.save(feedbackOrder);
        System.out.println("\u001B[1;35mThank you for your feedback!\u001B[0m");
    }


    public Order find(int id, List<Order> orderList) {
        for (Order order : orderList) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }
    public void displayOrderForAdmin(Order order, User orderUser) {

        System.out.println("-----------------------------------------------------------------" + "\n" +
                "Status: " + Message.getStatusByCode(order.getStatus()) + "\n" +
                "Order ID: " + order.getId() + " | Date: " + order.getBuyDate() + "\n" +
                "Customer: " + orderUser.getName() + " | Phone number: " + orderUser.getTel() + "\n" +
                "Payment method: "+order.getPaymentMethod()+ " | Shipping method: "+order.getShippingMethod()+  "\n" +
                "Receiver: " + order.getReceiver() + " | Phone number: " + order.getPhoneNumber() + "\n" +
                "Address: " + order.getAddress() + "\n"+
                "Details: " +
                order.getOrderDetail().toString().replace(", ","\n").replace("[","\n").replace("]","\n").replace("ID: ","").replace("║","").replace("│","") + "\n"+
                "Total:                                                " + Validation.formatPrice(order.getTotal()) + "\n");



    }
    public  void displayInvoice(Order order){
        System.out.println("-------------------------RECEIPT------------------------------" + "\n" +
                "                   Order ID: " + order.getId() + "\n"+
                "                   Date: " + order.getBuyDate() + "\n" +
                        "-------------------------INFORMATION--------------------------"+"\n"+
                "                   Receiver: " + order.getReceiver() + "\n" +
                "                   Phone number: " + order.getPhoneNumber() + "\n" +
                "                   Address: " + order.getAddress() + "\n" +
                "--------------------------Details-----------------------------"+ "\n" +
                order.getOrderDetail().toString().replace(", ","\n").replace("[","\n").replace("]","\n").replace("ID: ","").replace("║","").replace("│","") + "\n"+
                "Total:                                                 " + Validation.formatPrice(order.getTotal()) + "\n"+
                "\n"+

                "--------------------------THANK YOU----------------------------"+ "\n");
    }



}


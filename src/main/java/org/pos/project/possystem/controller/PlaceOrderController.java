package org.pos.project.possystem.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.pos.project.possystem.dto.*;
import org.pos.project.possystem.model.PlaceOrderModel;
import org.pos.project.possystem.model.ProductModel;
import org.pos.project.possystem.model.ProductStockModel;
import org.pos.project.possystem.tm.Cart;
import org.pos.project.possystem.util.CommonMethod;
import org.pos.project.possystem.util.CustomAlertType;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


public class PlaceOrderController {


    @FXML
    private ComboBox<String> cmbItemCode;

    @FXML
    private TableColumn<?, ?> colDesc;

    @FXML
    private TableColumn<?, ?> colItemCode;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colUnitPrice;

    @FXML
    private Label getPName;

    @FXML
    private Label getPPrice;

    @FXML
    private Label getPQ;

    @FXML
    private Label getpDes;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblOrderId;

    @FXML
    private Label lblTime;

    @FXML
    private TextField qtyFroCustomer;

    @FXML
    private TextField searchtxtOrder;

    @FXML
    private TableView<Cart> tblCart;

    @FXML
    private Label lblNetTotal;

    ObservableList<Cart> cartList = FXCollections.observableArrayList();


    public void initialize(){

        loadDateAndTime();
        loadProductIDs();
        loadId();

        colItemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> setDataForLabels());

    }


    private void loadId() {

        Integer id = PlaceOrderModel.generateOrderId();
        lblOrderId.setText(String.valueOf(id));

    }

    public void setDataForLabels(){

        ProductDTO productDTO = ProductModel.getProductByID(Integer.parseInt(cmbItemCode.getValue()));

        ProductStockDTO productStockDTO = ProductStockModel.getStockDetails(Integer.parseInt(cmbItemCode.getValue()));

        if(productStockDTO != null){
            getPName.setText(productDTO.getName());
            getPPrice.setText("Rs. " + productDTO.getUnitPrice());
            getPQ.setText(String.valueOf(productStockDTO.getQuantity()));
            getpDes.setText(productDTO.getDescription());
        }else {
            CommonMethod.showAlert("Stock Status","Not Available Stock", CustomAlertType.INFORMATION);
        }
    }

    private void loadProductIDs() {
        ObservableList<ProductDTO> allProducts = FXCollections.observableArrayList(ProductModel.getAllProducts());


        ObservableList<String> ids = FXCollections.observableArrayList();

        for (ProductDTO product : allProducts) {
            ids.add(String.valueOf(product.getId()));
        }
        cmbItemCode.setItems(ids);
    }


    private void loadDateAndTime(){

        // Set Date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(dateFormat.format(date));

        // Set Time
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime time = LocalTime.now();
            lblTime.setText(
                    time.getHour() + " : " + time.getMinute() + " : " + time.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {

        if (cmbItemCode.getValue() == null) {
            CommonMethod.showAlert("VALIDATION ERROR", "Please select a product/item code.", CustomAlertType.WARNING);
            return;
        }

        if (qtyFroCustomer.getText().isEmpty()) {
            CommonMethod.showAlert("VALIDATION ERROR", "Please enter the quantity.", CustomAlertType.WARNING);
            return;
        }

        ProductDTO productDTO = ProductModel.getProductByID(Integer.parseInt(cmbItemCode.getValue()));
        int qty = Integer.parseInt(qtyFroCustomer.getText());

        double total = qty * productDTO.getUnitPrice();

        Cart cart = Cart.builder()
                .itemCode(productDTO.getId())
                .name(productDTO.getName())
                .qty(qty)
                .unitPrice(productDTO.getUnitPrice())
                .total(total)
                .build();

        double qtyStock = Double.parseDouble(getPQ.getText());

        if (qtyStock < qty) {
            CommonMethod.showAlert("Invalid QTY","Not Available QTY", CustomAlertType.WARNING);
            return;
        }

        cartList.add(cart);
        tblCart.setItems(cartList);

        double tot = 0;
        for (Cart cartObj : cartList) {
            tot += cartObj.getTotal();
        }
        lblNetTotal.setText(String.valueOf(tot));
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        cartList.clear();
        tblCart.setItems(cartList);
        lblNetTotal.setText("0.00/=");
        CommonMethod.showAlert("SUCCESS","Cart cleared successfully!", CustomAlertType.INFORMATION);
        initialize();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {

        int id = PlaceOrderModel.generateOrderId();
        List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();

        for (Cart cart : cartList){
            int productId = cart.getItemCode();
            double qty = cart.getQty();
            orderDetailsDTOList.add(
                    OrderDetailsDTO.builder()
                            .transactionId(id)
                            .prductId(productId)
                            .qty((int) qty)
                            .build()
            );
        }
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date orderDate = format.parse(lblDate.getText());

            TransactionDTO transactionDTO = new TransactionDTO(id, orderDate, orderDetailsDTOList);

            boolean isOrderPlace = PlaceOrderModel.placeOrder(transactionDTO);

            if (isOrderPlace) {
                loadId();
                CommonMethod.showAlert("OK","order Place !!", CustomAlertType.INFORMATION);
            } else {
                CommonMethod.showAlert("ERROR","failed. please rollback the order !!", CustomAlertType.WARNING);
            }


        }catch (SQLException | ParseException e) {
            CommonMethod.showAlert("ERROR",e.getMessage(), CustomAlertType.WARNING);
        }

    }

    @FXML
    void searchOrderbtn(ActionEvent event) {

        if (searchtxtOrder.getText().isEmpty()) {
            CommonMethod.showAlert("Validation Error", "Please enter an Order ID.", CustomAlertType.WARNING);
            return;
        }

        int orderId = Integer.parseInt(searchtxtOrder.getText());

        try {
            List<OrderDetailsDTO> orderDetailsDTOList = PlaceOrderModel.searchOrderById(orderId);

            if(orderDetailsDTOList.isEmpty()){
                CommonMethod.showAlert("Error","Failed to search order", CustomAlertType.ERROR);
                return;
            }

            ObservableList<Cart> searchResultList = FXCollections.observableArrayList();

            for (OrderDetailsDTO orderDetail : orderDetailsDTOList) {
                ProductDTO product = ProductModel.getProductByID(orderDetail.getPrductId());

                double total = product.getUnitPrice() * orderDetail.getQty();

                searchResultList.add(
                        Cart.builder()
                                .itemCode(orderDetail.getPrductId())
                                .name(product.getName())
                                .qty(orderDetail.getQty())
                                .unitPrice(product.getUnitPrice())
                                .total(total)
                                .build()
                );
            }

            tblCart.setItems(searchResultList);

        }catch (SQLException e) {
            CommonMethod.showAlert("Error","Failed to search order", CustomAlertType.ERROR);
        }

    }


    @FXML
    void goBack(ActionEvent event) {

        CommonMethod.goToBack(event,getClass());


    }

    @FXML
    void billPrint(ActionEvent event) {

        try {
            InputStream report = getClass().getResourceAsStream("/org/pos/project/possystem/reports/Invoice.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(report);

            List<InvoiceDTO> dataBeanList = new ArrayList<>();

            String date = lblDate.getText();
            int oderId = Integer.parseInt(lblOrderId.getText());
            double netTotal= Double.parseDouble(lblNetTotal.getText());

            cartList.forEach(cartTBLModel -> dataBeanList.add(new InvoiceDTO(cartTBLModel.getName(), (int) cartTBLModel.getQty(), cartTBLModel.getUnitPrice(), date, oderId, netTotal)));

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataBeanList);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("MyParameter", dataSource);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

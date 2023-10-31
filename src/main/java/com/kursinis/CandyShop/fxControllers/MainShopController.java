package com.kursinis.CandyShop.fxControllers;

import com.kursinis.CandyShop.hibernateControllers.CustomHib;
import com.kursinis.CandyShop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainShopController implements Initializable {

    @FXML
    public ListView<Product> productList;
    @FXML
    public ListView<Product> currentOrder;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;
    @FXML
    public Tab ordersTab;
    @FXML
    public Tab productsTab;
    @FXML
    public TableView<CustomerTableData> customerTable;
    @FXML
    public TableView<ManagerTableData> managerTable;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab primaryTab;
    @FXML
    public ListView<Product> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea productDescriptionField;
    @FXML
    public ComboBox<ProductType> productType;
    @FXML
    public ComboBox<Warehouse> warehouseComboBox;
    @FXML
    public TextField weightField;
    @FXML
    public TextArea chemicalDescriptionField;
    @FXML
    public TextField productManufacturerField;
    @FXML
    public TextField commentTitleField;
    @FXML
    public TextArea commentBodyField;
    @FXML
    public ListView<Comment> commentListField;
    @FXML
    public Tab commentTab;
    @FXML
    public Label productCommentLabel;
    @FXML
    public TableColumn<CustomerTableData, String> customerTableName;
    @FXML
    public TableColumn<CustomerTableData, String> customerTableSurname;
    @FXML
    public TableColumn<CustomerTableData, Date> customerTableBirthdate;
    @FXML
    public TableColumn<CustomerTableData, String> customerTableAddress;
    @FXML
    public TableColumn<CustomerTableData, String> customerTableCardnumber;
    @FXML
    public TableColumn<ManagerTableData, String> managerEmployeeId;
    @FXML
    public TableColumn<ManagerTableData, String> managerTableName;
    @FXML
    public TableColumn<ManagerTableData, String> managerTableSurname;
    @FXML
    public TableColumn<ManagerTableData, String> managerBirthdate;
    @FXML
    public TableColumn<ManagerTableData, String> managerTableMedicalCertificate;
    @FXML
    public TableColumn<ManagerTableData, String> managerTableEmploymentDate;

    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        limitAccess();
        loadData();
    }

    private void loadData() {
        customHib = new CustomHib(entityManagerFactory);

        loadProductListInCart();
        loadCurrentOrder();
        loadUsers();
        loadManagers();

    }

    private void limitAccess() {
        if (currentUser.getClass() == Manager.class) {
            Manager manager = (Manager) currentUser;
            if (!manager.isAdmin()) {
                managerTable.setDisable(true);
            }
            tabPane.getTabs().remove(primaryTab);
            tabPane.getTabs().remove(commentTab);
        } else if (currentUser.getClass() == Customer.class) {
            Customer customer = (Customer) currentUser;
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(warehouseTab);
            tabPane.getTabs().remove(productsTab);

        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "WTF", "WTF", "WTF");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productType.getItems().addAll(ProductType.values());
    }


    public void loadTabValues() {
        if (productsTab.isSelected()) {
            loadProductListManager();
            loadCurrentOrder();
            List<Warehouse> record = customHib.getAllRecords(Warehouse.class);
            warehouseComboBox.getItems().clear();
            warehouseComboBox.getItems().addAll(customHib.getAllRecords(Warehouse.class));
            productTitleField.clear();
            productManufacturerField.clear();
            weightField.clear();
            productDescriptionField.clear();
            chemicalDescriptionField.clear();
        } else if (usersTab.isSelected()) {
            loadUsers();
            loadManagers();
        } else if (warehouseTab.isSelected()) {
            titleWarehouseField.clear();
            addressWarehouseField.clear();
            loadWarehouseList();
        } else if (commentTab.isSelected()) {
            productCommentLabel.setText("Product");
            commentTitleField.clear();
            commentBodyField.clear();
            loadCommentList();
        } else if (primaryTab.isSelected()) {
            loadProductListInCart();
            loadCurrentOrder();
        }
    }


    //----------------------Cart functionality-------------------------------//
    public void leaveComment() {
        try {
            Product selectProduct = productList.getSelectionModel().getSelectedItem();
            if (selectProduct != null) {
                tabPane.getSelectionModel().select(commentTab);
                productCommentLabel.setText(selectProduct.getTitle());
            }
        } catch (NullPointerException ignored) {

        }
    }

    public void addToCart() {
        try {
            Product selectProduct = productList.getSelectionModel().getSelectedItem();
            Cart cart = customHib.getEntityById(Cart.class, ((Customer) currentUser).getCart().getId());
            Product product = customHib.getEntityById(Product.class, selectProduct.getId());
            product.setCart(cart);
            cart.getItemsInCart().add(product);
            customHib.update(cart);
            loadProductListInCart();
            loadCurrentOrder();
        } catch (NullPointerException ignored) {

        }
    }

    public void deleteFromCart() {
        try {
            Product selectProduct = currentOrder.getSelectionModel().getSelectedItem();
            Cart cart = customHib.getEntityById(Cart.class, ((Customer) currentUser).getCart().getId());
            cart.getItemsInCart().remove(selectProduct);
            selectProduct.setCart(null);
            customHib.update(cart);
            customHib.update(selectProduct);
            loadProductListInCart();
            loadCurrentOrder();
        } catch (NullPointerException ignored) {
        }
    }

    private void loadCurrentOrder() {
        try {
            currentOrder.getItems().clear();
            currentOrder.getItems().addAll(customHib.getProductsByCartId(((Customer) currentUser).getCart().getId()));
        } catch (Exception ignored) {

        }
    }

    private void loadProductListInCart() {
        productList.getItems().clear();
        productList.getItems().addAll(customHib.getAvailableProducts());
    }

    //----------------------Users functionality-------------------------------//
    private void loadUsers() {
        List<Customer> customersList = customHib.getAllRecords(Customer.class);
        List<CustomerTableData> customerTableDataList = customersList.stream()
                .map(CustomerTableData::new)
                .collect(Collectors.toList());
        ObservableList<CustomerTableData> customers = FXCollections.observableArrayList(customerTableDataList);

        customerTable.setItems(customers);

        customerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        customerTableBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        customerTableAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerTableCardnumber.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));
    }

    private void loadManagers() {
        List<Manager> managerList = customHib.getAllRecords(Manager.class);
        List<ManagerTableData> managerTableDataList = managerList.stream()
                .map(ManagerTableData::new)
                .collect(Collectors.toList());
        ObservableList<ManagerTableData> managers = FXCollections.observableArrayList(managerTableDataList);

        managerTable.setItems(managers);

        managerEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        managerTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        managerTableSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        managerBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        managerTableMedicalCertificate.setCellValueFactory(new PropertyValueFactory<>("medicalCertificate"));
        managerTableEmploymentDate.setCellValueFactory(new PropertyValueFactory<>("employmentDate"));


    }

    private void test() {
        customerTable.getSelectionModel().getSelectedItem();

    }

    //----------------------Product functionality-------------------------------//

    public void enableProductFields() {
        if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
            weightField.setPromptText("Weight g.");
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
            weightField.setPromptText("Content volume ml.");
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
            weightField.setPromptText("Weight g.");
        }
    }

    private void loadProductListManager() {
        productListManager.getItems().clear();
        productListManager.getItems().addAll(customHib.getAllRecords(Product.class));
    }

    public void addNewProduct() {
        Warehouse selectedWarehouse = warehouseComboBox.getSelectionModel().getSelectedItem();
        Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());

        if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
            customHib.create(new Candy(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    warehouse,
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText()));
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
            customHib.create(new Drinks(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    warehouse,
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText()));
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
            customHib.create(new Snacks(
                    productTitleField.getText(),
                    productDescriptionField.getText(),
                    productManufacturerField.getText(),
                    warehouse,
                    Double.parseDouble(weightField.getText()),
                    chemicalDescriptionField.getText()));
        }

        loadProductListManager();
    }

    public void updateProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        Product product = customHib.getEntityById(Product.class, selectedProduct.getId());

        if (selectedProduct.getClass().getSimpleName().toUpperCase().equals(productType.getSelectionModel().getSelectedItem().name())) {
            product.setTitle(productTitleField.getText());
            product.setDescription(productDescriptionField.getText());
            product.setManufacturer(productManufacturerField.getText());
            product.setWarehouse(warehouseComboBox.getSelectionModel().getSelectedItem());

            if (productType.getSelectionModel().getSelectedItem() == ProductType.CANDY) {
                ((Candy) product).setWeight(Double.parseDouble(weightField.getText()));
                ((Candy) product).setChemicalContents(chemicalDescriptionField.getText());
            } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DRINK) {
                ((Drinks) product).setContentVolume(Double.parseDouble(weightField.getText()));
                ((Drinks) product).setChemicalContents(chemicalDescriptionField.getText());
            } else if (productType.getSelectionModel().getSelectedItem() == ProductType.SNACKS) {
                ((Snacks) product).setWeight(Double.parseDouble(weightField.getText()));
                ((Snacks) product).setChemicalContents(chemicalDescriptionField.getText());
            }

            customHib.update(product);


        } else {
            customHib.deleteProduct(selectedProduct.getId());
            addNewProduct();
        }

        loadProductListManager();
    }

    public void deleteProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        customHib.deleteProduct(selectedProduct.getId());
        loadProductListManager();
    }

    public void loadProductData() {
        try {
            Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
            productTitleField.setText(selectedProduct.getTitle());
            productDescriptionField.setText(selectedProduct.getDescription());
            productManufacturerField.setText(selectedProduct.getManufacturer());

            String productClass = selectedProduct.getClass().getSimpleName();
            switch (productClass) {
                case "Candy" -> {
                    productType.getSelectionModel().select(ProductType.CANDY);
                    weightField.setText(String.valueOf(((Candy) selectedProduct).getWeight()));
                    chemicalDescriptionField.setText(((Candy) selectedProduct).getChemicalContents());
                }
                case "Drinks" -> {
                    productType.getSelectionModel().select(ProductType.DRINK);
                    weightField.setText(String.valueOf(((Drinks) selectedProduct).getContentVolume()));
                    chemicalDescriptionField.setText(((Drinks) selectedProduct).getChemicalContents());
                }
                case "Snacks" -> {
                    productType.getSelectionModel().select(ProductType.SNACKS);
                    weightField.setText(String.valueOf(((Snacks) selectedProduct).getWeight()));
                    chemicalDescriptionField.setText(((Snacks) selectedProduct).getChemicalContents());

                }
            }

            warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());
        } catch (NullPointerException ignored) {

        }
    }

    //----------------------Warehouse functionality-----------------------------//

    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse() {
        if (!titleWarehouseField.getText().trim().isEmpty() && !addressWarehouseField.getText().trim().isEmpty()) {
            customHib.create(new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText()));
            loadWarehouseList();
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "Empty fields", "Please fill required fields");
        }
    }

    public void updateWarehouse() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
            warehouse.setTitle(titleWarehouseField.getText());
            warehouse.setAddress(addressWarehouseField.getText());
            customHib.update(warehouse);
            loadWarehouseList();
        } catch (Exception e) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "None selected", "Please select value to continue");
        }
    }

    public void removeWarehouse() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            if (selectedWarehouse.getInStockProducts().isEmpty()) {
                Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
                customHib.delete(Warehouse.class, selectedWarehouse.getId());
            } else {
                JavaFxCustomUtils.generateAlert(Alert.AlertType.INFORMATION, "Warning", "Warehouse is not empty", "Please remove products from warehouse");
                customHib.deleteWarehouse(selectedWarehouse.getId());
            }
            loadWarehouseList();

        } catch (Exception e) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.WARNING, "Warning", "None selected", "Please select value to continue");
        }

    }

    public void loadWarehouseData() {
        try {
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            titleWarehouseField.setText(selectedWarehouse.getTitle());
            addressWarehouseField.setText(selectedWarehouse.getAddress());
        } catch (NullPointerException ignored) {

        }
    }

    //----------------------Comment section functionality----------------------//

    private void loadCommentList() {
        commentListField.getItems().clear();
        commentListField.getItems().addAll(customHib.getAllRecords(Comment.class));
    }

    public void createComment() {
        try {
            Product selectedProduct = productList.getSelectionModel().getSelectedItem();
            Comment comment = new Comment(commentTitleField.getText(), commentBodyField.getText(), selectedProduct);
            customHib.create(comment);
            loadCommentList();
        } catch (NullPointerException ignored) {

        }
    }


    public void updateComment() {
        try {
            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
            Comment commentFromDb = customHib.getEntityById(Comment.class, selectedComment.getId());
            commentFromDb.setCommentTitle(commentTitleField.getText());
            commentFromDb.setCommentBody(commentBodyField.getText());
            customHib.update(commentFromDb);
            loadCommentList();
        } catch (NullPointerException ignored) {

        }
    }

    public void deleteComment() {
        try {
            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
            //Comment commentFromDb = genericHib.getEntityById(Comment.class, selectedComment.getId());
            customHib.deleteComment(selectedComment.getId());
            loadCommentList();
        } catch (NullPointerException ignored) {
        }
    }

    public void loadCommentInfo() {
        try {
            Comment selectedComment = commentListField.getSelectionModel().getSelectedItem();
            commentTitleField.setText(selectedComment.getCommentTitle());
            commentBodyField.setText(selectedComment.getCommentBody());
            productCommentLabel.setText(selectedComment.getProduct().getTitle());
        } catch (NullPointerException ignored) {

        }
    }
}

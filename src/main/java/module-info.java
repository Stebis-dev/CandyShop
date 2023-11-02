module com.kursinis.prif4kursinis {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires jbcrypt;


    opens com.CandyShop to javafx.fxml;
    exports com.CandyShop;
    opens com.CandyShop.model to javafx.fxml, org.hibernate.orm.core;
    exports com.CandyShop.model;
    opens com.CandyShop.fxControllers to javafx.fxml;
    exports com.CandyShop.fxControllers to javafx.fxml;
    exports com.CandyShop.fxControllers.tableViewGuidelines;
    opens com.CandyShop.fxControllers.tableViewGuidelines to javafx.fxml, org.hibernate.orm.core;
}
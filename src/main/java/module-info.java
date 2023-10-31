module com.kursinis.prif4kursinis {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;


    opens com.kursinis.CandyShop to javafx.fxml;
    exports com.kursinis.CandyShop;
    opens com.kursinis.CandyShop.model to javafx.fxml, org.hibernate.orm.core;
    exports com.kursinis.CandyShop.model;
    opens com.kursinis.CandyShop.fxControllers to javafx.fxml;
    exports com.kursinis.CandyShop.fxControllers to javafx.fxml;
}
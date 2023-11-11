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
    exports com.CandyShop.controllers.mainshop.tableViewGuidelines;
    opens com.CandyShop.controllers.mainshop.tableViewGuidelines to javafx.fxml, org.hibernate.orm.core;
    exports com.CandyShop.utils to javafx.fxml;
    opens com.CandyShop.utils to javafx.fxml;
    exports com.CandyShop.controllers.mainshop to javafx.fxml;
    opens com.CandyShop.controllers.mainshop to javafx.fxml;
    exports com.CandyShop.controllers.login to javafx.fxml;
    opens com.CandyShop.controllers.login to javafx.fxml;
    exports com.CandyShop.controllers.registration to javafx.fxml;
    opens com.CandyShop.controllers.registration to javafx.fxml;
    exports com.CandyShop.controllers.common to javafx.fxml;
    opens com.CandyShop.controllers.common to javafx.fxml;
}
module com.example.addressbook {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires org.junit.jupiter.api;


    opens com.example.addressbook to javafx.fxml;
    exports com.example.addressbook;
}
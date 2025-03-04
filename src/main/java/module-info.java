module com.example.cocktail {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires org.json;
    requires static lombok;
    requires mysql.connector.j;
    requires  java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.naming;

    opens com.example.cocktail to javafx.fxml, com.google.gson, org.json, lombok, mysql.connector.j, java.sql, org.hibernate.orm.core, jakarta.persistence, java.naming ;
    exports com.example.cocktail;


    opens com.example.cocktail.controller to com.google.gson, java.sql, javafx.fxml, lombok, mysql.connector.j, org.json, org.hibernate.orm.core, jakarta.persistence, java.naming;
    exports com.example.cocktail.controller;

    opens com.example.cocktail.model to com.google.gson, java.sql, javafx.fxml, lombok, mysql.connector.j, org.json, org.hibernate.orm.core, jakarta.persistence, java.naming;
    exports com.example.cocktail.model;

    opens com.example.cocktail.request to com.google.gson, java.sql, javafx.fxml, lombok, mysql.connector.j, org.json, org.hibernate.orm.core, jakarta.persistence, java.naming;
    exports com.example.cocktail.request;
}
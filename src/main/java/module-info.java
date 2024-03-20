open module com.fsd.management {
    /* jfx */
    requires javafx.controls;
    requires javafx.fxml;

    /* spring */
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    /* jackson */
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.annotation;
    requires org.yaml.snakeyaml;

    /* log */
    requires org.slf4j;

    /* jdk */
    requires java.desktop;
    requires org.apache.commons.lang3;
}
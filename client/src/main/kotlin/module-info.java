module client {
    requires spring.webflux;
    requires reactor.core;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires com.google.gson;


    opens com.manager.client.client to com.google.gson;
    opens com.manager.client.password to com.google.gson;
    opens com.manager.client to com.google.gson;
    opens com.manager.client.exceptions to com.google.gson;
    exports com.manager.client.client;
    exports com.manager.client.password;
    exports com.manager.client.exceptions;
    exports com.manager.client;
}
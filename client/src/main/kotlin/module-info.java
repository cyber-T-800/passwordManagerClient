module client {
    requires spring.webflux;
    requires reactor.core;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.databind;


    opens com.manager.client.client to com.google.gson;
    opens com.manager.client to com.google.gson;
    exports com.manager.client.client;
    exports com.manager.client;
}
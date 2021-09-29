module client {
    requires spring.webflux;
    requires reactor.core;
    requires kotlin.stdlib;
    requires com.fasterxml.jackson.databind;

    exports com.manager.client;
}
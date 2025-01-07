package ru.otus.hw.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.hw.handler.BookHandler;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.repositories.BookRepository;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Для того чтобы заработало надо раскомментировать в pom убрать spring-boot-starter-web
 * или исключить spring-boot-starter-tomcat
 */
@Configuration
public class FunctionalEndpointsBookConfig {

    @Bean
    RouterFunction<ServerResponse> getComposedRoutes(BookMapper mapper, BookRepository repository) {
        var handler = new BookHandler(mapper, repository);
        return route(GET("/library-api/route/book"), handler::read)
                .and(route(GET("/library-api/route/book/{id}"),
                        handler::readById
                    )
                );
    }

    @Bean
    RouterFunction<ServerResponse> crateRoute(BookMapper mapper, BookRepository repository) {
        var handler = new BookHandler(mapper, repository);
        return route(POST("/library-api/route/book"), handler::create);
    }

    @Bean
    RouterFunction<ServerResponse> updateRoute(BookMapper mapper, BookRepository repository) {
        var handler = new BookHandler(mapper, repository);
        return route(PUT("/library-api/route/book/{id}"), handler::update);
    }

    @Bean
    RouterFunction<ServerResponse> deleteRoute(BookMapper mapper, BookRepository repository) {
        var handler = new BookHandler(mapper, repository);
        return route(DELETE("/library-api/route/book/{id}"), handler::delete);
    }

}

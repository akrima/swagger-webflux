package com.akrima.contact;

import java.util.Arrays;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class AkrimaSwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AkrimaSwaggerApplication.class, args);
	}

}

@Configuration
class MyConfig {


	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
		return openApi -> openApi.servers(Arrays.asList(new Server().url("http://localhost:8080")))
				.path("/v1/employe",
						new PathItem().get(new Operation().operationId("getAll")
								.responses(new ApiResponses().addApiResponse("default", new ApiResponse().description("Avoir la liste des clients")
										.content(new Content().addMediaType(MediaType.APPLICATION_JSON.getSubtype(), new io.swagger.v3.oas.models.media.MediaType()))))))
				.path("/v1/employe/",
						new PathItem().get(new Operation().operationId("getById").parameters(Arrays.asList(new Parameter().allowEmptyValue(false).description("id du client").name("id")))
								.responses(new ApiResponses().addApiResponse("default", new ApiResponse().description("Avoir un client par id")
										.content(new Content().addMediaType(MediaType.APPLICATION_JSON.getSubtype(), new io.swagger.v3.oas.models.media.MediaType()))))));
	}

}

@Configuration
class EmployeRouter {

	@Bean
	public RouterFunction<ServerResponse> routes(EmployeHandler employeHandler) {
		return RouterFunctions.route(RequestPredicates.GET("/v1/employe/{id}"), employeHandler::get)
				.andRoute(RequestPredicates.GET("/v1/employe"), employeHandler::finAll);
	}
}

@Component
class EmployeHandler {

	Mono<ServerResponse> get(ServerRequest request) {
		String id = request.pathVariable("id");
		Employe employe = Employe.builder().id(1L).nom("krima").build();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(employe));
	}

	Mono<ServerResponse> finAll(ServerRequest request) {
		Employe employe = Employe.builder().id(1L).nom("krima").build();
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(employe));
	}
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Employe {
	private Long id;

	private String nom;
}

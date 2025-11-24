package server.dongmin.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("JWT Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        Server prodServer = new Server();
        prodServer.setUrl("https://dongmin.inuappcenter.kr");
        prodServer.description("Production Server");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.description("Local Development Server");

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme))
                .info(apiInfo())
                .servers(List.of(prodServer, localServer));
    }

    private Info apiInfo() {
        return new Info()
                .title("17.5th Server Study - dongmin") // API의 제목
                .description("17.5기 서버 스터디 과제 코드") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }
}

package ma.snrt.snrt.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {



  @Bean
  public OpenAPI myOpenAPI() {
    Server devServer = new Server();
    devServer.setUrl("http://localhost:8000/api/");
    devServer.setDescription("Server URL in Development environment");



    Server prodServer = new Server();
    prodServer.setUrl("http://localhost:8000/api/");
    prodServer.setDescription("Server URL in Production environment");





    Info info = new Info()
        .title("Enterprise Resource Management API")
        .version("1.0")
        .description("This API exposes endpoints to manage Enterprise Resource Management.");




    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
}
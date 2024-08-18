package io.github.douglas.ms_order.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setName("Douglas Liebl");
        contact.setEmail("douglasliebl@outlook.com");
        contact.setUrl("github.com/module-Douglas");

        Info info = new Info()
                .title("MS_ORDER")
                .description("Orders service.")
                .version("1.0.0")
                .contact(contact);

        return new OpenAPI()
                .info(info);
    }

}

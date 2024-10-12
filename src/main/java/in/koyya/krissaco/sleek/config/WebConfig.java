package in.koyya.krissaco.sleek.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration class to define global CORS settings.
 * 
 * This class implements the {@link WebMvcConfigurer} interface and overrides the
 * {@code addCorsMappings} method to allow cross-origin requests from specified origins.
 * 
 * @version v1.0
 * @since 2024-08-07
 * 
 * @author Sumanth
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures the CORS mappings.
     * 
     * This method allows cross-origin requests from http://localhost:3000
     * with specified methods and headers.
     * 
     * @param registry the {@link CorsRegistry} to configure
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        System.out.println("************************configuration is applied");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://localhost", "http://localhost:80", "https://krissaco.com", "http://krissaco-sleek.s3-website-ap-southeast-2.amazonaws.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
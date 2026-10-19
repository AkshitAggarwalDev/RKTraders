package RKTraders.web.Configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /*
     * CORS origins, externalized so production (Vercel) can be allowed without
     * a code change. Comma-separated list; localhost development always kept.
     * Set CORS_ALLOWED_ORIGINS=https://<your-app>.vercel.app in production.
     */
    @Value("${cors.allowed-origins:http://localhost:3000,http://127.0.0.1:3000}")
    private String[] allowedOrigins;

    /*
     * Serving directory for /uploads/** — the SAME directory that
     * ProductImageService writes to via file.upload-dir (e.g. /var/data/uploads
     * on Render's persistent disk). Default keeps local dev unchanged.
     */
    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Path.toUri() renders a valid file URL (absolute paths included);
        // a trailing slash is required for the resource-location root.
        String location = Paths.get(uploadDir).toUri().toString();
        if (!location.endsWith("/")) {
            location += "/";
        }

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization");
    }
}

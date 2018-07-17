package wifi4eu.wifi4eu.abac;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan
public class SpringRestConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}
}

/*
 * @Configuration
 * 
 * @EnableWebMvc
 * 
 * @ComponentScan(basePackages = "wifi4eu.wifi4eu.abac.rest") public class
 * SpringRestConfiguration extends WebMvcConfigurerAdapter {
 * 
 * @Bean public WebMvcConfigurer corsConfigurer() { return new
 * WebMvcConfigurerAdapter() {
 * 
 * @Override public void addCorsMappings(CorsRegistry registry) {
 * registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").
 * allowedHeaders("*").allowCredentials(true); } }; } }
 */

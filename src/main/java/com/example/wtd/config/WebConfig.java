package com.example.wtd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* CORS first solution: Configuration으로 해결하기 - 이 방법은 Global하게 적용하는 방법
*
*
* Default:
    Allow all origins.
    Allow "simple" methods GET, HEAD and POST.
    Allow all headers.
    Set max age to 1800 seconds (30 minutes).
*
*
* */



@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){     // CORS를 적용할 URL패턴을 정의
        registry.addMapping("/**")                //"/**" 와일드 카드를 사용할 수 있음
                .allowedOrigins("*")                        //자원 공유를 허락할 Origin을 지정할 수 있음
//                .addAllowedOriginPattern("*")
              //.allowedOrigins("http://localhost:8080", "http://localhost:8081");
                .allowedMethods("GET", "POST")             //HTTP method를 지정
                .maxAge(3000);                             //원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }


//
//    @Bean
//    public CorsFilter corsFilter() {
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(true);
//        // config.addAllowedOrigin("*");
//        config.addAllowedOriginPattern("*"); // addAllowedOriginPattern("*") 대신 사용
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsFilter(source);
//    }
}



/* CORS second solution: Controller 또는 메소드단에서 annotation을 통해 적용하는 방법

@RestController
@RequestMapping("/somePath")
public class SomeController {

    @CrossOrigin(origins="*")
    @RequestMapping(value = "/{something}",method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable Long reservationNo) throws Exception{
    }

}
*
출처: https://dev-pengun.tistory.com/entry/Spring-Boot-CORS-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0#2.%20Configuration%EC%9C%BC%EB%A1%9C%20%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0
*/
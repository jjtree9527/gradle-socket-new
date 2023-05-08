package top.inson.boot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author jingjitree
 * @description
 * @date 2023/4/20 10:40
 */
@Configuration
public class WebSocketConfiguration {


    @Bean
    ServerEndpointExporter serverEndpointExporter(){

        return new ServerEndpointExporter();
    }

}

package com.github.udinei.icompras.pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.github.udinei.icompras.pedidos.client")
public class ClientsConfig {

}

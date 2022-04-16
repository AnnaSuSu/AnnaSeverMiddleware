package com.AnnaSeverMiddleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import com.AnnaSeverMiddleware.config.MyFilter;

@SpringBootApplication
//自定义扫包和过滤器
@ComponentScan(excludeFilters = {@Filter(type = FilterType.CUSTOM, classes = MyFilter.class)})
public class AnnaSeverMiddlewareApplication {
	public static void main(String[] args) {
		SpringApplication.run(AnnaSeverMiddlewareApplication.class, args);
	}

}

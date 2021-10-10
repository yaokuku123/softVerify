package com.ustb.softverify;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.ustb.softverify.mapper")
@ComponentScan(basePackages = {"com.ustb"})
public class SoftCertificateApplication {
    public static void main(String[] args) {
        SpringApplication.run(SoftCertificateApplication.class,args);
    }
}

package com.wes.goddard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*
 * @ServletComponentScan: This annotation is used to scan all related annotations (@WebServlet, @WebFilter, @WebListener)
 * Here, to scan the filter configs in filter.LoginCheckFilter, so that the filter would work
 *
 * @EnableTransactionManagement : to enable the @Transaction annoation in the service layer method
 * */
@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class GoddardApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoddardApplication.class, args);
        log.info("Goddard Takeout Application Started Successfully!");
    }
}

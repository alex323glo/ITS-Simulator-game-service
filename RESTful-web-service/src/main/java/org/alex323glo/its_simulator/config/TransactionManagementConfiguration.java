package org.alex323glo.its_simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Transaction management's configuration class
 * (holds bean factory of PlatformTransactionManager bean).
 *
 * @author Alexey_O
 * @version 0.1
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagementConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean factoryBean) {
        return new JpaTransactionManager(factoryBean.getObject());
    }

}

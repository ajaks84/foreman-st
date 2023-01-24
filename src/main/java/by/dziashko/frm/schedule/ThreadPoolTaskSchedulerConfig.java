package by.dziashko.frm.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan(basePackages = "by.dziashko.frm.schedule", basePackageClasses = { ThreadPoolTaskSchedulerImpl.class })
public class ThreadPoolTaskSchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public PeriodicTrigger periodicTrigger() {

//        return new PeriodicTrigger(1, TimeUnit.MINUTES);
        return new PeriodicTrigger(4, TimeUnit.HOURS);
    }

}

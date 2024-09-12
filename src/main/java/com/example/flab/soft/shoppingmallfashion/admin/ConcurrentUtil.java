package com.example.flab.soft.shoppingmallfashion.admin;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConcurrentUtil {
    public static void collect(List<? extends Future<?>> jobs) {
        jobs.forEach(job -> {
            try {
                job.get();
            } catch (ExecutionException e) {
                log.warn(e.getMessage());
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

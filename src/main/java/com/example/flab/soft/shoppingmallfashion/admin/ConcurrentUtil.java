package com.example.flab.soft.shoppingmallfashion.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConcurrentUtil {
    public static <T> List<T> collect(List<? extends Future<T>> jobs) {
        List<T> results = new ArrayList<>();
        jobs.forEach(job -> {
            try {
                results.add(job.get());
            } catch (ExecutionException e) {
                log.warn("Execution exception: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return results;
    }
}

package com.jpmc.midascore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.foundation.Transaction;

@Service
public class IncentiveService {

    @Autowired
    private RestTemplate restTemplate;

    public float getIncentive(Transaction transaction) {
        try {
            String url = "http://localhost:8081/incentive";
            Incentive incentive = restTemplate.postForObject(
                url, transaction, Incentive.class
            );
            if (incentive != null) {
                return incentive.getAmount();
            }
        } catch (Exception e) {
            System.out.println("Incentive API error: " + e.getMessage());
        }
        return 0f;
    }
}
package com.example.springbootgraphql.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService {

  public Map<UUID, BigDecimal> getBalanceFor(Set<UUID> bankAccountIds, String userId) {
    log.info("Requesting bank account ids: {} for user ID: {}", bankAccountIds, userId);
    return Map.of(
        UUID.fromString("c6aa269a-812b-49d5-b178-a739a1ed74cc"),
        BigDecimal.ONE,
        UUID.fromString("024bb503-5c0f-4d60-aa44-db19d87042f4"),
        new BigDecimal("23431.22"));
  }
}

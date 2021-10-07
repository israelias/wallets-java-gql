package com.example.springbootgraphql.service;

import com.example.springbootgraphql.domain.bank.BankAccount;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BalanceService {

  public Map<UUID, BigDecimal> getBalanceFor(Map<UUID, BankAccount> bankAccountIds, String userId) {
    // logs map of id to entire bankaccount object associated with that id
    log.info("Requesting bank accounts: {} for user ID: {}", bankAccountIds, userId);
    // original set of ids
    var ids = bankAccountIds.keySet();
    log.info("Requesting bank account ids: {} for user ID: {}", ids, userId);

    /**
     * VisualVM JVM Profiling
     *
     * <p>Plant a massive hash set
     *
     * <p>1: initialize a new `bigCrazy` `HashSet` for every request coming into this method 2:
     * assign a random int isolated to the current ThreadLocal to the `size` variable. 3: initialize
     * a new `littleCrazy` `LinkedHashSet` with a capacity of `size` 4: Stream a range of 0 to
     * `size` and add each integer to the `littleCrazy` LinkedHashSet 5: add the `littleCrazy`
     * LinkedHash to the `bigCrazy` HashSet
     *
     * <p>Set<BigDecimal> bigCrazy = new HashSet<>(); var size =
     * ThreadLocalRandom.current().nextInt(250, 500); var littleCrazy = new
     * LinkedHashSet<BigDecimal>(size); IntStream.range(0, size).forEach(nextInt ->
     * littleCrazy.add(BigDecimal.valueOf(nextInt))); bigCrazy.addAll(littleCrazy);
     */
    return Map.of(
        UUID.fromString("c6aa269a-812b-49d5-b178-a739a1ed74cc"),
        BigDecimal.ONE,
        UUID.fromString("48e4a484-af2c-4366-8cd4-25330597473f"),
        new BigDecimal("23431.22"));
  }
}

package com.example.springbootgraphql.domain.bank;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Asset {
    UUID id;
}

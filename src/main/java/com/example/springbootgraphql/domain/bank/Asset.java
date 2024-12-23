package com.example.springbootgraphql.domain.bank;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Represents an asset in the banking domain.
 *
 * <p>This class is immutable and uses the Builder pattern for object creation.
 * It includes details such as the asset ID.
 */
@Builder
@Value
public class Asset {
    /**
     * The unique identifier of the asset.
     */
    UUID id;
}

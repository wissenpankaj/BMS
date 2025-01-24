package com.wissen.bms.reportingAPI.customIdGenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomFaultIdGenerator implements IdentifierGenerator {
    //generates unique fault id r with pattern "fault-001" using uuid
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return "fault-" + java.util.UUID.randomUUID().toString().substring(0, 3);
    }
}

package com.wissen.bms.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;


@Data
public class FaultLog {
    private Timestamp createdate = new Timestamp(System.currentTimeMillis());
    private Integer createdby;
    private Integer batteryid;
    private Integer faultid;
    private Integer servicestationid;
    private String description;
    private String faulttype;
}

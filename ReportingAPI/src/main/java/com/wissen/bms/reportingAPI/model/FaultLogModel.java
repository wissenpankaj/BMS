package com.wissen.bms.reportingAPI.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "faultlog")
public class FaultLogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer faultid;

    @Column(nullable = false)
    private Timestamp createdat = new Timestamp(System.currentTimeMillis());

    private Integer createdby;

    private Integer batteryid;

    @Column(nullable = false)
    private Integer servicestationid;

    private String description;

    @Column(length = 255)
    private String faulttype;

}

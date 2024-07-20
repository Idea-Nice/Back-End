package com.example.healax.asmr.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Asmr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fileName;

    private String fileType;
    private Long filesize;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

}

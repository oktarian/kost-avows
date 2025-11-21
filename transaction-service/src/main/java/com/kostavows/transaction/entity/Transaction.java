package com.kostavows.transaction.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String type;  // CREDIT / DEBIT

    private BigDecimal amount;

    private String kategori;  // Makan, Kost, Gaji, dll

    private String description;

    private LocalDate tanggal;
}
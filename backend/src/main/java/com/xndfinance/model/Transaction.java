package com.xndfinance.model;

import com.xndfinance.enumerators.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double amount;

    private String category;

    private TransactionTypeEnum transactionType;

    private String description;

    private LocalDate date;

    @ManyToOne
    private User user;

}

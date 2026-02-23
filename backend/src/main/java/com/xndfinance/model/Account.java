package com.xndfinance.model;

import com.xndfinance.enumerators.AccountTypeEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private AccountTypeEnum type;

    private Double balance;

    private String userId;
}

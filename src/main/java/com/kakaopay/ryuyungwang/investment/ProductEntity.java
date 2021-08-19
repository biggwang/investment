package com.kakaopay.ryuyungwang.investment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "product")
public class ProductEntity {

    @Id
    @Column(name = "productId", insertable = false, nullable = false)
    private Long productId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total_investing_amount", nullable = false)
    private Integer totalInvestingAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @CreationTimestamp
    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;
}

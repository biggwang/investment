package com.kakaopay.ryuyungwang.investment.entity;

import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "product")
public class ProductEntity {

    @Id
    @Column(name = "product_id", insertable = false, nullable = false)
    private Integer productId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total_investing_amount", nullable = false)
    private Integer totalInvestingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatusEnum status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Builder
    public ProductEntity(Integer productId, String title, Integer totalInvestingAmount, ProductStatusEnum status, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.productId = productId;
        this.title = title;
        this.totalInvestingAmount = totalInvestingAmount;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
}

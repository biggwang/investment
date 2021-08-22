package com.kakaopay.ryuyungwang.investment.entity;

import com.kakaopay.ryuyungwang.investment.code.ProductStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@DynamicUpdate
@NoArgsConstructor
@Entity(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "product_id", insertable = false, nullable = false)
    private Integer productId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total_investing_amount", nullable = false)
    private Integer totalInvestingAmount;

    @Column(name = "current_investing_amount")
    private Integer currentInvestingAmount = 0;

    @Column(name = "total_invester")
    private Integer totalInvestor = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatusEnum status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Builder
    public ProductEntity(String title, Integer totalInvestingAmount, ProductStatusEnum status, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.title = title;
        this.totalInvestingAmount = totalInvestingAmount;
        this.status = status;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
}

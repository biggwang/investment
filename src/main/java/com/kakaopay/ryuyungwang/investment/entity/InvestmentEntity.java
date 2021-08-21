package com.kakaopay.ryuyungwang.investment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity(name = "investment")
@Table(indexes = {
        @Index(name = "idx_investment_01", columnList = "product_id"),
        @Index(name = "idx_investment_02", columnList = "user_id"),
        @Index(name = "idx_investment_03", columnList = "investment_at"),
})
public class InvestmentEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "investment_id", insertable = false, nullable = false)
    private Integer investmentId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "investment_amount", nullable = false)
    private Integer investmentAmount;

    @CreationTimestamp
    @Column(name = "investment_at", nullable = false)
    private LocalDateTime investmentAt;

    @Builder
    public InvestmentEntity(Integer productId, Integer userId, Integer investmentAmount) {
        this.productId = productId;
        this.userId = userId;
        this.investmentAmount = investmentAmount;
    }
}

package com.kakaopay.ryuyungwang.investment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "investment")
public class InvestmentEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "investment_id", insertable = false, nullable = false)
    private Integer investmentId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "investment_amount", nullable = false)
    private Integer investmentAmount;

    @Builder
    public InvestmentEntity(Integer productId, String userId, Integer investmentAmount) {
        this.productId = productId;
        this.userId = userId;
        this.investmentAmount = investmentAmount;
    }
}

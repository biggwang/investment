package com.kakaopay.ryuyungwang.investment.service;

public interface InvestStatusStoreService {

    Long saveInvestmentAmount(Integer productId, Long investmentAmount);

    Long restoreInvestmentAmount(Integer productId, Long investmentAmount);

    Integer saveInvestorCount(Integer productId, Integer totalInvestorCount);

    Integer restoreInvestorCount(Integer productId, Integer totalInvestorCount);
}

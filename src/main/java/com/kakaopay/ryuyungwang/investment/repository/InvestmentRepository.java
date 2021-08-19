package com.kakaopay.ryuyungwang.investment.repository;

import com.kakaopay.ryuyungwang.investment.entity.InvestmentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends CrudRepository<InvestmentEntity, Integer> {

    List<InvestmentEntity> findByUserIdOrderByCreateAtDesc(String userID);
}

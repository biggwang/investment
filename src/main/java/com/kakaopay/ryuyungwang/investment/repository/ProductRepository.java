package com.kakaopay.ryuyungwang.investment.repository;

import com.kakaopay.ryuyungwang.investment.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {

    List<ProductEntity> findAllByStartedAtLessThanEqualAndFinishedAtGreaterThanEqual(LocalDateTime now1, LocalDateTime now2);
}

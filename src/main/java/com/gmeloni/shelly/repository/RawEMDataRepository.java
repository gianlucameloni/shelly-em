package com.gmeloni.shelly.repository;

import com.gmeloni.shelly.model.RawEMData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RawEMDataRepository extends CrudRepository<RawEMData, LocalDateTime> {

    @Query(value =  "select * from raw_em_data where sample_timestamp >= ?1 and sample_timestamp < ?2", nativeQuery = true)
    List<RawEMData> findAllBySampleTimestampBetween(LocalDateTime from, LocalDateTime to);
}

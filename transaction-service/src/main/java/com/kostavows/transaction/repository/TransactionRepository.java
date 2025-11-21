package com.kostavows.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kostavows.transaction.entity.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserIdAndTanggalBetween(Long userId, LocalDate start, LocalDate end);

    @Query(value = """
        SELECT 
          COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE 0 END), 0) as total_pemasukan,
          COALESCE(SUM(CASE WHEN t.type = 'DEBIT' THEN t.amount ELSE 0 END), 0) as total_pengeluaran,
          json_agg(
            json_build_object('kategori', t.kategori, 'total', t.amount)
            ORDER BY t.amount DESC
          ) FILTER (WHERE t.type = 'DEBIT') as pengeluaran_terbesar
        FROM transactions t
        WHERE t.user_id = :userId
          AND EXTRACT(MONTH FROM t.tanggal) = EXTRACT(MONTH FROM CURRENT_DATE)
          AND EXTRACT(YEAR FROM t.tanggal) = EXTRACT(YEAR FROM CURRENT_DATE)
        """, nativeQuery = true)
    Map<String, Object> getDashboard(@Param("userId") Long userId);
}
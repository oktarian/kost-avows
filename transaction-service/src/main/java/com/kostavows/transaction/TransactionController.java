package com.technicalassessment.kostavows.transaction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.technicalassessment.kostavows.auth.service.JwtService;
import com.technicalassessment.kostavows.transaction.entity.Transaction;
import com.technicalassessment.kostavows.transaction.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private JwtService jwtService;  // Asumsi shared atau duplikat di sini

    @Value("${auth.service.url:http://localhost:8081}")
    private String authServiceUrl;

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> addTransaction(@RequestHeader("Authorization") String token, @RequestBody Transaction tx) {
        String username = jwtService.extractUsername(token.replace("Bearer ", ""));
        // Ambil userId dari auth service atau cache
        Long userId = 1L;  // Simulasi, ganti dengan call ke auth-service
        tx.setUserId(userId);
        tx.setTanggal(LocalDate.now());
        Transaction saved = transactionRepository.save(tx);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard(@RequestHeader("Authorization") String token) {
        Long userId = 1L;  // Simulasi userId

        // Data utama dari repository (misal total debit/kredit)
        Map<String, Object> dbResult = transactionRepository.getDashboard(userId);

        // Range tanggal bulan ini (BENAR)
        LocalDate sekarang = LocalDate.now();
        LocalDate awalBulan = YearMonth.now().atDay(1);  // Sudah LocalDate

        // Ambil transaksi bulan ini
        List<Transaction> bulanIni = transactionRepository.findByUserIdAndTanggalBetween(
                userId,
                awalBulan,
                sekarang
        );

        // Hitung pengeluaran per kategori (hanya DEBIT)
        Map<String, Long> pengeluaranPerKategori = bulanIni.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .collect(Collectors.groupingBy(
                        Transaction::getKategori,
                        Collectors.summingLong(t -> t.getAmount().longValue())
                ));

        // Gabungkan hasil akhir untuk response
        Map<String, Object> response = new HashMap<>(dbResult);
        response.put("pengeluaranPerKategori", pengeluaranPerKategori);
        response.put("totalTransaksiBulanIni", bulanIni.size());
        response.put("periode", awalBulan + " s.d " + sekarang);

        return ResponseEntity.ok(response);
    }

   @GetMapping("/sisa-hidup")
    public ResponseEntity<Map<String, Object>> sisaHidup(@RequestHeader("Authorization") String token) {

        Long userId = 1L; // Simulasi user yang login

        LocalDate hariIni = LocalDate.now();
        LocalDate awalBulan = YearMonth.now().atDay(1);
        LocalDate akhirBulan = hariIni.withDayOfMonth(hariIni.lengthOfMonth());

        // Ambil transaksi bulan ini
        List<Transaction> bulanIni = transactionRepository.findByUserIdAndTanggalBetween(
                userId,
                awalBulan,
                hariIni
        );

        // Hitung total debit (pengeluaran)
        long totalDebit = bulanIni.stream()
                .filter(t -> "DEBIT".equalsIgnoreCase(t.getType()))
                .mapToLong(t -> t.getAmount() != null ? t.getAmount().longValue() : 0)
                .sum();

        // Hitung total credit (pemasukan)
        long totalCredit = bulanIni.stream()
                .filter(t -> "CREDIT".equalsIgnoreCase(t.getType()))
                .mapToLong(t -> t.getAmount() != null ? t.getAmount().longValue() : 0)
                .sum();

        // Hitung saldo bulan ini
        long sisaUang = totalCredit - totalDebit;

        // Hitung hari tersisa (termasuk hari ini → +1)
        int hariTersisa = (int) java.time.temporal.ChronoUnit.DAYS.between(hariIni, akhirBulan) + 1;

        long uangPerHari = hariTersisa > 0 ? sisaUang / hariTersisa : 0;

        // Saran dinamis
        String saran;
        if (sisaUang <= 0) {
            saran = "Sisa uang sudah habis, hati-hati ya!";
        } else if (uangPerHari < 50000) {
            saran = "Hemat dulu bro. Kurangi jajan boba!";
        } else if (uangPerHari < 100000) {
            saran = "Masih aman, tapi tetap jaga pengeluaran.";
        } else {
            saran = "Santuy aja! Keuangan kamu stabil bulan ini.";
        }

        // Response lengkap
        Map<String, Object> result = Map.of(
                "periode", awalBulan + " s.d " + hariIni,
                "totalDebit", totalDebit,
                "totalCredit", totalCredit,
                "sisaUang", sisaUang,
                "hariTersisa", hariTersisa,
                "uangPerHari", uangPerHari,
                "saran", saran
        );

        return ResponseEntity.ok(result);
    }

}
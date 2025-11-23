Nama Project: Kost-Avows
Arsitektur: Microservices (2 service utama 1 Repository)

Services                                        Tujuan Utama                                                                                                  Status Deploy
auth-service                            Authentication & Authorization (Register, Login, JWT)           Live - Swagger Success

transaction-service           Input transaction, lihat sisa uang, ringkasan keuangan                    Live - Swagger Success


Tech Stack :

Spring Boot 3.5.7 (Java 17)
Spring Data JPA + Hibernate
Spring Security 6 (stateless JWT)
SpringDoc OpenAPI (Swagger UI)
PostgreSQL (Native SQL Query on repository transaction-service)
Docker multi-stage build
Deploy on Railway
Environment variables & secret management
Networking container (IPv4 fix, port binding, healthcheck)

Swagger:
auth-service --> https://auth-service-production-f2e2.up.railway.app/swagger-ui/index.html#

transaction-service --> https://transaction-service-production-a93f.up.railway.app/swagger-ui/index.html#/

Spring IoC --> Semua @Service, @Repository, @Autowired, @Configuration ada di auth & transaction service
Java Stream --> Di transaction-service ada .stream().map().collect() di Repository atau Service
Intermediate Native SQL Query --> @Query(nativeQuery = true) atau JPQL kompleks di TransactionRepository
Containerization & Microservices -->2 service terpisah (auth + transaction) Deploy di Railway (Docker container otomatis) Database shared PostgreSQL







# StayView Backend

Spring Boot 기반 StayView 백엔드입니다. API 명세서와 DB 명세서를 기준으로 공간, 찜, 중개사 인증, 채팅, 사용자 프로필 API를 구현했습니다.

## 실행

Supabase 접속 정보는 `.env`에 두고 Git에는 올리지 않습니다. 새 환경에서는 `.env.example`을 복사해 실제 값을 채웁니다.

```bash
./gradlew test
./gradlew bootRun
```

## 데모 계정

`src/main/resources/db/seed.sql` 기준 샘플 계정입니다.

| User ID | 역할 | 용도 |
| --- | --- | --- |
| 1 | ADMIN | 중개사 승인/거절 |
| 2 | USER + 승인된 중개사 | 공간 등록/수정 |
| 3 | USER | 공간 조회, 찜, 채팅 |

보호 API는 프론트 인증 연동 전 발표용으로 `X-User-Id` 헤더를 사용합니다.

```bash
curl -H "X-User-Id: 3" http://localhost:8080/api/users/me
curl -H "X-User-Id: 3" http://localhost:8080/api/spaces
curl -H "X-User-Id: 1" http://localhost:8080/api/admin/agents
```

## Supabase DB 반영

```bash
psql "$SUPABASE_DATABASE_URL" -f src/main/resources/db/schema.sql
psql "$SUPABASE_DATABASE_URL" -f src/main/resources/db/seed.sql
```

현재 프로젝트의 DB 설계와 PPT 반영 내용은 [docs/ppt-implementation-report.md](docs/ppt-implementation-report.md)에 정리했습니다.

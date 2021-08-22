###지원자 정보
카카오페이 서버 개발자 지원자 류윤광 (ghmryg@gmail.com)

###문제해결 전략
- 대량의 동시 트래픽을 고려하여 빠른 응답을 위해 redis 사용하였습니다.
- 동시에 데이터 영속성을 위해 rdb로 mysql에 적재하도록 하였습니다.
- 통계성 데이터(현재 모집금액, 현재 투자자수)는 rdb로 적재시 데이터 추출을 위해 group by를 써야 하는데 
  데이터가 많아 진다면 속도가 빠르지도 않고 불필요한 요청이라 판단하여 마찬가지로 redis에서 제공하는 operation을 활용하여 
  데이터를 increase 하도록 하였습니다.
- 마찬가지로 SOLDOUT 처리 체크를 위해 동시에 들어오는 데이터를 순차처리가 필요했고 LOCK을 잡는것 보다 Single Thread 기반 Redis를 활용 하였습니다.

###참고사항
- 투자상품에 대한 데이터는 프로젝트 기동시 자동 INSERT 되게 하였습니다. (data.sql)
- profile local 환경에서는 embedded redis + H2로 구성
- profile dev 환경에서는 redis + myqsl 으로 docker-compose 구성하였습니다.
- 위와 같이 분리한 이유는, 개발시에는 간편하게 DB 접속 후 데이터를 확인하기 용도였고 대량의 트래픽을 고려 하기 위해서는 H2를 사용하는것은 적합하지 않았다고 판단하였습니다.

###프로젝트 동작 방법
```
docker-compose up
```

###API 확인
```
http://localhost:5000/swagger-ui.html
```

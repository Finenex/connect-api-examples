# Token Api Samples #

Token API 및 Points API를 사용하는 예제 제공.

## Source Structure ##

#### Token API Examples ####

- Address: 계좌 생성 예제
- Balance: 계좌 내 토큰 잔액 구하기 예제
- Transfer: 계좌간 토큰 이체 예제

#### Points API Examples ####

- Authentication: 인증 예제
- Points: 포인트 예제

#### Utilities ####

- JsonRpc: JSON RPC API 구현
- Rest: REST API 구현
- Crypto: SHA-256 해시 구현

## Run ##

gradle을 사용해서 java 프로그램으로 예제를 바로 실행할 수 있다.
실행할 수 있는 gradle task를 확인하려면,

```
$ gradlew tasks --group application
```

특정 예제의 gradle task를 실행하려면,

```
# 계좌 생성 예제
$ gradlew :createAddress 
# 토큰 잔액 예제
$ gradlew :getBalance
# 토큰 이체 예제
$ gradlew :transfer
# 포인트 예제
$ gradlew :points
```
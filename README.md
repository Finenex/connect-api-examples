# Token Api Samples #

Token API를 사용하는 방법을 제공하는 예제 모음.

## 소스 구조 ##

- Address: 계좌 생성 예제
- Balance: 계좌 내 토큰 잔액 구하기 예제
- Transfer: 계좌간 토큰 이체 예제
- utils
    - ApiException: API Exception
    - Crypto: SHA-256 해시 구현
    - JsonRpc: JSON RPC API 구현
    - Rest: REST API 구현

## 실행 ##

gradle을 사용해서 java 프로그램으로 바로 실행할 수 있다.
실행할 수 있는 gradle task를 확인하려면,

```
$ gradlew tasks --group application
```

특정 gradle task를 실행하려면,

```
# 계좌 생성 예제
$ gradlew :createAddress 
# 토큰 잔액 예제
$ gradlew :getBalance
# 토큰 이체 예제
$ gradlew :transfer
```
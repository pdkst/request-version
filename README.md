# request-version

基于请求的版本号来分发接口到方法上， 如果只有一个接口，则会忽略此项设置
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.pdkst/request-version/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.pdkst/request-version)

# 使用场景

- 旧接口无法变动，新接口需要做破坏性更新
- 不同版本返回不一样数据
- app兼容旧版本

# 快速开始

### maven引入

```xml

<dependency>
    <groupId>io.github.pdkst</groupId>
    <artifactId>request-version</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 1.没有自定义`RequestMappingHandlerMapping`的情况下

1. 引入jar
2. 复制一样的接口
3. 标注版本号 `@SinceVersion`
4. 请求增加版本号字段（默认`x-version`)

### 2.已使用自定义`RequestMappingHandlerMapping`的场景

1. 引入jar
2. 在自定义的`RequestMappingHandlerMapping`中重写相关接口：
    - getCustomMethodCondition
    - getCustomTypeCondition
3. 其他步骤同1

# 如何自定义使用

假设接口是： `GET http://localhost:8080/version`

### 1. 从请求头中获取版本：

```java
 @Bean
public VersionRequestConditionProvider versionRequestConditionProvider(){
        return new HeaderVersionRequestConditionProvider("x-version");
        }
```

测试：

```http request
GET http://localhost:8080/version
x-version: 1.0
```

### 2.从UserAgent中获取版本

```java
@Bean
public VersionRequestConditionProvider versionRequestConditionProvider(){
        return new UserAgentVersionRequestConditionProvider("version");
        }
```

测试：

```http request
GET http://localhost:8080/version
user-agent: version/1.0
```

### 3.从FormValue中获取版本

```java
@Bean
public VersionRequestConditionProvider versionRequestConditionProvider(){
        return new FormVersionRequestConditionProvider("v");
        }
```

测试：

```http request
GET http://localhost:8080/version?v=1.0
```

# 版本原理

1. 利用spring自定义`RequestCondition`的预留接口，实现同样的请求按照接口不同分发给不同方法，此方法比spring匹配优先级更低
    - PatternsRequestCondition 路径
    - RequestMethodsRequestCondition 方法
    - ParamsRequestCondition 参数
    - HeadersRequestCondition 请求头
    - ConsumesRequestCondition Consumes
    - ProducesRequestCondition Produces
    - customCondition **自定义**←
2. 版本管理逻辑同自然版本逻辑，`2.0.0`>`1.9.9`
3. 多个版本都会返回，但是按照匹配的版本，分发给版本最高的那个
4. 如果请求不存在版本，则默认分发给版本最大的那个（即最新的）



1. 生成 JKS 密钥库（KeyStore）
   使用 keytool 生成自签名证书（JKS 格式）：
   bash
   keytool -genkeypair \
   -alias mydomain \
   -keyalg RSA \
   -keysize 2048 \
   -keystore keystore.jks \
   -storepass your_keystore_password \
   -keypass your_key_password \
   -validity 365 \
   -dname "CN=localhost, OU=Dev, O=MyCompany, L=City, ST=State, C=US"
   生成的文件：keystore.jks

参数说明：

-keystore keystore.jks：指定密钥库文件名为 keystore.jks（默认类型为 JKS）。

-storepass 和 -keypass：分别设置密钥库密码和私钥密码（生产环境建议使用不同密码）。

windows下可运行的demo：

.\keytool.exe -genkeypair -alias localhost -storetype JKS -keyalg RSA -keysize 2048 -keystore keystore.jks -storepass your_keystore_password -keypass your_key_password -validity 365 -dname "CN=localhost, OU=Dev, O=MyCompany, L=City, ST=State, C=US"


2. 生成信任库（TrustStore）
   信任库用于存储受信任的证书（如 CA 证书或对方服务的证书）。如果应用需要验证客户端证书或信任其他服务，需配置 TrustStore。

示例：将密钥库中的证书导出并导入信任库
bash
# 1. 从密钥库导出证书
keytool -exportcert \
-alias mydomain \
-keystore keystore.jks \
-storepass your_keystore_password \
-file mydomain.cer

# 2. 创建信任库并导入证书
keytool -importcert \
-alias mydomain_trust \
-keystore truststore.jks \
-storepass your_truststore_password \
-file mydomain.cer \
-noprompt
生成的文件：truststore.jks

-noprompt：跳过确认提示。




3. 生产环境中应该用 PKCS12 还是 JKS？
   推荐 PKCS12：更安全、兼容性更好（符合行业标准）。

JKS 已过时：Oracle 从 Java 9 开始推荐使用 PKCS12 替代 JKS。
如果使用jks，则需要显式地指定 -storetype JKS




3. 将密钥库和信任库放入项目
   将 keystore.jks 和 truststore.jks 复制到项目的 src/main/resources 目录。

4. 配置 Spring Boot 的 SSL
   在 application.properties 中配置密钥库和信任库：

properties
# 启用 HTTPS
server.ssl.enabled=true

# 密钥库配置（JKS）
server.ssl.key-store-type=JKS
server.ssl.key-store=classpath:keystore.jks
server.ssl.key-store-password=your_keystore_password
server.ssl.key-alias=mydomain

# 信任库配置
server.ssl.trust-store-type=JKS
server.ssl.trust-store=classpath:truststore.jks
server.ssl.trust-store-password=your_truststore_password

# 可选：双向 SSL 认证（需客户端提供证书）
server.ssl.client-auth=need
参数说明：
server.ssl.client-auth：

none：不验证客户端证书（默认）。

want：验证客户端证书（可选）。

need：必须提供有效的客户端证书。

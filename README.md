## 三、实现用户登录以及分布式session功能

### 1、 明文密码两次md5入库

创建数据库

```sql
CREATE TABLE miaosha_user (
	id bigint(20) NOT NULL COMMENT '用户ID,手机号码',
	nickname varchar(255) NOT NULL,
	password varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt) + salt)',
	salt varchar(10) DEFAULT NULL,
	head varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
	register_date datetime DEFAULT NULL COMMENT '注册时间',
	last_login_date datetime DEFAULT NULL COMMENT '上次登录时间',
	login_count int(11) DEFAULT '0' COMMENT '登录次数',
	PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET = utf8mb4
```

两次MD5

1. 用户端：PASS = MD5(明文+固定salt)
2. 服务端：PASS = MD5（用户输入+随机salt）
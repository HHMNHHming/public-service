CREATE TABLE `resource` (
`id` bigint(32) NOT NULL AUTO_INCREMENT,
`sort` int(3) DEFAULT NULL,
`parent_id` bigint(32) DEFAULT NULL,
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
`name` varchar(64) CHARACTER SET utf8 NOT NULL,
`title` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
`url` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
`size` varchar(16) CHARACTER SET utf8 DEFAULT NULL,
`icon` varchar(40) CHARACTER SET utf8 DEFAULT NULL,
`type` int(1) DEFAULT NULL COMMENT '1.menu;2.resource',
`circle` int(1) DEFAULT NULL,
`operator` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
`hidden` tinyint(1) unsigned zerofill DEFAULT NULL,
`stype` varchar(10) DEFAULT NULL,
`path` varchar(128) DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `resourse_id_uindex` (`id`) USING BTREE,
KEY `parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

CREATE TABLE `role` (
`id` bigint(32) NOT NULL AUTO_INCREMENT,
`name` varchar(64) CHARACTER SET utf8 NOT NULL,
`description` varchar(256) CHARACTER SET utf8 DEFAULT NULL,
`status` int(1) NOT NULL DEFAULT '0',
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
`operator` varchar(64) CHARACTER SET utf8 DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `role_id_uindex` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

CREATE TABLE `role_resource` (
`role_id` bigint(32) NOT NULL,
`resource_id` bigint(32) NOT NULL,
`id` bigint(32) NOT NULL AUTO_INCREMENT,
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11669 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

CREATE TABLE `role_template` (
`id` bigint(32) NOT NULL AUTO_INCREMENT,
`role_id` bigint(32) NOT NULL,
`template_code` bigint(32) NOT NULL,
`operator` varchar(255) DEFAULT NULL,
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=509 DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
`id` bigint(32) NOT NULL AUTO_INCREMENT,
`user_name` varchar(64) COLLATE utf8_bin NOT NULL,
`pwd` varchar(256) COLLATE utf8_bin NOT NULL,
`role_id` bigint(32) NOT NULL,
`enabled` int(1) NOT NULL DEFAULT '0',
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
`operator` varchar(64) CHARACTER SET latin1 DEFAULT NULL,
`password_error_count` int(11) DEFAULT '0',
`password_update_time` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `user_user_name_uindex` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

CREATE TABLE `user_ext` (
`id` bigint(32) NOT NULL AUTO_INCREMENT,
`user_id` bigint(32) NOT NULL,
`app_id` bigint(32) NOT NULL,
`sk` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
`operator` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
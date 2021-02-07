
/*==============================================================*/
/* Table: classify_value                                        */
/*==============================================================*/
drop table if exists classify_value;
create table classify_value
(
    id                   bigint not null auto_increment comment 'ID',
    classify             tinyint comment '日志类型',
    code                 varchar(50) comment '编码',
    value                varchar(100) comment '值',
    creator_id           bigint comment '创建人',
    create_time          datetime default CURRENT_TIMESTAMP comment '创建时间',
    modifier_id          bigint comment '修改人',
    modify_time          datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    primary key (id)
);
alter table classify_value comment '日志类型的分类值';

INSERT INTO dili_logger.classify_value(classify,code,value,create_time,modify_time) SELECT 1,ddv.`code`,ddv.`name`,NOW(),NOW() FROM uap.data_dictionary_value ddv where ddv.dd_code='operation_type';
INSERT INTO dili_logger.classify_value(classify,code,value,create_time,modify_time) SELECT 2,ddv.`code`,ddv.`name`,NOW(),NOW() FROM uap.data_dictionary_value ddv where ddv.dd_code='exception_type';

INSERT INTO `uap`.`menu` (`code`, `system_id`, `parent_id`, `order_number`, `url`, `name`, `description`, `type`, `shortcut`) VALUES ('7c103262-0ed8-401f-9ae6-a7e959b9e20a', 2, 10, 12, 'http://logger.diligrp.com:8283/classifyValue/index.html', '日志类型值', '日志类型操作值配置', 1, 0);

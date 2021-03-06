DROP TABLE IF EXISTS `classify`;
CREATE TABLE `classify`
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    code          VARCHAR(32)  NOT NULL COMMENT '编码',
    name          VARCHAR(32)  NOT NULL COMMENT '名称',
    remark        VARCHAR(255) NOT NULL DEFAULT '' COMMENT '备注',
    type_id       BIGINT       NOT NULL COMMENT '类型',
    type_code     varchar(32)  NOT NULL COMMENT '类型编码',
    parent_id     BIGINT       NOT NULL COMMENT '父节点',
    level         TINYINT      NOT NULL COMMENT '层级',
    leaf          BIT          NOT NULL COMMENT '叶子节点',
    serial_number SMALLINT     NOT NULL COMMENT '序号',
    creator_id    BIGINT       NOT NULL COMMENT '创建者主键',
    created_time  DATETIME     NOT NULL COMMENT '创建时间',
    modifier_id   BIGINT       NOT NULL COMMENT '修改者主键',
    modified_time DATETIME     NOT NULL COMMENT '最近修改时间'
);

COMMENT ON TABLE `classify` IS '分类';
COMMENT ON COLUMN `classify`.id IS '主键';

insert into classify (id, type_id, type_code, code, name, remark, parent_id, level, leaf, serial_number, creator_id,
                      created_time,
                      modifier_id,
                      modified_time)
values (1, 1, 'test', 'root', '根节点', '', 1, 1, 1, 1, 1, current_timestamp, 1, current_timestamp);

CREATE TABLE `sequence` (
                            `sequence_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '序列号类型 = 区分业务类型',
                            `crt_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `start_value` bigint NOT NULL DEFAULT '1' COMMENT '起始值',
                            `curr_value` bigint NOT NULL DEFAULT '1' COMMENT '序列号当前值',
                            `upt_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`sequence_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
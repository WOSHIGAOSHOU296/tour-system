-- ============================================================
-- 个性化旅游定制平台 - 数据库初始化脚本
-- 数据库: tour_db
-- 执行方式: 在 Navicat 中新建查询，复制全部内容执行
-- ============================================================

CREATE DATABASE IF NOT EXISTS `tour_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `tour_db`;

-- ============================================================
-- 1. 用户信息表
-- ============================================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `user_id`    BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键',
    `username`   VARCHAR(30)  NOT NULL DEFAULT '' COMMENT '登录账号，唯一',
    `password`   VARCHAR(255) NOT NULL DEFAULT '' COMMENT '登录密码，加密存储',
    `nickname`   VARCHAR(50)  DEFAULT '' COMMENT '用户展示昵称',
    `email`      VARCHAR(30)  DEFAULT '' COMMENT '联系邮箱',
    `phone`      VARCHAR(20)  DEFAULT '' COMMENT '联系电话',
    `avatar_url` VARCHAR(100) DEFAULT '' COMMENT '头像图片路径',
    `gender`     TINYINT(1)   DEFAULT 0 COMMENT '性别: 0-未知, 1-男, 2-女',
    `status`     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '账号状态: 0-禁用, 1-正常',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号注册时间',
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ============================================================
-- 2. 角色信息表
-- ============================================================
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
    `role_id`     BIGINT(11)  NOT NULL AUTO_INCREMENT COMMENT '角色ID，主键',
    `role_name`   VARCHAR(20) NOT NULL DEFAULT '' COMMENT '角色名称',
    `description` VARCHAR(255) DEFAULT '' COMMENT '角色权限说明',
    PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ============================================================
-- 3. 用户-角色关联表
-- ============================================================
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
    `id`      BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '关联ID，主键',
    `user_id` BIGINT(11) NOT NULL DEFAULT 0 COMMENT '用户ID，外键关联users表',
    `role_id` BIGINT(11) NOT NULL DEFAULT 0 COMMENT '角色ID，外键关联roles表',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================================
-- 4. 所去城市表
-- ============================================================
DROP TABLE IF EXISTS `cities`;
CREATE TABLE `cities` (
    `city_id`    BIGINT(11)  NOT NULL AUTO_INCREMENT COMMENT '城市ID，主键',
    `city_name`  VARCHAR(30) NOT NULL DEFAULT '' COMMENT '城市名称',
    `province`   VARCHAR(30) NOT NULL DEFAULT '' COMMENT '所属省份',
    `city_type`  VARCHAR(20) DEFAULT '' COMMENT '热门/季节推荐城市',
    `created_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `updated_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='所去城市表';

-- ============================================================
-- 5. 景点表 (attractions)
-- ============================================================
DROP TABLE IF EXISTS `attractions`;
CREATE TABLE `attractions` (
    `scenic_id`     BIGINT(11)    NOT NULL AUTO_INCREMENT COMMENT '景点ID，主键',
    `city_id`       BIGINT(11)    NOT NULL DEFAULT 0 COMMENT '城市ID，外键关联cities表',
    `scenic_name`   VARCHAR(50)   NOT NULL DEFAULT '' COMMENT '景点全称',
    `scenic_type`   VARCHAR(20)   NOT NULL DEFAULT '' COMMENT '景点类型: 自然风光/人文古迹',
    `address`       VARCHAR(100)  DEFAULT '' COMMENT '详细地址',
    `ticket_price`  DECIMAL(8,2)  DEFAULT 0.00 COMMENT '参考门票价格',
    `open_time`     VARCHAR(50)   DEFAULT '' COMMENT '营业时间说明',
    `introduce`     VARCHAR(500)  DEFAULT '' COMMENT '景点文字介绍',
    `img_url`       VARCHAR(100)  DEFAULT '' COMMENT '景点配图路径',
    `average_score` DECIMAL(3,1)  NOT NULL DEFAULT 0.0 COMMENT '用户综合评分',
    `view_count`    INT(11)       NOT NULL DEFAULT 0 COMMENT '访问次数（热度排行）',
    `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    PRIMARY KEY (`scenic_id`),
    KEY `idx_city_id` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- ============================================================
-- 6. 周围美食表
-- ============================================================
DROP TABLE IF EXISTS `foods`;
CREATE TABLE `foods` (
    `food_id`    BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '美食ID，主键',
    `scenic_id`  BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '景点ID，外键关联attractions表',
    `food_name`  VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '美食名称',
    `address`    VARCHAR(100) DEFAULT '' COMMENT '店铺地址',
    `introduce`  VARCHAR(255) DEFAULT '' COMMENT '特色介绍',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`food_id`),
    KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周围美食表';

-- ============================================================
-- 7. 周围酒店表
-- ============================================================
DROP TABLE IF EXISTS `hotels`;
CREATE TABLE `hotels` (
    `hotel_id`    BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '酒店ID，主键',
    `scenic_id`   BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '景点ID，外键关联attractions表',
    `hotel_name`  VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '酒店名称',
    `address`     VARCHAR(100) DEFAULT '' COMMENT '酒店地址',
    `price_range` VARCHAR(30)  DEFAULT '' COMMENT '房价范围',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    PRIMARY KEY (`hotel_id`),
    KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='周围酒店表';

-- ============================================================
-- 8. 旅友圈攻略表
-- ============================================================
DROP TABLE IF EXISTS `strategies`;
CREATE TABLE `strategies` (
    `strategy_id` BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '攻略ID，主键',
    `scenic_id`   BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '景点ID，外键关联attractions表',
    `user_id`     BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '发布用户ID，外键关联users表',
    `title`       VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '攻略标题',
    `content`     VARCHAR(500) NOT NULL DEFAULT '' COMMENT '正文内容',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (`strategy_id`),
    KEY `idx_scenic_id` (`scenic_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='旅友圈攻略表';

-- ============================================================
-- 9. 景点留言表
-- ============================================================
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages` (
    `message_id` BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '留言ID，主键',
    `scenic_id`  BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '景点ID，外键关联attractions表',
    `user_id`    BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '留言用户ID，外键关联users表',
    `content`    VARCHAR(255) NOT NULL DEFAULT '' COMMENT '留言文字',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '留言时间',
    PRIMARY KEY (`message_id`),
    KEY `idx_scenic_id` (`scenic_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点留言表';

-- ============================================================
-- 10. 论坛帖子表
-- ============================================================
DROP TABLE IF EXISTS `forum_posts`;
CREATE TABLE `forum_posts` (
    `post_id`    BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '帖子ID，主键',
    `user_id`    BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '发布用户ID，外键关联users表',
    `category`   VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '帖子分类: 攻略/结伴/问答',
    `title`      VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '帖子标题',
    `content`    VARCHAR(500) NOT NULL DEFAULT '' COMMENT '帖子正文',
    `status`     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '审核状态: 0-违规删除, 1-正常',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='论坛帖子表';

-- ============================================================
-- 11. 帖子评论表
-- ============================================================
DROP TABLE IF EXISTS `post_comments`;
CREATE TABLE `post_comments` (
    `comment_id` BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '评论ID，主键',
    `post_id`    BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '帖子ID，外键关联forum_posts表',
    `user_id`    BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '评论用户ID，外键关联users表',
    `content`    VARCHAR(255) NOT NULL DEFAULT '' COMMENT '评论文字',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    PRIMARY KEY (`comment_id`),
    KEY `idx_post_id` (`post_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

-- ============================================================
-- 12. 行程表
-- ============================================================
DROP TABLE IF EXISTS `trips`;
CREATE TABLE `trips` (
    `trip_id`      BIGINT(11)    NOT NULL AUTO_INCREMENT COMMENT '行程ID，主键',
    `user_id`      BIGINT(11)    NOT NULL DEFAULT 0 COMMENT '所属用户ID，外键关联users表',
    `trip_name`    VARCHAR(50)   NOT NULL DEFAULT '' COMMENT '自定义行程名称',
    `start_date`   DATE          NOT NULL COMMENT '行程开始日期',
    `end_date`     DATE          NOT NULL COMMENT '行程结束日期',
    `trip_content` VARCHAR(1000) DEFAULT '' COMMENT '手动填写景点、住宿、美食安排',
    `created_at`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '行程创建时间',
    PRIMARY KEY (`trip_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行程表';

-- ============================================================
-- 13. 收藏表
-- ============================================================
DROP TABLE IF EXISTS `collections`;
CREATE TABLE `collections` (
    `collect_id`   BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键',
    `user_id`      BIGINT(11) NOT NULL DEFAULT 0 COMMENT '用户ID，外键关联users表',
    `collect_type` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '收藏类型: 1-景点, 2-攻略, 3-帖子, 4-行程',
    `target_id`    BIGINT(11) NOT NULL DEFAULT 0 COMMENT '对应资源ID',
    `created_at`   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`collect_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ============================================================
-- 14. 浏览记录表
-- ============================================================
DROP TABLE IF EXISTS `browse_records`;
CREATE TABLE `browse_records` (
    `record_id`   BIGINT(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID，主键',
    `user_id`     BIGINT(11) NOT NULL DEFAULT 0 COMMENT '用户ID，外键关联users表',
    `browse_type` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '浏览类型: 1-景点, 2-攻略, 3-帖子',
    `target_id`   BIGINT(11) NOT NULL DEFAULT 0 COMMENT '对应资源ID',
    `browse_time` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`record_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览记录表';

-- ============================================================
-- ============================================================
-- 15. 景区公告表
-- ============================================================
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE `announcements` (
    `ann_id`     BIGINT(11)   NOT NULL AUTO_INCREMENT COMMENT '公告ID，主键',
    `scenic_id`  BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '景区ID，外键关联attractions表',
    `user_id`    BIGINT(11)   NOT NULL DEFAULT 0 COMMENT '发布者ID，外键关联users表',
    `title`      VARCHAR(50)  NOT NULL DEFAULT '' COMMENT '公告标题',
    `content`    VARCHAR(500) NOT NULL DEFAULT '' COMMENT '公告内容',
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    PRIMARY KEY (`ann_id`),
    KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景区公告表';

-- 种子数据
-- ============================================================

-- 角色数据
INSERT INTO `roles` (`role_name`, `description`) VALUES
('游客', '未登录或刚注册用户，只能浏览首页和景点'),
('普通用户', '注册后默认角色，可使用论坛、攻略、行程等功能'),
('景区管理员', '管理单个景点板块，发布公告和管理留言'),
('平台管理员', '全局管理员，管控整个系统资源和用户数据');

-- 测试用户 (密码: 123456，MD5加盐)
-- 盐值示例: "a1b2c3"，加密后密码 = MD5("123456" + "a1b2c3")
-- 实际项目中使用 PasswordUtil 动态生成
INSERT INTO `users` (`username`, `password`, `nickname`, `email`, `phone`, `gender`, `status`) VALUES
('admin',  'e10adc3949ba59abbe56e057f20f883e', '平台管理员', 'admin@tour.com',  '13800000001', 1, 1),
('user1',  'e10adc3949ba59abbe56e057f20f883e', '旅行达人张三', 'zhang@tour.com', '13800000002', 1, 1),
('scenic1','e10adc3949ba59abbe56e057f20f883e', '景区管理员李四', 'li@tour.com',   '13800000003', 2, 1);

-- 用户角色关联 (admin=平台管理员, user1=普通用户, scenic1=景区管理员)
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
(1, 4),
(2, 2),
(3, 3);

-- 城市数据
INSERT INTO `cities` (`city_name`, `province`, `city_type`) VALUES
('北京',   '北京市', '热门'),
('杭州',   '浙江省', '热门'),
('成都',   '四川省', '热门'),
('西安',   '陕西省', '季节推荐'),
('厦门',   '福建省', '季节推荐'),
('三亚',   '海南省', '热门'),
('丽江',   '云南省', '热门'),
('桂林',   '广西壮族自治区', '季节推荐');

-- 景点数据
INSERT INTO `attractions` (`city_id`, `scenic_name`, `scenic_type`, `address`, `ticket_price`, `open_time`, `introduce`, `img_url`, `average_score`, `view_count`) VALUES
(1, '故宫博物院',  '人文古迹', '北京市东城区景山前街4号',        60.00,  '08:30-17:00(旺季)/08:30-16:30(淡季)', '故宫又称紫禁城，是明清两代的皇家宫殿，中国古代宫廷建筑之精华，世界五大宫之首。', 'static/images/gugong.jpg', 4.8, 15230),
(1, '八达岭长城',  '人文古迹', '北京市延庆区G6京藏高速58号出口', 40.00,  '07:30-18:00',                  '八达岭长城是明长城中保存最完好、最具代表性的一段，史称天下九塞之一。',        'static/images/changcheng.jpg', 4.7, 12890),
(2, '西湖风景区',  '自然风光', '浙江省杭州市西湖区龙井路1号',     0.00,   '全天开放',                     '西湖是中国大陆首批国家重点风景名胜区，中国十大风景名胜之一，以秀丽的湖光山色闻名。', 'static/images/xihu.jpg', 4.9, 20150),
(2, '灵隐寺',      '人文古迹', '浙江省杭州市西湖区法云弄1号',     75.00,  '07:00-18:00',                  '灵隐寺始建于东晋咸和元年，中国佛教禅宗十大古刹之一，江南著名古刹。',            'static/images/lingyin.jpg', 4.6, 9870),
(3, '宽窄巷子',    '人文古迹', '四川省成都市青羊区长顺街',        0.00,   '全天开放',                     '宽窄巷子是成都遗留下来的较成规模的清朝古街道，由宽巷子、窄巷子、井巷子平行排列组成。', 'static/images/kuanzhai.jpg', 4.5, 18600),
(3, '大熊猫繁育研究基地', '自然风光', '四川省成都市成华区熊猫大道1375号', 55.00, '07:30-18:00',           '世界著名的大熊猫迁地保护基地、科研繁育基地，集大熊猫科研繁育、保护教育和旅游为一体。', 'static/images/panda.jpg', 4.7, 16540),
(4, '兵马俑', '人文古迹', '陕西省西安市临潼区秦陵北路', 120.00, '08:30-17:00', '秦始皇兵马俑博物馆是秦始皇陵的陪葬坑，被誉为世界第八大奇迹。',                    'static/images/bingmayong.jpg', 4.8, 22100),
(4, '大雁塔', '人文古迹', '陕西省西安市雁塔区雁塔南路', 50.00,  '08:00-18:00', '大雁塔又名大慈恩寺塔，唐永徽三年玄奘为保存由天竺经丝绸之路带回长安的经卷佛像而建。', 'static/images/dayanta.jpg', 4.5, 11050),
(5, '鼓浪屿', '自然风光', '福建省厦门市思明区鼓浪屿', 35.00, '全天开放', '鼓浪屿素有海上花园之称，融历史、人文和自然景观于一体，为世界文化遗产。',                'static/images/gulangyu.jpg', 4.8, 19870),
(6, '亚龙湾', '自然风光', '海南省三亚市吉阳区亚龙湾路', 0.00, '全天开放', '亚龙湾被誉为天下第一湾，拥有7000米银白色海滩，沙质细腻，海水洁净透明。',               'static/images/yalongwan.jpg', 4.6, 14320);

-- 美食数据 (关联景点)
INSERT INTO `foods` (`scenic_id`, `food_name`, `address`, `introduce`) VALUES
(1, '北京烤鸭(全聚德)', '北京市东城区前门大街30号',    '全聚德烤鸭以色泽红艳、肉质细嫩、味道醇厚、肥而不腻著称，被誉为中华第一吃。'),
(1, '老北京炸酱面',     '北京市东城区南锣鼓巷',        '地道老北京风味，炸酱浓郁，面条筋道，配以黄瓜丝、豆芽等时蔬，回味无穷。'),
(2, '延庆火盆锅',       '北京市延庆区城区',            '延庆传统农家菜，以炭火铜锅炖煮豆腐、五花肉、粉条等食材，鲜香扑鼻。'),
(3, '西湖醋鱼',         '杭州市西湖区楼外楼',         '杭州传统名菜，选用鲜活草鱼，醋香浓郁，酸甜适口，鱼肉鲜嫩。'),
(5, '成都火锅(小龙坎)', '成都市锦江区东大街',         '地道成都老火锅，牛油锅底麻辣鲜香，配上各种食材，回味无穷。'),
(6, '钟水饺',           '成都市成华区建设路',         '成都传统小吃，皮薄馅嫩，配上红油和花椒面，麻辣鲜香。'),
(7, '肉夹馍',           '西安市临潼区秦陵路',         '西安传统名吃，腊汁肉夹在白吉馍中，饼酥肉香，肥而不腻。'),
(9, '沙茶面',           '厦门市思明区中山路',         '厦门特色小吃，沙茶酱浓郁鲜香，配以海鲜和时蔬，鲜美可口。');

-- 酒店数据 (关联景点)
INSERT INTO `hotels` (`scenic_id`, `hotel_name`, `address`, `price_range`) VALUES
(1, '北京王府井希尔顿酒店', '北京市东城区王府井大街', '800-2000元/晚'),
(1, '北京天安门快捷酒店',   '北京市东城区前门大街',   '200-500元/晚'),
(3, '杭州西湖国宾馆',       '杭州市西湖区杨公堤18号', '600-1500元/晚'),
(3, '杭州西湖青年旅舍',     '杭州市西湖区虎跑路',     '50-200元/晚'),
(5, '成都春熙路希尔顿酒店', '成都市锦江区春熙路',     '500-1200元/晚'),
(6, '成都熊猫基地附近民宿', '成都市成华区熊猫大道',   '150-400元/晚');

-- 公告种子数据
INSERT INTO `announcements` (`scenic_id`, `user_id`, `title`, `content`) VALUES
(1, 3, '故宫暑期开放时间调整', '7月1日至8月31日故宫博物院延长开放至18:00，最后入馆时间为17:00。请游客合理安排参观时间。'),
(3, 3, '西湖景区赏荷季公告', '6月中旬至8月为西湖荷花最佳观赏期，曲院风荷、断桥等区域荷花已陆续盛开，欢迎广大游客前来观赏。');

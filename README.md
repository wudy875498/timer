# timer
定时任务中心，技术redis+mongodb+rabbitmq

   
    本次分享的是如何配置一个定时任务中心，通过redis的过期订阅事件来触发任务，通过rabbitmq或这rest远程调用
    来调度任务，通过mongodb来记录任务的执行记录。只是一个简单的demo

开发环境：
    redis+mongodb+rabbitmq

redis需要配置 键空间通知 配置方式有两种：
1。CONFIG set notify-keyspace-events Ex（非持久化）

2。配置文件配置 notify-keyspace-events Ex

相关参数如下：
字符	发送通知
* K	键空间通知，所有通知以 keyspace@ 为前缀，针对Key
* E	键事件通知，所有通知以 keyevent@ 为前缀，针对event
* g	DEL 、 EXPIRE 、 RENAME 等类型无关的通用命令的通知
* $	字符串命令的通知
* l	列表命令的通知
* s	集合命令的通知
* h	哈希命令的通知
* z	有序集合命令的通知
* x	过期事件：每当有过期键被删除时发送
* e	驱逐(evict)事件：每当有键因为 maxmemory 政策而被删除时发送
* A	参数 g$lshzxe 的别名，相当于是All


关键说明：
com.wudy.timer.doc.TimerPayload

* taskName   String   任务名称，方便在mongodb中查看
* times      String   执行次数，取值有-1（无限次执行），0（立即执行），正整数n（表示执行n次）
* runTimes   Integer  已经执行次数，times-runTimes=剩下执行次数
* cron       String   定时器cron表达式，当times为0时，无意义，当times=n时，受n次数限制，当times=-1时，相当于正常的schedule任务
* seconds    long     两次执行时间间隔,单位：s，优先于cron表达式，当times=0时，无意义
* notifyType String   通知类型（任务调用方式）URL：rest远程调用，MQ：消息通知（本项目集成rabbitmq）
* exchange   String   交换机名称，结合notifyType=mq使用
* routingKey String   路由key，结合notifyType=mq使用
* jsonBody   boolean  是否post jsonBody调用，结合notifyType=URL使用
* params     Map      业务参数

MapleHelper ---- 冒险岛辅助小程序

本程序是针对网游“冒险岛online”开发的辅助攻略类安卓程序，目前有数值成长收益计算、托德上星路线计算、小游戏模拟三个部分。
安装包路径为 /app/release/release/app_main.apk


1. 数值成长收益计算

1）简单模拟

用户只需将在游戏面板界面的一些数据录入程序，程序即可简单的计算各种属性的提升带来的收益百分比

第一个界面为数据输入界面，用户将数据输入后，可以点击“计算”按钮进入结果界面

程序可以保存一份数据，以便下次使用时直接加载


进入结果界面后，用户可以设置每种属性的变化值来查看该变化带来的收益百分比

用户可以点击左侧的行名将该行数据纳入总收益计算中

无视属性的收益计算单独使用一个界面

在无视属性界面，用户按照提示输入即可查看无视变化带来的收益


2）完整模拟（尚未开发）

计划开发成能录入技能、装备、联盟等各种信息的界面



2. 托德上星攻略

用户只需要设置托德装备和被托德装备的等级和星之力，然后点击计算按钮，程序就会计算出结果

在结果表中，程序会告知用户装备在每个等级应当被强化到几级星之力，以及整个路径的星星耗费期望



3. 小游戏模拟

该板块是对冒险岛中在活动中出现的小游戏的模拟，以便用户练习

目前开发的两个小游戏是猜拳和猜密码

1）猜拳

和熊猫猜 9 次拳，熊猫在石头、剪刀、布每种最多出三次的前提下随机出拳


2）猜密码

系统随机生成一个从“123456789”中挑选的 3 位有序不重复数字作为密码，用户有 10 次机会猜测

每次猜测后，程序会告知用户的猜测中有几个数字存在且位置正确，有几个数字存在但位置不正确

系统也可以帮用户猜密码，用户只要按程序给出的建议猜测，并将结果反馈给程序即可

如果用户失误，没有按程序给出的建议进行猜测，可以选择自定义猜测，并将自己的猜测和得到的结果反馈给程序


未来可能再添加翻转棋、金字塔等小游戏
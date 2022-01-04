package org.springblade.common.tool;

/**
 * 所有的通知消息。
 *
 * @author by SUMMER
 * @date 2020/4/8.
 */
public enum ChatType {

    MSG_SYSTEM("10000","系统消息"),
    MSG_PING("0", "心跳检测"),
    MSG_READY("1", "链接就绪"),
    READED("0", "已读消息"),
    UNREAD("1", "未读消息"),
    MSG_FRIEND("2", "好友消息"),
    FRIEND_CHAT("100", "单聊"),
    FRIEND_APPLY("101", "申请添加好友通知"),
    FRIEND_ADD("102", "同意添加好友通知"),
    FRIEND_DEL("103", "删除好友通知"),
    FRIEND_REJECT("104", "拒绝添加好友通知"),
    MSG_GROUP("3", "群组消息"),
    GROUP_CHAT("200", "群聊"),
    GROUP_CREATE("201", "创建群聊通知"),
    GROUP_ADD("202", "添加群聊通知"),
    GROUP_DROPOUT("203", "退出群聊通知"),
    GROUP_DEL("204", "踢出群聊通知"),
    GROUP_UPDATE("205", "更改群聊信息通知"),
    GROUP_TRAN("206", "群主转让群聊信息通知"),
    GROUP_DISBAND("207", "解散群通知"),
    MSG_MAC("4", "机台消息"),
    MAC_LOGIN("300", "机台登录通知"),
    MAC_LOGOUT("301", "机台登出通知"),
    MAC_DOWN("302", "机台停机通知"),
    MAC_QUALITY("303", "机台质检通知"),
    MAC_ACCEPTORDER("304", "机台接单通知"),
    MAC_PROORDER("305", "机台开始生产通知"),
    MAC_FINISHORDER("306", "机台结束生产通知"),
    MAC_MAINTAIN("307", "机台保养时长通知"),
    MAC_CHANGE("308", "机台换膜时长通知"),
    MAC_REPORTORDER("309", "下班上报通知");


    String type;
    String desc;

    ChatType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 前端接收的字段和数据库字段转化进行模糊查询
     * @param desc
     * @return
     */
    public static String typeToDesc(String desc){

        if(ChatType.MAC_QUALITY.getDesc().contains(desc+"")){
            desc = ChatType.MAC_QUALITY.getType();
            return desc;
        }else if(ChatType.MAC_PROORDER.getDesc().contains(desc+"")){
            desc =ChatType.MAC_PROORDER.getType();
            return desc;
        }else if(ChatType.MAC_DOWN.getDesc().contains(desc+"")){
            desc =ChatType.MAC_DOWN.getType();
            return desc;
        }else if(ChatType.MAC_LOGIN.getDesc().contains(desc+"")){
            desc =ChatType.MAC_LOGIN.getType();
            return desc;
        }else if(ChatType.MAC_ACCEPTORDER.getDesc().contains(desc+"")){
            desc =ChatType.MAC_ACCEPTORDER.getType();
            return desc;
        }else if(ChatType.MAC_CHANGE.getDesc().contains(desc+"")){
            desc =(ChatType.MAC_CHANGE.getType());
            return desc;
        }else if(ChatType.MAC_FINISHORDER.getDesc().contains(desc+"")){
            desc =ChatType.MAC_FINISHORDER.getType();
            return desc;
        }else if(ChatType.MAC_LOGOUT.getDesc().contains(desc+"")){
            desc =ChatType.MAC_LOGOUT.getType();
            return desc;
        }else if(ChatType.MAC_REPORTORDER.getDesc().contains(desc+"")){
            desc =ChatType.MAC_REPORTORDER.getType();
            return desc;
        }else if(ChatType.MAC_MAINTAIN.getDesc().contains(desc+"")){
            desc =ChatType.MAC_MAINTAIN.getType();
            return desc;
        }
        else if(ChatType.MSG_SYSTEM.getDesc().equals(desc+"")){
            desc =ChatType.MSG_SYSTEM.getType();
            return desc;
        }
        return desc;

    }
}

package cn.paradox.common;


import com.thoughtworks.xstream.annotations.XStreamAlias;

public class WXTextMessageEntity {
    @XStreamAlias("ToUserName")
    private String ToUserName;
    @XStreamAlias("FromUserName")
    private String FromUserName;
    @XStreamAlias("CreateTime")
    private String CreateTime;
    @XStreamAlias("MsgType")
    private String MsgType;
    @XStreamAlias("Content")
    private String Content;
    @XStreamAlias("MsgId")
    private String MsgId;
    @XStreamAlias("MsgDataId")
    private String MsgDataId;


    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgDataId() {
        return MsgDataId;
    }

    public void setMsgDataId(String msgDataId) {
        MsgDataId = msgDataId;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}

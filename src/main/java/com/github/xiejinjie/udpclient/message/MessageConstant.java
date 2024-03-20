package com.github.xiejinjie.udpclient.message;

/**
 * 报文类型常量
 */
public class MessageConstant {
    /*
     * 消息类型定义
     */

    //CPE开局请求验证消息 30
    public static final int CPE_START = 0x1e;

    //CPE通告上线结果 31
    public static final int CPE_ONLINE_RESULT = 0x1f;

    //CPE LAN网配置 32
    public static final int CPE_LAN_CONFIGURE = 0x20;

    //虚拟网络中的CPE信息下发接口 33
    public static final int CPE_VIRTUAL_CONFIGURE = 0x21;

    //CPE流量识别规则配置 34
    public static final int CPE_TRAFFIC_RULES = 0x22;

    //CPE动作操作指令 35
    public static final int CPE_INSTRUCT = 0x23;

    //CPE配置查询消息 36
    public static final int CPE_CONFIG_QUERY = 0x24;

    //CPE配置更新通告消息 37
    public static final int CPE_CONFIG_UPDATE_NOTIFY = 0x25;

    //vCPE上线 40
    public static final int VCPE_ONLINE = 0x28;

    //vCPE查询 41
    public static final int VCPE_LIST = 0x29;

    // 由门户向PE_A下发对应PE需要加速的CDN节点关系-新增 60
    public static final int PE_A_DOMAIN_CONFIGURE = 0x3c;

    // 由门户向PE_A下发对应PE需要加速的CDN节点关系-删除 61
    public static final int PE_A_DOMAIN_CLEAN = 0x3d;

    // 向PE下发管理的CDN节点的证书路径-新增 62
    public static final int PE_CERTIFICATE_CONFIGURE = 0x3e;

    // 向PE下发管理的CDN节点的证书路径-删除 63
    public static final int PE_CERTIFICATE_CLEAN = 0x3f;

    // 私钥和公钥证书配置结果通知 65
    public static final int PE_CERT_RESULT = 0x41;

    // 由门户向PE_B下发对应PE需要加速的CDN域名和父节点关系-新增 62
    public static final int PE_B_NODE_CONFIGURE = 0x3e;

    // 由门户向PE_B下发对应PE需要加速的CDN域名和父节点关系-删除 63
    public static final int PE_B_NODE_CLEAN = 0x3f;

    //由门户向PE下发其证书路径，包括私钥和公钥 64
    public static final int PE_CERT_CONFIGURE = 0x40;
    //通用响应消息类型
    public static final int MSG_TYPE_RESP = 0x63;


    /*
     * 消息常量
     */
    public static final int MSG_TYPE_LEN = 1;
    public static final int MSG_REQUEST_ID_LEN = 4;
    public static final int MSG_TLV_PART_LEN = 2;
    public static final int MSG_TLV_TAG_LEN = 1;
    public static final int MSG_TLV_VALUE_LEN = 1;


    /*
     * 通用响应状态
     */
    public static final int RESP_STATUS_SUCCESS = 0;
    public static final int RESP_STATUS_INVALID_ARG = 1;
    public static final int RESP_STATUS_ERROR = 2;
    public static final int RESP_STATUS_OTHER_ERROR = 3;

}

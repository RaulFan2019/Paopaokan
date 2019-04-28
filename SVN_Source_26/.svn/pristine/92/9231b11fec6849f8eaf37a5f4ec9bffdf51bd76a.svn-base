package com.app.pao.data.db;

import android.content.Context;

import com.app.pao.LocalApplication;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.entity.network.GetJpushMessageResult;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/30.
 * 消息数据库处理工具
 */
public class MessageData {


    /**
     * 获取用户的所有消息
     *
     * @param userid
     * @return
     */
    public static List<DBEntityMessage> getAllMessageByUser(final Context context, final int userid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("userid", "=", userid));
            if (list != null) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取用户的所有未读消息个数
     *
     * @param userid
     * @return
     */
    public static int getAllNewMessageByUserCount(final Context context, final int userid) {

        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("userid", "=", userid)
                            .and("status", "=", AppEnum.messageRead.NEW));
            if (list != null) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result.size();
    }

    /**
     * 保存新消息
     *
     * @param msg
     */
    public static void saveNewMessage(final Context context, final DBEntityMessage msg) {
        try {
            LocalApplication.getInstance().getDbUtils(context).save(msg);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Jpush保存新消息
     *
     * @param JpushMsg
     */
    public static void saveNewMessage(final Context context, final GetJpushMessageResult JpushMsg) {
        int isread = AppEnum.messageRead.NEW;
        if (JpushMsg.getType() == AppEnum.messageType.APPROVE_JOIN_RUN_GROUP
                || JpushMsg.getType() == AppEnum.messageType.ADD_KUDOS) {
            isread = AppEnum.messageRead.OLD;
        }
        DBEntityMessage messageEntity = new DBEntityMessage(JpushMsg.messageid, JpushMsg.fromuserid,
                JpushMsg.fromusernickname, JpushMsg.fromuseravatar, JpushMsg.fromusergender, JpushMsg
                .sendtime, AppEnum.messageRead.NEW, JpushMsg.type, JpushMsg.extra, JpushMsg.message,
                AppEnum.messageShow.NEW, JpushMsg.workoutid, JpushMsg.workoutstarttime, JpushMsg.groupid);
        messageEntity.setUserid(LocalApplication.getInstance().getLoginUser(context).userId);
        try {
            LocalApplication.getInstance().getDbUtils(context).save(messageEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取有关新的好友申请消息数量
     */
    public static int getNewFriendMsgCount(final Context context, final int userid) {
        int result = 0;
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_ADD_FRIEND)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            if (msgs != null && msgs.size() != 0) {
                result = msgs.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取有关好友的新消息
     */
    public static List<DBEntityMessage> getNewFriendApplyMsg(final Context context, final int userid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_ADD_FRIEND)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            if (msgs != null && msgs.size() != 0) {
                result = msgs;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取有关好友的未SHOW的消息
     */
    public static List<DBEntityMessage> getFriendApplyNeedShowMsg(final Context context, final int userid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_ADD_FRIEND)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("showstatus", "=", AppEnum.messageShow.NEW)
                            .and("userid", "=", userid));
            if (msgs != null && msgs.size() != 0) {
                result = msgs;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取申请入团的消息通知数量
     *
     * @param userid
     * @return
     */
    public static int getNewGroupMsgCount(final Context context, final int userid) {
        int result = 0;
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            if (msgs != null && msgs.size() != 0) {
                result = msgs.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取有申请入团消息的跑团ID列表
     *
     * @return
     */
    public static List<Integer> getHasApplyGroupIdList(final Context context, final int userid) {
        List<Integer> result = new ArrayList<>();
        try {
            List<DbModel> dbModel = LocalApplication.getInstance().getDbUtils(context)
                    .findDbModelAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid)
                            .groupBy("groupid"));

            if (dbModel != null) {
                for (DbModel model : dbModel) {
                    result.add(model.getInt("groupid"));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取申请入团的消息通知
     *
     * @param userid
     * @return
     */
    public static List<DBEntityMessage> getNewGroupMsg(final Context context, final int userid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            if (list != null && list.size() != 0) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取申请入团的消息通知
     *
     * @param userid
     * @return
     */
    public static List<DBEntityMessage> getGroupShowMsg(final Context context, final int userid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("showstatus", "=", AppEnum.messageShow.NEW)
                            .and("userid", "=", userid));
            if (list != null && list.size() != 0) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<DBEntityMessage> getGroupShowMsgByGroupId(final Context context, final int userid, final int groupid) {
        List<DBEntityMessage> result = new ArrayList<DBEntityMessage>();
        try {
            List<DBEntityMessage> list = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("showstatus", "=", AppEnum.messageShow.NEW)
                            .and("userid", "=", userid)
                            .and("groupid", "=", groupid));
            if (list != null && list.size() != 0) {
                result = list;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取新的需要批复团申请的消息数量
     */
    public static int getApplyJoinGroupMsgCount(final Context context, final int userid, final int groupid) {
        int result = 0;
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW).and("userid", "=", userid)
                            .and("extra", "=", String.valueOf(groupid)));
            if (msgs != null && msgs.size() != 0) {
                result = msgs.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 更新消息
     *
     * @param message
     */
    public static void updateMessage(final Context context, final DBEntityMessage message) {
        try {
            LocalApplication.getInstance().getDbUtils(context).update(message);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 已阅读所有好友相关的消息
     *
     * @param userid
     */
    public static void readAllNewFriendMessage(final Context context, final int userid) {
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "<", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            List<DBEntityMessage> newmsg = new ArrayList<DBEntityMessage>();
            if (msgs != null) {
                for (DBEntityMessage entityMessage : msgs) {
                    entityMessage.setStatus(AppEnum.messageRead.OLD);
                    newmsg.add(entityMessage);
                }
                LocalApplication.getInstance().getDbUtils(context).updateAll(newmsg);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 已SHOW所有好友相关的消息
     *
     * @param userid
     */
    public static void showAllFriendMessage(final Context context, final int userid) {
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "<", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("showstatus", "=", AppEnum.messageShow.NEW)
                            .and("userid", "=", userid));
            List<DBEntityMessage> newmsg = new ArrayList<DBEntityMessage>();
            if (msgs != null) {
                for (DBEntityMessage entityMessage : msgs) {
                    entityMessage.setShowStatus(AppEnum.messageShow.OLD);
                    newmsg.add(entityMessage);
                }
                LocalApplication.getInstance().getDbUtils(context).updateAll(newmsg);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    /**
     * 已阅读所有申请入团相关的消息
     *
     * @param userid
     */
    public static void readAllNewGroupMessage(final Context context, final int userid) {
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("userid", "=", userid));
            List<DBEntityMessage> newmsg = new ArrayList<DBEntityMessage>();
            if (msgs != null) {
                for (DBEntityMessage entityMessage : msgs) {
                    entityMessage.setStatus(AppEnum.messageRead.OLD);
                    newmsg.add(entityMessage);
                }
            }
            LocalApplication.getInstance().getDbUtils(context).updateAll(newmsg);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 已SHOW所有申请入团相关的消息
     *
     * @param userid
     */
    public static void showAllGroupMessage(final Context context, final int userid) {
        try {
            List<DBEntityMessage> msgs = LocalApplication.getInstance().getDbUtils(context)
                    .findAll(Selector.from(DBEntityMessage.class)
                            .where("type", "=", AppEnum.messageType.APPLY_JOIN_RUNGROUP)
                            .and("status", "=", AppEnum.messageRead.NEW)
                            .and("showstatus", "=", AppEnum.messageShow.NEW)
                            .and("userid", "=", userid));
            List<DBEntityMessage> newmsg = new ArrayList<DBEntityMessage>();
            if (msgs != null) {
                for (DBEntityMessage entityMessage : msgs) {
                    entityMessage.setShowStatus(AppEnum.messageShow.OLD);
                    newmsg.add(entityMessage);
                }
            }
            LocalApplication.getInstance().getDbUtils(context).updateAll(newmsg);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}

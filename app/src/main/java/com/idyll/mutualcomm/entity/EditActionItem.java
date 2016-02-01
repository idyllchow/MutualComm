package com.idyll.mutualcomm.entity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.event
 * @description 事件行为
 * @date 15/9/25
 */
public class EditActionItem {

    public final int menuClass;
    public final String action;
    public final int code;
    public boolean isSelected;
    public final boolean immediatelyDismiss;//点击松手后立刻消失
    public ArrayList<EditActionItem> nextActions;
    public final int actionType;

    public EditActionItem(int menuClass, String action, int code, int actionType) {
        this.menuClass = menuClass;
        this.action = action;
        this.code = code;
        immediatelyDismiss = true;
        this.actionType = actionType;
    }

    public EditActionItem(int menuClass, String action, int code, int actionType, boolean immediatelyDismiss, EditActionItem[] nextActions) {
        this.menuClass = menuClass;
        this.action = action;
        this.code = code;
        this.actionType = actionType;
        this.immediatelyDismiss = immediatelyDismiss;
        if (null != nextActions && 0 != nextActions.length) {
            this.nextActions = new ArrayList<>();
            this.nextActions.addAll(Arrays.asList(nextActions));
        }
    }
}

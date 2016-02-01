package com.idyll.mutualcomm.entity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author shibo
 * @packageName com.sponia.soccerstats.event
 * @description 事件行为
 * @date 15/9/25
 */
public class StatsActionItem {

    public final int menuClass;
    public final String action;
    public final int code;
//    public final int drawableId;
    public boolean isSelected;
    public final boolean immediatelyDismiss;//点击松手后立刻消失
    public ArrayList<StatsActionItem> nextActions;


    public StatsActionItem(int menuClass, String action, int code) {
        this.menuClass = menuClass;
        this.action = action;
        this.code = code;
//        drawableId = 0;
        immediatelyDismiss = true;
    }

    public StatsActionItem(int menuClass, String action, int code, /*int drawableId,*/ boolean immediatelyDismiss, StatsActionItem[] nextActions) {
        this.menuClass = menuClass;
        this.action = action;
        this.code = code;
//        this.drawableId = drawableId;
        this.immediatelyDismiss = immediatelyDismiss;
        if (null != nextActions && 0 != nextActions.length) {
            this.nextActions = new ArrayList<>();
            this.nextActions.addAll(Arrays.asList(nextActions));
        }
    }
}

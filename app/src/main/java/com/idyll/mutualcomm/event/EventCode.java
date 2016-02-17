package com.idyll.mutualcomm.event;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.comm.MCConstants;

import java.util.HashMap;

/**
 * @packageName com.sponia.soccerstats.http.model
 * @description 事件代码
 * @date 15/9/11
 * @auther shibo
 */
public class EventCode {
    public static final int ShotOnTarget = 11;//射正
    public static final int ShotOffTarget = 12;//射偏
    public static final int SuccessfulPass = 21;//传球成功
    public static final int UnsuccessfulPass = 22;//传球失败
    public static final int SuccessfulTakeOn = 31; //过人成功
    public static final int UnsuccessfulTakeOn = 32; //过人失败
    public static final int TackleWon = 33;//抢断成功
    public static final int TackleLost = 34;//抢断失败
    public static final int ChallengeWon = 35; //防守成功
    public static final int ChallengeLost = 36; //防守失败
    public static final int Interception = 41;//拦截
    public static final int Clearance = 42;//解围
    public static final int Save = 51;//扑救
    public static final int ThrowIn = 61;//界外球
    public static final int Corner = 62;//角球
    public static final int FreeKick = 63;//任意球
    public static final int PenaltyKick = 64;//点球
    public static final int Goal = 71;//进球
    public static final int OwnGoal = 72;//乌龙
    public static final int GoalAssist = 73;//助攻
    public static final int KeyPass = 74;//关键传球
    public static final int FoulConceded = 81;//犯规
    public static final int YellowCard = 82;//黄牌
    public static final int RedCard = 83;//红牌
    public static final int Offside = 84;//越位

    public static final int Ctrl = 91;//控球
    public static final int Begin = 100;//比赛开始
    public static final int KickOffFirstHalf = 101;//上半场开始
    public static final int HalfTime = 102;//上半场结束
    public static final int KickOffSecondHalf = 103;//下半场开始
    public static final int FullTime = 104;//下半场结束
    public static final int KickOffExtraTimeFirstHalf = 105;//加时赛上半场开始
    public static final int ExtraTimeHalfTime = 106;//加时赛上半场结束
    public static final int KickOffExtraTimeSecondHalf = 107;//加时赛下半场开始
    public static final int ExtraTimeFullTime = 108;//加时赛下半场结束
    public static final int PenaltyShootOutBegin = 109;//点球大战开始
    public static final int PenaltyShootOutFinish = 110;//点球大战结束
    public static final int Finish = 111;//比赛结束
    public static final int EnterThePitch  = 200;//球员上场
    public static final int LeaveThePitch = 201;//球员下场

    public static final int penaltyShotOnTarget = 311;//点球射正
    public static final int penaltyShotOffTarget = 312;//点球射偏
    public static final int penaltySave = 351;//点球扑救
    public static final int penaltyGoal = 371;//点球进球


    public static final HashMap<Integer, String> sEventNameMap = new HashMap<>();

    static {
//        sEventNameMap.put(ShotOnTarget, /*"射正"*/MCConstants.application.getString(R.string.shot_on_target));
//        sEventNameMap.put(ShotOffTarget, "射偏");
//        sEventNameMap.put(SuccessfulPass, "传球成功");
//        sEventNameMap.put(UnsuccessfulPass, "传球失败");
//        sEventNameMap.put(SuccessfulTakeOn, "过人成功");
//        sEventNameMap.put(UnsuccessfulTakeOn, "过人失败");
//        sEventNameMap.put(TackleWon, "抢断成功");
//        sEventNameMap.put(TackleLost, "抢断失败");
//        sEventNameMap.put(ChallengeWon, "防守成功");
//        sEventNameMap.put(ChallengeLost, "防守失败");
//        sEventNameMap.put(Interception, "拦截");
//        sEventNameMap.put(Clearance, "解围");
//        sEventNameMap.put(Save, "扑救");
//        sEventNameMap.put(ThrowIn, "界外球");
//        sEventNameMap.put(Corner, "角球");
//        sEventNameMap.put(FreeKick, "任意球");
//        sEventNameMap.put(PenaltyKick, "点球");
//        sEventNameMap.put(Goal, "进球");
//        sEventNameMap.put(OwnGoal, "乌龙");
//        sEventNameMap.put(GoalAssist, "助攻");
//        sEventNameMap.put(KeyPass, "关键传球");
//        sEventNameMap.put(FoulConceded, "犯规");
//        sEventNameMap.put(YellowCard, "黄牌");
//        sEventNameMap.put(RedCard, "红牌");
//        sEventNameMap.put(Offside, "越位");
//        //shibo add
//        sEventNameMap.put(Ctrl, "控球");
//        sEventNameMap.put(Begin, "比赛开始");
//        sEventNameMap.put(KickOffFirstHalf, "上半场开始");
//        sEventNameMap.put(HalfTime, "上半场结束");
//        sEventNameMap.put(KickOffSecondHalf, "下半场开始");
//        sEventNameMap.put(FullTime, "下半场结束");
//        sEventNameMap.put(KickOffExtraTimeFirstHalf, "加时赛上半场开始");
//        sEventNameMap.put(ExtraTimeHalfTime, "加时赛上半场结束");
//        sEventNameMap.put(KickOffExtraTimeSecondHalf, "加时赛下半场开始");
//        sEventNameMap.put(ExtraTimeFullTime, "加时赛下半场结束");
//        sEventNameMap.put(PenaltyShootOutBegin, "点球大战开始");
//        sEventNameMap.put(PenaltyShootOutFinish, "点球大战结束");
//        sEventNameMap.put(Finish, "比赛结束");
//        sEventNameMap.put(EnterThePitch, "球员上场");
//        sEventNameMap.put(LeaveThePitch, "球员下场");
//        //点球大战
//        sEventNameMap.put(penaltyShotOnTarget, "射正");
//        sEventNameMap.put(penaltyShotOffTarget, "射偏");
//        sEventNameMap.put(penaltySave, "扑救");
//        sEventNameMap.put(penaltyGoal, "进球");

        sEventNameMap.put(ShotOnTarget, MCConstants.application.getString(R.string.shot_on_target));
        sEventNameMap.put(ShotOffTarget, MCConstants.application.getString(R.string.shot_off_target));
        sEventNameMap.put(SuccessfulPass, MCConstants.application.getString(R.string.successful_pass));
        sEventNameMap.put(UnsuccessfulPass, MCConstants.application.getString(R.string.unsuccessful_pass));
        sEventNameMap.put(UnsuccessfulPass, MCConstants.application.getString(R.string.unsuccessful_pass));
        sEventNameMap.put(SuccessfulTakeOn, MCConstants.application.getString(R.string.successful_take_on));
        sEventNameMap.put(UnsuccessfulTakeOn, MCConstants.application.getString(R.string.unsuccessful_take_on));
        sEventNameMap.put(TackleWon, MCConstants.application.getString(R.string.tackle_won));
        sEventNameMap.put(TackleLost, MCConstants.application.getString(R.string.tackle_lost));
        sEventNameMap.put(ChallengeWon, MCConstants.application.getString(R.string.challenge_won));
        sEventNameMap.put(ChallengeLost, MCConstants.application.getString(R.string.challenge_lost));
        sEventNameMap.put(Interception, MCConstants.application.getString(R.string.interception));
        sEventNameMap.put(Clearance, MCConstants.application.getString(R.string.clearance));
        sEventNameMap.put(Save, MCConstants.application.getString(R.string.save));
        sEventNameMap.put(ThrowIn, MCConstants.application.getString(R.string.throw_in));
        sEventNameMap.put(Corner, MCConstants.application.getString(R.string.corner));
        sEventNameMap.put(FreeKick, MCConstants.application.getString(R.string.free_kick));
        sEventNameMap.put(PenaltyKick, MCConstants.application.getString(R.string.penalty_kick));
        sEventNameMap.put(Goal, MCConstants.application.getString(R.string.goal));
        sEventNameMap.put(OwnGoal, MCConstants.application.getString(R.string.goal_own));
        sEventNameMap.put(GoalAssist, MCConstants.application.getString(R.string.goal_assist));
        sEventNameMap.put(KeyPass, MCConstants.application.getString(R.string.key_pass));
        sEventNameMap.put(FoulConceded, MCConstants.application.getString(R.string.foul_conceded));
        sEventNameMap.put(YellowCard, MCConstants.application.getString(R.string.yellow_card));
        sEventNameMap.put(RedCard, MCConstants.application.getString(R.string.red_card));
        sEventNameMap.put(Offside, MCConstants.application.getString(R.string.offside));
        //
        sEventNameMap.put(Ctrl, MCConstants.application.getString(R.string.ctrl));
        sEventNameMap.put(Begin, MCConstants.application.getString(R.string.match_begin));
        sEventNameMap.put(KickOffFirstHalf, MCConstants.application.getString(R.string.match_kick_off_first_half));
        sEventNameMap.put(HalfTime, MCConstants.application.getString(R.string.match_half_time));
        sEventNameMap.put(KickOffSecondHalf, MCConstants.application.getString(R.string.match_kick_off_second_half));
        sEventNameMap.put(FullTime, MCConstants.application.getString(R.string.match_second_half_end));
        sEventNameMap.put(KickOffExtraTimeFirstHalf, MCConstants.application.getString(R.string.match_kick_off_extra_time_first_half));
        sEventNameMap.put(ExtraTimeHalfTime, MCConstants.application.getString(R.string.match_extra_time_half_time));
        sEventNameMap.put(KickOffExtraTimeSecondHalf, MCConstants.application.getString(R.string.match_kick_off_extra_time_second_half));
        sEventNameMap.put(ExtraTimeFullTime, MCConstants.application.getString(R.string.match_extra_time_full_time));
        sEventNameMap.put(Finish, MCConstants.application.getString(R.string.match_finish));
        sEventNameMap.put(EnterThePitch, MCConstants.application.getString(R.string.enter_the_pitch)); //enter_the_pitch
        sEventNameMap.put(LeaveThePitch, MCConstants.application.getString(R.string.leave_the_pitch)); //leave_the_pitch
        //点球大战
        sEventNameMap.put(penaltyShotOnTarget, MCConstants.application.getString(R.string.shot_on_target_penalty));
        sEventNameMap.put(penaltyShotOffTarget, MCConstants.application.getString(R.string.shot_off_target_penalty));
        sEventNameMap.put(penaltySave, MCConstants.application.getString(R.string.save_penalty));
        sEventNameMap.put(penaltyGoal, MCConstants.application.getString(R.string.goal_penalty));
        sEventNameMap.put(PenaltyShootOutBegin, MCConstants.application.getString(R.string.match_penalty_shoot_out_begin)); //match_penalty_shoot_out_begin
        sEventNameMap.put(PenaltyShootOutFinish, MCConstants.application.getString(R.string.match_penalty_shoot_out_finish)); //match_penalty_shoot_out_finish
    }
}

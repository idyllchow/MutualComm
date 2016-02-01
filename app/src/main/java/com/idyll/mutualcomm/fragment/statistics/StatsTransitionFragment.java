package com.idyll.mutualcomm.fragment.statistics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.idyll.mutualcomm.R;
import com.idyll.mutualcomm.activity.StatsOperateActivity;
import com.idyll.mutualcomm.adapter.LeftDragAdapter;
import com.idyll.mutualcomm.adapter.RightDragAdapter;
import com.idyll.mutualcomm.adapter.StatsPlayerGVAdapter;
import com.idyll.mutualcomm.comm.MCConstants;
import com.idyll.mutualcomm.entity.StatsMatchFormationBean;
import com.idyll.mutualcomm.fragment.StatsBaseFragment;
import com.idyll.mutualcomm.view.DragGridView;
import com.idyll.mutualcomm.view.StatsDragGridView;
import com.idyll.mutualcomm.view.TransGridView;
import com.sponia.foundationmoudle.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @packageName com.sponia.soccerstats.fragment
 * @description 换人界面, 左边场上球员, 右边场下球员
 * @date 15/9/14
 * @auther shibo
 */
public class StatsTransitionFragment extends StatsBaseFragment {
    private static final String TAG = "Player";
    private static final boolean DEBUG = false;
    private ArrayList<StatsMatchFormationBean> mRightItems = new ArrayList<>();
    private ArrayList<StatsMatchFormationBean> mLeftItems = new ArrayList<>(12);
    private DragGridView mDragLeftGridView;
    private StatsDragGridView mDragRightGridView;
    //场上球员
    private LeftDragAdapter mDragLeftAdapter;
    //场下球员
    private RightDragAdapter mDragRightAdapter;

    private View mAnimateView;
    private TextView mAnimateTv;

    private int itemTop;
    private int itemLeft;
    //比赛人数
    private int matchType;
    //为了边框效果不被点击等事件影响,
    private TransGridView mGVPlayerBg;
    //场下球员背景
    private StatsPlayerGVAdapter mPlayerBgAdapter;

    public static StatsTransitionFragment getInstance(ArrayList<StatsMatchFormationBean> playerList, int matchType) {
        StatsTransitionFragment fragment = new StatsTransitionFragment();
        Bundle args = new Bundle();
        LogUtil.defaultLog("getInstance playerList size " + playerList.size());
        if (playerList != null && playerList.size() > 0) {
            args.putParcelableArrayList("playerList", playerList);
            args.putInt("matchType", matchType);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.defaultLog("savedInstanceState == " + savedInstanceState + "; getArguments == " + getArguments());
        if (null != savedInstanceState) {
            mLeftItems = savedInstanceState.getParcelableArrayList("leftItems");
            mRightItems = savedInstanceState.getParcelableArrayList("rightItems");
            matchType = savedInstanceState.getInt("matchType");
        } else if (null != getArguments()) {
            ArrayList<StatsMatchFormationBean> players = getArguments().getParcelableArrayList("playerList");
            matchType = getArguments().getInt("matchType");
            LogUtil.defaultLog("playerList size " + players.size());
            generateDatas(players);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("leftItems", mLeftItems);
        outState.putParcelableArrayList("rightItems", mRightItems);
        outState.putInt("matchType", matchType);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transition, null, false);

        mDragLeftGridView = (DragGridView) rootView.findViewById(R.id.drag_gridView_left);
        mGVPlayerBg = (TransGridView) rootView.findViewById(R.id.gv_player_bg);
        mDragRightGridView = (StatsDragGridView) rootView.findViewById(R.id.drag_gridView_right);
        mDragLeftGridView.setStatusHeight(0);
        mDragRightGridView.setStatusHeight(0);

        int windowHeight = MCConstants.getScreenHeight() - getResources().getDimensionPixelOffset(R.dimen.tab_height);
        int viewWidth = MCConstants.getScreenWidth() / 2;
        int playerItemWidth = viewWidth / 3 - getResources().getDimensionPixelOffset(R.dimen.player_reduce_height);

        //以高度为准
        if (windowHeight < viewWidth) {
            viewWidth = windowHeight;
        }
        int leftItemWidth = viewWidth / 3;
        int rightItemWidth = windowHeight / 4;

        itemTop = (leftItemWidth - rightItemWidth) / 2;
        itemLeft = (leftItemWidth - rightItemWidth) / 2;
        //设置左边球员fragment纵向间隙,选择球员时生效
        if (matchType == MCConstants.MAX_ONFIELD_NINE) {
            mDragLeftGridView.setVerticalSpacing(windowHeight / 3 - leftItemWidth);
        } else if (matchType == MCConstants.MAX_ONFIELD_TWELVE) {
            mDragLeftGridView.setVerticalSpacing(windowHeight / 4 - leftItemWidth);
        }

//        mDragLeftGridView.getLayoutParams().width = viewWidth;
        mDragLeftGridView.getLayoutParams().width = MCConstants.getScreenWidth() - viewWidth;
        mDragLeftGridView.getLayoutParams().height = windowHeight;
        mDragRightGridView.getLayoutParams().width = windowHeight;
        mDragRightGridView.getLayoutParams().height = windowHeight;

        mGVPlayerBg.getLayoutParams().width = windowHeight;
        mGVPlayerBg.getLayoutParams().height = windowHeight;

        mDragLeftAdapter = new LeftDragAdapter(getActivity(), mLeftItems);
        mDragRightAdapter = new RightDragAdapter(getActivity(), mRightItems);
        mPlayerBgAdapter = new StatsPlayerGVAdapter(getActivity());
        //设置左边球员item
//        mDragLeftAdapter.setItemWidthAndHeight(playerItemWidth, playerItemWidth);
        mPlayerBgAdapter.setItemWidthAndHeight(rightItemWidth, rightItemWidth);
        LogUtil.defaultLog("StatsTranstion onCreateView playerItemWidth " + playerItemWidth + "; leftItemWidth: " + leftItemWidth);

        mDragLeftGridView.setAdapter(mDragLeftAdapter);
        mDragRightGridView.setAdapter(mDragRightAdapter);
        mGVPlayerBg.setAdapter(mPlayerBgAdapter);

        mAnimateView = rootView.findViewById(R.id.animate_view);
        mAnimateView.setLayoutParams(new FrameLayout.LayoutParams(rightItemWidth, rightItemWidth));
        mAnimateTv = (TextView) rootView.findViewById(R.id.animate_tv);
        initAnimate();

        return rootView;
    }

    /**
     * 刷新场上球员
     */
    public void refreshOnFieldPlayer(ArrayList<StatsMatchFormationBean> playerList) {
        generateDatas(playerList);
        mDragLeftAdapter.notifyDataSetChanged();
        mDragRightAdapter.notifyDataSetChanged();
    }


    private void initAnimate() {

        mDragRightGridView.setOnSwapItemCallback(new StatsDragGridView.OnSwapItemCallback() {
            @Override
            public int onSwapItem(final int moveX, final int moveY, final int oldPos) {
                LogUtil.e(TAG, "onSwapItem() moveX: " + moveX + ", moveY: " + moveY + ", oldPos: " + oldPos);
                if (!mAnimationEnd) {
                    return AdapterView.INVALID_POSITION;
                }
                final int left = mDragRightGridView.getLeft();
                int adjustMoveX = left + moveX;
//                if (adjustMoveX >= 0 && adjustMoveX <= mGaps) {
//                    adjustMoveX = mGaps + 1;
//                }
                final int newPos = mDragLeftGridView.pointToPosition(adjustMoveX, moveY);
                if (DEBUG) {
                    LogUtil.e(TAG, "newPos: " + newPos + ", left: " + left);
                }
                if (newPos != AdapterView.INVALID_POSITION && mAnimationEnd) {
                    swapRightToLeft(oldPos, newPos);
                }
                return newPos;
            }

            @Override
            public void onSwapAnimate(int newPos, int oldPos, View oldView) {
                if (null != mRightItems.get(oldPos) && null != oldView && mAnimationEnd) {
                    View newView = mDragLeftGridView.getChildAt(newPos - mDragLeftGridView.getFirstVisiblePosition());
                    if (null != newView) {
                        mAnimateTv.setText(mRightItems.get(oldPos).Player_Num + "");
                        final int left = mDragRightGridView.getLeft();
                        final int newX = newView.getLeft();
                        final int newY = newView.getTop();
                        final int oldX = oldView.getLeft() + left;
                        final int oldY = oldView.getTop();
                        if (DEBUG) {
                            LogUtil.e(TAG, "newX: " + newX + ", newY: " + newY + ", oldX: " + oldX + ", oldY: " + oldY);
                        }
                        swapAnimate(oldView, newX, oldX, newY, oldY);
                    }
                }
            }

            @Override
            public boolean checkHasPosition() {
                for (StatsMatchFormationBean player : mLeftItems) {
                    if (null == player) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onItemClick(final int clickedPos, final View oldView) {
                LogUtil.defaultLog("onItemClick clickedPos: " + clickedPos + "; oldView == " + oldView);
                ((StatsOperateActivity) getActivity()).hideTipView();
                if (null == oldView) {
                    return;
                }
                StatsMatchFormationBean player = mRightItems.get(clickedPos);
                LogUtil.defaultLog("matchType----------->" + matchType);
                //根据比赛人数往左边添加球员
                for (int i = 0; i < matchType; i++) {
                    if (null == mLeftItems.get(i)) {
                        mLeftItems.set(i, player);
                        final int newPos = i;
                        mAnimateTv.setText(player.Player_Num);
                        final ViewTreeObserver observer = mDragLeftGridView.getViewTreeObserver();
                        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                            @Override
                            public boolean onPreDraw() {
                                observer.removeOnPreDrawListener(this);
                                final View newView = mDragLeftGridView.getChildAt(newPos - mDragLeftGridView.getFirstVisiblePosition());
                                if (null != newView) {
                                    final int left = mDragRightGridView.getLeft();
                                    final int newX = newView.getLeft() + itemLeft;
                                    final int newY = newView.getTop() + itemTop;
                                    final int oldX = oldView.getLeft() + left;
                                    final int oldY = oldView.getTop();
                                    if (DEBUG) {
                                        LogUtil.e(TAG, "newX: " + newX + ", newY: " + newY + ", oldX: " + oldX + ", oldY: " + oldY);
                                    }
                                    swapAnimate(newView, oldX, newX, oldY, newY);
                                }
                                return true;
                            }
                        });

                        mRightItems.set(clickedPos, null);
                        mDragRightAdapter.notifyDataSetChanged();
                        mDragLeftAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });

        mDragLeftGridView.setOnSwapItemCallback(new DragGridView.OnSwapItemCallback() {
            @Override
            public int onSwapItem(int moveX, int moveY, int oldPos) {
                LogUtil.e(TAG, "onSwapItem() moveX: " + moveX + ", moveY: " + moveY + ", oldPos: " + oldPos);
                if (!mAnimationEnd) {
                    return AdapterView.INVALID_POSITION;
                }
                final int left = mDragRightGridView.getLeft();
                int adjustMoveX = moveX - left;
                final int newPos = mDragRightGridView.pointToPosition(adjustMoveX, moveY);
                LogUtil.e(TAG, "newPos: " + newPos + ", left: " + left);
                if (newPos != AdapterView.INVALID_POSITION && mAnimationEnd) {
                    swapLeftToRight(oldPos, newPos);
                }
                return newPos;
            }

            @Override
            public void onSwapAnimate(int newPos, int oldPos, View oldView) {
                if (null != mLeftItems.get(oldPos) && null != oldView && mAnimationEnd) {
                    View newView = mDragRightGridView.getChildAt(newPos - mDragRightGridView.getFirstVisiblePosition());
                    if (null != newView) {
                        mAnimateTv.setText(mLeftItems.get(oldPos).Player_Num + "");
                        final int left = mDragRightGridView.getLeft();
                        final int newX = newView.getLeft() + left;
                        final int newY = newView.getTop();
                        final int oldX = oldView.getLeft();
                        final int oldY = oldView.getTop();
                        if (DEBUG) {
                            LogUtil.e(TAG, "newX: " + newX + ", newY: " + newY + ", oldX: " + oldX + ", oldY: " + oldY);
                        }
                        swapAnimate(oldView, newX, oldX, newY, oldY);
                    }
                }
            }

            @Override
            public boolean checkHasPosition() {
                for (StatsMatchFormationBean player : mRightItems) {
                    if (null == player) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onItemClick(int clickedPos, final View oldView) {
                LogUtil.defaultLog("mDragLeftGridView onItemClick() clickedPos: " + clickedPos);
                ((StatsOperateActivity) getActivity()).hideTipView();
                StatsMatchFormationBean player = mLeftItems.get(clickedPos);
                for (int i = 0, len = mRightItems.size(); i < len; i++) {
                    if (null == mRightItems.get(i)) {
                        mRightItems.set(i, player);
                        final int newPos = i;
                        mAnimateTv.setText(player.Player_Num + "");
                        final ViewTreeObserver observer = mDragRightGridView.getViewTreeObserver();
                        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                            @Override
                            public boolean onPreDraw() {
                                observer.removeOnPreDrawListener(this);
                                final View newView = mDragRightGridView.getChildAt(newPos - mDragRightGridView.getFirstVisiblePosition());
                                if (null != newView) {
                                    final int left = mDragRightGridView.getLeft();
                                    final int newX = newView.getLeft() + left;
                                    final int newY = newView.getTop();
                                    final int oldX = oldView.getLeft();
                                    final int oldY = oldView.getTop();
                                    if (DEBUG) {
                                        LogUtil.e(TAG, "newX: " + newX + ", newY: " + newY + ", oldX: " + oldX + ", oldY: " + oldY);
                                    }
                                    swapAnimate(newView, oldX, newX, oldY, newY);
                                }
                                return true;
                            }
                        });

                        mLeftItems.set(clickedPos, null);
                        mDragLeftAdapter.notifyDataSetChanged();
                        mDragRightAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        });
    }

    private boolean mAnimationEnd = true;

    private void swapAnimate(final View view, int newX, int oldX, int newY, int oldY) {
        view.setVisibility(View.INVISIBLE);
        mAnimateView.setVisibility(View.VISIBLE);
        AnimatorSet resultSet = createXYAnimations(mAnimateView, newX, oldX, newY, oldY);
        resultSet.setDuration(300);
        resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
        resultSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationEnd = true;
                mAnimateView.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        });
        resultSet.start();
    }

    private void swapRightToLeft(int oldPosition, int newPosition) {
        StatsMatchFormationBean oldItem = mRightItems.get(oldPosition);
        StatsMatchFormationBean newItem = mLeftItems.get(newPosition);
        mLeftItems.set(newPosition, oldItem);
        mRightItems.set(oldPosition, newItem);
        mDragLeftAdapter.notifyDataSetChanged();
    }

    private void swapLeftToRight(int oldPosition, int newPosition) {
        StatsMatchFormationBean oldItem = mLeftItems.get(oldPosition);
        StatsMatchFormationBean newItem = mRightItems.get(newPosition);
        mRightItems.set(newPosition, oldItem);
        mLeftItems.set(oldPosition, newItem);
        mDragRightAdapter.notifyDataSetChanged();
    }

    /**
     * 创建移动动画
     */
    private AnimatorSet createXYAnimations(View view, float startX,
                                           float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "x",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "y",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    private void generateDatas(ArrayList<StatsMatchFormationBean> playerList) {
        ArrayList<StatsMatchFormationBean> onFieldList = new ArrayList<>();
        ArrayList<StatsMatchFormationBean> offFieldList = new ArrayList<>();
        for (StatsMatchFormationBean player : playerList) {
            LogUtil.defaultLog("playerList onField " + player.onField);
            if (player.onField) {
                onFieldList.add(player);
            } else {
                offFieldList.add(player);
            }
        }

        int onFieldNum = onFieldList.size();
        int offFieldNum = offFieldList.size();
        int totalNum = offFieldNum + onFieldNum;

        //上场球员
        StatsMatchFormationBean[] onFieldArray = new StatsMatchFormationBean[matchType];
        for (StatsMatchFormationBean player : onFieldList) {
            if (player.position < matchType) {
                onFieldArray[player.position] = player;
            } else { //比赛过程中修改人制,多出的球员移为替补
                offFieldList.add(player);
            }
        }
        mLeftItems.clear();
        Collections.addAll(mLeftItems, onFieldArray);

        //未上场球员
        if (0 != totalNum % 4) {
            totalNum += (4 - totalNum % 4);
        }
        StatsMatchFormationBean[] offFieldArray = new StatsMatchFormationBean[totalNum];
        for (StatsMatchFormationBean player : offFieldList) {
            //TODO 未能复现的越界,
            try {
                offFieldArray[player.position] = player;
            } catch (ArrayIndexOutOfBoundsException e) {
                LogUtil.defaultLog(e);
            }
        }
        mRightItems.clear();
        Collections.addAll(mRightItems, offFieldArray);
        LogUtil.defaultLog("offFieldList size " + offFieldList.size());
    }

    public void toggle() {
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(0, 0);
        if (!this.isVisible()) {
            fragmentTransaction.show(this).commitAllowingStateLoss();
            ObjectAnimator transX = ObjectAnimator.ofFloat(mDragRightGridView, "translationX", 960, 0);
            transX.setInterpolator(new AccelerateDecelerateInterpolator());
            transX.start();
        } else {
            ObjectAnimator transX = ObjectAnimator.ofFloat(mDragRightGridView, "translationX", 0, 960);
            transX.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    fragmentTransaction.hide(StatsTransitionFragment.this).commitAllowingStateLoss();
                }
            });
            transX.start();
        }
    }

    public ArrayList<StatsMatchFormationBean> getOnFieldList() {
        if (null != mLeftItems && !mLeftItems.isEmpty()) {
//            ArrayList<StatsMatchFormationBean> result = new ArrayList<>(9);
            ArrayList<StatsMatchFormationBean> result = new ArrayList<>(matchType);
            for (StatsMatchFormationBean player : mLeftItems) {
                if (null != player) {
                    player.onField = true;
                    player.position = mLeftItems.indexOf(player);
                    result.add(player);
                }
            }
            return result;
        }
        return null;
    }


    public ArrayList<StatsMatchFormationBean> getOffFieldList() {
        if (null != mRightItems && !mRightItems.isEmpty()) {
            ArrayList<StatsMatchFormationBean> result = new ArrayList<>(25);
            for (StatsMatchFormationBean player : mRightItems) {
                if (null != player) {
                    player.onField = false;
                    player.position = mRightItems.indexOf(player);
                    result.add(player);
                }
            }
            return result;
        }
        return null;
    }
}
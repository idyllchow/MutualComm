package com.idyll.mutualcomm.view;

public interface IDrag {
    /**
     * 重新排列数据
     *
     * @param oldPosition
     * @param newPosition
     */
    void reorderItems(int oldPosition, int newPosition);


    /**
     * 设置某个item隐藏
     *
     * @param hidePosition
     */
    void setHideItem(int hidePosition);

    /**
     * 删除某个item
     *
     * @param removePosition
     */
    void removeItem(int removePosition);


}
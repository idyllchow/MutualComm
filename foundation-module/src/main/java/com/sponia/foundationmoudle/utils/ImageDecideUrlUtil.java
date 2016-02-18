package com.sponia.foundationmoudle.utils;

import android.text.TextUtils;

/**
 * Created by caolijie on 15/5/26.
 * 根据图片宽度以及原始路径决定图片最终路径
 */
public class ImageDecideUrlUtil {

    static int binarySearch(int[] srcArray, int des, boolean higher) {

        int low = 0;
        int high = srcArray.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (des == srcArray[middle]) {
                return middle;
            } else if (des < srcArray[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        if (high < 0)
            return 0;
        if (higher) {
            if (des > srcArray[high] && high + 1 <= srcArray.length - 1) {
                high = high + 1;
            }
        } else {
            if (des < srcArray[high] && high - 1 >= 0)
                high = high - 1;
        }
        return high;
    }

    private static final int[] URL_SIZE = {16, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            120, 160, 200, 300, 400, 500, 600};
    /**
     * 传入原始url,以及宽度,
     * 返回一个原始url+"-w"+传入的宽度在URL_SIZE这个数组中最贴近的值作为最终的图片url路径名.
     * */
    public static String onDecide(String originUrl, int width) {
        if (TextUtils.isEmpty(originUrl)) {
            return "";
        }
        int index = binarySearch(URL_SIZE, width, true);
        return originUrl + "-w" + URL_SIZE[index];
    }

}

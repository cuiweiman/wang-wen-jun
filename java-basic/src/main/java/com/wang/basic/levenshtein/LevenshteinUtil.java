package com.wang.basic.levenshtein;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 编辑距离算法——判断字符串相似度
 * @author: wei·man cui
 * @date: 2020/11/20 17:19
 */
@Slf4j
public class LevenshteinUtil {

    public static Double levenshtein(String str1, String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }

        //计算相似度,取数组右下角的值，同样不同位置代表不同字符串的比较
        double similarity = 1 - (double) dif[len1][len2] / Math.max(str1.length(), str2.length());
        log.info("字符串 {} 与 {} 的比较，差异步骤为：{}，相似度为：{}", str1, str2, dif[len1][len2], similarity);
        return similarity;
    }

    /**
     * 得到最小值
     *
     * @param is
     * @return
     */
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        //要比较的两个字符串
        String str1 = "兹的一声,卡怎么出不来呢";
        String str2 = "吃的一声啊卡怎么出不来呢？";
        final Double levenshtein = levenshtein(str1, str2);
        System.out.println(levenshtein);
    }

}

package com.example.android.framework.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Founder: shaobin
 * Create Date: 2020/1/9
 * Profile: 生成dimens文件  sw320dp
 */
public class GenerateDimen {

    private static PrintWriter out;
    private static StringBuilder swdef;

    public static void main(String[] args) {
        //genDimen();
        //genFont();
    }

    public static void genDimen() {
        swdef = new StringBuilder();

        try {
            swdef.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<resources>");
            double value;
            for (int i = 0; i < 500; i++) {
                //这里控制对应转换的值，如果是标准尺寸就一对一转换
                // 1 代表sw380
                // sw420 转换
                //value = (i + 1) * 420/380;
                value = (i + 1) * 1;
                value = Math.round(value * 100) / 100;
                swdef.append("<dimen name=\"dp_" + (i + 1) + "\">" + value + "dp</dimen>\r\n");
            }
            swdef.append("</resources>");

            //这里是文件名，1 注意修改 sw 后面的值，和转换值一一对应  2 文件夹和文件要先创建好否则要代码创建
            String filedef = "./app/src/main/res/values/dimens.xml";

            out = new PrintWriter(new BufferedWriter(new FileWriter(filedef)));
            out.println(swdef.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
    public static void genFont() {
        swdef = new StringBuilder();

        try {
            swdef.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<resources>");
            for (int i = 0; i < 200; i++) {
                if (i % 2 == 0)
                    swdef.append("<dimen name=\"font_" + i + "\">" + i + "sp</dimen> \n");
            }
            swdef.append("</resources>");

            //这里是文件名，1 注意修改 sw 后面的值，和转换值一一对应  2 文件夹和文件要先创建好否则要代码创建
            String filedef = "./app/src/main/res/values/fronts.xml";

            out = new PrintWriter(new BufferedWriter(new FileWriter(filedef)));
            out.println(swdef.toString());
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}

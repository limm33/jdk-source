/*
 * 文件名：BorderStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： BorderStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年3月8日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

/**
 * 为组件设置边框.
 * 
 * 空边框EmptyBorder，线边框LineBorder，斜切边框BevelBorder，软斜切边框SoftBevelBorder，
 * 
 * 粗糙边框MatteBorder，混合边框CompoundBorder，标题边框TitledBorder，蚀刻边框EtchedBorder
 * 
 * @author zxiaofan
 */
public class BorderStudy {
    /**
     * 顶层容器.
     */
    private JFrame jFrame = new JFrame("zxiaofan.com Border_Study");

    /**
     * 顶层容器.
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        new BorderStudy().init();
    }

    /**
     * 初始化UI.
     * 
     */
    public void init() {
        jFrame.setLayout(new GridLayout(2, 4)); // GridLayout(int rows, int cols) 创建具有指定行数和列数的网格布局，Rows为行数，cols为列数。
        Border border = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.GREEN, Color.BLUE, Color.GRAY); // highlightOuter、highlightInner、shadowOuter、shadowInner
        jFrame.add(getPanelWithBorder(border, "BevelBorder"));
        // 使用静态工厂方法创建LineBorder
        Border borderLine = BorderFactory.createLineBorder(Color.orange, 10);
        jFrame.add(getPanelWithBorder(borderLine, "Line_Border"));
        // EmptyBorder：组件四周留空
        Border borderEmpty = BorderFactory.createEmptyBorder(20, 5, 10, 30);
        jFrame.add(getPanelWithBorder(borderEmpty, "EmptyBorder"));
        // EtchedBorder
        Border borderEtched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.RED, Color.GREEN); // 红色高亮，绿色阴影
        jFrame.add(getPanelWithBorder(borderEtched, "EtchedBorder"));
        // TitleBorder为边框增加标题
        TitledBorder borderTitle = new TitledBorder(borderLine, "Test Title");
        jFrame.add(getPanelWithBorder(borderTitle, "TitleBorder"));
        // MatteBorder:EmptyBorder的子类，可指定留空区域的颜色或背景
        MatteBorder matteBorder = new MatteBorder(20, 5, 10, 30, Color.GREEN); // 指定颜色，色彩渲染为绿色
        jFrame.add(getPanelWithBorder(matteBorder, "MatteBorder"));
        // CompoundBorder：将两个边框组合成新边框
        CompoundBorder compoundBorder = new CompoundBorder(new LineBorder(Color.RED, 8), borderTitle);
        jFrame.add(getPanelWithBorder(compoundBorder, "CompoundBorder"));
        jFrame.pack();
        jFrame.setVisible(true);
    }

    /**
     * 获取JPanel.
     * 
     * @param border
     *            边框组件
     * @param borderName
     *            边框名字
     * @return JPanel
     */
    private JPanel getPanelWithBorder(Border border, String borderName) {
        JPanel panel = new JPanel(); // 中间容器
        panel.add(new JLabel(borderName));
        panel.setBorder(border); // 为pannel设置边框
        return panel;
    }
}

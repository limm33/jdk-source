/*
 * 文件名：JToolbarStudy.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： JToolbarStudy.java
 * 修改人：xiaofan
 * 修改时间：2017年3月8日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * 使用JToolBar创建工具条.
 * 
 * Copy-Paste
 * 
 * @author xiaofan
 */
public class JToolbarStudy {
    JFrame jFrame = new JFrame("zxiaofan.com Copy_Paste");

    /**
     * 文本域.
     */
    JTextArea textArea = new JTextArea(6, 38);

    JToolBar toolBar = new JToolBar();

    JMenuBar menuBar = new JMenuBar();

    JMenu menuEdit = new JMenu("Edit");

    /**
     * 获取系统剪贴板.
     */
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    /**
     * Paste-Action.
     */
    Action actionPaste = new AbstractAction("Paste") {

        @Override
        public void actionPerformed(ActionEvent e) {
            // 如果剪贴板包含stringFlavor内容
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板的stringFlavor
                    String content = (String) clipboard.getData(DataFlavor.stringFlavor);
                    // 替换选中内容为剪贴板内容
                    textArea.replaceRange(content, textArea.getSelectionStart(), textArea.getSelectionEnd());
                } catch (UnsupportedFlavorException | IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    };

    /**
     * Copy-Action.
     */
    Action actionCopy = new AbstractAction("Copy") {

        @Override
        public void actionPerformed(ActionEvent e) {
            StringSelection content = new StringSelection(textArea.getSelectedText());
            // 将StringSelection对象放入剪贴板
            clipboard.setContents(content, null);
            // 如果剪贴板中包含StringSelection内容
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                // 激活actionPaste
                actionPaste.setEnabled(true);
            }
        }
    };

    /**
     * 复制粘贴菜单.
     * 
     * @param args
     *            .
     */
    public static void main(String[] args) {
        new JToolbarStudy().init();
    }

    /**
     * init GUI.
     * 
     */
    public void init() {
        actionPaste.setEnabled(false); // 剪贴板默认不激活
        textArea.setLineWrap(true); // 激活自动换行功能
        textArea.setWrapStyleWord(true); // 激活断行不断字功能
        jFrame.add(new JScrollPane(textArea));
        // 用Action创建按钮，并将按钮添加到Panel中
        JButton copyBn = new JButton(actionCopy);
        JButton pasteBn = new JButton(actionPaste);
        JPanel panel = new JPanel();
        panel.add(copyBn);
        panel.add(pasteBn);
        jFrame.add(panel, BorderLayout.SOUTH);
        // 向工具条添加Action对象（该对象将转换成工具按钮）
        toolBar.add(actionCopy);
        toolBar.addSeparator();
        toolBar.add(actionPaste);
        // 向菜单中添加Action对象，该对象将转换成菜单项
        menuEdit.add(actionCopy);
        menuEdit.add(actionPaste);
        // 将菜单menuEdit添加到菜单条中
        menuBar.add(menuEdit);
        jFrame.setJMenuBar(menuBar);
        // 设置工具条和工具按钮间的页边距
        toolBar.setMargin(new Insets(20, 10, 5, 30)); // 上左下右
        // 向窗口添加工具条
        jFrame.add(toolBar, BorderLayout.NORTH);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }
}

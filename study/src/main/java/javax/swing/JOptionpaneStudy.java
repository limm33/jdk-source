/*
 * 文件名：JOptionpaneStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： JOptionpaneStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年3月10日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * JOptionpaneStudy创建简单的对话框.
 * 
 * @author zxiaofan
 */
public class JOptionpaneStudy {
    JFrame jFrame = new JFrame("JOptionpane By zxiaofan.com");

    // 定义6中对话框
    private ButtonPanel messagePanel;

    private ButtonPanel messageTypePanel;

    private ButtonPanel msgPanel;

    private ButtonPanel confirmPanel;

    private ButtonPanel optionsPanel;

    private ButtonPanel inputPanel;

    private String messageString = "消息区内容";

    private Object messageObject = new Date();

    private Component messageComponent = new JButton("组件消息");

    private JButton msgButton = new JButton("消息对话框");

    private JButton confirmButton = new JButton("确认对话框");

    private JButton inputButton = new JButton("输入对话框");

    private JButton optionButton = new JButton("选项对话框");

    public void init() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(new TitledBorder(new EtchedBorder(), "对话框通用选项", TitledBorder.CENTER, TitledBorder.TOP));
        topPanel.setLayout(new GridLayout(1, 2));
        // 消息类型Panel，该Panel中的选项决定对话框中的图标
        messagePanel = new ButtonPanel("选择消息的类型", new String[]{"Error_Message", "Information_Message", "Warning_Message", "Question_Message", "Plain_Message"});
        // 消息内容类型Panel，该Panel中的选项决定对话框消息区的内容
        messagePanel = new ButtonPanel("选择消息内容的类型", new String[]{"字符串消息", "图标消息", "组件消息"});
        topPanel.add(messageTypePanel);
        topPanel.add(messagePanel);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new TitledBorder(new EtchedBorder(), "弹出不同的对话框", TitledBorder.CENTER, TitledBorder.TOP));
        bottomPanel.setLayout(new GridLayout(1, 4));
        // 创建用于弹出消息对话框的Panel
        msgPanel = new ButtonPanel("消息对话框", null);
        msgButton.addActionListener(new ShowAction());
    }

    /**
     * 为各按钮定义时间监听器.
     * 
     * @author yunhai
     *
     */
    class ShowAction implements ActionListener {

        /**
         * {@inheritDoc}.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "确认对话框":
                    JOptionPane.showConfirmDialog(jFrame, getMessage(messagePanel));
                    break;
                case "输入对话框":
                    if ("单行文本框".equals(inputPanel.getSelection())) {
                        JOptionPane.showConfirmDialog(jFrame, getMessage(messagePanel), "输入对话框", getDialogType(messageTypePanel));
                    }
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * 根据用户选择返回消息类型（决定图标区 的图标）.
     * 
     * @return 消息类型
     */
    private int getDialogType(ButtonPanel typePanel) {
        switch (typePanel.getSelection()) {
            case "Error_Message":
                return JOptionPane.ERROR_MESSAGE;
            case "Waring_Message":
                return JOptionPane.WARNING_MESSAGE;
            case "Question_Message":
                return JOptionPane.QUESTION_MESSAGE;

            default:
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    /**
     * 根据用户选择返回消息.
     * 
     * @return 消息
     */
    private Object getMessage(ButtonPanel panel) {
        switch (panel.getSelection()) {
            case "字符串消息":
                return messageString;
            case "组件消息":
                return messageComponent;
            case "普通对象消息":
                return messageObject;
            default:
                return new Object[]{messageString, messageObject, messageComponent};
        }
    }
}

/**
 * JPanel扩展类ButtonPanel
 * 
 * ButtonPanel包含多个纵向排列的JRadioButton控件，且Panel扩展类可以指定一个字符串作为TitleBorder.
 * 
 * @author yunhai
 *
 */
class ButtonPanel extends JPanel {
    /**
     * Button集.
     */
    private ButtonGroup buttonGroup;

    /**
     * 构造函数.
     * 
     */
    public ButtonPanel(String title, String[] options) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        buttonGroup = new ButtonGroup();
        for (int i = 0; null != options && i < options.length; i++) {
            JRadioButton radioButton = new JRadioButton(options[i]);
            radioButton.setActionCommand(options[i]);
            add(radioButton);
            buttonGroup.add(radioButton);
            radioButton.setSelected(i == 0);
        }
    }

    /**
     * 返回用户选择选项.
     * 
     * @return selected
     */
    public String getSelection() {
        return buttonGroup.getSelection().getActionCommand();
    }

}
/*
 * 文件名：SwingBasicStudy.java
 * 版权：Copyright 2007-2017 517na Tech. Co. Ltd. All Rights Reserved. 
 * 描述： SwingBasicStudy.java
 * 修改人：xiaofan
 * 修改时间：2017年3月7日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.UIManager.LookAndFeelInfo;

import junit.framework.TestCase;

/**
 * SwingBasic.
 * 
 * Swing组件按功能划分：
 * 
 * 顶层容器：JFrame、JApplet、JDialog、JWindow
 * 
 * 中间容器：Jpanel、JScrollPane、JSplitPane、JToolBar
 * 
 * 特殊容器：JInternalFrame、JLayeredPane、JRootPane、JDestopPane
 * 
 * 基本组件：实现人机交互，JButton、JComboBox、JList、JMenu、JSlider
 * 
 * 不可编辑信息的显示组件：JTable、JTextArea、JTextField
 * 
 * 特殊对话框组件：JColorChooser、JFileChooser
 * 
 * JFrame相当与桌子，Jpanel相当桌布，JButton，JTable等相当于杯子，碗筷。杯子，碗筷虽可以直接放到桌子上但不规范，应该放到桌布上。
 * 
 * BorderLayout布局排列:
 * 
 * North
 * 
 * LineStart(WEST) Center LineEnd(EAST)
 * 
 * South
 * 
 * @author xiaofan
 */
public class SwingBasicStudy extends TestCase {
    /**
     * UIManager.
     * 
     * LAF:Look and Feel（界面外观）
     */
    public void testUIManager() {
        // 当前系统可用的LAF
        LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
        for (LookAndFeelInfo info : lookAndFeels) {
            System.out.println(info.getName() + ":" + info);
        }
    }

    JFrame jFrame = new JFrame("zxiaofan.com");

    // 定义按钮，指定图标
    Icon okIcon = new ImageIcon("");

    // JButton buttonOk = new JButton("OK", okIcon);
    JButton buttonOk = new JButton("OK");

    /**
     * 定义单选按钮，初始状态选中.
     */
    JRadioButton radioMale = new JRadioButton("Male", true);

    /**
     * 定义单选按钮，初始状态未选中.
     */
    JRadioButton radiofeMale = new JRadioButton("FeMale", false);

    /**
     * 定义ButtonGroup，组合以上两个性别JRadioButton.
     */
    ButtonGroup buttonGroup = new ButtonGroup();

    /**
     * 定义复选框，初始状态选中.
     */
    JCheckBox checkBoxProgrammer = new JCheckBox("是否是程序员", true);

    String[] colors = new String[]{"Red", "Blue", "Green"};

    /**
     * 定义下拉选择框.
     */
    JComboBox<String> colorChooser = new JComboBox<>(colors);

    /**
     * 定义列表选择框.
     */
    JList<String> colorList = new JList<>(colors);

    /**
     * 定义3行8列的多行文本域.
     */
    JTextArea textArea = new JTextArea(3, 8);

    /**
     * 定义9列的单行文本域.
     */
    JTextField textFieldName = new JTextField(9);

    JMenuBar menuBar = new JMenuBar(); // 创建菜单条

    JMenu menuFile = new JMenu("File"); // 创建菜单

    JMenu menuEdit = new JMenu("Edit");

    // 创建新建菜单选项，并制定图标
    // Icon iconNew = new ImageIcon("");

    // JMenuItem menuItemNew = new JMenuItem("New", iconNew);
    JMenuItem menuItemNew = new JMenuItem("New");

    /**
     * 创建保存菜单选项.
     */
    JMenuItem menuItemSave = new JMenuItem("Save");

    /**
     * 创建退出菜单选项.
     */
    JMenuItem menuItemExit = new JMenuItem("Exit");

    /**
     * 自动换行.
     */
    JCheckBoxMenuItem autoWrap = new JCheckBoxMenuItem("AutoWrap");

    /**
     * 创建复制菜单选项.
     */
    JMenuItem menuItemCopy = new JMenuItem("Copy");

    /**
     * 创建粘贴菜单选项.
     */
    JMenuItem menuItemPaste = new JMenuItem("Paste");

    JMenu menuFormat = new JMenu("Format"); // 菜单-格式

    JMenuItem menuItemComment = new JMenuItem("Comment"); // 注释

    JMenuItem menuItemCommentCancel = new JMenuItem("CommentCancel"); // 取消注释

    /**
     * 定义右键菜单，用于设置程序风格.
     */
    JPopupMenu popupMenu = new JPopupMenu(); // 下拉式菜单(menu)、弹出式菜单(JPopupMenu)、选项卡窗体(JTabbedPane)

    /**
     * 组合3个菜单风格选项的ButtonGroup.
     */
    ButtonGroup buttonGroupFlavor = new ButtonGroup();

    /**
     * 创建5个单选按钮，用于设定程序的外观风格.
     */
    JRadioButtonMenuItem itemMetal = new JRadioButtonMenuItem("Metal Style", true);

    JRadioButtonMenuItem itemNimbus = new JRadioButtonMenuItem("Nimbus Style");

    JRadioButtonMenuItem itemWindows = new JRadioButtonMenuItem("Windows Style");

    JRadioButtonMenuItem itemWindowsClassic = new JRadioButtonMenuItem("WindowsClassic Style");

    JRadioButtonMenuItem itemMotif = new JRadioButtonMenuItem("Motif Style");

    /**
     * Swing创建简单窗口应用.
     * 
     */
    public static void main(String[] args) {
        // 设置Swing窗口使用Java风格
        // JFrame.setDefaultLookAndFeelDecorated(true); // 比较丑
        new SwingBasicStudy().initSwing(); // init GUI：界面初始化
    }

    /**
     * init GUI：界面初始化.
     * 
     */
    private void initSwing() {
        // 创建一个装载了文本框、按钮的JPanel
        JPanel jPanel = new JPanel(); // 桌布
        jPanel.add(textFieldName);
        jPanel.add(buttonOk);
        jFrame.add(jPanel, BorderLayout.SOUTH);
        // 创建装载了下拉选择框、3个JCheckBox的JPanel
        JPanel panelCheck = new JPanel();
        panelCheck.add(colorChooser);
        buttonGroup.add(radioMale);
        buttonGroup.add(radiofeMale);
        panelCheck.add(radioMale);
        panelCheck.add(radiofeMale);
        panelCheck.add(checkBoxProgrammer);
        // 创建垂直排列组件的Box，放置多行文本域Jpanel
        Box boxTopLeft = Box.createVerticalBox();
        // 使用JScrollPane作为普通组件的JViewPort
        JScrollPane scrollPane = new JScrollPane(textArea);
        boxTopLeft.add(scrollPane);
        boxTopLeft.add(panelCheck);
        // 创建水平排列组件的Box，放置boxTopLeft、colorList
        Box boxTop = Box.createHorizontalBox();
        boxTop.add(boxTopLeft);
        boxTop.add(colorList);
        // 将boxTop添加到窗口中间
        jFrame.add(boxTop, BorderLayout.CENTER); // 必须指定BorderLayout

        // 组合菜单，并为菜单添加监听器
        // 为newItem设置快捷键，设置快捷键时需使用大写字母
        menuItemNew.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK)); // 快捷键CTRL+N
        // KeyStroke.getKeyStroke('n', InputEvent.CTRL_MASK)不代表CTRL+n
        menuItemNew.addActionListener(e -> textArea.append("用户单击了“新建”菜单\n"));
        // 为menuFile添加菜单项
        menuFile.add(menuItemNew);
        menuFile.add(menuItemSave);
        menuFile.add(menuItemExit);
        // 为menuEdit添加菜单项
        menuEdit.add(autoWrap);
        // 添加分隔线
        menuEdit.addSeparator();
        menuEdit.add(menuItemCopy);
        menuEdit.add(menuItemPaste);
        // 为menuItemComment添加提示信息
        menuItemComment.setToolTipText("注释代码");
        // 为menuFormat添加菜单项
        menuFormat.add(menuItemComment);
        menuFormat.add(menuItemCommentCancel);
        // 使用添加new JMenuItem("-")的方式不能添加菜单分隔符
        menuEdit.add(new JMenuItem("-"));
        // 将menuFormat菜单组合到menuEdit菜单中，从而形成二级菜单
        menuEdit.add(menuFormat);
        // 将menuFile、menuEdit菜单添加到menuBar菜单条
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        // 为jFrame设置菜单条
        jFrame.add(menuBar, BorderLayout.NORTH);
        // 组合右键菜单，并安装右键菜单
        buttonGroupFlavor.add(itemMetal);
        buttonGroupFlavor.add(itemNimbus);
        buttonGroupFlavor.add(itemWindows);
        buttonGroupFlavor.add(itemWindowsClassic);
        buttonGroupFlavor.add(itemMotif);
        popupMenu.add(itemMetal);
        popupMenu.add(itemNimbus);
        popupMenu.add(itemWindows);
        popupMenu.add(itemWindowsClassic);
        popupMenu.add(itemMotif);

        // 为5个风格菜单创建时间监听器
        ActionListener listenerFlavor = e -> {
            try {
                switch (e.getActionCommand()) {
                    case "Metal Style":
                        changeFlavor(1);
                        break;
                    case "Nimbus Style":
                        changeFlavor(2);
                        break;
                    case "Windows Style":
                        changeFlavor(3);
                        break;
                    case "WindowsClassic Style":
                        changeFlavor(4);
                        break;
                    case "Motif Style":
                        changeFlavor(5);
                        break;
                    default:
                        break;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        };
        // 为5个风格菜单项添加事件监听器
        itemMetal.addActionListener(listenerFlavor);
        itemMotif.addActionListener(listenerFlavor);
        itemNimbus.addActionListener(listenerFlavor);
        itemWindows.addActionListener(listenerFlavor);
        itemWindowsClassic.addActionListener(listenerFlavor);
        // 调用设置右键菜单，该方法无需使用事件机制
        textArea.setComponentPopupMenu(popupMenu);
        // 设置关闭窗口，退出程序
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    /**
     * 改变界面风格.
     * 
     * @param flavor
     *            flavor
     * @throws Exception
     *             异常
     */
    private void changeFlavor(int flavor) throws Exception {
        switch (flavor) {
            case 1:
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            case 2:
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                break;
            case 3:
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                break;
            case 4:
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                break;
            case 5:
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                break;

            default:
                break;
        }
        // 更新jFrame窗口内顶级容器及内部所有组件的UI
        SwingUtilities.updateComponentTreeUI(jFrame.getContentPane());
        // 更新menuBar菜单条及内部所有组件的UI
        SwingUtilities.updateComponentTreeUI(menuBar);
        // 更新popupMenu菜单条及内部所有组件的UI
        SwingUtilities.updateComponentTreeUI(popupMenu);
    }
}

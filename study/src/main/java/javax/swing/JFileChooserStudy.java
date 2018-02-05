/*
 * 文件名：JFileChooserStudy.java
 * 版权：Copyright 2007-2017 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： JFileChooserStudy.java
 * 修改人：zxiaofan
 * 修改时间：2017年3月9日
 * 修改内容：新增
 */
package javax.swing;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * @author zxiaofan
 */
public class JFileChooserStudy {
    /**
     * ImageViewer.
     * 
     * @param args
     *            args
     */
    public static void main(String[] args) {
        new JFileChooserStudy().init();
    }

    /**
     * 图片预览组大小.
     */
    final int PREVIEW_SIZE = 900;

    /**
     * 屏幕尺寸.
     */
    Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();

    JFrame jFrame = new JFrame("ImageViewer By zxiaofan.com");

    /**
     * 菜单栏组件.
     */
    JMenuBar menuBar = new JMenuBar();

    /**
     * 图片显示标签.
     */
    JLabel labelImage = new JLabel();

    /**
     * 以当前路径创建文件选择器.
     */
    JFileChooser fileChooser = new JFileChooser(".");

    JLabel accessoryLabel = new JLabel();

    /**
     * 文件过滤器.
     */
    JFileChooser fileFilter = new JFileChooser();

    /**
     * init GUI-ImageViewer.
     * 
     */
    public void init() {
        labelImage.setBorder(new EmptyBorder(2, 2, 2, 2));// 设置面板的边框
        initFileChooser(); // 初始化JFileChooser
        // 为窗口安装菜单
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItemOpen = new JMenuItem("Open");
        menu.add(menuItemOpen);
        ActionListener fileOpenListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 设置文件对话框为当前路径
                // fileChooser.setCurrentDirectory(new File("."));
                // 显示文件对话框
                int result = fileChooser.showDialog(jFrame, "Open");
                // 如果用户选择了APPROVE（同意）按钮，即打开
                if (result == JFileChooser.APPROVE_OPTION) {
                    String imagePath = fileChooser.getSelectedFile().getPath();
                    // 显示指定图片
                    ImageIcon icon = new ImageIcon(imagePath);
                    labelImage.setIcon(icon);
                }
            }
        };
        // 单击menuItemOpen菜单项显示“打开文件”对话框
        menuItemOpen.addActionListener(fileOpenListener);
        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        // 为退出绑定事件监听器
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        jFrame.setJMenuBar(menuBar);
        labelImage.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        labelImage.setBounds(10, 10, 20, 20);
        // 添加用于显示图片的JLabel组件
        jFrame.add(new JScrollPane(labelImage));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    /**
     * 初始化JFileChooser.
     * 
     */
    private void initFileChooser() {
        // 初始化一个FileFilter
        fileChooser.setMultiSelectionEnabled(false); // 不可多选
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // 只能选择文件
        fileChooser.setFileHidingEnabled(true);// 显示隐藏文件
        // 设置文件筛选器
        // fileChooser.setFileFilter(new MyFilter("zip"));
        // 限制文件类型
        String[] images = new String[]{"jpg", "jpeg", "gif", "png"};
        FileFilter filter = new FileNameExtensionFilter("Image File" + Arrays.asList(images).toString(), images);
        fileChooser.addChoosableFileFilter(filter);
        // 禁止文件类型下拉列表中显示“所有文件”类型
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setAccessory(accessoryLabel);
        // 设置预览图片组件的大小和边框
        accessoryLabel.setPreferredSize(new Dimension(PREVIEW_SIZE, PREVIEW_SIZE));
        accessoryLabel.setBorder(BorderFactory.createEtchedBorder());
        PropertyChangeListener changeListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // JFileChooser的被选文件发生改变
                if (evt.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
                    // 获取用户选择的新文件
                    File file = (File) evt.getNewValue();
                    if (null == file) {
                        accessoryLabel.setIcon(null);
                        return;
                    }
                    // 将所选文件读入ImageIcon对象中
                    ImageIcon icon = new ImageIcon(file.getPath());
                    // 如果图像太大，则缩小
                    if (icon.getIconWidth() > PREVIEW_SIZE) {
                        // if (icon.getIconWidth() > screensize.getWidth() || icon.getIconHeight() > screensize.getHeight()) {
                        // // getScaledInstance:Java图片缩放
                        // icon = new ImageIcon(icon.getImage().getScaledInstance((int) screensize.getWidth(), (int) screensize.getWidth() - 120, Image.SCALE_DEFAULT));
                        // } else {
                        icon = new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth(), -1, Image.SCALE_DEFAULT));
                        // }
                        // icon = new ImageIcon(icon.getImage().getScaledInstance(PREVIEW_SIZE, -1, Image.SCALE_DEFAULT));
                    }
                    // 改变accessory Label的图标
                    accessoryLabel.setIcon(icon);
                }
            }
        };
        // 用于检测被选择文件的改变事件
        fileChooser.addPropertyChangeListener(changeListener);
    }
}

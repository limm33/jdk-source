/*
 * 文件名：AnnoListVo.java
 * 版权：Copyright 2007-2016 zxiaofan.com. Co. Ltd. All Rights Reserved. 
 * 描述： AnnoListVo.java
 * 修改人：zxiaofan
 * 修改时间：2016年10月14日
 * 修改内容：新增
 */
package java1.lang.annotation;

import java.util.List;

import java1.lang.annotation.validation.StringCut;

/**
 * 
 * @author zxiaofan
 */
@StringCut
public class AnnoListVo {
    private List<AnnoVo> listAnnovos;

    /**
     * 设置listAnnovos.
     * 
     * @return 返回listAnnovos
     */
    public List<AnnoVo> getListAnnovos() {
        return listAnnovos;
    }

    /**
     * 获取listAnnovos.
     * 
     * @param listAnnovos
     *            要设置的listAnnovos
     */
    public void setListAnnovos(List<AnnoVo> listAnnovos) {
        this.listAnnovos = listAnnovos;
    }
}

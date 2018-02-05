package java1.lang.annotation;

import java.util.Map;

public class Request<T> {

    /**
     * 添加字段注释.
     */
    private String trackID;

    /**
     * 添加字段注释.
     */
    // @StringCut(isGeneric = "obj")
    private T obj;

    /**
     * 添加字段注释.
     */
    private Map<String, Object> map;

    /**
     * 设置trackID.
     * 
     * @return 返回trackID
     */
    public String getTrackID() {
        return trackID;
    }

    /**
     * 获取trackID.
     * 
     * @param trackID
     *            要设置的trackID
     */
    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    /**
     * 设置obj.
     * 
     * @return 返回obj
     */
    public T getObj() {
        return obj;
    }

    /**
     * 获取obj.
     * 
     * @param obj
     *            要设置的obj
     */
    public void setObj(T obj) {
        this.obj = obj;
    }

    /**
     * 设置map.
     * 
     * @return 返回map
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * 获取map.
     * 
     * @param map
     *            要设置的map
     */
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

}

package opera;

import java.util.List;
import java.util.Map;

/** 树的节点：表示一个标签
 * Created by ctis-szx on 2017/8/4.
 */
public class TagNode {

    private String tagName; //标签名
    private Map<String, Object> tagProperty; //标签属性Map, map的值可能为String,也可能为map
    private String tagValue; //标签的值
    private List<TagNode> childTagNode; //子标签节点集合
    private boolean isEndTag; //当前标签是否是结束标签
    private boolean hasMaped; //判断当前标签是否已匹配
    @Override
    public String toString() {
        return "opera.TagNode{" +
                "tagName='" + tagName + '\'' +
                ", tagProperty=" + tagProperty +
                ", tagValue='" + tagValue + '\'' +
                ", childTagNode=" + childTagNode +
                ", isEndTag=" + isEndTag +
                '}';
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Map<String, Object> getTagProperty() {
        return tagProperty;
    }

    public void setTagProperty(Map<String, Object> tagProperty) {
        this.tagProperty = tagProperty;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public List<TagNode> getChildTagNode() {
        return childTagNode;
    }

    public void setChildTagNode(List<TagNode> childTagNode) {
        this.childTagNode = childTagNode;
    }

    public boolean isEndTag() {
        return isEndTag;
    }

    public void setEndTag(boolean isEndTag) {
        this.isEndTag = isEndTag;
    }

    public boolean isHasMaped() {
        return hasMaped;
    }

    public void setHasMaped(boolean hasMaped) {
        this.hasMaped = hasMaped;
    }
}

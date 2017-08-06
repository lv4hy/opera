package opera;

/**
 * Created by zhixin on 2017/8/6.
 */
public class DocTypeNode {
    private String tagName;
    private String tagValue;
    private TagNode childNode;

    @Override
    public String toString() {
        return "opera.DocTypeNode{" +
                "tagName='" + tagName + '\'' +
                ", tagValue='" + tagValue + '\'' +
                ", childNode=" + childNode +
                '}';
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public TagNode getChildNode() {
        return childNode;
    }

    public void setChildNode(TagNode childNode) {
        this.childNode = childNode;
    }
}

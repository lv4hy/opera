package opera;

/**
 * Created by ctis-szx on 2017/8/4.
 */
public class TagTree {

    private TagNode root; //树的根节点
    private Integer treeHeight; //树的高度

    @Override
    public String toString() {
        return "opera.TagTree{" +
                "root=" + root +
                ", treeHeight=" + treeHeight +
                '}';
    }

    public TagNode getRoot() {
        return root;
    }

    public void setRoot(TagNode root) {
        this.root = root;
    }

    public Integer getTreeHeight() {
        return treeHeight;
    }

    public void setTreeHeight(Integer treeHeight) {
        this.treeHeight = treeHeight;
    }
}

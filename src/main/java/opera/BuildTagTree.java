package opera;

import java.io.*;
import java.util.*;

/**整体思想：根据输入的html页面，构造一棵标签树:
 * 程序思路：
 * 第一步：构造一个可以表示标签的树节点结构:opera.TagNode，因为html文件的首行是不同的结构，所以构建一个头结点: opera.DocTypeNode
 * 第二步：根据输入的html文件，逐行遍历文件,首先判断是否是注释行(单行/多行注释)，如果是注释行,继续下一行,否则转到步骤3
 * 第三步：根据输入行，构建标签节点，该行有可能只有一个标签，也有可能包含多个标签，要分情况得到各个标签的标签名，标签属性以及标签值
 * 第四步：得到标签节点后，需要对该标签进行处理，以得到该标签节点的子节点，这一步需要将以及得到匹配的节点进行标记，并入栈。
 * 第五步：将主栈最后的元素即为html的根节点，将该节点赋给头节点即可。
 * Created by szx on 2017/8/4.
 */
public class BuildTagTree {

    private static String COMMENT_START = "<!--";
    private static String COMMENT_END = "-->";
    private static String TAG_START = "<";
    private static String TAG_END = ">";
    private static String DOCUMENT_DECLARE ="!DOCTYPE";

    private Stack<TagNode> nodeStack = new Stack<>(); //辅助栈

    private Stack<String> commentStack = new Stack<>(); //判断多行注释的辅助栈

    private DocTypeNode head = new DocTypeNode();

    /**
     * 构造标签树主程序入口
     * @return html的头结点
     */
    public DocTypeNode buildTagTree(String filePath)throws Exception{
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()){
                FileInputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = reader.readLine()) != null){
                    lineParse(line);
                }
                reader.close();
                head.setChildNode(nodeStack.pop());
                return head;
            }else {
                throw new FileNotFoundException("给定的路径不存在，或文件有误");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 深入优先遍历多叉树
     * @param head
     */
    public StringBuilder traverseTagTree(DocTypeNode head){
        if(head == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        Stack<TagNode> traverseStack = new Stack<>();
        TagNode root = head.getChildNode();
        TagNode tmp;
        if(root != null){
            traverseStack.push(root);
        }
        while(!traverseStack.isEmpty()){
            StringBuilder sbTemp = new StringBuilder();
            tmp = traverseStack.pop();
            sbTemp = visit(tmp);
            //将sbTemp插入sb的中间
            if(tmp.getChildTagNode() != null){
                for(int i = 0; i < tmp.getChildTagNode().size(); i++){
                    traverseStack.push(tmp.getChildTagNode().get(i));
                }
            }
        }
        return sb;
    }


    /**
     * 重新渲染Html, 未完，待做
     * 思路：将数据结构按深度优先遍历，不断将新标签插入已有标签序列中部即可。
     * @param head
     */

    public void renderDom(DocTypeNode head){
        try{
            if(head == null){
                return;
            }
            StringBuilder sb =  new StringBuilder();
            File file = new File("src/myhtml.html");
            file.createNewFile();
            sb = traverseTagTree(head);
            PrintStream printStream = new PrintStream(new FileOutputStream(file));
            printStream.println(sb.toString());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 访问当前节点
     * @param node
     */
    private StringBuilder visit(TagNode node){
        if(node == null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<"+node.getTagName());
        if(node.getTagProperty().size() > 0){
            for(Map.Entry entry : node.getTagProperty().entrySet()){
                sb.append(entry.getKey());
                sb.append("=");
                sb.append("\"");
                sb.append(entry.getValue());
                sb.append("\"");
                sb.append(" ");
            }
        }
        sb.append(">");
        if(node.getTagValue() != null){
            sb.append(node.getTagValue());
        }
        sb.append("</").append(node.getTagName()).append(">");
        return sb;
    }

    /**
     * 对输入的行进行解析
     * @param line
     */
    private void lineParse(String line){
        try {
            if(null == line || line.trim().equals("")){
                return;
            }
            if(isCommentLine(line)){
                return;
            }
            if(isDocumentDeclareLine(line)){
                head = getDocTypeNode(line);
                return;
            }
            buildTagNode(line);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 判断当前行是否是文件头
     * @param line
     * @return
     */
    private boolean isDocumentDeclareLine(String line){
        String tagLine = line.trim();
        if(tagLine != null && tagLine.contains(DOCUMENT_DECLARE)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据当前行，构造html标签树头节点
     * @param line
     * @return
     * @throws Exception
     */
    private DocTypeNode getDocTypeNode(String line) throws Exception {
        String tagLine = line.trim();
        DocTypeNode docNode = new DocTypeNode();
        int tagStartIndex = tagLine.indexOf(TAG_START);
        int tagEndIndex = tagLine.indexOf(TAG_END);
        int k = tagStartIndex;
        if(tagStartIndex > -1 && tagEndIndex > 0){
            while(k < tagEndIndex){
                if(!" ".equals(Character.toString(line.charAt(k)))){
                    k++;
                }
                docNode.setTagName(line.substring(tagStartIndex + 1, k));
                while(" ".equals(Character.toString(line.charAt(k)))){
                    k++;
                }
                docNode.setTagValue(line.substring(k, tagEndIndex));
                docNode.setChildNode(null);
            }
        }else {
            throw new Exception("html 标签格式有误, line"+tagLine);
        }
        return docNode;
    }
    /**
     * 判断当前行是否是注释
     * @param line
     * @return
     */
    private boolean isCommentLine(String line){
        //单行注释
        if(line.trim().startsWith(COMMENT_START) && line.trim().endsWith(COMMENT_END)){
            return true;
        }
        //多行注释开始
        if(line.trim().startsWith(COMMENT_START)){
            commentStack.push(COMMENT_START);
        }
        //多行注释结束
        if(line.trim().endsWith(COMMENT_END)){
            commentStack.pop();
        }
        if(!commentStack.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    /**
     *解析当前行，构造标签节点
     * @Param line 当前输入行字符串
     * */

    private void buildTagNode(String line){
        try {
            String tagStringTmp = null;
            String value = null;
            String tagLine = line.trim();

            int tagStartIndex = tagLine.indexOf(TAG_START);
            int tagEndIndex = tagLine.indexOf(TAG_END);

            if(tagStartIndex >= 0 && tagEndIndex > 0){

                if(tagEndIndex == tagLine.length() - 1){//一行只有一个标签
                    TagNode tagNode = getTagNode(tagLine);
                    handleNode(tagNode);
                }else if(tagEndIndex < tagLine.length()){//一行有多个标签,分别解析每个标签
                    int start = 0;
                    int end = 0;
                    while(start < tagLine.length()){
                        start = tagLine.indexOf(TAG_START, start);
                        end = tagLine.indexOf(TAG_END, start);
                        if(start > -1 && end > -1){
                            if(end >= tagLine.length() - 1){
                                tagStringTmp = tagLine.substring(start, end);
                                tagStringTmp = tagStringTmp + ">";
                            }else {
                                tagStringTmp = tagLine.substring(start, end + 1);
                            }
                            TagNode node = getTagNode(tagStringTmp);
                            if(node.getTagName().startsWith("/")){//处理标签值
                                int valueEndIndex = start;
                                int s = valueEndIndex;
                                while(s >= 0 ){
                                    if(!TAG_END.equals(Character.toString(tagLine.charAt(s)))){
                                        s--;
                                    }else {
                                        break;
                                    }
                                }
                                if(s > 0 && (valueEndIndex - s) > 1){
                                    value = tagLine.substring(s + 1, valueEndIndex);
                                    TagNode tmp = nodeStack.peek();
                                    if(tmp.getTagName().equals(node.getTagName().substring(1))){
                                        tmp.setTagValue(value);
                                    }
                                }
                            }
                            handleNode(node);
                            start = end + 1;
                        } else if(start == -1 && end == -1 && start < tagLine.length()){//得到标签值
                            value = tagLine.substring(start, tagLine.length());
                            TagNode tmp = nodeStack.peek();
                            tmp.setTagValue(value);
                            break;
                        }else {
                            break;
                        }
                    }
                }
            }else {
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理得到的结点，得到该结点的子节点，如果是div标签需要单独处理，需借助两个辅助栈
     * @param tagNode 待处理的节点
     */
    private void handleNode(TagNode tagNode){
        List<TagNode> childNodeList = new ArrayList<>();
        List<TagNode> tmp = new ArrayList<>();
        if(tagNode != null && tagNode.isEndTag()){//结束标签
            while(!nodeStack.isEmpty()){
                TagNode node = nodeStack.pop();
                if(!node.isHasMaped() && node.getTagName().equals(tagNode.getTagName().substring(1))){
                    node.setChildTagNode(childNodeList);
                    node.setHasMaped(true);
                    nodeStack.push(node);
                    break;
                }else {
                    childNodeList.add(node);//出栈，并保存
                }
            }
        }else if(tagNode != null){//非结束标签，且不是div标签，直接入栈
            nodeStack.push(tagNode);
        }
    }


    /**
     * 根据输入的字符串，构造一个标签
     * @param tagString
     * @return
     */
    private TagNode getTagNode(String tagString){
        if(tagString == null || "".equals(tagString)){
            return null;
        }
        TagNode tagNode = new TagNode();
        tagNode.setTagName(getTagName(tagString));
        tagNode.setTagProperty(getTagProp(tagString));
        tagNode.setTagValue(getTagValue(tagString));
        tagNode.setEndTag(endTagOrNot(tagNode.getTagName()));
        tagNode.setChildTagNode(null);
        return tagNode;
    }

    /**
     * 得到标签名
     * @param line 输入的标签字符串
     * @return
     */
    private String getTagName(String line){
        String tagName = null;
        int tagStartIndex = line.indexOf(TAG_START);
        int tagEndIndex = line.indexOf(TAG_END);
        int k = tagStartIndex;

        while(k < tagEndIndex){
            if(!" ".equals(Character.toString(line.charAt(k)))){
                k++;
            }else {
                if(k < tagEndIndex){
                    tagName = line.substring(tagStartIndex + 1, k);
                    break;
                }
            }
        }
        if(k == tagEndIndex){
            tagName = line.substring(tagStartIndex + 1, tagEndIndex);
        }
        if(tagName != null){
            return tagName;
        }else {
            throw new IllegalArgumentException("标签解释异常,line = "+line);
        }
    }

    /**
     * 得到标签的属性
     * @param line 标签字符串
     * @return
     */
    private Map<String, Object> getTagProp(String line){
        Map<String, Object> tagPropMap = new HashMap<>();
        String propKey = null;
        String propValue = null;
        int tagStartIndex = line.indexOf(TAG_START);
        int tagEndIndex = line.indexOf(TAG_END);
        int k = tagStartIndex;
        while(k < tagEndIndex){
            if(!" ".equals(Character.toString(line.charAt(k)))){
                k ++;
            }else {
                if(k < tagEndIndex){
                    while (" ".equals(Character.toString(line.charAt(k)))){
                        k++;
                    }
                    int t = k;
                    int equalIndex = line.indexOf("=", k);
                    int z = equalIndex;
                    while (t < tagEndIndex && !" ".equals(Character.toString(line.charAt(t)))){
                        t ++;
                    }
                    if(t < equalIndex){
                        z = t;
                    }
                    if(equalIndex > 0 && z < tagEndIndex){
                        propKey = line.substring(k, z);
                    }else {
                        if(equalIndex < 0 || z > tagEndIndex){
                            throw new IllegalArgumentException("该行标签有误, line= "+line);
                        }
                    }
                    int quotFirstIndex = line.indexOf("\"", equalIndex);
                    int quotSecondIndex = line.indexOf("\"", quotFirstIndex + 1);
                    if(quotFirstIndex > 0 && quotSecondIndex > 0 && quotFirstIndex < tagEndIndex && quotSecondIndex < tagEndIndex){
                        propValue = line.substring(quotFirstIndex + 1, quotSecondIndex);
                    }
                    if(propKey != null && propValue != null){
                        tagPropMap.put(propKey, propValue);
                        k = quotSecondIndex + 1;
                    }
                }
            }
        }
        return tagPropMap;
    }

    /**
     * 得到标签的值
     */
    private String getTagValue(String line){
        if(line == null){
            return null;
        }
        String value = null;
        int valueStartIndex = line.indexOf(TAG_END);
        int valueEndIndex = line.indexOf(TAG_START, valueStartIndex + 1);
        if(valueStartIndex > -1 && valueEndIndex >-1 && valueEndIndex < line.length()){
            value = line.substring(valueStartIndex, valueEndIndex + 1);
        }
        return value;
    }

    /**
     * 根据标签名，判断当前标签是否是结束标签
     * @param tagName
     * @return true：结束标签，false：非结束标签
     */
    private boolean endTagOrNot(String tagName){
        return tagName != null && tagName.startsWith("/");
    }


}

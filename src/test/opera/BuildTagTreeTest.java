package opera;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

import static org.junit.Assert.*;

public class BuildTagTreeTest {

    private static String FILE_PATH ="src/temp.html";

    //@Test
    public void testBuildTagTree(){
        try {
            BuildTagTree tagTree =  new BuildTagTree();
            DocTypeNode head = null;
            head = tagTree.buildTagTree(FILE_PATH);
            System.out.println(head);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testTraverseTagTree(){
        try {
            BuildTagTree tagTree =  new BuildTagTree();
            DocTypeNode head = tagTree.buildTagTree(FILE_PATH);
            tagTree.traverseTagTree(head);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testRenderDom(){
        //未做完，待做
    }
}
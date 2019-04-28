import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {

    private SimpleNode root;
    private PrintWriter output;
    private StringBuilder builder;

    public CodeGenerator(SimpleNode root) {
        this.root = (SimpleNode) root.children[0];

        this.builder = new StringBuilder();

        String filename = this.root.value + ".j";

        FileWriter fileWriter;
		try {
            fileWriter = new FileWriter(filename, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            this.output = new PrintWriter(bufferedWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
    }

    public void generate() {
        generateHeader();
        generateGlobals();
        generateMethods();

        // outputs the final string builded to the file
        output.println(builder);
    }

    private void generateHeader() {
        builder.append(".class public ");
        builder.append(root.jjtGetValue());
        builder.append("\n");
        builder.append(".super java/lan/Object");
        builder.append("\n");
    }

    private void generateGlobals() {

        for (int i = 0; i < root.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) root.jjtGetChild(i);

            // if()
        }
    }

    private void generateMethods() {

        for (int i = 0; i < root.jjtGetNumChildren(); i++) {
            SimpleNode childRoot = (SimpleNode) root.jjtGetChild(i);

            // if()
        }
    }

    private void generateOperations() {
        
    }
}

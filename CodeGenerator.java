import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {
    private SimpleNode root;
    private PrintWriter out;
    private StringBuilder builder;
    private SimpleNode node;

    public CodeGenerator(SimpleNode root) {
        this.root = root.getChild(0);
        this.builder = new StringBuilder();
        this.node = this.root.next();
        String filename = this.node.getName() + ".j";

		try {
            FileWriter fileWriter = new FileWriter(filename, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            this.out = new PrintWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
        }
    }

    public void generate() {
        generateHeader();
        generateGlobals();
        // outputs the final string builded to the file
        out.println(this.builder);
        out.close();
    }

    private void generateHeader() {
        write(".class public ");
        write(this.node.getName());
        nl();

        this.node = this.root.next();

        if(this.node.getName().equals("extends")){
            write(".super ");
            write(this.node.getChild(0).getName());
            nl();
        } else {
            write(".super java/lan/Object");
            nl();
        }
    }

    private void generateGlobals() {
        for(int i = 0; i < this.root.jjtGetNumChildren(); i++) {

            if(this.root.getChild(i).getName().equals("varDeclaration")) {
                generateVarDeclaration((ASTvarDeclaration) this.root.getChild(i));
            }
        }  
    }

    private void generateVarDeclaration(ASTvarDeclaration varDeclaration) {
        
        switch(varDeclaration.getChild(0).getName()){
            case "int":
                break;
            case "boolean":
                break;
            default:
            break;
        }

    }

    private void nl(){
        this.builder.append("\n");
    }

    private void write(String content){
        this.builder.append(content);
    }
}

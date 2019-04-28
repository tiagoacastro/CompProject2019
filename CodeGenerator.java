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
        nl();

        if(generateGlobals())
            nl();

        generateMethods();

        // outputs the final string builded to the file
        out.println(this.builder);
        out.close();
    }

    private void generateHeader() {
        write(".class public ");
        write(this.node.getName());
        nl();

        SimpleNode next = this.root.next();

        if(next.getName().equals("extends")){
            write(".super ");
            write(next.next().getName());
        } else {
            write(".super java/lan/Object");
        }

        nl();
    }

    private boolean generateGlobals() {
        SimpleNode next;
        boolean has = false;

        while((next = this.root.next()).getName().equals("varDeclaration")) {
            generateGlobalDeclaration(next);
            has = true;
        }  

        this.root.previous();
        return has;
    }

    private void generateGlobalDeclaration(SimpleNode var) {
        write(".field static ");

        var.next();
        write(var.next().getName());

        switch(var.previous().getName()){
            case "int":
                write(" I");
                break;
            case "boolean":
                write(" Z");
                break;
        }

        nl();
    }

    private void generateMethods(){
        generateConstructor();
    }

    private void generateConstructor(){
        write(".method <init>()V");
        nl();
        write(".limit stack 1");
        nl();
        write(".limit locals 1");
        nl();
        write(".var0 is this L");
        write(this.node.getName());
        write("; from Label0 to Label1");
        nl();
        write("Label 0:");
        nl();
        write("aload_0");
        nl();
        write("invokespecial java/lang/Object/<init>()V");
        nl();
        write("Label 1:");
        nl();
        write("return");
        nl();
        write(".end method");
        nl();
    }

    private void nl(){
        this.builder.append("\n");
    }

    private void space(){
        this.builder.append(" ");
    }

    private void write(String content){
        this.builder.append(content);
    }
}

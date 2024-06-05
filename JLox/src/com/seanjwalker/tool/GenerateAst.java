package com.seanjwalker.tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Generates a .java file containing an AST composed by an abstract class and nested subclasses
 */
public class GenerateAst {
    /**
     * Entrypoint to generate AST
     * @param args contains 1 argument which is the output directory
     * @throws IOException if there is an error writing to output file
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }

        String outputDirectory = args[0];

        defineAst(outputDirectory, "Expression", Arrays.asList(
                "Binary   : Expression left, Token operator, Expression right",
                "Grouping : Expression expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expression right"
        ));
    }

    /**
     * Writes to the file, defining the AST
     * @param outputDir the directory to write the file to
     * @param baseName the name of the parent class
     * @param types String representations of the signatures of the subclasses
     * @throws IOException if there is an error writing to file
     */
    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        File outputFile = new File(outputDir + '/' + baseName + ".java");
        PrintWriter writer = new PrintWriter(outputFile, StandardCharsets.UTF_8);

        writer.println("package com.seanjwalker.lox.model;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method
        writer.println();
        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");

        writer.close();
    }

    /**
     * Defines a Visitor interface for the visitor pattern
     * @param writer a PrintWriter used to write the interface
     * @param baseName the name of the root of the AST
     * @param types the names of the types being added to
     */
    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("        R visit" + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("    }");
    }


    /**
     * Creates a type in the AST as a class
     * @param writer the PrintWriter used to write out the class definition
     * @param baseName the name of the root of the AST
     * @param className the name of the class to create
     * @param fieldList the fields used to construct the type, as a single String
     */
    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");

        // Visitor pattern.
        writer.println();
        writer.println("        @Override");
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit(this);");
        writer.println("        }");

        // Fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            writer.println("        final " + field + ";");
        }

        // constructor
        writer.println();
        writer.println("        " + className + '(' + fieldList + ") {");

        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ';');
        }

        writer.println("        }");
        writer.println("    }");
    }
}

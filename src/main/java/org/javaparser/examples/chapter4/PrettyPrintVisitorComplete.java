package org.javaparser.examples.chapter4;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.printer.DefaultPrettyPrinter;
import com.github.javaparser.printer.configuration.DefaultPrinterConfiguration;

public class PrettyPrintVisitorComplete {

    public static void main(String[] args) {
        ClassOrInterfaceDeclaration myClass = new ClassOrInterfaceDeclaration();
        myClass.setComment(new LineComment("A very cool class!"));
        myClass.setName("MyClass");
        myClass.addField("String", "foo");
        myClass.addAnnotation("MySecretAnnotation");

        DefaultPrinterConfiguration conf = new DefaultPrinterConfiguration();
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(conf);
        System.out.println(prettyPrinter.print(myClass));
    }

}

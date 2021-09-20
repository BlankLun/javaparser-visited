package org.javaparser.examples.chapter6;

import com.github.javaparser.Range;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nullable;
import java.io.FileInputStream;

public class CallMethodVisitorComplete {

    private static final String FILE_PATH = "src/main/java/org/javaparser/samples/ReversePolishNotation.java";

    public static void main(String[] args) throws Exception {

        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));

        VoidVisitor<Void> methodCallVisitor = new MethodCallPrinter();
        methodCallVisitor.visit(cu, null);
    }

    private static class MethodCallPrinter extends VoidVisitorAdapter<Void> {

        @Override
        public void visit(MethodCallExpr mc, Void arg) {
            super.visit(mc, arg);
            Range range = mc.getRange().orElse(Range.range(-1, -1, -1, -1));
            int startLine = range.begin.line;
            int endLine = range.end.line;

            MethodDeclaration methodDeclaration = funcCalledByMethod(mc);
            ClassOrInterfaceDeclaration classDeclaration = funcCalledByClass(methodDeclaration);

            System.out.printf("Method call Printed -> method %s called in %s.%s() line [%d, %d]\n",
                    mc.getName().getIdentifier(), classDeclaration == null ? "" : classDeclaration.getName().getIdentifier(),
                    methodDeclaration == null ? "" : methodDeclaration.getName().getIdentifier(), startLine, endLine);
        }

        /**
         * 查找节点在哪个函数里调用
         *
         * @param node 节点
         * @return 函数声明
         */
        private MethodDeclaration funcCalledByMethod(@Nullable Node node) {
            if (node == null) {
                return null;
            }

            Node parentNode = node.getParentNode().get();
            if (parentNode instanceof MethodDeclaration) {
                return (MethodDeclaration) parentNode;
            } else {
                return funcCalledByMethod(parentNode);
            }
        }

        /**
         * 查找节点在哪个类或接口里
         *
         * @param node 节点
         * @return 类或接口声明
         */
        private ClassOrInterfaceDeclaration funcCalledByClass(@Nullable Node node) {
            if (node == null) {
                return null;
            }

            Node parentNode = node.getParentNode().get();
            if (parentNode instanceof ClassOrInterfaceDeclaration) {
                return (ClassOrInterfaceDeclaration) parentNode;
            } else {
                return funcCalledByClass(parentNode);
            }
        }
    }
}

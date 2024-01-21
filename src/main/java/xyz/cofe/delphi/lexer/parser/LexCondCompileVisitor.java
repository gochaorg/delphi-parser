// Generated from C:/code/other/delphi-parser/src/main/resources/LexCondCompile.g4 by ANTLR 4.13.1
package xyz.cofe.delphi.lexer.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LexCondCompileParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LexCondCompileVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(LexCondCompileParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#logicOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicOp(LexCondCompileParser.LogicOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#andOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndOp(LexCondCompileParser.AndOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#relOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelOp(LexCondCompileParser.RelOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#unaryOp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOp(LexCondCompileParser.UnaryOpContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtom(LexCondCompileParser.AtomContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#postfix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostfix(LexCondCompileParser.PostfixContext ctx);
	/**
	 * Visit a parse tree produced by {@link LexCondCompileParser#call}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCall(LexCondCompileParser.CallContext ctx);
}
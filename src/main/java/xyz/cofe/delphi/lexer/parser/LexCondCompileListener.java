// Generated from C:/code/other/delphi-parser/src/main/resources/LexCondCompile.g4 by ANTLR 4.13.1
package xyz.cofe.delphi.lexer.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LexCondCompileParser}.
 */
public interface LexCondCompileListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(LexCondCompileParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(LexCondCompileParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#logicOp}.
	 * @param ctx the parse tree
	 */
	void enterLogicOp(LexCondCompileParser.LogicOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#logicOp}.
	 * @param ctx the parse tree
	 */
	void exitLogicOp(LexCondCompileParser.LogicOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#andOp}.
	 * @param ctx the parse tree
	 */
	void enterAndOp(LexCondCompileParser.AndOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#andOp}.
	 * @param ctx the parse tree
	 */
	void exitAndOp(LexCondCompileParser.AndOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#relOp}.
	 * @param ctx the parse tree
	 */
	void enterRelOp(LexCondCompileParser.RelOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#relOp}.
	 * @param ctx the parse tree
	 */
	void exitRelOp(LexCondCompileParser.RelOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOp(LexCondCompileParser.UnaryOpContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#unaryOp}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOp(LexCondCompileParser.UnaryOpContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(LexCondCompileParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(LexCondCompileParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#postfix}.
	 * @param ctx the parse tree
	 */
	void enterPostfix(LexCondCompileParser.PostfixContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#postfix}.
	 * @param ctx the parse tree
	 */
	void exitPostfix(LexCondCompileParser.PostfixContext ctx);
	/**
	 * Enter a parse tree produced by {@link LexCondCompileParser#call}.
	 * @param ctx the parse tree
	 */
	void enterCall(LexCondCompileParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by {@link LexCondCompileParser#call}.
	 * @param ctx the parse tree
	 */
	void exitCall(LexCondCompileParser.CallContext ctx);
}
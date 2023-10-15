package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.Fn1;
import xyz.cofe.coll.im.Fn2;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.*;

import java.util.Optional;

/**
 * <a href="https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Procedural_Types_(Delphi)">Процедурный тип</a>
 * <p/>
 *
 * Может быть
 * <ul>
 *     <li>Указателем на функцию</li>
 *     <li>Лямбдой</li>
 * </ul>
 */
public sealed interface ProcedureTypeAst extends TypeDeclAst {
    record FunctionType(
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        Optional<CallConvention> callConvention,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<FunctionType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(result).prepend(upcast(arguments));
        }

        @Override
        public FunctionType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            var res = result.astUpdate(updateCtx);
            if( res==result && args.isEmpty() )return this;

            return new FunctionType(args.orElse(arguments), res, callConvention, position);
        }
    }

    record ProcedureType(
        ImList<ArgumentAst> arguments,
        Optional<CallConvention> callConvention,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<ProcedureType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments);
        }

        @Override
        public ProcedureType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            if( args.isEmpty() )return this;

            return new ProcedureType(args.orElse(arguments),callConvention,position);
        }
    }

    record ObjFunctionType(
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<ObjFunctionType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(result).prepend(upcast(arguments));
        }

        @Override
        public ObjFunctionType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            var res = result.astUpdate(updateCtx);
            if( res==result && args.isEmpty() )return this;

            return new ObjFunctionType(args.orElse(arguments),res,position);
        }
    }

    record ObjProcedureType(
        ImList<ArgumentAst> arguments,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<ObjProcedureType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments);
        }

        @Override
        public ObjProcedureType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            if( args.isEmpty() )return this;

            return new ObjProcedureType(args.orElse(arguments), position);
        }
    }

    record RefFunctionType(
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<RefFunctionType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(result).prepend(upcast(arguments));
        }

        @Override
        public RefFunctionType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            var res = result.astUpdate(updateCtx);
            if( res==result && args.isEmpty() )return this;

            return new RefFunctionType(args.orElse(arguments),res,position);
        }
    }
    record RefProcedureType(
        ImList<ArgumentAst> arguments,
        SourcePosition position
    ) implements ProcedureTypeAst, AstUpdate<RefProcedureType>, SrcPos {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(arguments);
        }

        @Override
        public RefProcedureType astUpdate(AstUpdate.UpdateContext updateCtx) {
            var args = AstUpdate.astUpdates(arguments,updateCtx);
            if( args.isEmpty() )return this;

            return new RefProcedureType(args.orElse(arguments),position);
        }
    }

    enum CallConvention {
        Cdecl,
        Pascal,
        Register,
        SafeCall,
        StdCall,
        Export;

        public static CallConvention of(DelphiParser.CallConventionNoSemiContext ctx){
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            if( ctx.getText().equalsIgnoreCase("cdecl") )return CallConvention.Cdecl;
            if( ctx.getText().equalsIgnoreCase("Pascal") )return CallConvention.Pascal;
            if( ctx.getText().equalsIgnoreCase("Register") )return CallConvention.Register;
            if( ctx.getText().equalsIgnoreCase("SafeCall") )return CallConvention.SafeCall;
            if( ctx.getText().equalsIgnoreCase("StdCall") )return CallConvention.StdCall;
            if( ctx.getText().equalsIgnoreCase("Export") )return CallConvention.Export;
            throw AstParseError.unExpected(ctx);
        }
    }

    private static <R> R of(
        DelphiParser.ProcedureTypeHeadingContext ctx,
        Fn2<R,ImList<ArgumentAst>,TypeDeclAst> funMatch,
        Fn1<R,ImList<ArgumentAst>> procMatch
    ) {
        ImList<ArgumentAst> args = ctx.formalParameterSection()!=null
            && ctx.formalParameterSection().formalParameterList()!=null
            && !ctx.formalParameterSection().formalParameterList().isEmpty() ?
            ArgumentAst.of(ctx.formalParameterSection().formalParameterList()) : ImListLinked.<ArgumentAst>of();

        if( ctx.FUNCTION()!=null && ctx.FUNCTION().getText().equalsIgnoreCase("function") ){
            if( ctx.typeDecl()==null )throw new AstParseError("unexpected null ref in ProcedureTypeHeadingContext");
            var returns = TypeDeclAst.of(ctx.typeDecl());

            return funMatch.apply(args, returns);
        }

        return procMatch.apply(args);
    }

    static ProcedureTypeAst of(DelphiParser.ProcedureTypeContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var simple = ctx.simpleProcedureType();
        if( simple!=null && !simple.isEmpty() ){
            Optional<CallConvention> callConv = simple.callConventionNoSemi()!=null && !simple.callConventionNoSemi().isEmpty()
                ? Optional.of(CallConvention.of(simple.callConventionNoSemi()))
                : Optional.empty();

            return of(simple.procedureTypeHeading(),
                (arguments, returns) -> new FunctionType(arguments,returns,callConv,SourcePosition.of(ctx)),
                arguments -> new ProcedureType(arguments,callConv,SourcePosition.of(ctx))
            );
        }

        var method = ctx.methodType();
        if(method!=null && !method.isEmpty()){
            return of(method.procedureTypeHeading(),
                (arguments, res) -> new ObjFunctionType(arguments,res,SourcePosition.of(ctx)),
                arguments -> new ObjProcedureType(arguments,SourcePosition.of(ctx))
                );
        }

        var refFunc = ctx.procedureReference();
        if(refFunc!=null && !refFunc.isEmpty()){
            return of(refFunc.procedureTypeHeading(),
                (arguments, returns) -> new RefFunctionType(arguments,returns,SourcePosition.of(ctx)),
                arguments -> new RefProcedureType(arguments,SourcePosition.of(ctx))
                );
        }

        throw AstParseError.unExpected(ctx);
    }
}

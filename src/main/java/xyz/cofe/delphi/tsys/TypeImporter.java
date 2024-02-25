package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.*;
import xyz.cofe.delphi.ast.*;
import xyz.cofe.delphi.ast.ClazzTypeAst;
import xyz.cofe.delphi.tsys.tm.*;

import java.util.Optional;

/**
 * Импорт типов из AST
 */
public class TypeImporter {
    private final Fn1<TypeName, Optional<Type>> typeFind;

    public TypeImporter(Fn1<TypeName, Optional<Type>> typeFind) {
        if (typeFind == null) throw new IllegalArgumentException("typeFind==null");
        this.typeFind = typeFind;
    }

    public InterfaceType interfaceTypeOf(UnitAst unit, TypeIdentAst ident, InterfaceTypeAst astItf) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if (ident == null) throw new IllegalArgumentException("ident==null");
        if (astItf == null) throw new IllegalArgumentException("astItf==null");

        var unitName = TypeName.of(unit.name());
        var itfName = TypeName.of(ident.name());
        var typeName = unitName.append(itfName);

        var itf = new InterfaceType.Named();
        itf.setName(typeName);
        itf.setComments(astItf.comments());

        var methods = astItf.body().map(itfItm -> {
            if (itfItm instanceof ClassMethodAst cm) {
                if (cm instanceof ClassMethodAst.Procedure p) {
                    return procedureOf(unit, p, Optional.of(new ClassOrItfNamedType(itf, ident))).mapErr(e -> "procedure " + p.name() + " " + e);
                } else if (cm instanceof ClassMethodAst.Constructor c) {
                    return Result.error("unexpected Constructor in interface");
                } else if (cm instanceof ClassMethodAst.Destructor d) {
                    return Result.error("unexpected Destructor in interface");
                } else if (cm instanceof ClassMethodAst.Function f) {
                    return functionOf(unit, f, Optional.of(new ClassOrItfNamedType(itf, ident))).mapErr(e -> "function " + f.name() + " " + e);
                } else if (cm instanceof ClassMethodAst.Operator o) {
                    return Result.error("unexpected Operator in interface");
                }
            } else if (itfItm instanceof ClassPropertyAst cp) {
                if (cp instanceof ClassPropertyAst.Property p) {
                    return propertyOf(unit, itf, ident, p).mapErr(e -> "property " + p.name() + " " + e);
                }
            }

            return Result.<InterfaceItem, String>error("can't map " + itfItm.getClass());
        });

        methods.each(m -> itf.setBody(itf.getBody().append(m.map(i -> (InterfaceItem) i).unwrap(InterfaceItem.Broken::new))));

        itf.setParents(astItf.parents().map(pAst -> {
            var tn = TypeName.of(pAst.name());
            return typeFind.apply(tn).orElse(new Type.UnitTypeRef(unit, TypeIdentAst.of(tn)));
        }));

        return itf;
    }

    public Result<ProcBase, String> procedureOrFunctionOf(UnitAst unit, ProcDeclAst proc) {
        if (proc == null) throw new IllegalArgumentException("proc==null");
        if (unit == null) throw new IllegalArgumentException("unit==null");

        if (proc.result().isPresent()) {
            var f = new Function();
            f.setName(proc.name());
            f.setDeclaration(Optional.of(proc.position()));
            f.setImplementation(Optional.empty());
            f.setDirectives(proc.directives());
            f.setComments(proc.comments());
            f.setReturns(typeOf(unit, proc.result().get()));
            return argsParse(unit, proc.arguments(), Optional.empty()).map(argz -> {
                f.setArguments(argz);
                return f;
            });
        } else {
            var p = new Procedure();
            p.setName(proc.name());
            p.setDeclaration(Optional.of(proc.position()));
            p.setImplementation(Optional.empty());
            p.setDirectives(proc.directives());
            p.setComments(proc.comments());
            return argsParse(unit, proc.arguments(), Optional.empty()).map(argz -> {
                p.setArguments(argz);
                return p;
            });
        }
    }

    /**
     * @param objType     тип интерфейса/класса в котором ссылка
     * @param objTypeName имя типа интерфейса/класса в котором ссылка
     */
    public record ClassOrItfNamedType(
        Type objType,
        TypeIdentAst objTypeName
    ) {
    }

    private record ClassAddState(ImListLinked<Result<ClassItem, String>> items, Visibility visibility) {
    }

    @SuppressWarnings("RedundantCast")
    public ClassType classTypeOf(UnitAst unit, TypeIdentAst ident, ClazzTypeAst astClass) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if (ident == null) throw new IllegalArgumentException("ident==null");
        if (astClass == null) throw new IllegalArgumentException("astClass==null");

        var unitName = TypeName.of(unit.name());
        var clsName = TypeName.of(ident.name());
        var typeName = unitName.append(clsName);

        var cls = new ClassType.Named();
        cls.setName(typeName);
        cls.setComments(astClass.comments());
        cls.setParents(astClass.parents().map(pAst -> {
            var tn = TypeName.of(pAst.name());
            return typeFind.apply(tn).orElse(new Type.UnitTypeRef(unit, TypeIdentAst.of(tn)));
        }));

        var body = astClass.body().foldLeft(new ClassAddState(ImListLinked.of(), Visibility.Public), (acc, classItem) -> {
            if (classItem instanceof ClassMethodAst cm) {
                if (cm instanceof ClassMethodAst.Procedure p) {
                    return new ClassAddState(
                        acc.items.prepend(
                            procedureOf(unit, p, Optional.of(new ClassOrItfNamedType(cls, ident))).mapErr(e -> "procedure " + p.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return i;
                            })
                        ),
                        acc.visibility
                    );
                } else if (cm instanceof ClassMethodAst.Constructor c) {
                    return new ClassAddState(
                        acc.items.prepend(
                            constructorOf(unit, cls, ident, c).mapErr(e -> "constructor " + c.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return i;
                            })
                        ),
                        acc.visibility
                    );
                } else if (cm instanceof ClassMethodAst.Destructor d) {
                    return new ClassAddState(
                        acc.items.prepend(
                            destructorOf(unit, cls, ident, d).mapErr(e -> "destructor " + d.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return i;
                            })
                        ),
                        acc.visibility
                    );
                } else if (cm instanceof ClassMethodAst.Function f) {
                    return new ClassAddState(
                        acc.items.prepend(
                            functionOf(unit, f, Optional.of(new ClassOrItfNamedType(cls, ident))).mapErr(e -> "function " + f.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return (ClassItem) i;
                            })
                        ),
                        acc.visibility
                    );
                } else if (cm instanceof ClassMethodAst.Operator o) {
                    return new ClassAddState(
                        acc.items.prepend(
                            operatorOf(unit, cls, ident, o).mapErr(e -> "operator " + o.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return (ClassItem) i;
                            })
                        ),
                        acc.visibility
                    );
                }
            } else if (classItem instanceof ClassPropertyAst cp) {
                if (cp instanceof ClassPropertyAst.Property p) {
                    return new ClassAddState(
                        acc.items.prepend(
                            propertyOf(unit, cls, ident, p).mapErr(e -> "property " + p.name() + " " + e).map(i -> {
                                i.setVisibility(acc.visibility);
                                return (ClassItem) i;
                            })
                        ),
                        acc.visibility
                    );
                }
            } else if (classItem instanceof VisibilityAst v) {
                return new ClassAddState(
                    acc.items,
                    Visibility.of(v)
                );
            } else if (classItem instanceof ClassFieldAst f) {
                return new ClassAddState(acc.items.prepend(
                    fieldOf(unit, cls, ident, f).mapErr(e -> "field " + f.name() + " " + e).map(i -> {
                        i.setVisibility(acc.visibility);
                        return (ClassItem) i;
                    })
                ), acc.visibility);
            }

            return new ClassAddState(
                acc.items.prepend(Result.error("can't map " + classItem.getClass())),
                acc.visibility
            );
        });
        cls.setBody(body.items().reverse().map(clsRes -> clsRes.unwrap(ClassItem.Broken::new)));

        return cls;
    }

    protected Result<Constructor, String> constructorOf(UnitAst unit, Type self, TypeIdentAst selfName, ClassMethodAst.Constructor ctorAst) {
        var ctor = new Constructor();
        ctor.setName(ctorAst.name());
        ctor.setVisibility(Visibility.Public);
        ctor.setDeclaration(Optional.of(ctorAst.position()));
        ctor.setImplementation(Optional.empty());
        ctor.setDirectives(ctorAst.directives().map(MethodDirective::of));
        ctor.setComments(ctorAst.comments());
        var args = argsParse(unit, ctorAst.arguments(), Optional.of(new ClassOrItfNamedType(self, selfName)));
        return args.map(argz -> {
            ctor.setArguments(argz);
            return ctor;
        });
    }

    protected Result<Destructor, String> destructorOf(UnitAst unit, Type self, TypeIdentAst selfName, ClassMethodAst.Destructor dtorAst) {
        var dtor = new Destructor();
        dtor.setName(dtorAst.name());
        dtor.setVisibility(Visibility.Public);
        dtor.setDeclaration(Optional.of(dtorAst.position()));
        dtor.setImplementation(Optional.empty());
        dtor.setDirectives(dtorAst.directives().map(MethodDirective::of));
        dtor.setComments(dtorAst.comments());
        return argsParse(unit, dtorAst.arguments(), Optional.of(new ClassOrItfNamedType(self, selfName))).map(argz -> {
            dtor.setArguments(argz);
            return dtor;
        });
    }

    protected Result<Operator, String> operatorOf(UnitAst unit, Type self, TypeIdentAst selfName, ClassMethodAst.Operator otor) {
        var op = new Operator();
        op.setName(otor.name());
        op.setVisibility(Visibility.Public);
        op.setDeclaration(Optional.of(otor.position()));
        op.setImplementation(Optional.empty());
        op.setComments(otor.comments());
        return argsParse(unit, otor.arguments(), Optional.of(new ClassOrItfNamedType(self, selfName))).map(argz -> {
            op.setArguments(argz);
            return op;
        });
    }

    protected Result<Field, String> fieldOf(UnitAst unit, Type self, TypeIdentAst selfName, ClassFieldAst fieldAst) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if (self == null) throw new IllegalArgumentException("self==null");
        if (selfName == null) throw new IllegalArgumentException("selfName==null");
        if (fieldAst == null) throw new IllegalArgumentException("fieldAst==null");

        var field = new Field();
        field.setFieldType(typeOf(unit, self, selfName, fieldAst.type()));
        field.setName(fieldAst.name());
        field.setVisibility(Visibility.Public);
        field.setComments(fieldAst.comments());
        field.setDeclaration(Optional.of(fieldAst.position()));
        return Result.ok(field);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Result<MethodProcedure, String> procedureOf(UnitAst unit, ClassMethodAst.Procedure proc, Optional<ClassOrItfNamedType> classItfName) {
        if (unit == null) throw new IllegalArgumentException("unit==null");
        if (proc == null) throw new IllegalArgumentException("proc==null");
        //noinspection OptionalAssignedToNull
        if (classItfName == null) throw new IllegalArgumentException("classItfName==null");
        var p = new MethodProcedure();
        p.setName(proc.name());
        p.setVisibility(Visibility.Public);
        p.setDeclaration(Optional.of(proc.position()));
        p.setImplementation(Optional.empty());
        p.setDirectives(proc.directives().map(MethodDirective::of));
        p.setComments(proc.comments());
        return argsParse(unit, proc.arguments(), classItfName).map(argz -> {
            p.setArguments(argz);
            return p;
        });
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Result<MethodFunction, String> functionOf(UnitAst unit, ClassMethodAst.Function fun, Optional<ClassOrItfNamedType> classItfName) {
        var f = new MethodFunction();
        f.setName(fun.name());
        f.setReturns(typeOf(unit, classItfName, fun.result()));
        f.setVisibility(Visibility.Public);
        f.setDeclaration(Optional.of(fun.position()));
        f.setImplementation(Optional.empty());
        f.setDirectives(fun.directives().map(MethodDirective::of));
        f.setComments(fun.comments());
        return argsParse(unit, fun.arguments(), classItfName).map(argz -> {
            f.setArguments(argz);
            return f;
        });
    }

    protected Result<Property, String> propertyOf(UnitAst unit, Type self, TypeIdentAst selfName, ClassPropertyAst.Property prop) {
        var p = new Property();
        p.setName(prop.name());
        argsParse(unit, prop.propertyArray(), Optional.of(new ClassOrItfNamedType(self, selfName))).fmap(arrArgs -> {
            p.setArrayArguments(arrArgs);

            if (prop.type().isEmpty()) return Result.error("property type not defined");
            p.setType(typeOf(unit, self, selfName, prop.type().get()));

            p.setDeclaration(Optional.of(prop.position()));

            p.setComments(prop.comments());
            p.setStatik(prop.classFlag());

            ImList<PropertySpecifier> propSpec = prop.specifiers().map(s -> propertySpecifier(unit, self, selfName, s));
            p.setSpecifiers(propSpec);

            return Result.ok(p);
        });

        return Result.error("not implemented");
    }

    /**
     * Парсинг параметров метода класса/интерфейса
     *
     * @param unit         модуль
     * @param classItfName описание типа класса/интерфейса
     * @param arguments    аргументы
     * @return аргументы
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected Result<ImList<Argument>, String> argsParse(UnitAst unit, ImList<ArgumentAst> arguments,
                                                         Optional<ClassOrItfNamedType> classItfName
    ) {
        return arguments.foldRight(
            Result.ok(ImListLinked.<xyz.cofe.delphi.tsys.tm.Argument>of(), String.class),
            (acc, it) -> acc.fmap(lst -> {
                var ma = new Argument();

                if (it.typeDecl().isEmpty() && it.defaultValue().isEmpty()) {
                    return Result.error("arg " + it.name() + " both typeDecl and defaultValue is empty - not implemented");
                }
                if (it.typeDecl().isEmpty()) {
                    return Result.error("arg " + it.name() + " typeDecl not implemented");
                }

                var argTypeAst = it.typeDecl().get();
                ma.setType(typeOf(unit, classItfName, argTypeAst));
                ma.setName(it.name());

                if (it.constraint().isEmpty()) {
                    ma.setDirection(ArgumentDirection.Input);
                } else {
                    ma.setDirection(switch (it.constraint().get()) {
                        case Out -> ArgumentDirection.Output;
                        case Var -> ArgumentDirection.InputOutput;
                        case Const -> ArgumentDirection.ConstInput;
                    });
                }

                var res = lst.prepend(ma);
                return Result.ok(res);
            })
        );
    }

    protected PropertySpecifier propertySpecifier(UnitAst unit, Type self, TypeIdentAst selfName, ClassPropertyAst.Specifier spec) {
        if (spec instanceof ClassPropertyAst.Read r) {
            return new PropertySpecifier.Read(
                r.name()
            );
        }
        if (spec instanceof ClassPropertyAst.Write w) {
            return new PropertySpecifier.Write(
                w.name()
            );
        }
        if (spec instanceof ClassPropertyAst.ReadOnly r) {
            return new PropertySpecifier.ReadOnly();
        }
        if (spec instanceof ClassPropertyAst.WriteOnly r) {
            return new PropertySpecifier.WriteOnly();
        }
        if (spec instanceof ClassPropertyAst.DispID r) {
            return new PropertySpecifier.DispID(r.expression());
        }
        if (spec instanceof ClassPropertyAst.Stored s) {
            return new PropertySpecifier.Stored(s.expression());
        }
        if (spec instanceof ClassPropertyAst.Default d) {
            return new PropertySpecifier.Default(d.expression());
        }
        if (spec instanceof ClassPropertyAst.NoDefault n) {
            return new PropertySpecifier.NoDefault();
        }
        if (spec instanceof ClassPropertyAst.Implements i) {
            Type t = null;
            var tn = TypeName.of(i.typeId());
            var selfTn = TypeName.of(selfName.name());
            if (tn.equals(selfTn)) {
                t = self;
            } else {
                var t2 = typeFind.apply(tn);
                t = t2.orElseGet(() -> new Type.UnitTypeRef(unit, new TypeIdentAst(i.typeId(), ImListLinked.of())));
            }

            return new PropertySpecifier.Implements(t);
        }

        throw new RuntimeException("bug! unexpected");
    }

    /**
     * Поиск подходящего типа
     *
     * @param unit         модуль в котором ссылка
     * @param self         тип интерфейса/класса в котором ссылка
     * @param selfName     имя типа интерфейса/класса в котором ссылка
     * @param typeIdOrName ссылка
     * @return тип или Type.UnitTypeRef(unit, typeIdOrName)
     */
    protected Type typeOf(UnitAst unit, Type self, TypeIdentAst selfName, TypeDeclAst typeIdOrName) {
        if (selfName.equals(typeIdOrName)) {
            return self;
        } else if (typeIdOrName instanceof TypeIdentAst t) {
            return typeFind.apply(TypeName.of(t.name())).orElse(new Type.UnitTypeRef(unit, typeIdOrName));
        } else if (typeIdOrName instanceof VariantTypeAst) {
            return typeFind.apply(TypeName.of("Variant")).orElse(new Type.UnitTypeRef(unit, typeIdOrName));
        } else if (typeIdOrName instanceof StringTypeAst.StrIng s) {
            if (s.expression().isEmpty()) {
                return StringType.stringWithOutLengthType;
            }
            return new Type.UnitTypeRef(unit, typeIdOrName);
        } else {
            return new Type.UnitTypeRef(unit, typeIdOrName);
        }
    }

    /**
     * Поиск подходящего типа
     *
     * @param unit         модуль в котором ссылка
     * @param typeIdOrName ссылка
     * @return тип или Type.UnitTypeRef(unit, typeIdOrName)
     */
    protected Type typeOf(UnitAst unit, TypeDeclAst typeIdOrName) {
        if (typeIdOrName instanceof TypeIdentAst t) {
            return typeFind.apply(TypeName.of(t.name())).orElse(new Type.UnitTypeRef(unit, typeIdOrName));
        } else if (typeIdOrName instanceof VariantTypeAst) {
            return typeFind.apply(TypeName.of("Variant")).orElse(new Type.UnitTypeRef(unit, typeIdOrName));
        } else if (typeIdOrName instanceof StringTypeAst.StrIng s) {
            if (s.expression().isEmpty()) {
                return StringType.stringWithOutLengthType;
            }
            return new Type.UnitTypeRef(unit, typeIdOrName);
        } else {
            return new Type.UnitTypeRef(unit, typeIdOrName);
        }
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    protected Type typeOf(UnitAst unit, Optional<ClassOrItfNamedType> classItfName, TypeDeclAst typeDeclAst) {
        return classItfName.map(obj -> typeOf(unit, obj.objType(), obj.objTypeName(), typeDeclAst)).orElseGet(() -> typeOf(unit, typeDeclAst));
    }
}

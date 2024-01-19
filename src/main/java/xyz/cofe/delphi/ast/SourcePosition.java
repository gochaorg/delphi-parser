package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.Objects;
import java.util.Optional;

/**
 * Позиция в исходном файле
 */
public sealed interface SourcePosition {
    static SourcePosition non() {
        return new FilePoint("!non!", -1, -1);
    }

    Optional<SourcePosition> intersection(SourcePosition other);

    boolean before(SourcePosition sourcePosition);

    sealed interface FileName {
        String fileName();
    }

    sealed interface Point extends SourcePosition {
        int lineNumber();

        int charNumber();

        default boolean before(Point p) {
            if (p == null) return false;
            if (lineNumber() < p.lineNumber()) return true;
            if( lineNumber() > p.lineNumber() )return false;
            return charNumber() < p.charNumber();
        }

        default boolean after(Point p) {
            if (p == null) return false;
            if (lineNumber() > p.lineNumber()) return true;
            if (lineNumber() < p.lineNumber()) return false;
            return charNumber() > p.charNumber();
        }
    }

    sealed interface Range<P extends Point> {
        P start();

        P end();

        @SuppressWarnings("RedundantIfStatement")
        default boolean contains(Point p) {
            if (p == null) return false;

            var left = start();
            var right = end();
            if (!start().before(end())) {
                left = end();
                right = start();
            }

            if (left.equals(p)) return true;
            if (right.equals(p)) return true;

            if (!left.before(p)) return false;
            if (!p.before(right)) return false;

            return true;
        }
    }

    /**
     * "Точка" в исходном коде, которая не связана с файлом
     *
     * @param lineNumber номер строки
     * @param charNumber номер символа в строке
     */
    record FileLessPoint(int lineNumber, int charNumber)
        implements SourcePosition, Point {
        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof FileLessPoint p)) return false;

            if (!Objects.equals(lineNumber, p.lineNumber)) return false;
            if (!Objects.equals(charNumber, p.charNumber)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lineNumber, charNumber);
        }

        public Optional<SourcePosition> intersection(SourcePosition other) {
            if (other == null) return Optional.empty();
            if (other instanceof Point p && p.lineNumber() == lineNumber && p.charNumber() == charNumber) {
                return Optional.of(this);
            }
            if (other instanceof FileRange fileRange) {
                return fileRange.intersection(this);
            }
            if (other instanceof FileLessRange fileLessRange) {
                return fileLessRange.intersection(this);
            }
            return Optional.empty();
        }

        public static FileLessPoint of(Point p) {
            if (p == null) throw new IllegalArgumentException("p==null");
            return new FileLessPoint(p.lineNumber(), p.charNumber());
        }

        public boolean before(SourcePosition sourcePosition) {
            if (sourcePosition == null) return false;

            if (sourcePosition instanceof Point p) {
                if( lineNumber() < p.lineNumber() ) return true;
                if( lineNumber() > p.lineNumber() ) return false;
                return charNumber() < p.charNumber();
            }

            if (sourcePosition instanceof Range range) {
                var a_left = range.start();
                var a_right = range.end();
                if (a_right.before(a_left)) {
                    a_right = range.start();
                    a_left = range.end();
                }

                return before(a_left);
            }

            return false;
        }
    }

    /**
     * "Точка" в исходном коде
     *
     * @param fileName   имя файла
     * @param lineNumber номер строки
     * @param charNumber номер символа в строке
     */
    record FilePoint(String fileName, int lineNumber, int charNumber)
        implements SourcePosition, FileName, Point {
        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof FilePoint p)) return false;

            if (!Objects.equals(fileName, p.fileName)) return false;
            if (!Objects.equals(lineNumber, p.lineNumber)) return false;
            if (!Objects.equals(charNumber, p.charNumber)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lineNumber, charNumber, fileName);
        }

        @Override
        public Optional<SourcePosition> intersection(SourcePosition other) {
            if (other == null) return Optional.empty();
            if (other instanceof Point p) {
                if (other instanceof FileName fname) {
                    if (!Objects.equals(fname.fileName(), fileName))
                        return Optional.empty();

                    if (p.lineNumber() == lineNumber && p.charNumber() == charNumber)
                        return Optional.of(this);
                } else {
                    if (p.lineNumber() == lineNumber && p.charNumber() == charNumber)
                        return Optional.of(p);
                }
            }
            if (other instanceof FileRange fileRange) {
                return fileRange.intersection(this);
            }
            if (other instanceof FileLessRange fileLessRange) {
                return fileLessRange.intersection(this);
            }
            return Optional.empty();
        }

        public static FilePoint of(Point p, String fileName) {
            if (p == null) throw new IllegalArgumentException("p==null");
            if (fileName == null) throw new IllegalArgumentException("fileName==null");
            return new FilePoint(fileName, p.lineNumber(), p.charNumber());
        }

        public boolean before(SourcePosition sourcePosition) {
            if (sourcePosition == null) return false;
            if (sourcePosition instanceof FileName fileName1) {
                if (!Objects.equals(fileName1.fileName(), this.fileName))
                    return false;
            }

            if (sourcePosition instanceof Point p) {
                if (lineNumber() < p.lineNumber()) return true;
                if (lineNumber() > p.lineNumber()) return false;
                return charNumber() < p.charNumber();
            }

            if (sourcePosition instanceof Range range) {
                var a_left = range.start();
                var a_right = range.end();
                if (a_right.before(a_left)) {
                    a_right = range.start();
                    a_left = range.end();
                }

                return before(a_left);
            }

            return false;
        }
    }

    /**
     * "Отрезок" в исходном коде
     *
     * @param start начало отрезка
     * @param end   конец отрезка включительно
     */
    record FileLessRange(FileLessPoint start, FileLessPoint end)
        implements SourcePosition, Range<FileLessPoint> {
        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof FileLessRange r)) return false;

            if (!Objects.equals(start, r.start)) return false;
            if (!Objects.equals(end, r.end)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public Optional<SourcePosition> intersection(SourcePosition other) {
            if (other == null) return Optional.empty();
            if (other instanceof Range<?> r) {
                var a_left = start();
                var a_right = end();
                if (a_right.before(a_left)) {
                    a_right = start();
                    a_left = end();
                }

                var b_left = r.start();
                var b_right = r.end();
                if (b_right.before(b_left)) {
                    b_right = r.start();
                    b_left = r.end();
                }

                if (b_right.before(a_left)) return Optional.empty();
                if (a_right.before(b_left)) return Optional.empty();

                var r_left = a_left.before(b_left) ? b_left : a_left;
                var r_right = a_right.before(b_right) ? a_right : b_right;
                if (r_left.lineNumber() == r_right.lineNumber()
                    && r_left.charNumber() == r_right.charNumber()
                ) {
                    return Optional.of(new FileLessPoint(
                        r_left.lineNumber(), r_left.charNumber()
                    ));
                }

                return Optional.of(new FileLessRange(
                    FileLessPoint.of(r_left),
                    FileLessPoint.of(r_right)
                ));
            }

            if (other instanceof Point p && contains(p)) {
                return Optional.of(FileLessPoint.of(p));
            }

            return Optional.empty();
        }

        public boolean before(SourcePosition sourcePosition) {
            if (sourcePosition == null) return false;

            var a_left = start();
            var a_right = end();
            if (a_right.before(a_left)) {
                a_right = start();
                a_left = end();
            }

            if (sourcePosition instanceof Range<?> r) {
                var b_left = r.start();
                var b_right = r.end();
                if (b_right.before(b_left)) {
                    b_right = r.start();
                    b_left = r.end();
                }

                return a_left.before(b_left);
            }

            if (sourcePosition instanceof Point p) {
                return a_left.before(p);
            }

            return false;
        }
    }

    /**
     * "Отрезок" в исходном коде
     *
     * @param start начало отрезка
     * @param end   конец отрезка включительно
     */
    record FileRange(FilePoint start, FilePoint end)
        implements SourcePosition, Range<FilePoint>, FileName {
        @Override
        public String fileName() {
            return start.fileName;
        }

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof FileRange r)) return false;

            if (!Objects.equals(start, r.start)) return false;
            if (!Objects.equals(end, r.end)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }

        @Override
        public Optional<SourcePosition> intersection(SourcePosition other) {
            if (other == null) return Optional.empty();
            if (other instanceof Range<?> r) {
                if (r.start() instanceof FileName fname) {
                    if (!Objects.equals(fname.fileName(), start.fileName))
                        return Optional.empty();
                }
                if (r.end() instanceof FileName fname) {
                    if (!Objects.equals(fname.fileName(), end.fileName))
                        return Optional.empty();
                }

                var a_left = start();
                var a_right = end();
                if (a_right.before(a_left)) {
                    a_right = start();
                    a_left = end();
                }

                var b_left = r.start();
                var b_right = r.end();
                if (b_right.before(b_left)) {
                    b_right = r.start();
                    b_left = r.end();
                }

                if (b_right.before(a_left)) return Optional.empty();
                if (a_right.before(b_left)) return Optional.empty();

                var r_left = a_left.before(b_left) ? b_left : a_left;
                var r_right = a_right.before(b_right) ? a_right : b_right;
                if (r_left.lineNumber() == r_right.lineNumber()
                    && r_left.charNumber() == r_right.charNumber()
                ) {
                    return Optional.of(new FileLessPoint(
                        r_left.lineNumber(), r_left.charNumber()
                    ));
                }

                if (other instanceof FileRange frange) {
                    return Optional.of(new FileRange(
                        FilePoint.of(r_left, frange.start.fileName),
                        FilePoint.of(r_right, frange.start.fileName)
                    ));
                }

                return Optional.of(new FileLessRange(
                    FileLessPoint.of(r_left),
                    FileLessPoint.of(r_right)
                ));
            }

            if (other instanceof Point p && contains(p)) {
                if (p instanceof FilePoint fpnt) {
                    if (!Objects.equals(fpnt.fileName, start.fileName)) return Optional.empty();
                    return Optional.of(FilePoint.of(p, fpnt.fileName));
                }

                return Optional.of(FileLessPoint.of(p));
            }

            return Optional.empty();
        }

        public boolean before(SourcePosition sourcePosition) {
            if (sourcePosition == null) return false;
            if (sourcePosition instanceof FileName fname && !Objects.equals(fname.fileName(), start.fileName))
                return false;

            var a_left = start();
            var a_right = end();
            if (a_right.before(a_left)) {
                a_right = start();
                a_left = end();
            }

            if (sourcePosition instanceof Range<?> r) {
                var b_left = r.start();
                var b_right = r.end();
                if (b_right.before(b_left)) {
                    b_right = r.start();
                    b_left = r.end();
                }

                return a_left.before(b_left);
            }

            if (sourcePosition instanceof Point p) {
                return a_left.before(p);
            }

            return false;
        }
    }

    static SourcePosition of(ParserRuleContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.getStart() != null
            && ctx.getStart().getTokenSource() != null
            && ctx.getStart().getTokenSource().getSourceName() != null
        ) {
            var src = ctx.getStart().getTokenSource().getSourceName();
            return new FileRange(
                new FilePoint(src, ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()),
                new FilePoint(src, ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine())
            );
        } else {
            return new FileLessRange(
                new FileLessPoint(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine()),
                new FileLessPoint(ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine())
            );
        }
    }

    static SourcePosition of(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getTokenSource() != null && token.getTokenSource().getSourceName() != null) {
            return new FilePoint(
                token.getTokenSource().getSourceName(),
                token.getLine(),
                token.getCharPositionInLine()
            );
        } else {
            return new FileLessPoint(token.getLine(), token.getCharPositionInLine());
        }
    }
}

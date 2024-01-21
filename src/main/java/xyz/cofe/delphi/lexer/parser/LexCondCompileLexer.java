// Generated from C:/code/other/delphi-parser/src/main/resources/LexCondCompile.g4 by ANTLR 4.13.1
package xyz.cofe.delphi.lexer.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class LexCondCompileLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, IDENT=17, 
		NUM=18, WS=19;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "IDENT", 
			"DIGIT", "NUM", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'or'", "'and'", "'<='", "'>='", "'<>'", "'!='", "'<'", "'='", 
			"'>'", "'!'", "'not'", "'-'", "'+'", "'('", "')'", "','", null, null, 
			"' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "IDENT", "NUM", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public LexCondCompileLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LexCondCompile.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0013p\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001"+
		"\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0005\u0010U\b\u0010\n\u0010\f\u0010X\t\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0005\u0012^\b\u0012\n\u0012\f\u0012"+
		"a\t\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012f\b\u0012\n\u0012"+
		"\f\u0012i\t\u0012\u0003\u0012k\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0000\u0000\u0014\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0000%\u0012"+
		"\'\u0013\u0001\u0000\b\u0002\u0000OOoo\u0002\u0000RRrr\u0002\u0000AAa"+
		"a\u0002\u0000NNnn\u0002\u0000DDdd\u0002\u0000TTtt\u0003\u0000AZ__az\u0004"+
		"\u000009AZ__azr\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000"+
		"\'\u0001\u0000\u0000\u0000\u0001)\u0001\u0000\u0000\u0000\u0003,\u0001"+
		"\u0000\u0000\u0000\u00050\u0001\u0000\u0000\u0000\u00073\u0001\u0000\u0000"+
		"\u0000\t6\u0001\u0000\u0000\u0000\u000b9\u0001\u0000\u0000\u0000\r<\u0001"+
		"\u0000\u0000\u0000\u000f>\u0001\u0000\u0000\u0000\u0011@\u0001\u0000\u0000"+
		"\u0000\u0013B\u0001\u0000\u0000\u0000\u0015D\u0001\u0000\u0000\u0000\u0017"+
		"H\u0001\u0000\u0000\u0000\u0019J\u0001\u0000\u0000\u0000\u001bL\u0001"+
		"\u0000\u0000\u0000\u001dN\u0001\u0000\u0000\u0000\u001fP\u0001\u0000\u0000"+
		"\u0000!R\u0001\u0000\u0000\u0000#Y\u0001\u0000\u0000\u0000%[\u0001\u0000"+
		"\u0000\u0000\'l\u0001\u0000\u0000\u0000)*\u0007\u0000\u0000\u0000*+\u0007"+
		"\u0001\u0000\u0000+\u0002\u0001\u0000\u0000\u0000,-\u0007\u0002\u0000"+
		"\u0000-.\u0007\u0003\u0000\u0000./\u0007\u0004\u0000\u0000/\u0004\u0001"+
		"\u0000\u0000\u000001\u0005<\u0000\u000012\u0005=\u0000\u00002\u0006\u0001"+
		"\u0000\u0000\u000034\u0005>\u0000\u000045\u0005=\u0000\u00005\b\u0001"+
		"\u0000\u0000\u000067\u0005<\u0000\u000078\u0005>\u0000\u00008\n\u0001"+
		"\u0000\u0000\u00009:\u0005!\u0000\u0000:;\u0005=\u0000\u0000;\f\u0001"+
		"\u0000\u0000\u0000<=\u0005<\u0000\u0000=\u000e\u0001\u0000\u0000\u0000"+
		">?\u0005=\u0000\u0000?\u0010\u0001\u0000\u0000\u0000@A\u0005>\u0000\u0000"+
		"A\u0012\u0001\u0000\u0000\u0000BC\u0005!\u0000\u0000C\u0014\u0001\u0000"+
		"\u0000\u0000DE\u0007\u0003\u0000\u0000EF\u0007\u0000\u0000\u0000FG\u0007"+
		"\u0005\u0000\u0000G\u0016\u0001\u0000\u0000\u0000HI\u0005-\u0000\u0000"+
		"I\u0018\u0001\u0000\u0000\u0000JK\u0005+\u0000\u0000K\u001a\u0001\u0000"+
		"\u0000\u0000LM\u0005(\u0000\u0000M\u001c\u0001\u0000\u0000\u0000NO\u0005"+
		")\u0000\u0000O\u001e\u0001\u0000\u0000\u0000PQ\u0005,\u0000\u0000Q \u0001"+
		"\u0000\u0000\u0000RV\u0007\u0006\u0000\u0000SU\u0007\u0007\u0000\u0000"+
		"TS\u0001\u0000\u0000\u0000UX\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000"+
		"\u0000VW\u0001\u0000\u0000\u0000W\"\u0001\u0000\u0000\u0000XV\u0001\u0000"+
		"\u0000\u0000YZ\u000209\u0000Z$\u0001\u0000\u0000\u0000[_\u0003#\u0011"+
		"\u0000\\^\u0003#\u0011\u0000]\\\u0001\u0000\u0000\u0000^a\u0001\u0000"+
		"\u0000\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`j\u0001"+
		"\u0000\u0000\u0000a_\u0001\u0000\u0000\u0000bc\u0005.\u0000\u0000cg\u0003"+
		"#\u0011\u0000df\u0003#\u0011\u0000ed\u0001\u0000\u0000\u0000fi\u0001\u0000"+
		"\u0000\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000hk\u0001"+
		"\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000jb\u0001\u0000\u0000\u0000"+
		"jk\u0001\u0000\u0000\u0000k&\u0001\u0000\u0000\u0000lm\u0005 \u0000\u0000"+
		"mn\u0001\u0000\u0000\u0000no\u0006\u0013\u0000\u0000o(\u0001\u0000\u0000"+
		"\u0000\u0005\u0000V_gj\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
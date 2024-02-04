package xyz.cofe.delphi.lexer;

import java.util.*;

/**
https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Compiler_Versions

 <table summary="Версии компилятора Delphi">
 <tr><td>Delphi conditional VER&lt;nnn&gt;</td> <td>	Product</td>	<td>Product Version</td>	<td>Package Version	</td><td>CompilerVersion</td></tr>
 <tr><td>VER350</td>	<td>Delphi 11.0 Alexandria / C++Builder 11.0 Alexandria	</td> <td>28</td>	<td>280</td>	<td>35.0</td></tr>
 <tr><td>VER340</td>	<td>Delphi 10.4 Sydney / C++Builder 10.4 Sydney	        </td> <td>27</td>	<td>270</td>	<td>34.0</td></tr>
 <tr><td>VER330</td>	<td>Delphi 10.3 Rio / C++Builder 10.3 Rio	            </td> <td>26</td>	<td>260</td>	<td>33.0</td></tr>
 <tr><td>VER320</td>	<td>Delphi 10.2 Tokyo / C++Builder 10.2 Tokyo	        </td> <td>25</td>	<td>250</td>	<td>32.0</td></tr>
 <tr><td>VER310</td>	<td>Delphi 10.1 Berlin / C++Builder 10.1 Berlin	        </td> <td>24</td>	<td>240</td>	<td>31.0</td></tr>
 <tr><td>VER300</td>	<td>Delphi 10 Seattle / C++Builder 10 Seattle	        </td> <td>23</td>	<td>230</td>	<td>30.0</td></tr>
 <tr><td>VER290</td>	<td>Delphi XE8 / C++Builder XE8	                        </td> <td>22</td>	<td>220</td>	<td>29.0</td></tr>
 <tr><td>VER280</td>	<td>Delphi XE7 / C++Builder XE7	                        </td> <td>21</td>	<td>210</td>	<td>28.0</td></tr>
 <tr><td>VER270</td>	<td>Delphi XE6 / C++Builder XE6	                        </td> <td>20</td>	<td>200</td>	<td>27.0</td></tr>
 <tr><td>VER260</td>	<td>Delphi XE5 / C++Builder XE5	                        </td> <td>19</td>	<td>190</td>	<td>26.0</td></tr>
 <tr><td>VER250</td>	<td>Delphi XE4 / C++Builder XE4	                        </td> <td>18</td>	<td>180</td>	<td>25.0</td></tr>
 <tr><td>VER240</td>	<td>Delphi XE3 / C++Builder XE3	                        </td> <td>17</td>	<td>170</td>	<td>24.0</td></tr>

 <tr><td>VER230</td>	<td>Delphi XE2 / C++Builder XE2
 </td>
    <td>16</td>
    <td>
    160161 is the version for the five FireMonkey packages at XE2 Update 2: fmi161.bpl, fmx161.bpl, fmxase161.bpl, fmxdae161.bpl, and fmxobj161.bpl.
    </td>
    <td>23.0</td></tr>

 <tr><td>VER220</td>	<td>Delphi XE / C++Builder XE	                        </td><td>15</td>	<td>150</td>	<td>22.0</td></tr>
 <tr><td>VER210</td>	<td>Delphi 2010 / C++Builder 2010	                    </td><td>14</td>	<td>140</td>	<td>21.0</td></tr>
 <tr><td>VER200</td>	<td>Delphi 2009 / C++Builder 2009	                    </td><td>12</td>	<td>120</td>	<td>20.0</td></tr>
 <tr><td>VER190</td>	<td>Delphi 2007 for .Net [1]	                        </td><td>11</td>	<td>110</td>	<td>19.0</td></tr>
 <tr><td>VER180 or VER185</td>    <td>Delphi 2007 / C++Builder 2007 for Win32 [1]	</td><td>11</td>	<td>110</td>	<td>18.5</td></tr>
 <tr><td>VER180</td>	<td>Delphi 2006 / C++Builder 2006	                    </td><td>10</td>	<td>100</td>	<td>18.0</td></tr>
 <tr><td>VER170</td>	<td>Delphi 2005	                                        </td><td>9</td>	<td>90</td>	<td>17.0</td></tr>
 <tr><td>VER160</td>	<td>Delphi 8 for .Net	                                </td><td>8</td>	<td>80</td>	<td>16.0</td></tr>
 <tr><td>VER150</td>	<td>Delphi 7 (and 7.1)	                                </td><td>7</td>	<td>70</td>	<td>15.0</td></tr>
 <tr><td>VER140</td>	<td>Delphi 6 / C++Builder 6                             <td>6</td>	<td>60</td>	<td>14.0</td></tr>
 <tr><td>VER130</td>	<td>Delphi 5 / C++Builder 5                              </td> <td>5</td>	<td>NA</td> <td>NA</td></tr>
 <tr><td>VER125</td>	<td>C++Builder 4	                                    <td>4</td><td>NA</td><td>NA</td></tr>
 <tr><td>VER120</td>	<td>Delphi 4	                            </td><td>4	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER110</td>	<td>C++Builder 3	                        </td><td>3	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER100</td>	<td>Delphi 3	                            </td><td>3	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER93</td>	<td>C++Builder 1	                            </td><td>NA	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER90</td>	<td>Delphi 2	                                </td><td>2	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER80</td>	<td>Delphi 1	                                </td><td>1	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER70</td>	<td>Borland Pascal 7.0	                        </td><td>NA	</td><td>NA</td>	<td>NA</td></tr>
 <tr><td>VER15</td>	<td>Turbo Pascal for Windows 1.5</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 <tr><td>VER10</td>	<td>Turbo Pascal for Windows 1.0</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 <tr><td>VER60</td>	<td>Turbo Pascal 6.0</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 <tr><td>VER55</td>	<td>Turbo Pascal 5.5</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 <tr><td>VER50</td>	<td>Turbo Pascal 5.0</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 <tr><td>VER40</td>	<td>Turbo Pascal 4.0</td>	<td>NA</td>	<td>NA</td>	<td>NA</td></tr>
 </table>

 */
public class PreProcState {

    public PreProcState(){
        define("MSWINDOWS");
        define("RTLVersion",21);
        define("CompilerVersion",18.0);
    }

    public PreProcState(PreProcState sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        symbols.putAll(sample.symbols);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public PreProcState clone(){
        PreProcState clone = (PreProcState) new PreProcState(this);
        return new PreProcState(this);
    }
    private final Map<String,Double> symbols = new HashMap<>();
    public PreProcState define(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.put(symbol,1.0);
        return this;
    }
    public PreProcState define(String symbol,double value){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.put(symbol,value);
        return this;
    }
    public PreProcState undef(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.remove(symbol);
        return this;
    }
    public boolean isDefined(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        return symbols.containsKey(symbol);
    }
    public Optional<Double> get(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        var v = symbols.get(symbol);
        return v==null ? Optional.empty() : Optional.of(v);
    }
    public Set<String> toSet(){
        return new TreeSet<>(symbols.keySet());
    }
}

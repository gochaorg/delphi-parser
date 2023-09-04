unit Logging;

interface

uses
  SysUtils, Classes;

type
  // Запись в лог сообщения
  ILog = interface
    // добавления в лог сообщения
    // Аргументы
    //   messageText - текст
    procedure print( const messageText: string );

    // добавления в лог сообщения с переходом на новую строку (CRLF)
    // Аргументы
    //   messageText - текст
    procedure println( const messageText: string );
  end;

  // Фиктивная запись лога
  TDummyLog = class(TInterfacedObject,ILog)
    procedure print( const messageText: string ); virtual;
    procedure println( const messageText: string ); virtual;
  end;

  // Запись в лог файл
  TFileLog = class(TInterfacedObject,ILog)
    public
      // Конструктор
      // Аргументы
      //   fileName - имя лог файла 
      //   appendFile 
      //     true - добавляет записи в конец, если файл существует
      //     false - переписывает файл, если файл существует
      constructor Create( fileName: WideString; appendFile:Boolean );
      destructor Close(); virtual;
      procedure print( const messageText: string ); virtual;
      procedure println( const messageText: string ); virtual;
    private
      textFile: TextFile;
  end;

  TDelegateLogListenerProc = procedure ();
  TDelegateLogListenerMethod = procedure () of object;

  TDelegateLogListener = class
    private
      proc: TDelegateLogListenerProc;
      meth: TDelegateLogListenerMethod;
    public
      constructor Create( target:TDelegateLogListenerProc ); overload;
      constructor Create( target:TDelegateLogListenerMethod ); overload;
      destructor Destroy; override;
      procedure fire;
  end;

  // Перенаправляет лог
  TDelegateLog = class(TInterfacedObject,ILog)
    private
      target:ILog;
      onTargetListeners: TList; // TDelegateLogListener
    public
      constructor Create();
      destructor Destroy(); override;

      // Указывает новое целевое назначение
      // куда записывать сообщения (делегировать вызовы)
      procedure setTarget( const target:ILog ); virtual;
      procedure addListener( target:TDelegateLogListenerProc ); overload;
      procedure addListener( target:TDelegateLogListenerMethod ); overload;

      procedure print( const messageText: string ); virtual;
      procedure println( const messageText: string ); virtual;
  end;

  // Добавление префикса в сообщения лог файла
  TPrefixBuilder = function():string of object;

  // Добавляет перед сообщением лога текст
  TPrefixLog = class(TInterfacedObject,ILog)
    private
      needPrefix: boolean;
      prefixBuilder: TPrefixBuilder;
      targetLog: ILog;
    public
      // Конструктор
      // Аргументы
      //   logTo - логгер приемник
      //   prefix - функция возвращающая префикс текстового сообщения
      constructor Create( logTo:ILog; prefix:TPrefixBuilder );
      destructor Destroy(); override;
      procedure print( const messageText: string ); virtual;
      procedure println( const messageText: string ); virtual;
  end;

  // Константное значение в префиксе лог записи
  IConstPrefixLog = interface
    function getMessage():string;
  end;
  TConstPrefixLog = class(TInterfacedObject, IConstPrefixLog)
    private
      prefix: string;
    public
      // Конструктор
      //   text - префикс текстового сообщения
      constructor Create(text:string);
      destructor Destroy(); override;
      function getMessage():string;
  end;

  // Текущая дата.время в префиксе лог записи
  IDateTimePrefixLog = interface
    function getMessage():string;
  end;

  TDateTimePrefixLog = class(TInterfacedObject, IDateTimePrefixLog)
    private
      fmt: TFormatSettings;
    public
      constructor Create();
      destructor Destroy(); override;
      function getMessage():string;
  end;

var
  unnamedLog: ILog;
  rootLog: TDelegateLog;
  dummyLog: TDummyLog;

implementation

uses
  Config;

{ TDummyLog }

procedure TDummyLog.print(const messageText: string);
begin
  // no operation
end;

procedure TDummyLog.println(const messageText: string);
begin
  // no operation
end;

{ TFileLog }

constructor TFileLog.Create(fileName: WideString; appendFile:Boolean);
begin
  inherited Create();
  if appendFile then begin
    if not FileExists( fileName ) then
      begin
        AssignFile( textFile, fileName );
        Rewrite( textFile );
      end
    else
      begin
        AssignFile( textFile, fileName );
        Append( textFile );
      end;
    end
  else
    begin
      AssignFile( textFile, fileName );
      Rewrite( textFile );
    end;
end;

destructor TFileLog.Close;
begin
  CloseFile( textFile );
  inherited Destroy;
end;

procedure TFileLog.print(const messageText: string);
begin
  Write( textFile, messageText );
  Flush( textFile );
end;

procedure TFileLog.println( const messageText: string );
begin
  Writeln( textFile, messageText );
  Flush( textFile );
end;

{ TPrefixLog }

constructor TPrefixLog.Create(logTo: ILog; prefix: TPrefixBuilder);
begin
  inherited Create();
  self.targetLog := logTo;
  self.prefixBuilder := prefix;
  self.needPrefix := true;
end;

destructor TPrefixLog.Destroy;
begin
  self.prefixBuilder := nil;
  self.targetLog := nil;
  inherited Destroy;
end;

procedure TPrefixLog.print(const messageText: string);
var
  newMessage: string;
  prefixMessage: string;
begin
  if self.needPrefix then
    begin
      self.needPrefix := false;
      prefixMessage := prefixBuilder();
      newMessage := prefixMessage + messageText;
      targetLog.print(newMessage);
    end
  else
    begin
      targetLog.print(messageText);
    end;
end;

procedure TPrefixLog.println(const messageText: string);
var
  newMessage: string;
  prefixMessage: string;
begin
  if self.needPrefix then
    begin
      prefixMessage := prefixBuilder();
      newMessage := prefixMessage + messageText;
      targetLog.println(newMessage);
    end
  else
    begin
      targetLog.println(messageText);
      self.needPrefix := true;
    end;
end;

{ TConstPrefixLog }

constructor TConstPrefixLog.Create(text: string);
begin
  inherited Create();
  self.prefix := text;
end;

destructor TConstPrefixLog.Destroy;
begin
  inherited Destroy();
end;

function TConstPrefixLog.getMessage: string;
begin
  result := self.prefix;
end;

{ TDateTimePrefixLog }

constructor TDateTimePrefixLog.Create;
begin
  inherited Create();

  self.fmt.ShortDateFormat :='yyyy-MM-dd';
  self.fmt.DateSeparator :='-';
  self.fmt.LongTimeFormat :='HH:nn:ss.zzz';
  self.fmt.TimeSeparator :=':';
end;

destructor TDateTimePrefixLog.Destroy;
begin
  inherited Destroy();
end;

function TDateTimePrefixLog.getMessage: string;
var
  time: TDateTime;
begin
  time := now();
  result := DateTimeToStr(time,self.fmt);
end;

{ TDelegateLog }

procedure TDelegateLog.addListener(target: TDelegateLogListenerProc);
begin
  if assigned(self.onTargetListeners) then begin
    self.onTargetListeners.Add(TDelegateLogListener.Create(target));
  end;
end;

procedure TDelegateLog.addListener(target: TDelegateLogListenerMethod);
begin
  if assigned(self.onTargetListeners) then begin
    self.onTargetListeners.Add(TDelegateLogListener.Create(target));
  end;
end;

constructor TDelegateLog.Create;
begin
  inherited Create;
  self.target := TDummyLog.Create;
  self.onTargetListeners := TList.Create;
end;

destructor TDelegateLog.Destroy;
var
  i:Integer;
  ls: TDelegateLogListener;
begin
  self.target := nil;

  if assigned(self.onTargetListeners) then begin
    for i:=0 to (self.onTargetListeners.Count-1) do begin
      ls := self.onTargetListeners[i];
      if assigned(ls) then begin
        ls.Destroy;
      end;
    end;
    self.onTargetListeners.Destroy;
    Self.onTargetListeners := nil;
  end;

  inherited Destroy;
end;

procedure TDelegateLog.print(const messageText: string);
begin
  self.target.print(messageText);
end;

procedure TDelegateLog.println(const messageText: string);
begin
  self.target.println(messageText);
end;

procedure TDelegateLog.setTarget(const target: ILog);
var
  i:Integer;
  ls: TDelegateLogListener;
begin
  if assigned(target) then begin
    self.target := target;
    if assigned(self.onTargetListeners) then begin
      for i:=0 to (self.onTargetListeners.Count-1) do begin
        ls := self.onTargetListeners[i];
        if assigned(ls) then begin
          ls.fire;
        end;
      end;
    end;
  end;
end;

type

// Инициализация логгирования
TInitLog = class
  public
    procedure reInit;
end;

var
  initLog: TInitLog;
  fileLog: ILog;

{ TInitLog }

procedure TInitLog.reInit;
begin
  if applicationConfigObj.isLogEnabled then begin
    if not assigned(fileLog) then begin
      fileLog := TFileLog.Create(
        GetCurrentDir()+'\'+applicationConfigObj.getLogFilename,
        applicationConfigObj.isAppendLogFile
      );
    end;
    rootLog.setTarget(fileLog);
  end else begin
    rootLog.setTarget(dummyLog);
  end;
end;

{ TDelegateLogListener }

constructor TDelegateLogListener.Create(target: TDelegateLogListenerProc);
begin
  self.proc := target;
  self.meth := nil;
end;

constructor TDelegateLogListener.Create(
  target: TDelegateLogListenerMethod);
begin
  self.proc := nil;
  self.meth := target;
end;

destructor TDelegateLogListener.Destroy;
begin
  self.proc := nil;
  self.meth := nil;
end;

procedure TDelegateLogListener.fire;
begin
  if assigned(self.proc) then self.proc;
  if assigned(self.meth) then self.meth;
end;

initialization
  // Общий лог - без всяких префиксов логирования
  rootLog := TDelegateLog.Create;
  unnamedLog := rootLog;

  // Отсуствие логирования
  dummyLog := TDummyLog.Create;

  // Инициализация логгирования
  initLog := TInitLog.Create;
  applicationConfigObj.addListener(initLog.reInit);

end.

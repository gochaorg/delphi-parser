unit Loggers;

// ��������� ������������ ��������
// ������ - ��� ������ ������� ����� � ��� ��������� 
// � �������������� ���������� (�������� ���� ������)

interface

uses
  Map, Logging, SyncObjs, SysUtils;

// ������� ��� ���������� ����� ��������� ����������� ������           
// ���������
//   name - ��� �������
// ���������
//   ������
function logger(name:WideString):ILog;

implementation

type

// �������� ��������� ��������
ILoggers = interface
  // ������� ��� ���������� ����� ��������� ������
  function GetLogger(name:WideString): ILog;
end;

// ���������� ILoggers
// ������� �� ���������, � ���������� �� ���������� ����� �������
TLoggers = class(TInterfacedObject, ILoggers)
  private
    // ��������� ����������� ��������
    loggers: TStringMap;

    // ���������� ��� ���������� �������� �������
    lock: TCriticalSection;
  public
    constructor Create();
    destructor Destroy(); override;
    function GetLogger(name:WideString): ILog;
end;

// �������� ������ �� ������
TLogHolder = class
  private
    logValue: ILog;
  public
    constructor Create( log:ILog );
    destructor Destroy();

    // ���������� ������ �� ������
    property log:ILog read logValue;
end;

{ TLoggers }

constructor TLoggers.Create;
begin
  inherited Create;
  self.loggers := TStringMap.Create;
  self.lock := TCriticalSection.Create;
end;

destructor TLoggers.Destroy;
begin
  FreeAndNil(self.lock);
  inherited Destroy;
end;

function TLoggers.GetLogger(name: WideString): ILog;
var
  value : Variant;
  logger: ILog;
  logHolder: TLogHolder;
begin
  self.lock.Enter;
  try

    if self.loggers.exists(name) then begin
      value := self.loggers.get(name);
      logHolder := TVarData(value).VPointer;
      result := logHolder.log;
    end else begin
      logger := TPrefixLog.Create(rootLog,
        TDateTimePrefixLog.Create.getMessage);

      logger := TPrefixLog.Create(logger,
        TConstPrefixLog.Create(' '+name+' >> ').getMessage);

      TVarData(value).VType := VarByRef or VarUnknown;
      TVarData(value).VPointer := TLogHolder.Create(logger);

      self.loggers.put(name, value);

      result := logger;
    end;

  finally
    self.lock.Leave;
  end;
end;

{ TLogHolder }

constructor TLogHolder.Create(log: ILog);
begin
  self.logValue := log;
end;

destructor TLogHolder.Destroy;
begin
  self.logValue := nil;
end;

var
loggersInstances : TLoggers;

function logger(name:WideString):ILog;
begin
  result := loggersInstances.GetLogger(name);
end;

initialization
loggersInstances := TLoggers.Create;

end.

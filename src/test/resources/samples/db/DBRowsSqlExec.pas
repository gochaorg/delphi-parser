unit DBRowsSqlExec;

interface

  uses
    ADODB, ComObj, Variants,

    Loggers, Logging, Map;

  type

  /////////////////////////////////////////////
  // ���������� �������� ��� ������� �����
  TDBRowsSqlExec = class(TObject)
  private
    query: TADOQuery;
    mapParams: TStringMap;
    errorsCount: Integer;
  public
    // �����������
    // ���������
    //   query - ������ ������ ����� ��������
    constructor Create( query:TADOQuery );
    destructor Destroy; override;

    // ��������� ����� ������� ���������� ��� ���������
    // ���������
    //   columnName - ������ � ������, ��� �������
    //   paramName - ��� ��������� SQL
    procedure Map( columnName:string; paramName:string );  virtual;

    // ���������� �������
    // ���������
    //   row - ������ ������
    procedure Execute(row:TStringMap); virtual;

    // ���������� ���-�� ������ ��� ���������� �������
    function getErrorsCount: Integer; virtual;
  end;

implementation

var
  log: ILog;

{ TDBRowsSqlExec }

constructor TDBRowsSqlExec.Create(query: TADOQuery);
begin
  inherited Create();
  self.query := query;
  self.mapParams := TStringMap.Create;
  self.errorsCount := 0;
end;

destructor TDBRowsSqlExec.Destroy;
begin
  self.query := nil;
  self.mapParams.Destroy;
  self.mapParams := nil;
  inherited;
end;

procedure TDBRowsSqlExec.Map( columnName:string; paramName:string );
begin
  self.mapParams.put(columnName, paramName);
end;

procedure TDBRowsSqlExec.Execute(row: TStringMap);
var
  columnName: string;
  paramName: string;
  paramValue: Variant;
  i: Integer;
begin
  log.println('execute sql '+self.query.SQL.Text);
  for i:=0 to self.mapParams.count-1 do begin
    columnName := self.mapParams.key(i);
    paramName := VarToStr(self.mapParams.get(columnName));
    paramValue := row.get(columnName);
    log.println('  param '+paramName+' = '+VarToStr(paramValue));
    self.query.Parameters.ParamByName(paramName).Value := paramValue;
  end;
  try
    self.query.ExecSQL;
    log.println('  successfully executed');
  except
    on e:EOleException do begin
      self.errorsCount := self.errorsCount + 1;
      log.println('  got error: '+e.Message);
    end
  end;
end;


function TDBRowsSqlExec.getErrorsCount: Integer;
begin
  result := self.errorsCount;
end;

initialization
log := logger('DBRowsSqlExec');

end.

unit DMLOperation;

interface

uses
  DB, ADODB, SysUtils, Variants,

  Logging, Loggers,
  Map;

type

// Функции для добавления / обновления информации о сущности в БД
// Представляет информацию о машине
IDMLOperation = interface
  // Выполнение операции
  // Аргументы
  //   connection - Соединение с БД
  // Возвращает
  //   id добавленной или обновленной записи
  function Execute( connection: TADOConnection ): Variant;
end;

// Выполнение операции Insert и получение сгенерированного идентификатора
TSqlInsertOperation = class(TInterfacedObject,IDMLOperation)
  private
    sql: WideString;
    params: TStringMap;
    generatedIdColumn: WideString;
  public
    // Конструктор
    // Аргументы
    //   sql - Запрос
    //   params - Параметры
    //   generatedIdColumn - название колонки в
    //                       выборке содержащаяя идентификатор
    constructor Create(
      sql:WideString;
      params:TStringMap;
      generatedIdColumn: WideString
    );
    destructor Destroy; override;
    function Execute( connection: TADOConnection ): Variant;
end;

// Выполнение операции update
TSqlUpdateOperation = class(TInterfacedObject,IDMLOperation)
  private
    sql: WideString;
    params: TStringMap;
  public
    // Конструктор
    // Аргументы
    //   sql - Запрос
    //   params - Параметры
    constructor Create(
      sql:WideString;
      params:TStringMap
    );
  destructor Destroy; override;
  function Execute( connection: TADOConnection ): Variant;
end;

implementation

var
log: ILog;

{ TSqlExecOperation }

constructor TSqlInsertOperation.Create(
  sql: WideString;
  params: TStringMap;
  generatedIdColumn: WideString
);
begin
  inherited Create;
  self.sql := sql;
  self.params := params;
  self.generatedIdColumn := generatedIdColumn;
end;

destructor TSqlInsertOperation.Destroy;
begin
  FreeAndNil(self.params);
  inherited Destroy;
end;

function TSqlInsertOperation.Execute(connection: TADOConnection): Variant;
var
  query: TADOQuery;
  name: String;
  param_value: variant;
  i: Integer;
begin
  log.println('TSqlInsertOperation.Execute');
  log.println('query := TADOQuery.Create(nil)');
  query := TADOQuery.Create(nil);
  try
    query.Connection := connection;

    log.println('query.SQL.Text := '+self.sql);
    query.SQL.Text := self.sql;
    for i:=0 to self.params.count - 1 do begin
      name := self.params.key(i);
      param_value := self.params.get(name);

      log.println('param '+name+' = '+VarToStr(param_value));
      query.Parameters.ParamByName(name).Value := param_value;
    end;

    log.println('query.Open');
    query.Open;
    query.First;
    while not query.Eof do begin
      result := query.FieldByName(self.generatedIdColumn).Value;
      query.Next;
    end;
    query.Close;
  finally
    log.println('Free TADOQuery');
    query.Connection := nil;
    FreeAndNil(query);
  end;
end;

{ TSqlUpdateOperation }

constructor TSqlUpdateOperation.Create(
  sql: WideString;
  params: TStringMap
);
begin
  inherited Create;
  self.sql := sql;
  self.params := params;
end;

destructor TSqlUpdateOperation.Destroy;
begin
  FreeAndNil(self.params);
  inherited Destroy;
end;

function TSqlUpdateOperation.Execute( connection: TADOConnection ): Variant;
var
  query: TADOQuery;
  name: String;
  i: Integer;
  param_value: variant;
  update_count: Integer;
begin
  result := UnAssigned();

  log.println('TSqlUpdateOperation.Execute');
  log.println('query := TADOQuery.Create(nil)');
  query := TADOQuery.Create(nil);
  try
    query.Connection := connection;

    log.println('query.SQL.Text := '+self.sql);
    query.SQL.Text := self.sql;
    for i:=0 to self.params.count - 1 do begin
      name := self.params.key(i);
      param_value := self.params.get(name);

      log.println('param '+name+' = '+VarToStr(param_value));
      query.Parameters.ParamByName(name).Value := param_value;
    end;

    log.println('query.ExecSQL');
    update_count := query.ExecSQL;

    log.println('  update count '+IntToStr(update_count));
  finally
    query.Connection := nil;
    FreeAndNil(query);
  end;
end;

initialization
log := logger('DMLOperation');

end.

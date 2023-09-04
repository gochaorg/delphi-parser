unit WaybillSQLView;

// Функции SQL по работе с путевыми листами
// Составление запросов к СУБД

interface

uses
  SysUtils, ADODB, Variants, DB,

  Loggers, Logging,
  Map;

type

// Готовый запрос к СУБД для выполнения
// Имеется ввиду SELECT
IWaybillsQuery = interface
  // Применяет запрос к объекту TADOQuery
  procedure apply( query:TADOQuery );
end;

// Реализация IWaybillsQuery
TWaybillsQuery = class(TInterfacedObject, IWaybillsQuery)
  private
    sql: WideString;
    params: TStringMap;
    cursorLocation: TCursorLocation;
  public
    constructor Create(sql:WideString; params:TStringMap; cursorLocation:TCursorLocation);
    destructor Destroy; override;
    procedure apply( query:TADOQuery );
end;

// Контекст для построения параметров запроса
IParamBuildContext = interface
  // Возвращает кол-во параметров
  function GetParamCount:Integer;

  // Добавляет параметр
  //   name - имя параметра
  //   value - значение
  // Возвращает
  //   true - успешно
  //   fale - не добавлен, тк уже параметр с таким именем уже ранее был добавлен
  function AddParam( name:string; value:variant ):boolean;
end;

// Реализация контекста параметров
TParamBuildContext = class(TInterfacedObject,IParamBuildContext)
  private
    params: TStringMap;
    ownParams: boolean;
  public
    constructor Create(params:TStringMap; ownParams:boolean);
    destructor Destroy; override;
    function GetParamCount:Integer;
    function AddParam( name:string; value:variant ):boolean;
end;

// Некое обощеное where выражение в SQL/SELECT запросе
IWhereExpression = interface
  // Генерирует часть SQL запроса
  // Аргументы
  //   paramContext - параметры запроса
  // Результат
  //   Часть SQL запроса
  ['{E3A98B73-0FD2-49BF-BB50-61A0592C5D71}']
  function BuildSql( paramContext:IParamBuildContext ):string;
end;

// операция LIKE для отображаемых данных в части WHERE
// Применяется для запроса содержащего колонку search_text типа char/varchar/...
TWhereSearchTextLikeExpression = class(TInterfacedObject,IWhereExpression)
  private
    what: WideString;
  public
    // Конструктор
    // Аргументы
    //   what - искомая часть
    constructor Create( what:WideString );
    function BuildSql( paramContext:IParamBuildContext ):string;
end;

// операция and для where выражение
TWhereAndExpression = class(TInterfacedObject,IWhereExpression)
  private
    leftExpression : IWhereExpression;
    rightExpression : IWhereExpression;
  public
    // Конструктор
    //   leftExp  - левый операнд оператора AND
    //   rightExp - правый операнд оператора AND
    constructor Create(leftExp: IWhereExpression; rightExp: IWhereExpression);
    function BuildSql( paramContext:IParamBuildContext ):string;
end;

// операция or для where выражение
TWhereOrExpression = class(TInterfacedObject,IWhereExpression)
  private
    leftExpression : IWhereExpression;
    rightExpression : IWhereExpression;
  public
    // Конструктор
    //   leftExp  - левый операнд оператора OR
    //   rightExp - правый операнд оператора OR
    constructor Create(leftExp: IWhereExpression; rightExp: IWhereExpression);
    function BuildSql( paramContext:IParamBuildContext ):string;
end;

// операция like для where выражение
TWhereLikeExpression = class(TInterfacedObject,IWhereExpression)
  private
    key:string;
    value:variant;
  public
    // Конструктор
    //   key - левый операнд оператора LIKE - имя колонки/выражение
    //   value - правый операнд оператора LIKE
    constructor Create(key:string; value:variant);
    function BuildSql( paramContext:IParamBuildContext ):string;
end;

// пустое выражение,
// должно использоваться самостоятельно бeз других выражений
IWhereEmptyExpression = interface(IWhereExpression)
   ['{89CDB6E5-C5E2-45E8-9C25-3F2211FCEFE6}']
end;
TWhereEmptyExpression = class(TInterfacedObject,IWhereExpression,IWhereEmptyExpression)
  public
    constructor Create();
    function BuildSql(paramContext:IParamBuildContext ):string;
end;

// Создание запроса
IWaybillsQueryBuilder = interface
  // Создает запрос
  function build:IWaybillsQuery;

  // свойство history - отображать или нет историю
  function getHistory:boolean;
  procedure setHistory(show:boolean);
  property history:boolean read getHistory write setHistory;

  // Свойство where
  function getWhereExpression: IWhereExpression;
  procedure setWhereExpression( expression:IWhereExpression );
  property whereExpresion:IWhereExpression
    read getWhereExpression
    write setWhereExpression;

  // Возвращает
  //   true - свойство whereExpresion установленно
  //   false - свойство whereExpresion не установленно, не содержит значения
  function hasWhereExpression: boolean;

  // очищает свойство whereExpresion
  procedure resetWhereExpression;

  // Сброс параметров сортировки
  procedure resetOrder;

  // Указание сортировки
  //   orderKey - имя колонки по которой происходит файтическая сортировка
  //   reverse - обратная (true) или прямая сортировка
  procedure setOrder( orderKey:WideString; reverse:boolean );

  // Устанавливает сортировку или переключает направление
  // С учетом отображаемых синонимичных колонок
  //   columnAlias - имя колонки в выборке 
  procedure toggleOrder( columnAlias:WideString );

end;

// Констркуктор запроса
TWaybillsQueryBuilder = class(TInterfacedObject, IWaybillsQueryBuilder)
  private
    withHistoryValue: boolean;
    searchIsSet: boolean;

    // может быть nil
    whereExpressionValue: IWhereExpression;

    // задан или нет параметр сортировки    
    orderIsSet: boolean;

    // сортировка по какому полю
    orderKey: WideString;

    // true - обратная сортировка
    // false - прямая сортировка
    orderReverse: boolean;
  public
    constructor Create;
    destructor Destroy; override;

    // свойство history
    function getHistory:boolean;
    procedure setHistory(show:boolean);
    property history:boolean read getHistory write setHistory;

    // свойство whereExpresion
    function getWhereExpression: IWhereExpression;
    procedure setWhereExpression( expression:IWhereExpression );
    property whereExpresion:IWhereExpression
      read getWhereExpression
      write setWhereExpression;

    function hasWhereExpression: boolean;
    procedure resetWhereExpression;

    // свойство  order
    procedure resetOrder;
    procedure setOrder( orderKey:WideString; reverse:boolean );
    procedure toggleOrder( columnAlias:WideString );

    function build:IWaybillsQuery;
end;

// Выбираемая колонка
TWaybillColumn = class
  private
    // синоним после ключевого слова AS
    aliasValue: WideString;

    // выражение
    expressionValue: WideString;

    // колонка видимая
    visibleValue: boolean;

    // ключ (колонка) для сортировки
    orderKeyValue: WideString;

    // ключ для сортирвки задан
    orderKeyExistsValue: boolean;
  public
    constructor Create(
      alias:WideString;
      expr:WideString;
      visible:boolean;
      orderKey: WideString;
      orderKeyExists: boolean
    );

    // синоним после ключевого слова AS
    property alias:WideString read aliasValue;

    // выражение
    property expression:WideString read expressionValue;

    // колонка видимая
    property visible:boolean read visibleValue;

    // ключ (колонка) для сортировки
    property orderKey: WideString read orderKeyValue;
    
    // ключ для сортирвки задан
    property orderKeyExists: boolean read orderKeyExistsValue;
end;

implementation

var
  // логи
  log:ILog;

  // акутальные колонки в SELECT
  columns:array[0 .. 17] of TWaybillColumn;

  // исторические колонки в SELECT
  histColumns:array[0 .. 17] of TWaybillColumn;
  
  emptyWhere: IWhereEmptyExpression;

{ TWaybillsQuery }

procedure TWaybillsQuery.apply(query: TADOQuery);
var
  i:Integer;
  name:String;
  value:variant;
begin
  log.println('query.Active := false');
  query.Active := false;

  if cursorLocation = clUseClient
    then log.println('query.CursorLocation := clUseClient')
  else if cursorLocation = clUseServer
    then log.println('query.CursorLocation := clUseServer')
  else log.println('query.CursorLocation := ???');

  query.CursorLocation := self.cursorLocation;

  log.println('query.SQL.Text := '+self.sql);
  query.SQL.Text := self.sql;

  for i:=0 to (self.params.count-1) do begin
    name := self.params.key(i);
    value := self.params.get(name);

    log.println('query param '+name+' = '+VarToStr(value));
    query.Parameters.ParamByName(name).Value := value;
  end;

  log.println('query.Active := true');
  try
    query.Active := true;
  except
    on e:EDatabaseError do begin
      log.println('! query.Active error: '+e.Message);
    end;
  end;
end;

constructor TWaybillsQuery.Create(sql: WideString; params: TStringMap; cursorLocation:TCursorLocation);
begin
  inherited Create;
  self.sql := sql;
  self.params := params;
  self.cursorLocation := cursorLocation;  
  log.println('TWaybillsQuery.Create');
end;

destructor TWaybillsQuery.Destroy;
begin
  log.println('TWaybillsQuery.Destroy');
  FreeAndNil(self.params);
  inherited Destroy;
end;

{ TWaybillsQueryBuilder }

constructor TWaybillsQueryBuilder.Create;
begin
  self.withHistoryValue := false;
end;

destructor TWaybillsQueryBuilder.Destroy;
begin
  inherited Destroy;
end;

function TWaybillsQueryBuilder.build: IWaybillsQuery;
  // Построение sql - перечня выбираемых колонок: select _это_
  function columnsExp( cols: array of TWaybillColumn ):WideString;
    var
      exp:WideString;
      i: Integer;
  begin
    exp := '';
    for i:=low(cols) to high(cols) do begin
      if i>0 then exp := exp + ' , ';
      exp := exp + ' ' + cols[i].expression + ' as ' + cols[i].alias;
    end;
    result := exp;
  end;
var
  sql: WideString;
  params: TStringMap;
begin
  // отображаемые столбцы
  sql := 'select * from ( select * from ( select ' +
    columnsExp(columns) +
    ' from '+
    ' waybills w '+
    ' left join drivers dr on (dr.id = w.driver) '+
    ' left join dispatchers ds on (ds.id = w.dispatcher) '+
    ' left join cars c on (c.id = w.car) '+
    ' left join cars_model cm on (c.model = cm.id) '+
    ') a'
    ;

  // исторические данные
  if self.withHistoryValue then begin
    sql := sql + ' union all ';
    sql := sql + 'select * from ( select ' +
      columnsExp(histColumns) +
      ' from '+
      ' waybills_hist w '+
      ' left join drivers dr on (dr.id = w.driver) '+
      ' left join dispatchers ds on (ds.id = w.dispatcher) '+
      ' left join cars c on (c.id = w.car) '+
      ' left join cars_model cm on (c.model = cm.id) '+
      ') b'
      ;
  end;
  sql := sql + ') aa';

  log.println('params := TStringMap.Create');
  params := TStringMap.Create;

  if self.hasWhereExpression then begin
    log.println('hasWhereExpression');
    sql := sql + ' where ' +
      self.whereExpresion.BuildSql(
        TParamBuildContext.Create(params,false));
  end;

  if self.orderIsSet then begin
    if self.orderReverse
    then sql := sql + ' order by '+self.orderKey
    else sql := sql + ' order by '+self.orderKey+' desc';
  end;

  if self.withHistoryValue
  then result := TWaybillsQuery.Create(sql, params, clUseClient)
  else result := TWaybillsQuery.Create(sql, params, clUseServer);
end;


function TWaybillsQueryBuilder.getHistory: boolean;
begin
  result := self.withHistoryValue;
end;

procedure TWaybillsQueryBuilder.setHistory(show: boolean);
begin
  self.withHistoryValue := show;
  log.println('setHistory '+BoolToStr(show));
end;

function TWaybillsQueryBuilder.hasWhereExpression: boolean;
begin
  result := true;
  if not assigned(self.whereExpressionValue)
  then result := false
  else if Supports(self.whereExpressionValue,IWhereEmptyExpression)
  then result := false;
end;

function TWaybillsQueryBuilder.getWhereExpression: IWhereExpression;
begin
  if assigned(self.whereExpressionValue)
  then result := self.whereExpressionValue
  else result := emptyWhere;
end;

procedure TWaybillsQueryBuilder.resetWhereExpression;
begin
  log.println('resetWhereExpression');
  self.whereExpressionValue := nil;
end;

procedure TWaybillsQueryBuilder.setWhereExpression(
  expression: IWhereExpression);
begin
  log.println('setWhereExpression');
  self.whereExpressionValue := expression;
end;

procedure TWaybillsQueryBuilder.resetOrder;
begin
  log.println('resetOrder');
  self.orderIsSet := false;
end;

procedure TWaybillsQueryBuilder.setOrder(orderKey: WideString;
  reverse: boolean);
begin
  log.println('setOrder');
  self.orderIsSet := true;
  self.orderKey := orderKey;
  self.orderReverse := reverse;
end;

procedure TWaybillsQueryBuilder.toggleOrder(columnAlias: WideString);
var
  i:Integer;
begin
  log.println('toggleOrder');
  for i:=low(columns) to high(columns) do begin
    if (columns[i].alias = columnAlias) and (columns[i].orderKeyExists)
    then begin
      if self.orderKey = columns[i].orderKey
      then self.orderReverse := not self.orderReverse
      else begin
        self.orderIsSet := true;
        self.orderReverse := false;
        self.orderKey := columns[i].orderKey;
      end;

      break;
    end;
  end;
end;

{ TWaybillColumn }

constructor TWaybillColumn.Create(
  alias, expr: WideString;
  visible: boolean;
  orderKey: WideString;
  orderKeyExists: boolean
);
begin
  self.aliasValue := alias;
  self.expressionValue := expr;
  self.visibleValue := visible;
  self.orderKeyValue := orderKey;
  self.orderKeyExistsValue := orderKeyExists;
end;

{ TWhereAndExpression }

constructor TWhereAndExpression.Create(leftExp,
  rightExp: IWhereExpression);
begin
  self.leftExpression := leftExp;
  self.rightExpression := rightExp;
end;

function TWhereAndExpression.BuildSql(
  paramContext: IParamBuildContext): string;
begin
  Result := ' ( ' +
    self.leftExpression.BuildSql(paramContext) + ' and ' +
    self.rightExpression.BuildSql(paramContext) + ' ) ';
end;


{ TWhereOrExpression }

constructor TWhereOrExpression.Create(leftExp, rightExp: IWhereExpression);
begin
  log.println('TWhereOrExpression.Create');
  self.leftExpression := leftExp;
  self.rightExpression := rightExp;
end;

function TWhereOrExpression.BuildSql(
  paramContext: IParamBuildContext): string;
begin
  result :=
    ' ( ' +
    leftExpression.BuildSql(paramContext) + ' or ' +
    rightExpression.BuildSql(paramContext) + ' ) ';
end;

{ TWhereLikeExpression }

constructor TWhereLikeExpression.Create(key: string; value: variant);
begin
  log.println('TWhereLikeExpression.Create');
  self.key := key;
  self.value := value;
end;

function TWhereLikeExpression.BuildSql(
  paramContext: IParamBuildContext): string;
var
  name : string;
begin
  log.println('TWhereLikeExpression.BuildSql');
  name := 'p_' + self.key + IntToStr(paramContext.GetParamCount);
  if not paramContext.AddParam(name, self.value) then begin
    log.println('! param '+name+' not added into context');
  end;
  Result := self.key + ' like :'+name;
end;

{ TParamBuildContext }

constructor TParamBuildContext.Create(params: TStringMap;
  ownParams: boolean);
begin
  inherited Create;
  self.params := params;
  self.ownParams := ownParams;
end;

destructor TParamBuildContext.Destroy;
begin
  if self.ownParams
  then FreeAndNil(self.params)
  else self.params := nil;

  inherited Destroy;
end;

function TParamBuildContext.AddParam(
  name: string;
  value: variant): boolean;
begin
  if self.params.exists(name) then begin
    log.println('! parameter '+name+' already added');
    result := false;
  end else begin
    log.println(
      'TParamBuildContext.AddParam '+
      name+'='+VarToStr(value)
    );
    self.params.put(name, value);
    result := true;
  end;
end;

function TParamBuildContext.GetParamCount: Integer;
begin
  result := self.params.count;
end;

{ TWhereEmptyExpression }

constructor TWhereEmptyExpression.Create;
begin
  inherited Create;
end;

function TWhereEmptyExpression.BuildSql(
  paramContext: IParamBuildContext): string;
begin
  result := '';
end;

{ TWhereSearchTextLikeExpression }

constructor TWhereSearchTextLikeExpression.Create(what: WideString);
begin
  self.what := what;
end;

function TWhereSearchTextLikeExpression.BuildSql(
  paramContext: IParamBuildContext): string;
var
  name : string;
begin
  name := 'p_search_'+IntToStr(paramContext.GetParamCount);
  if not paramContext.AddParam(name, what) then begin
    log.println('! can''t add parameter '+name);
  end;
  result := ' search_text like :'+name+' ';
end;

type
IWaybillColumnBuilder = interface
  function Build:TWaybillColumn;
  function Visible:IWaybillColumnBuilder;
  function OrderBy(key:WideString):IWaybillColumnBuilder;
end;

// Шаблон Builder для TWaybillColumn
TWaybillColumnBuilder = class(TInterfacedObject,IWaybillColumnBuilder)
  private
    aliasValue: WideString;
    exprValue: WideString;
    visibleValue: boolean;
    orderKeyValue: WideString;
    orderKeyExistsValue: boolean;
  public
    constructor Create(alias:WideString; expr:WideString);
    destructor Destroy; override;

    function Build:TWaybillColumn;

    function Visible:IWaybillColumnBuilder;
    function OrderBy(key:WideString):IWaybillColumnBuilder;
end;

{ TWaybillColumnBuilder }

function column(alias:WideString; expr:WideString):IWaybillColumnBuilder;
begin
  result := TWaybillColumnBuilder.Create(alias, expr);
end;

constructor TWaybillColumnBuilder.Create(alias:WideString; expr:WideString);
begin
  inherited Create;
  self.aliasValue := alias;
  self.exprValue := expr;
  self.visibleValue := false;
  self.orderKeyValue := '';
  self.orderKeyExistsValue := false;
end;

destructor TWaybillColumnBuilder.Destroy;
begin
  inherited Destroy;
end;

function TWaybillColumnBuilder.Build: TWaybillColumn;
begin
  result := TWaybillColumn.Create(
    self.aliasValue,
    self.exprValue,
    self.visibleValue,
    self.orderKeyValue,
    self.orderKeyExistsValue
  );
end;

function TWaybillColumnBuilder.Visible: IWaybillColumnBuilder;
begin
  self.visibleValue := true;
  result := self;
end;

function TWaybillColumnBuilder.OrderBy(
  key: WideString): IWaybillColumnBuilder;
begin
  self.orderKeyValue := key;
  self.orderKeyExistsValue := true;
  result := self;
end;

initialization
log := logger('WaybillSQLView');

emptyWhere := TWhereEmptyExpression.Create;

// колонки актуальных данных
columns[ 0] := column('id', 'w.id').Visible.OrderBy('id').Build;

columns[ 1] := column('state', '''actual''').Build;

columns[ 2] := column('car_id', 'w.car').Build;

columns[ 3] := column('car_model_id', 'c.model').Build;

columns[ 4] := column('car_model_name', 'cm.name')
  .Visible.OrderBy('car_model_name').Build;

columns[ 5] := column('car_total_wear',
  'isnull((select sum(wear) from waybills where car = w.car), 0) + c.wear'
  ).Visible.OrderBy('car_total_wear').Build;

columns[ 6] := column('car_legal_number', 'c.legal_number')
  .Visible.OrderBy('car_legal_number').Build;

columns[ 7] := column('driver_id', 'w.driver').Build;

columns[ 8] := column('driver_name', 'dr.name')
  .Visible.OrderBy('driver_name').Build;

columns[ 9] := column('dispatcher_id', 'w.dispatcher').Build;

columns[10] := column('dispatcher_name', 'ds.name')
  .Visible.OrderBy('dispatcher_name').Build;

columns[11] := column('outcome_date', 'w.outcome_date').Build;

columns[12] := column(
  'outcome_date_s', 'convert( nvarchar(100), w.outcome_date, 23 ) '+
  ' + '' '' + convert( nvarchar(50), w.outcome_date, 108 )')
  .Visible.OrderBy('outcome_date').Build;

columns[13] := column('income_date', 'w.income_date').Build;

columns[14] := column('income_date_s',
  'convert( nvarchar(100), w.income_date, 23 ) '+
  '+ '' '' + convert( nvarchar(50), w.income_date, 108 )')
  .Visible.OrderBy('income_date').Build;

columns[15] := column('fuel_cons', 'w.fuel_cons')
  .Visible.OrderBy('fuel_cons').Build;

columns[16] := column('wear', 'w.wear')
  .Visible.OrderBy('wear').Build;

columns[17] := column('search_text',
	'cm.name + c.legal_number + dr.name + ds.name +' +
	'(convert( nvarchar(100), w.outcome_date, 23 ) + '' '' + convert( nvarchar(50), w.outcome_date, 108 )) +' +
	'(convert( nvarchar(100), w.income_date, 23 ) + '' '' + convert( nvarchar(50), w.income_date, 108 ))'+
	'+ cast(isnull((select sum(wear) from waybills where car = w.car), 0) + c.wear as nvarchar(100))'+
	'+ cast(w.fuel_cons as nvarchar(50))'+
	'+ cast(w.wear as nvarchar(50))'+
	'+ cast(w.id as nvarchar(50))'
  ).Build;

// колонки исторических данных
histColumns[ 0] := columns[0];
histColumns[ 1] := column('state', '''hist''').Build;
histColumns[ 2] := columns[2];
histColumns[ 3] := columns[3];
histColumns[ 4] := columns[4];
histColumns[ 5] := columns[5];
histColumns[ 6] := columns[6];
histColumns[ 7] := columns[7];
histColumns[ 8] := columns[8];
histColumns[ 9] := columns[9];
histColumns[10] := columns[10];
histColumns[11] := columns[11];
histColumns[12] := columns[12];
histColumns[13] := columns[13];
histColumns[14] := columns[14];
histColumns[15] := columns[15];
histColumns[16] := columns[16];
histColumns[17] := columns[17];

end.

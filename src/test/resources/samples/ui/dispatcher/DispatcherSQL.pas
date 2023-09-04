unit DispatcherSQL;

interface

uses
  SysUtils,
  ADODB, DateUtils,

  SQLDateParam,
  Config,
  Loggers,Logging,
  MyDate,
  Map,
  DMLOperation,
  Validation;

type

// Ошибка создания/валидации при построение операций
EDispatcherDataBuilder = class(Exception);

// Шаблон строитель
// для создания либо запроса insert либо update
IDispatcherDataBuilder = interface
  // Сброс состояния, надо заново указать значения
  procedure Reset;

  // Указывает обновляемую запись
  procedure setDriverId(id:Integer);

  // Указывает имя диспетчера
  procedure setName(name:WideString);

  // Указывает день рождения
  procedure setBirthDay(date:WideString); overload;

  // Указывает день рождения
  // Аргументы
  //   date - строка содрежащаяя дату в формате yyyy-MM-dd
  procedure setBirthDay(date:TDateTime); overload;

  // Проверка данных перед INSERT
  function ValidateInsert: IDataValidation;

  // Создает операцию INSERT.
  // Если какие данные указаны не верно,
  //   то генерирует исключение EDriverDataBuilder
  function BuildInsert: IDMLOperation;

  // Проверка данных перед UPDATE
  function ValidateUpdate: IDataValidation;

  // Создает операцию UPDATE.
  // Если какие данные указаны не верно,
  //   то генерирует исключение EDriverDataBuilder
  function BuildUpdate: IDMLOperation;
end;

// Реализация IDispatcherDataBuilder
TDispatcherDataBuilder = class(TInterfacedObject, IDispatcherDataBuilder)
  private
    driverId: Integer;
    driverIdExists: boolean;

    name: WideString;
    nameExists: boolean;

    birthDay: TMyDate;
    birthDayExists: boolean;
    birthDayConvError: WideString;
  public
    constructor Create;
    destructor Destroy; override;

    procedure Reset;

    procedure setDriverId(id:Integer);

    procedure setName(name:WideString);
    procedure setBirthDay(date:WideString); overload;
    procedure setBirthDay(date:TDateTime); overload;

    function ValidateInsert: IDataValidation;
    function BuildInsert: IDMLOperation;

    function ValidateUpdate: IDataValidation;
    function BuildUpdate: IDMLOperation;
  private
    function Validate(insert:boolean): IDataValidation;
end;

// Запись - диспетчер
TDispatcher = class
  private
    idValue: Integer;
    nameValue: WideString;
  public
    // Конструктор
    // Аргументы
    //   id - ид записи
    //   name - имя диспетчера
    constructor Create( id:Integer; name:WideString );

    // Конструктор копирования
    // Аргументы
    //   sample - пример для копирования
    constructor Copy( sample: TDispatcher );

    // id записи
    property id:Integer read idValue;

    // Имя
    property name:WideString read nameValue;
end;

// Функция принимающая запись о диспетчере
// значение не передается во владение
TDispatcherConsumer = procedure ( car: TDispatcher ) of object;

// Поиск диспетчеров
IDispatcherFinder = interface
  // Поиск всех диспетчеров
  //   consumer - функция принимающая запись
  procedure findAll( consumer:TDispatcherConsumer );

  // Поиск диспетчеров совпадающих условию
  //   what - искомое условие
  //     по name (имя) - операция like
  //   consumer - функция принимающая запись
  procedure findLike( what:WideString; consumer:TDispatcherConsumer );
end;

// Реализация IDispatcherFinder
TDispatcherFinder = class (TInterfacedObject,IDispatcherFinder)
  private
    connection: TADOConnection;
  public
    constructor Create(connection: TADOConnection);
    destructor Destroy; override;

    procedure findAll( consumer:TDispatcherConsumer );
    procedure findLike( what:WideString; consumer:TDispatcherConsumer );
end;

implementation

var
log: ILog;

constructor TDispatcherDataBuilder.Create;
begin
  inherited Create;
end;

destructor TDispatcherDataBuilder.Destroy;
begin
  if assigned(self.birthDay) then FreeAndNil(self.birthDay);
  inherited Destroy;
end;

procedure TDispatcherDataBuilder.Reset;
begin
  self.driverIdExists := false;
  self.nameExists := false;
  self.birthDayExists := false;
  self.birthDayConvError := '';
end;

procedure TDispatcherDataBuilder.setBirthDay(date: WideString);
var
  myDate: TMyDate;
  nextFrom: Integer;
  parseError: WideString;
  validate: IDataValidationMut;
begin
  validate := TDataValidation.Create;
  myDate := TMyDate.Create(0,0,0);
  try
    if TryParseDate(
      date,applicationConfigObj.getDateFormat,
      myDate,validate)
    then begin
      if assigned(self.birthDay) then FreeAndNil(self.birthDay);
      self.birthDay := TMyDate.Copy(myDate);
      self.birthDayExists := true;
      self.birthDayConvError := validate.getMessage;
    end else begin
      self.birthDayExists := false;
      self.birthDayConvError := parseError;
    end;
  finally
    FreeAndNil(myDate);
  end;
end;

procedure TDispatcherDataBuilder.setBirthDay(date: TDateTime);
begin
  if assigned(self.birthDay) then FreeAndNil(self.birthDay);
  self.birthDay := TMyDate.From(date);
  self.birthDayExists := true;
  self.birthDayConvError := '';
end;

procedure TDispatcherDataBuilder.setName(name: WideString);
begin
  self.name := name;
  self.nameExists := true;
end;

procedure TDispatcherDataBuilder.setDriverId(id: Integer);
begin
  self.driverId := id;
  self.driverIdExists := true;
end;

function TDispatcherDataBuilder.ValidateInsert: IDataValidation;
begin
  result := validate(true);
end;

function TDispatcherDataBuilder.ValidateUpdate: IDataValidation;
begin
  result := validate(false);
end;

function TDispatcherDataBuilder.Validate(insert:boolean): IDataValidation;
var
  validation : TDataValidation;
begin
  validation := TDataValidation.Create;
  if not self.nameExists then validation.addError('Не указано имя');
  if not self.birthDayExists then
    if length(self.birthDayConvError)>0 then
      validation.addError('Формат даты задан не верно '+self.birthDayConvError)
    else
      validation.addError('Дата рождения не указана');

  if (not insert) and (not self.driverIdExists) then
    validation.addError('Не указан id обновляемой записи');

  result := validation;
end;

function TDispatcherDataBuilder.BuildInsert: IDMLOperation;
var
  params : TStringMap;
  dmlOp : TSqlInsertOperation;
  validation : IDataValidation;
  sql: string;
begin
  validation := ValidateInsert;
  if not validation.isOk then
    raise EDispatcherDataBuilder.Create(validation.getMessage);

  sql := 'insert into dispatchers (name, birth_day)' +
         ' values (:name, :birth_day) '+
         ';'+
         'select @@IDENTITY as _id';
  params := TStringMap.Create;
  params.put('name', self.name);
  params.put('birth_day',DateToSQL(self.birthDay));

  dmlOp := TSqlInsertOperation.Create(sql, params, '_id');
  result := dmlOp;
end;

function TDispatcherDataBuilder.BuildUpdate: IDMLOperation;
var
  params : TStringMap;
  dmlOp : TSqlUpdateOperation;
  validation : IDataValidation;
  sql: string;
begin
  validation := ValidateUpdate;
  if not validation.isOk then
    raise EDispatcherDataBuilder.Create(validation.getMessage);

  sql := 'update dispatchers set'+
    ' name = :name,'+
    ' birth_day = :birth_day'+
    ' where id = :id';

  params := TStringMap.Create;
  params.put('name', self.name);
  params.put('birth_day', DateToSQL(self.birthDay));
  params.put('id', self.driverId);

  dmlOp := TSqlUpdateOperation.Create(sql, params);
  result := dmlOp;
end;

{ TDispatcher }

constructor TDispatcher.Copy(sample: TDispatcher);
begin
  self.idValue := sample.idValue;
  self.nameValue := sample.nameValue;
end;

constructor TDispatcher.Create(
  id: Integer;
  name: WideString
  );
begin
  self.idValue := id;
  self.nameValue := name;
end;

{ TDispatcherFinder }

constructor TDispatcherFinder.Create(connection: TADOConnection);
begin
  self.connection := connection;
  inherited Create;
end;

destructor TDispatcherFinder.Destroy;
begin
  self.connection := nil;
  inherited Destroy;
end;

function readRow(query:TADOQuery):TDispatcher;
begin
  result := TDispatcher.Create(
    query.FieldValues['id'],
    query.FieldValues['name']
  );
end;

procedure TDispatcherFinder.findAll(consumer: TDispatcherConsumer);
var
  query:TADOQuery;
  dispatcher: TDispatcher;
begin
  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	d.id as id,'+
      '	d.name as name,'+
      '	d.birth_day as birth_day '+
      ' from dispatchers d'+
      ' order by d.name';
    query.Connection := self.connection;
    query.Open;
    while not query.Eof do begin
      dispatcher := readRow(query);
      try
        consumer(dispatcher);
      finally
        FreeAndNil(dispatcher);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

procedure TDispatcherFinder.findLike(what: WideString;
  consumer: TDispatcherConsumer);
var
  query:TADOQuery;
  dispatcher: TDispatcher;
begin
  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	d.id as id,'+
      '	d.name as name,'+
      '	d.birth_day as birth_day '+
      ' from dispatchers d'+
      ' where d.name like :what'+
      ' order by d.name';
    query.Connection := self.connection;
    query.Parameters.ParamByName('what').Value := what;
    query.Open;
    while not query.Eof do begin
      dispatcher := readRow(query);
      try
        consumer(dispatcher);
      finally
        FreeAndNil(dispatcher);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

initialization
log := logger('DispatcherSQL');

end.

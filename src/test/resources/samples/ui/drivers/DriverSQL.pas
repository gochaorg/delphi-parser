unit DriverSQL;

interface

uses
  SysUtils, ADODB,

  SQLDateParam,
  Config,
  Loggers,Logging,
  MyDate,
  Map,
  DMLOperation,
  Validation;

type

// Ошибка создания/валидации при построение операций
EDriverDataBuilder = class(Exception);

// Шаблон строитель
// для создания либо запроса insert либо update
IDriverDataBuilder = interface
  // Сброс состояния, надо заново указать значения
  procedure Reset;

  // Указывает обновляемую запись
  procedure setDriverId(id:Integer);

  // Указывает имя водителя
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

// Реализация IDriverDataBuilder
TDriverDataBuilder = class(TInterfacedObject, IDriverDataBuilder)
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

// Запись - водитель
TDriver = class
  private
    idValue: Integer;
    nameValue: WideString;
  public
    // Конструктор
    // Аргументы
    //   id - ид записи
    //   name - имя водителя
    constructor Create( id:Integer; name:WideString );
    constructor Copy( sample: TDriver );

    // id записи
    property id:Integer read idValue;

    // Имя
    property name:WideString read nameValue;
end;

// Функция принимающая запись о водителе
// значение не передается во владение
TDriverConsumer = procedure ( car: TDriver ) of object;

// Поиск диспетчеров
IDriverFinder = interface
  // Поиск всех диспетчеров
  //   consumer - функция принимающая запись
  procedure findAll( consumer:TDriverConsumer );

  // Поиск диспетчеров совпадающих условию
  //   what - искомое условие
  //     по name (имя) - операция like
  //   consumer - функция принимающая запись
  procedure findLike( what:WideString; consumer:TDriverConsumer );
end;

// Реализация IDriverFinder
TDriverFinder = class (TInterfacedObject,IDriverFinder)
  private
    connection: TADOConnection;
  public
    constructor Create(connection: TADOConnection);
    destructor Destroy; override;

    procedure findAll( consumer:TDriverConsumer );
    procedure findLike( what:WideString; consumer:TDriverConsumer );
end;


implementation

var

// Логирование
log: ILog;

{ TDriverDataBuilder }

constructor TDriverDataBuilder.Create;
begin
  inherited Create;
end;

destructor TDriverDataBuilder.Destroy;
begin
  if assigned(self.birthDay) then FreeAndNil(self.birthDay);
  inherited Destroy;
end;

procedure TDriverDataBuilder.Reset;
begin
  self.driverIdExists := false;
  self.nameExists := false;
  self.birthDayExists := false;
  self.birthDayConvError := '';
end;

procedure TDriverDataBuilder.setBirthDay(date: WideString);
var
  myDate: TMyDate;
  next: Integer;
  err: WideString;
  validate: IDataValidationMut;
begin
  myDate := TMyDate.Create(0,0,0);
  validate := TDataValidation.Create;
  try
    if TryParseDate(date,applicationConfigObj.getDateFormat,myDate,validate)
    then begin
      if assigned(self.birthDay) then FreeAndNil(self.birthDay);
      self.birthDay := TMyDate.Copy(myDate);
      self.birthDayExists := true;
      self.birthDayConvError := '';
    end else begin
      self.birthDayConvError := validate.getMessage;
      self.birthDayExists := false;
    end;
  finally
    myDate.Destroy;
  end;
end;

procedure TDriverDataBuilder.setBirthDay(date: TDateTime);
begin
  if assigned(self.birthDay) then FreeAndNil(self.birthDay);
  self.birthDay := TMyDate.From(date);
  self.birthDayExists := true;
  self.birthDayConvError := '';
end;

procedure TDriverDataBuilder.setName(name: WideString);
begin
  self.name := name;
  self.nameExists := true;
end;

procedure TDriverDataBuilder.setDriverId(id: Integer);
begin
  self.driverId := id;
  self.driverIdExists := true;
end;

function TDriverDataBuilder.ValidateInsert: IDataValidation;
begin
  result := validate(true);
end;

function TDriverDataBuilder.ValidateUpdate: IDataValidation;
begin
  result := validate(false);
end;

function TDriverDataBuilder.Validate(insert:boolean): IDataValidation;
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

function TDriverDataBuilder.BuildInsert: IDMLOperation;
var
  params : TStringMap;
  dmlOp : TSqlInsertOperation;
  validation : IDataValidation;
  sql: string;
begin
  validation := ValidateInsert;
  if not validation.isOk then raise EDriverDataBuilder.Create(validation.getMessage);

  sql := 'insert into drivers (name, birth_day)' +
         ' values (:name, :birth_day) '+
         ';'+
         'select @@IDENTITY as _id';
  params := TStringMap.Create;
  params.put('name', self.name);
  params.put('birth_day', DateToSQL(self.birthDay));

  dmlOp := TSqlInsertOperation.Create(sql, params, '_id');
  result := dmlOp;
end;

function TDriverDataBuilder.BuildUpdate: IDMLOperation;
var
  params : TStringMap;
  dmlOp : TSqlUpdateOperation;
  validation : IDataValidation;
  sql: string;
begin
  validation := ValidateUpdate;
  if not validation.isOk then raise EDriverDataBuilder.Create(validation.getMessage);

  sql := 'update drivers set'+
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

{ TDriver }

constructor TDriver.Copy(sample: TDriver);
begin
  self.idValue := sample.idValue;
  self.nameValue := sample.nameValue;
end;

constructor TDriver.Create(
  id: Integer;
  name: WideString);
begin
  self.idValue := id;
  self.nameValue := name;
end;

constructor TDriverFinder.Create(connection: TADOConnection);
begin
  self.connection := connection;
  inherited Create;
end;

destructor TDriverFinder.Destroy;
begin
  self.connection := nil;
  inherited Destroy;
end;

function readRow(query:TADOQuery):TDriver;
begin
  result := TDriver.Create(
    query.FieldValues['id'],
    query.FieldValues['name']
  );
end;

procedure TDriverFinder.findAll(consumer: TDriverConsumer);
var
  query:TADOQuery;
  person: TDriver;
begin
  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	d.id as id,'+
      '	d.name as name,'+
      '	d.birth_day as birth_day '+
      ' from drivers d'+
      ' order by d.name';
    query.Connection := self.connection;
    query.Open;
    while not query.Eof do begin
      person := readRow(query);
      try
        consumer(person);
      finally
        FreeAndNil(person);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

procedure TDriverFinder.findLike(
  what: WideString;
  consumer: TDriverConsumer
);
var
  query:TADOQuery;
  person: TDriver;
begin
  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	d.id as id,'+
      '	d.name as name,'+
      '	d.birth_day as birth_day '+
      ' from drivers d'+
      ' where d.name like :what'+
      ' order by d.name';
    query.Connection := self.connection;
    query.Parameters.ParamByName('what').Value := what;
    query.Open;
    while not query.Eof do begin
      person := readRow(query);
      try
        consumer(person);
      finally
        FreeAndNil(person);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

initialization
log := logger('DriverSQL');

end.

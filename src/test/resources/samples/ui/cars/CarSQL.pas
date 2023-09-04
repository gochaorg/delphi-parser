unit CarSQL;

interface

uses
  Classes,
  SysUtils,
  ADODB,

  SQLDateParam,
  MyDate,
  Config,
  Loggers, Logging,
  IntegerList,
  DMLOperation,
  Validation,
  Map;

type

// Ошибка создания/валидации при построение операций
ECarDataBuilder = class(Exception);

// Ошибка создания/валидации при построение операций
ECarDataEraser = class(Exception);

// Шаблон строитель
// для создания либо запроса insert либо update
ICarDataBuilder = interface
  // Сброс состояния, надо заново указать значения
  procedure Reset;

  // Указывает id машины, необходимо для update
  procedure SetCarID( id:Integer );

  // Указывает Гос номер
  procedure SetLegalNumber( num: WideString );

  // Указывает ссылку на модель
  procedure SetModelId( id:Integer );

  // Указывает пробег
  procedure SetWear( wear:Integer ); overload;
  procedure SetWear( wear:WideString ); overload;

  // Указывает год выпуска
  procedure SetBirthYear( year:Integer ); overload;

  // Указывает год выпуска
  // Аргумент
  //   year - число
  procedure SetBirthYear( year:WideString ); overload;

  // Указывает дату прохождения ТО
  procedure SetMaintainceDate( date:TMyDate; own:boolean ); overload;

  // Указывает дату прохождения ТО
  // Аргумент
  //   date - дата в формате yyyy-MM-dd
  procedure SetMaintainceDate( date:WideString ); overload;

  // Проверка данных перед INSERT
  function ValidateInsert: IDataValidation;

  // Создает операцию INSERT.
  // Если какие данные указаны не верно,
  //   то генерирует исключение ECarDataBuilder
  function BuildInsert: IDMLOperation;

  // Проверка данных перед UPDATE
  function ValidateUpdate: IDataValidation;

  // Создает операцию UPDATE.
  // Если какие данные указаны не верно,
  //   то генерирует исключение ECarDataBuilder
  function BuildUpdate: IDMLOperation;
end;

TCarDataBuilder = class(TInterfacedObject,ICarDataBuilder)
  private
    // Id - обязательное для update
    updateId: Integer;
    updateIdExists: boolean;

    // Гос номер - обязательное
    legalNumber: WideString;
    legalNumberExists: boolean;

    // Ссылка на модель - обязательное
    modelId: Integer;
    modelIdExists: boolean;

    // Пробег - обязательное
    wear: Integer;
    wearExists: boolean;
    wearConvError: WideString;

    // Год выпуска - обязательное
    birthYear: Integer;
    birthYearExists: boolean;
    birthYearConvError: WideString;

    // Дата прохождения ТО - опциональное поле
    maintainceDate: TMyDate;
    maintainceDateOwn: boolean;
    maintainceDateExists: boolean;
    maintainceDateConvError: WideString;
  public
    constructor Create;
    destructor Destroy; override;

    procedure Reset;

    procedure SetCarID( id:Integer );

    procedure SetLegalNumber( str: WideString );

    procedure SetModelId( id:Integer );

    procedure SetWear( wear:Integer ); overload;
    procedure SetWear( wear:WideString ); overload;

    procedure SetBirthYear( year:Integer ); overload;
    procedure SetBirthYear( year:WideString ); overload;

    procedure SetMaintainceDate( date:TMyDate; own:boolean ); overload;
    procedure SetMaintainceDate( date:WideString ); overload;

    function ValidateInsert: IDataValidation;
    function BuildInsert: IDMLOperation;

    function ValidateUpdate: IDataValidation;
    function BuildUpdate: IDMLOperation;
  private
    // Проверка данных
    //   insert = true  - проверка для операции buildInsert
    //   insert = false - проверка для операции buildUpdate
    function Validate(insert:boolean):IDataValidation;
end;

// Шаблон строитель - создание операции DELETE
ICarDataEraser = interface
  // Сброс состояния, надо заново указать значения
  procedure Reset;

  // Указывает id машины которую надо удалить
  procedure AddCarId( id:Integer );

  // Проверяет данные перед формированием операции
  function ValidateDelete: IDataValidation;

  // Создает операцию DELETE
  // Если какие данные указаны не верно,
  //   то генерирует исключение ECarDataBuilder
  function BuildDelete: IDMLOperation;
end;

TCarDataEraser = class(TInterfacedObject,ICarDataEraser)
  private
    idList: TIntegerList;
  public
    constructor Create;
    destructor Destroy; override;

    procedure Reset;
    procedure AddCarId( id:Integer );
    function ValidateDelete: IDataValidation;
    function BuildDelete: IDMLOperation;
end;

// Описывает машину
TCar = class
  private
    carIdValue: Integer;
    legalNumberValue: WideString;
    modelIdValue: Integer;
    modelNameValue: WideString;
    totalWearValue: Integer;
  public
    // id запииси
    property carId:Integer read carIdValue;

    // Гос номер
    property legalNumber:WideString read legalNumberValue;

    // Модель машины - идентификатор
    property modelId:Integer read modelIdValue;

    // Модель машины - название
    property modelName:WideString read modelNameValue;

    // Суммарный пробег
    property totalWear:Integer read totalWearValue;

    constructor Create(
      carId: Integer;
      legalNumber: WideString;
      modelId: Integer;
      modelName: WideString;
      totalWear: Integer
    );

    constructor Copy( sample: TCar );
end;

// Функция принимающая машину
// значение не передается во владение
TCarConsumer = procedure ( car: TCar ) of object;

// Поиск машины
ICarFinder = interface
  // Поиск всех мащин
  //   consumer - функция принимающая машину
  procedure findAll( consumer:TCarConsumer );

  // Поиск машин совпадающих условию
  //   what - искомое условие
  //     либо по гос номеру   - операция like
  //     либо по марке машины - операция like
  //   consumer - функция принимающая машину
  procedure findLike( what:WideString; consumer:TCarConsumer );
end;

TCarFinder = class (TInterfacedObject,ICarFinder)
  private
    connection: TADOConnection;
  public
    constructor Create(connection: TADOConnection);
    destructor Destroy; override;

    procedure findAll( consumer:TCarConsumer );
    procedure findLike( what:WideString; consumer:TCarConsumer );
end;

implementation

var
  log: ILog;

{ TCarDataBuilder }

constructor TCarDataBuilder.Create;
begin
  inherited Create;
  self.updateIdExists := false;
  self.legalNumberExists := false;
  self.modelIdExists := false;
  self.wearExists := false;
  self.wearConvError := '';
  self.birthYearExists := false;
  self.maintainceDateExists := false;
  self.maintainceDateOwn := false;
  self.birthYearConvError := '';
  self.maintainceDateConvError := '';
end;

destructor TCarDataBuilder.Destroy;
begin
  if self.maintainceDateOwn and assigned(self.maintainceDate) then begin
    FreeAndNil(self.maintainceDate);
  end;
  inherited Destroy;
end;

//------------------------------------------------------

procedure TCarDataBuilder.Reset;
begin
  self.birthYearExists := false;
  self.birthYearConvError := '';

  self.legalNumberExists := false;

  self.maintainceDateExists := false;
  self.maintainceDateConvError := '';

  self.modelIdExists := false;

  self.updateIdExists := false;

  self.wearExists := false;
  self.wearConvError := '';
end;

//------------------------------------------------------

procedure TCarDataBuilder.SetBirthYear(year: Integer);
begin
  self.birthYear := year;
  self.birthYearExists := true;
end;

procedure TCarDataBuilder.SetBirthYear(year: WideString);
begin
  try
    self.setBirthYear(StrToInt(year));
  except
    on e:EConvertError do begin
      self.birthYearExists := false;
      self.birthYearConvError := e.Message;
    end;
  end;
end;

//------------------------------------------------------

procedure TCarDataBuilder.SetCarID(id: Integer);
begin
  self.updateId := id;
  Self.updateIdExists := true;
end;

//------------------------------------------------------

procedure TCarDataBuilder.SetLegalNumber(str: WideString);
begin
  self.legalNumber := str;
  self.legalNumberExists := true;
end;

//------------------------------------------------------

procedure TCarDataBuilder.SetMaintainceDate(date: TMyDate; own: boolean);
begin
  if assigned(self.maintainceDate) and self.maintainceDateOwn then
  begin
    FreeAndNil(self.maintainceDate);
  end;

  self.maintainceDate := date;
  self.maintainceDateOwn := own;
  self.maintainceDateExists := true;
end;

procedure TCarDataBuilder.SetMaintainceDate(date: WideString);
var
  parsedDate: TMyDate;
  next: Integer;
  err: WideString;
  validate: IDataValidationMut;
begin
  validate := TDataValidation.Create;
  if length(date)>0 then
  begin
    parsedDate := TMyDate.Create(0,0,0);
    try
      if TryParseDate(date,applicationConfigObj.getDateFormat,parsedDate,validate)
      then begin
        if assigned(self.maintainceDate)
        then FreeAndNil(self.maintainceDate);

        self.maintainceDate := TMyDate.Copy(parsedDate);
        self.maintainceDateExists := true;
        self.maintainceDateConvError := '';
      end else begin
        self.maintainceDateExists := false;
        self.maintainceDateConvError := validate.getMessage;
      end;
    finally
      FreeAndNil(parsedDate);
    end;
  end else begin
    self.maintainceDateExists := false;
  end;
end;

procedure TCarDataBuilder.SetModelId(id: Integer);
begin
  self.modelId := id;
  self.modelIdExists := true;
end;

procedure TCarDataBuilder.SetWear(wear: Integer);
begin
  self.wear := wear;
  self.wearExists := true;
  self.wearConvError := '';
end;

procedure TCarDataBuilder.SetWear(wear: WideString);
begin
  if length(wear)>0 then begin
    try
      self.setWear(StrToInt(wear));
    except
      on e:EConvertError do begin
        self.wearExists := false;
        self.wearConvError := e.Message;
      end;
    end;
  end else begin
    self.wearExists := false;
    self.wearConvError := '';
  end;
end;

function TCarDataBuilder.ValidateInsert: IDataValidation;
begin
  result := validate(true);
end;

function TCarDataBuilder.ValidateUpdate: IDataValidation;
begin
  result := validate(false);
end;

function TCarDataBuilder.Validate(insert:boolean): IDataValidation;
var
  validation: IDataValidationMut;
begin
  validation := TDataValidation.Create;
  result := validation;

  if not self.birthYearExists then
    if length(self.birthYearConvError)>0
    then validation.addError(self.birthYearConvError)
    else validation.addError('Не указан год выпуска');

  if not self.legalNumberExists then validation.addError('Не указан Гос номер');

  if not self.maintainceDateExists then
    if length(self.maintainceDateConvError)>0
    then validation.addError(
      'Дата прохождения указана не верно: '+self.maintainceDateConvError);

  if not self.modelIdExists then
    validation.addError('Не указана ссылка на модель машины');

  if not self.wearExists then
    if length(self.wearConvError)>0
    then validation.addError('Пробег машины не является числом')
    else validation.addError('Не указан пробег машины');

  if self.wearExists and (self.wear < 0) then
    validation.addError('Пробег машины - отрицательное число');

  if self.birthYearExists and (self.birthYear < 1800) then
    validation.addError('Год выпуска меньше 1800 года');

  if not insert then
  begin
    if not self.updateIdExists then
      validation.addError('Не указан id обновляемой записи');
  end;
end;

function TCarDataBuilder.BuildInsert: IDMLOperation;
var
  validation: IDataValidation;
  params: TStringMap;
  sql: String;
begin
  validation := validateInsert;
  if not validation.isOk then
    raise ECarDataBuilder.Create(validation.getMessage);

  sql :=
    'insert into cars (legal_number, model, wear, birth_year) '+
    'values (:legal_number, :model, :wear, :birth_year '+
    '); ' +
    'select @@IDENTITY as _id';

  params := TStringMap.Create;
  params.put('legal_number', self.legalNumber);
  params.put('model',        self.modelId );
  params.put('wear',         self.wear);
  params.put('birth_year',   self.birthYear);

  if maintainceDateExists then
  begin
    // Нашел странный баг
    // если использовать SQL
    //
    // insert into cars (... maintenance )
    //   values (... convert( datetime2, :M_DATE, 23 )
    //
    // где M_DATE - параметр '2020-01-01'
    // то блин валиться с EOleAccessViolation
    // если просто передавать как строку
    //
    // insert into cars (... maintenance )
    //   values (... convert( datetime2, '2020-01-01', 23 )
    //
    // то проблем нет
    sql :=
      'insert into cars (legal_number, model, wear, birth_year, maintenance) '+
      'values (:legal_number, :model, :wear, :birth_year, :maintenance);' +
      'select @@IDENTITY as _id';

    params.put('maintenance',
      DateToSQL(self.maintainceDate));
  end;

  result := TSqlInsertOperation.Create( sql, params, '_id');
end;

function TCarDataBuilder.BuildUpdate: IDMLOperation;
var
  validation: IDataValidation;
  params: TStringMap;
  sql: String;
begin
  validation := validateUpdate;
  if not validation.isOk then
    raise ECarDataBuilder.Create(validation.getMessage);

  sql :=
    'update cars set '+
    ' legal_number = :legal_number,'+
    ' model = :model,'+
    ' wear = :wear,'+
    ' birth_year = :birth_year,'+
    ' maintenance = null'+
    ' where id = :id';

  params := TStringMap.Create;
  params.put('legal_number', self.legalNumber);
  params.put('model',        self.modelId );
  params.put('wear',         self.wear);
  params.put('birth_year',   self.birthYear);
  params.put('id',           self.updateId);

  if maintainceDateExists then
  begin
  sql :=
    'update cars set '+
    ' legal_number = :legal_number,'+
    ' model = :model,'+
    ' wear = :wear,'+
    ' birth_year = :birth_year,'+
    ' maintenance = :maintenance'+
    ' where id = :id';

    params.put('maintenance',  DateToSQL(self.maintainceDate));
  end;

  result := TSqlUpdateOperation.Create( sql, params);
end;

{ TCarDataEraser }

constructor TCarDataEraser.Create;
begin
  inherited Create;
  self.idList := TIntegerList.Create;
end;

destructor TCarDataEraser.Destroy;
begin
  FreeAndNil(self.idList);
  inherited Destroy;
end;

procedure TCarDataEraser.Reset;
begin
  self.idList.Clear;
end;

procedure TCarDataEraser.AddCarId(id: Integer);
begin
  self.idList.Add(id);
end;

function TCarDataEraser.ValidateDelete: IDataValidation;
var
  validation : TDataValidation;
begin
  validation := TDataValidation.Create;
  if self.idList.isEmpty then begin
    validation.addError('Не указан id удаляемой записи');
  end;
  result := validation;
end;

function TCarDataEraser.BuildDelete: IDMLOperation;
var
  validation : IDataValidation;
  params: TStringMap;
  sql: String;
begin
  validation := self.ValidateDelete;
  if not validation.isOk then begin
    raise ECarDataEraser.Create('Данные не подготовленны '+validation.getMessage);
  end;

  sql :=
    'delete from cars where id in ('+self.idList.toString(', ')+')';

  params := TStringMap.Create;
  result := TSqlUpdateOperation.Create(sql,params);
end;


{ TCar }

constructor TCar.Copy(sample: TCar);
begin
  self.carIdValue       := sample.carIdValue;
  self.legalNumberValue := sample.legalNumberValue;
  self.modelIdValue     := sample.modelIdValue;
  self.modelNameValue   := sample.modelNameValue;
  self.totalWearValue   := sample.totalWearValue;
end;

constructor TCar.Create(
    carId: Integer;
    legalNumber: WideString;
    modelId: Integer;
    modelName: WideString;
    totalWear: Integer
);
begin
  self.carIdValue := carId;
  self.legalNumberValue := legalNumber;
  self.modelIdValue := modelId;
  self.modelNameValue := modelName;
  self.totalWearValue := totalWear;
end;

{ TCarFinder }

constructor TCarFinder.Create(connection: TADOConnection);
begin
  self.connection := connection;
  inherited Create;
end;

destructor TCarFinder.Destroy;
begin
  self.connection := nil;
  inherited Destroy;
end;

////////////
function readRow(query:TADOQuery):TCar;
begin
  log.println('read row');
  result := TCar.Create(
    query.FieldValues['car_id'],
    query.FieldValues['legal_number'],
    query.FieldValues['model_id'],
    query.FieldValues['model_name'],
    query.FieldValues['total_wear']
  );
end;
////////////

procedure TCarFinder.findAll(consumer: TCarConsumer);
var
  query:TADOQuery;
  car: TCar;
begin
  log.println('findAll');

  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	c.id as car_id,'+
      '	c.legal_number as legal_number,'+
      '	c.model as model_id,'+
      '	cm.name as model_name, '+
      ' (c.wear + isnull((select sum(wear) from waybills where car = c.id ),0)) as total_wear'+
      ' from cars c'+
      ' left join cars_model cm on (cm.id = c.model)'+
      ' order by c.legal_number';
    query.Connection := self.connection;
    query.Open;
    while not query.Eof do begin
      car := readRow(query);
      try
        consumer(car);
      finally
        FreeAndNil(car);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

procedure TCarFinder.findLike(what: WideString; consumer: TCarConsumer);
var
  query:TADOQuery;
  car: TCar;
begin
  log.println('findLike');

  query := TADOQuery.Create(nil);
  try
    query.SQL.Text :=
      'select '+
      '	c.id as car_id,'+
      '	c.legal_number as legal_number,'+
      '	c.model as model_id,'+
      '	cm.name as model_name '+
      ' (c.wear + isnull((select sum(wear) from waybills where car = c.id ),0)) as total_wear'+
      ' from cars c'+
      ' left join cars_model cm on (cm.id = c.model)'+
      ' where c.legal_number like :what or cm.name like :what'+
      ' order by c.legal_number';
    query.Connection := self.connection;
    query.Parameters.ParamByName('what').Value := what;
    query.Open;
    while not query.Eof do begin
      car := readRow(query);
      try
        consumer(car);
      finally
        FreeAndNil(car);
      end;
      query.Next;
    end;
    query.Close;
  finally
    FreeAndNil(query);
  end;
end;

initialization
log := logger('CarSQL');

end.

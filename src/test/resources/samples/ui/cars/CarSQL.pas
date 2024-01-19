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

// ������ ��������/��������� ��� ���������� ��������
ECarDataBuilder = class(Exception);

// ������ ��������/��������� ��� ���������� ��������
ECarDataEraser = class(Exception);

// ������ ���������
// ��� �������� ���� ������� insert ���� update
ICarDataBuilder = interface
  // ����� ���������, ���� ������ ������� ��������
  procedure Reset;

  // ��������� id ������, ���������� ��� update
  procedure SetCarID( id:Integer );

  // ��������� ��� �����
  procedure SetLegalNumber( num: WideString );

  // ��������� ������ �� ������
  procedure SetModelId( id:Integer );

  // ��������� ������
  procedure SetWear( wear:Integer ); overload;
  procedure SetWear( wear:WideString ); overload;

  // ��������� ��� �������
  procedure SetBirthYear( year:Integer ); overload;

  // ��������� ��� �������
  // ��������
  //   year - �����
  procedure SetBirthYear( year:WideString ); overload;

  // ��������� ���� ����������� ��
  procedure SetMaintainceDate( date:TMyDate; own:boolean ); overload;

  // ��������� ���� ����������� ��
  // ��������
  //   date - ���� � ������� yyyy-MM-dd
  procedure SetMaintainceDate( date:WideString ); overload;

  // �������� ������ ����� INSERT
  function ValidateInsert: IDataValidation;

  // ������� �������� INSERT.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� ECarDataBuilder
  function BuildInsert: IDMLOperation;

  // �������� ������ ����� UPDATE
  function ValidateUpdate: IDataValidation;

  // ������� �������� UPDATE.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� ECarDataBuilder
  function BuildUpdate: IDMLOperation;
end;

TCarDataBuilder = class(TInterfacedObject,ICarDataBuilder)
  private
    // Id - ������������ ��� update
    updateId: Integer;
    updateIdExists: boolean;

    // ��� ����� - ������������
    legalNumber: WideString;
    legalNumberExists: boolean;

    // ������ �� ������ - ������������
    modelId: Integer;
    modelIdExists: boolean;

    // ������ - ������������
    wear: Integer;
    wearExists: boolean;
    wearConvError: WideString;

    // ��� ������� - ������������
    birthYear: Integer;
    birthYearExists: boolean;
    birthYearConvError: WideString;

    // ���� ����������� �� - ������������ ����
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
    // �������� ������
    //   insert = true  - �������� ��� �������� buildInsert
    //   insert = false - �������� ��� �������� buildUpdate
    function Validate(insert:boolean):IDataValidation;
end;

// ������ ��������� - �������� �������� DELETE
ICarDataEraser = interface
  // ����� ���������, ���� ������ ������� ��������
  procedure Reset;

  // ��������� id ������ ������� ���� �������
  procedure AddCarId( id:Integer );

  // ��������� ������ ����� ������������� ��������
  function ValidateDelete: IDataValidation;

  // ������� �������� DELETE
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� ECarDataBuilder
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

// ��������� ������
TCar = class
  private
    carIdValue: Integer;
    legalNumberValue: WideString;
    modelIdValue: Integer;
    modelNameValue: WideString;
    totalWearValue: Integer;
  public
    // id �������
    property carId:Integer read carIdValue;

    // ��� �����
    property legalNumber:WideString read legalNumberValue;

    // ������ ������ - �������������
    property modelId:Integer read modelIdValue;

    // ������ ������ - ��������
    property modelName:WideString read modelNameValue;

    // ��������� ������
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

// ������� ����������� ������
// �������� �� ���������� �� ��������
TCarConsumer = procedure ( car: TCar ) of object;

// ����� ������
ICarFinder = interface
  // ����� ���� �����
  //   consumer - ������� ����������� ������
  procedure findAll( consumer:TCarConsumer );

  // ����� ����� ����������� �������
  //   what - ������� �������
  //     ���� �� ��� ������   - �������� like
  //     ���� �� ����� ������ - �������� like
  //   consumer - ������� ����������� ������
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
    else validation.addError('�� ������ ��� �������');

  if not self.legalNumberExists then validation.addError('�� ������ ��� �����');

  if not self.maintainceDateExists then
    if length(self.maintainceDateConvError)>0
    then validation.addError(
      '���� ����������� ������� �� �����: '+self.maintainceDateConvError);

  if not self.modelIdExists then
    validation.addError('�� ������� ������ �� ������ ������');

  if not self.wearExists then
    if length(self.wearConvError)>0
    then validation.addError('������ ������ �� �������� ������')
    else validation.addError('�� ������ ������ ������');

  if self.wearExists and (self.wear < 0) then
    validation.addError('������ ������ - ������������� �����');

  if self.birthYearExists and (self.birthYear < 1800) then
    validation.addError('��� ������� ������ 1800 ����');

  if not insert then
  begin
    if not self.updateIdExists then
      validation.addError('�� ������ id ����������� ������');
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
    // ����� �������� ���
    // ���� ������������ SQL
    //
    // insert into cars (... maintenance )
    //   values (... convert( datetime2, :M_DATE, 23 )
    //
    // ��� M_DATE - �������� '2020-01-01'
    // �� ���� �������� � EOleAccessViolation
    // ���� ������ ���������� ��� ������
    //
    // insert into cars (... maintenance )
    //   values (... convert( datetime2, '2020-01-01', 23 )
    //
    // �� ������� ���
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
    validation.addError('�� ������ id ��������� ������');
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
    raise ECarDataEraser.Create('������ �� ������������� '+validation.getMessage);
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

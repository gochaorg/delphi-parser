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

// ������ ��������/��������� ��� ���������� ��������
EDispatcherDataBuilder = class(Exception);

// ������ ���������
// ��� �������� ���� ������� insert ���� update
IDispatcherDataBuilder = interface
  // ����� ���������, ���� ������ ������� ��������
  procedure Reset;

  // ��������� ����������� ������
  procedure setDriverId(id:Integer);

  // ��������� ��� ����������
  procedure setName(name:WideString);

  // ��������� ���� ��������
  procedure setBirthDay(date:WideString); overload;

  // ��������� ���� ��������
  // ���������
  //   date - ������ ����������� ���� � ������� yyyy-MM-dd
  procedure setBirthDay(date:TDateTime); overload;

  // �������� ������ ����� INSERT
  function ValidateInsert: IDataValidation;

  // ������� �������� INSERT.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� EDriverDataBuilder
  function BuildInsert: IDMLOperation;

  // �������� ������ ����� UPDATE
  function ValidateUpdate: IDataValidation;

  // ������� �������� UPDATE.
  // ���� ����� ������ ������� �� �����,
  //   �� ���������� ���������� EDriverDataBuilder
  function BuildUpdate: IDMLOperation;
end;

// ���������� IDispatcherDataBuilder
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

// ������ - ���������
TDispatcher = class
  private
    idValue: Integer;
    nameValue: WideString;
  public
    // �����������
    // ���������
    //   id - �� ������
    //   name - ��� ����������
    constructor Create( id:Integer; name:WideString );

    // ����������� �����������
    // ���������
    //   sample - ������ ��� �����������
    constructor Copy( sample: TDispatcher );

    // id ������
    property id:Integer read idValue;

    // ���
    property name:WideString read nameValue;
end;

// ������� ����������� ������ � ����������
// �������� �� ���������� �� ��������
TDispatcherConsumer = procedure ( car: TDispatcher ) of object;

// ����� �����������
IDispatcherFinder = interface
  // ����� ���� �����������
  //   consumer - ������� ����������� ������
  procedure findAll( consumer:TDispatcherConsumer );

  // ����� ����������� ����������� �������
  //   what - ������� �������
  //     �� name (���) - �������� like
  //   consumer - ������� ����������� ������
  procedure findLike( what:WideString; consumer:TDispatcherConsumer );
end;

// ���������� IDispatcherFinder
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
  if not self.nameExists then validation.addError('�� ������� ���');
  if not self.birthDayExists then
    if length(self.birthDayConvError)>0 then
      validation.addError('������ ���� ����� �� ����� '+self.birthDayConvError)
    else
      validation.addError('���� �������� �� �������');

  if (not insert) and (not self.driverIdExists) then
    validation.addError('�� ������ id ����������� ������');

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
